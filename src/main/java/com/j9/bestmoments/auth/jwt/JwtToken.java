package com.j9.bestmoments.auth.jwt;

public record JwtToken(
        String grantType,
        String accessToken,
        String refreshToken
) {

}