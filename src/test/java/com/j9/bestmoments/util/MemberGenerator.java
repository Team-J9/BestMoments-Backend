package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.MemberRole;

public final class MemberGenerator {

    public static Member createGoogleUser(String name) {
        return Member.builder()
                .oauthId(name + "'s google_oauth_id")
                .oauthProvider("google")
                .name(name)
                .email(name + "@gmail.com")
                .profileImageUrl(name + "'s profileImgUrl")
                .role(MemberRole.USER)
                .build();
    }

    public static Member createGoogleAdmin(String name) {
        return Member.builder()
                .oauthId(name + "'s google_oauth_id")
                .oauthProvider("google")
                .name(name)
                .email(name + "@gmail.com")
                .profileImageUrl(name + "'s profileImgUrl")
                .role(MemberRole.ADMIN)
                .build();
    }

}
