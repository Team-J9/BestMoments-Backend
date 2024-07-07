package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Token;
import com.j9.bestmoments.dto.response.JwtTokenDto;
import com.j9.bestmoments.jwt.JwtTokenProvider;
import com.j9.bestmoments.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtTokenDto create(Member member) {
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
        return new JwtTokenDto("Bearer", accessToken, refreshToken);
    }

    public Token findByAnyToken(String token) {
        return tokenRepository.findByAccessToken(resolveToken(token))
                .or(() -> tokenRepository.findByRefreshToken(resolveToken(token)))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 토큰입니다."));
    }

    public Token findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(resolveToken(accessToken))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 액세스 토큰입니다."));
    }

    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(resolveToken(refreshToken))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 리프래시 토큰입니다."));
    }

    public void checkExpired(String token) {
        Token foundToken = findByAnyToken(token);
        if (foundToken.getIsExpired()) {
            throw new AccessDeniedException("만료된 액세스 토큰입니다.");
        }
    }

    @Transactional
    public void expire(String refreshToken) {
        log.error(refreshToken);
        Token foundToken = findByRefreshToken(refreshToken);
        foundToken.expire();
        tokenRepository.save(foundToken);
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
