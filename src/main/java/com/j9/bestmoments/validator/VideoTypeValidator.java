package com.j9.bestmoments.validator;

import com.j9.bestmoments.constants.VideoType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class VideoTypeValidator implements ConstraintValidator<VideoTypeCheck, MultipartFile> {

    @Override
    public boolean isValid(@NotNull MultipartFile file, ConstraintValidatorContext context) {
        String contentType = file.getContentType();
        return VideoType.contains(contentType);
    }
}