package com.j9.bestmoments.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MemberRole {

    USER("user"),
    ADMIN("admin"),
    ;

    private final String key;

}
