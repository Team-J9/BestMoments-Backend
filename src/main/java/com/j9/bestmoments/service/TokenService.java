package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Token;
import com.j9.bestmoments.domain.TokenType;
import com.j9.bestmoments.dto.response.LoginDto;
import com.j9.bestmoments.jwt.JwtTokenProvider;
import com.j9.bestmoments.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Transactional
    public LoginDto create(Member member) {
        String accessToken = createAccessToken(member);
        String refreshToken = createRefreshToken(member);
        log.info("토큰 발급됨\n  - accessToken: {}\n  - refreshToken: {}", accessToken, refreshToken);
        this.findByToken(accessToken);
        return new LoginDto(accessToken, refreshToken, member.getDeletedAt());
    }

    private String createAccessToken(Member member) {
        String token = jwtTokenProvider.generateAccessToken(member);
        Token accessToken = Token.builder()
                .member(member)
                .tokenType(TokenType.ACCESS_TOKEN)
                .token(token)
                .build();
        tokenRepository.save(accessToken);
        return token;
    }

    private String createRefreshToken(Member member) {
        String token = jwtTokenProvider.generateRefreshToken(member);
        Token refreshToken = Token.builder()
                .member(member)
                .tokenType(TokenType.REFRESH_TOKEN)
                .token(token)
                .build();
        tokenRepository.save(refreshToken);
        return token;
    }

    @Transactional
    public void expire(String token) {
        Token foundToken = this.findByToken(token);
        tokenRepository.delete(foundToken);
    }

    public Token findByToken(String token) {
        return tokenRepository.findById(token)
                .orElseThrow(() -> new AccessDeniedException("만료되거나 발급되지 않은 토큰입니다."));
    }

    @Transactional
    public String refresh(String refreshToken) {
        Token foundToken = this.findByToken(refreshToken);
        if (!foundToken.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
            throw new AccessDeniedException("만료되거나 발급되지 않은 토큰입니다.");
        }
        Member member = memberService.findById(foundToken.getMemberId());
        String newAccessToken = createAccessToken(member);
        return newAccessToken;
    }

}
