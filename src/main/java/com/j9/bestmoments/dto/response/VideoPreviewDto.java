package com.j9.bestmoments.dto.response;

import com.j9.bestmoments.domain.Video;
import com.j9.bestmoments.domain.VideoStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record VideoPreviewDto (
        UUID id,
        String title,
        String description,
        UUID uploaderId,
        LocalDateTime createdAt,
        VideoStatus videoStatus
) {

    public static VideoPreviewDto of (Video video) {
        return new VideoPreviewDto(
                video.getId(),
                video.getTitle(),
                video.getDescription(),
                video.getUploader().getId(),
                video.getCreatedAt(),
                video.getVideoStatus()
        );
    }

}
