package com.j9.bestmoments.validator;

import com.j9.bestmoments.constants.ImageFileTypes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ImageTypeValidator implements ConstraintValidator<ImageTypeCheck, MultipartFile> {

    @Override
    public boolean isValid(@NotNull MultipartFile file, ConstraintValidatorContext context) {
        String contentType = file.getContentType();
        return ImageFileTypes.contains(contentType);
    }
}