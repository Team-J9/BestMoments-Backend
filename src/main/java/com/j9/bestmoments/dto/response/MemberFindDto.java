package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Member;

public record MemberFindDto(
        String id,
        String name,
        String email,
        String profileImageUrl,
        String description
) {

    public static MemberFindDto of(Member member) {
        return new MemberFindDto(
                member.getId().toString(),
                member.getName(),
                member.getEmail(),
                member.getProfileImageUrl(),
                member.getDescription()
        );
    }

}
