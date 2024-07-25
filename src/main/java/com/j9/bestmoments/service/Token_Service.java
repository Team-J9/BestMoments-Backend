package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.AccessToken;
import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.RefreshToken;
import com.j9.bestmoments.domain.Token_;
import com.j9.bestmoments.dto.response.LoginDto;
import com.j9.bestmoments.jwt.JwtTokenProvider;
import com.j9.bestmoments.repository.Token_Repository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Token_Service {

    private final Token_Repository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public LoginDto create(Member member) {
        String accessToken = createAccessToken(member);
        String refreshToken = createRefreshToken(member);
        log.info("토큰 발급됨\n  - accessToken: {}\n  - refreshToken: {}", accessToken, refreshToken);
        return new LoginDto("Bearer", accessToken, refreshToken, member.getDeletedAt());
    }

    private String createAccessToken(Member member) {
        String accessTokenValue = jwtTokenProvider.generateAccessToken(member);
        AccessToken accessToken = new AccessToken(member, accessTokenValue);
        tokenRepository.save(accessToken);
        return accessTokenValue;
    }

    private String createRefreshToken(Member member) {
        String refreshTokenValue = jwtTokenProvider.generateRefreshToken(member);
        RefreshToken refreshToken = new RefreshToken(member, refreshTokenValue);
        tokenRepository.save(refreshToken);
        return refreshTokenValue;
    }

    @Transactional
    public void expire(String token) {
        Token_ foundToken = this.findByToken(token);
        tokenRepository.delete(foundToken);
    }

    private Token_ findByToken(String token) {
        return tokenRepository.findById(token)
                .orElseThrow(() -> new AccessDeniedException("만료되거나 발급되지 않은 토큰입니다."));
    }

    @Transactional
    public String refresh(String refreshToken) {
        Token_ foundToken = this.findByToken(refreshToken);
        Member member = memberService.findById(foundToken.getMemberId());
        String newAccessToken = createAccessToken(member);
        return newAccessToken;
    }

}
