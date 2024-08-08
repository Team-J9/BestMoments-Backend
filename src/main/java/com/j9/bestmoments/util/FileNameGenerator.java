package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FileNameGenerator {

    public static String generateProfileImageFileName(Member member) {
        String memberId = member.getId().toString();
        String dateString = generateDateString();
        return String.format("profile/%s/%s", memberId, dateString);
    }

    private static String generateDateString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

}
