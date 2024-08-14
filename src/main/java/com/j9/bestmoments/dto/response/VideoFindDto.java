package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record VideoFindDto(
        UUID id,
        String fileUrl,
        String title,
        String description,
        UUID uploaderId,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        VideoStatus videoStatus
) {

    public static VideoFindDto of (Video video) {
        return new VideoFindDto(
                video.getId(),
                video.getVideoUrl(),
                video.getTitle(),
                video.getDescription(),
                video.getUploader().getId(),
                video.getCreatedAt(),
                video.getDeletedAt(),
                video.getVideoStatus()
        );
    }

}
