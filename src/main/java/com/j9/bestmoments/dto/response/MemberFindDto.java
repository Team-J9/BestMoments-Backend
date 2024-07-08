package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Member;
import java.time.LocalDateTime;

public record MemberFindDto(
        String id,
        String name,
        String email,
        String profileImageUrl,
        String description,
        LocalDateTime createdAt,
        LocalDateTime deletedAt
) {

    public static MemberFindDto of(Member member) {
        return new MemberFindDto(
                member.getId().toString(),
                member.getName(),
                member.getEmail(),
                member.getProfileImageUrl(),
                member.getDescription(),
                member.getCreatedAt(),
                member.getDeletedAt()
        );
    }

}
