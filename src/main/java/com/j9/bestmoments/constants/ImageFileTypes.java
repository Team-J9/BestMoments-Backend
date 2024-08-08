package com.j9.bestmoments.constants;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFileTypes {
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),
    BMP("image/bmp"),
    TIFF("image/tiff"),
    WEBP("image/webp"),
    SVG("image/svg+xml"),
    ;

    private final String contentType;

    public static boolean contains(String contentType) {
        System.out.println(contentType);
        if (contentType == null) {
            return false;
        }
        String findingContentType = contentType.toLowerCase();
        return Arrays.stream(ImageFileTypes.values())
                .map(ImageFileTypes::getContentType)
                .anyMatch(type -> type.equals(findingContentType));
    }

}
