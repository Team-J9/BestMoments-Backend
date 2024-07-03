package com.j9.bestmoments.dto.request;

import com.j9.bestmoments.domain.VideoStatus;
import org.springframework.web.multipart.MultipartFile;

public record VideoCreateDto(
        MultipartFile file,
        String title,
        String description,
        VideoStatus videoStatus
) {

}
