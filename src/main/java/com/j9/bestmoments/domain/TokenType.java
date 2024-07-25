package com.j9.bestmoments.domain;

import com.j9.bestmoments.constants.TokenExpiration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {

    ACCESS_TOKEN("accessToken", TokenExpiration.ACCESS_TOKEN),
    REFRESH_TOKEN("refreshToken", TokenExpiration.REFRESH_TOKEN),
    ;

    private final String name;
    private final long expiration;

}
