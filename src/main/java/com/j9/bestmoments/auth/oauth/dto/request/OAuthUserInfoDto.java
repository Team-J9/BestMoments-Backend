package com.j9.bestmoments.auth.oauth.dto.request;

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
