package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Member;

public record MemberPreviewDto(
        String id,
        String name,
        String profileImageUrl
) {

    public static MemberPreviewDto of(Member member) {
        return new MemberPreviewDto(
                member.getId().toString(),
                member.getName(),
                member.getProfileImageUrl()
        );
    }

}
