package com.j9.bestmoments.dto.response;

public record JwtTokenDto(
        String grantType,
        String accessToken,
        String refreshToken
) {

}