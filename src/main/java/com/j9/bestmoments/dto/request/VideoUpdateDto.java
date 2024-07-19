package com.j9.bestmoments.dto.request;

import com.j9.bestmoments.domain.VideoStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VideoUpdateDto(

        @NotNull
        @NotBlank
        @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
        String title,

        @NotNull
        @NotBlank
        @Size(max = 2000, message = "상세설명은 2000자 이내로 입력해주세요.")
        String description,

        @NotNull
        VideoStatus videoStatus
) {

}
