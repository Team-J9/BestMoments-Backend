package com.j9.bestmoments.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VideoStatus {

    PUBLIC("공개"),
    URL_PUBLIC("링크 공개"),
    PRIVATE("비공개"),
    DELETED("삭제된 동영상"),
    ;

    private final String description;

}
