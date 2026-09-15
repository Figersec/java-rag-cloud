package com.kailin.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 / Knife4j 文档配置。
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:java-rag-cloud}")
    private String applicationName;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme authorization = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER);
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .description("企业级 RAG 服务接口"))
                .components(new Components().addSecuritySchemes("Authorization", authorization))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }
}
