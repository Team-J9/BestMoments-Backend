package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FileNameGenerator {

    public static String generateProfileImageFileName(Member member) {
        String memberId = member.getId().toString();
        String dateString = generateDateString();
        return String.format("profile/%s/%s", memberId, dateString);
    }

    public static String generateVideoFileName(Video video) {
        String videoId = video.getId().toString();
        return String.format("video/%s/video-origin", videoId);
    }

    private static String generateDateString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

}
