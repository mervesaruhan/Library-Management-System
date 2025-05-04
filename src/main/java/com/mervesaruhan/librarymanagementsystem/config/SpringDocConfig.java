package com.mervesaruhan.librarymanagementsystem.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSchemas("RestResponse", new Schema<>()
                        .addProperty("data", new ObjectSchema())
                        .addProperty("message", new StringSchema())
                        .addProperty("isSuccess", new BooleanSchema())
                        .addProperty("responseTime", new StringSchema().example("2025-05-03 10:32:00"))));
    }
}
