package com.j9.bestmoments.dto.response;

public record LoginDto(
        String accessToken,
        String refreshToken,
        boolean isDeleted
) {

}