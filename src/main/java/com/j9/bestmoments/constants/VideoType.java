package com.j9.bestmoments.constants;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VideoType {
    MP4("video/mp4"),
    AVI("video/x-msvideo"),
    MKV("video/x-matroska"),
    WEBM("video/webm"),
    MOV("video/quicktime"),
    MPEG("video/mpeg"),
    FLV("video/x-flv");

    private final String contentType;

    public static boolean contains(String contentType) {
        if (contentType == null) {
            return false;
        }
        String findingContentType = contentType.toLowerCase();
        return Arrays.stream(VideoType.values())
                .map(VideoType::getContentType)
                .anyMatch(type -> type.equals(findingContentType));
    }

}
