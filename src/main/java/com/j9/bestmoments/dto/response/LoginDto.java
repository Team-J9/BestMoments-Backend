package com.j9.bestmoments.dto.response;

import java.time.LocalDateTime;

public record LoginDto(
        String grantType,
        String accessToken,
        String refreshToken,
        LocalDateTime deletedAt
) {

}