package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Member;

public record MemberSummaryDto(
        String id,
        String name,
        String profileImageUrl
) {

    public static MemberSummaryDto of(Member member) {
        return new MemberSummaryDto(
                member.getId().toString(),
                member.getName(),
                member.getProfileImageUrl()
        );
    }

}
