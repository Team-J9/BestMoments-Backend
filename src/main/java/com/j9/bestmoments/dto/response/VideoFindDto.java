package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VideoFindDto(
        UUID id,
        List<String> videoUrls,
        String thumbnailUrl,
        String title,
        String description,
        UUID uploaderId,
        LocalDateTime createdAt,
        boolean isDeleted,
        VideoStatus videoStatus
) {

    public static VideoFindDto of (Video video) {
        return new VideoFindDto(
                video.getId(),
                video.getEncodedVideoUrls(),
                video.getThumbnailUrl(),
                video.getTitle(),
                video.getDescription(),
                video.getUploader().getId(),
                video.getCreatedAt(),
                video.isDeleted(),
                video.getVideoStatus()
        );
    }

}
