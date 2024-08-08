package com.j9.bestmoments.dto.request;

import com.j9.bestmoments.validator.ImageTypeCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record MemberUpdateDto (

        @ImageTypeCheck
        MultipartFile file,

        @NotBlank
        @Size(max = 20, message = "이름은 20자 이내로 입력해주세요.")
        String name,

        @NotBlank
        @Size(max = 1000, message = "자기소개는 1000자 이내로 입력해주세요.")
        String description

) {

}
