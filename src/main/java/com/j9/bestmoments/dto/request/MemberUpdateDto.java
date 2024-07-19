package com.j9.bestmoments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberUpdateDto (

        @NotNull
        @NotBlank
        @Size(max = 20, message = "이름은 20자 이내로 입력해주세요.")
        String name,

        @NotNull
        @NotBlank
        @Size(max = 1000, message = "자기소개는 1000자 이내로 입력해주세요.")
        String description

) {

}
