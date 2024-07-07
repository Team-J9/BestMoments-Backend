package com.j9.bestmoments.service;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Token;
import com.j9.bestmoments.dto.response.JwtTokenDto;
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
        return tokenRepository.findByAccessToken(token)
                .or(() -> tokenRepository.findByRefreshToken(token))
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 토큰입니다."));
    }

    public Token findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new AccessDeniedException("존재하지 않는 액세스 토큰입니다."));
    }

    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
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
        Token foundToken = findByRefreshToken(refreshToken);
        foundToken.expire();
        tokenRepository.save(foundToken);
    }

}
