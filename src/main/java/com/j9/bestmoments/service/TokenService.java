package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Token;
import com.j9.bestmoments.dto.response.LoginDto;
import com.j9.bestmoments.jwt.JwtTokenProvider;
import com.j9.bestmoments.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public LoginDto create(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(member);
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        Token token = Token.builder()
                .member(member)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);
        log.error(token.getAccessToken());
        log.error(token.getRefreshToken());
        log.error(tokenRepository.findAll().get(0).getAccessToken());
        return new LoginDto("Bearer", accessToken, refreshToken, member.getDeletedAt());
    }

    public Token findByAnyToken(String token) {
        return tokenRepository.findByAccessToken(resolveToken(token))
                .or(() -> tokenRepository.findByRefreshToken(resolveToken(token)))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않거나 만료된 토큰입니다."));
    }

    public Token findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(resolveToken(accessToken))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않거나 만료된 액세스 토큰입니다."));
    }

    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(resolveToken(refreshToken))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않거나 만료된 리프래시 토큰입니다."));
    }

    public void checkExpired(String token) {
        findByAnyToken(token);
    }

    @Transactional
    public void expire(String token) {
        Token foundToken = findByAnyToken(token);
        tokenRepository.delete(foundToken);
    }

    @Transactional
    public String refresh(String refreshToken) {
        Token foundToken = findByRefreshToken(refreshToken);
        String accessToken = jwtTokenProvider.generateAccessToken(foundToken.getMember());
        foundToken.setAccessToken(accessToken);
        tokenRepository.save(foundToken);
        return accessToken;
    }

    private String resolveToken(String token) {
        if (token.startsWith("Bearer")) {
            return token.substring(7);
        }
        return token;
    }

}
