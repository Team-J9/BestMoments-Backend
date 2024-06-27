package com.j9.bestmoments.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "BestMoments API 명세서",
                description = "",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

}