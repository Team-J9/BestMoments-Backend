package com.j9.bestmoments.dto.response;

import lombok.Builder;

@Builder
public record OAuthUserInfoDto(
        String provider,
        String id,
        String name,
        String email,
        String profileImageUrl
) {

}
