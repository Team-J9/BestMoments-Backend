package com.j9.bestmoments.util;

import com.j9.bestmoments.domain.Member;
import com.j9.bestmoments.domain.Video;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.web.multipart.MultipartFile;

public final class FileNameGenerator {

    public static String generateProfileImageFileName(Member member, MultipartFile file) {
        String memberId = member.getId().toString();
        String dateString = generateDateString();
        String contentType = getContentType(file);
        return String.format("profile/%s/%s.%s", memberId, dateString, contentType);
    }

    public static String generateVideoFileName(Video video, MultipartFile file) {
        String videoId = video.getId().toString();
        String contentType = getContentType(file);
        return String.format("video/%s/video-origin.%s", videoId, contentType);
    }

    public static String generateEncodedVideoFileName(String videoFileName, String resolution) {
        String[] splitFileName = videoFileName.split("-origin.");
        String originVideoFileName = splitFileName[0];
        return String.format("%s-%s.mp4", originVideoFileName, resolution);
    }

    public static String generateThumbnailImageFileName(Video video, MultipartFile file) {
        String videoId = video.getId().toString();
        String dateString = generateDateString();
        String contentType = getContentType(file);
        return String.format("video/%s/thumbnail-%s.%s", videoId, dateString, contentType);
    }

    private static String generateDateString() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    private static String getContentType(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("파일이 존재하지 않음");
        }
        return file.getContentType().split("/")[1];
    }

}
