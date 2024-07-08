package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record VideoPreviewMoreDto(
        UUID id,
        String title,
        UUID uploaderId,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        VideoStatus videoStatus
) {

    public static VideoPreviewMoreDto of (Video video) {
        return new VideoPreviewMoreDto(
                video.getId(),
                video.getTitle(),
                video.getUploader().getId(),
                video.getCreatedAt(),
                video.getDeletedAt(),
                video.getVideoStatus()
        );
    }

}
