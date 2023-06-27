package com.kailin.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.text.StrFormatter;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <h2>SwaggerConfig.java</h2>
 *
 * @author Daizc-kl
 * @date 2020/11/24 19:55
 */
@Profile({"dev", "test", "local", "prod"})
@EnableSwagger2WebMvc
@EnableKnife4j
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final ApplicationContext applicationContext;
    @Autowired(required = false)
    private BuildProperties buildProperties;
    @Autowired(required = false)
    private GitProperties gitProperties;

    private String getApplicationDescription() {

        final StringBuilder builder = new StringBuilder();

        final String instanceInfoHtml = StrFormatter.format(
                "<hr/>" +
                        "<table>" +
                        "<tr><th style=\"margin: 0 20px;\">实例名称:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                        "</table>" +
                        "",
                Optional.ofNullable(applicationContext).map(ApplicationContext::getApplicationName).orElse("NULL")
        );
        builder.append(instanceInfoHtml);

        if (gitProperties != null) {
            final String branch = gitProperties.getBranch();
            final String commitId = gitProperties.getCommitId();
            final String shortCommitId = gitProperties.getShortCommitId();
            final Instant commitTime = gitProperties.getCommitTime();

            final String buildInfoHtml = StrFormatter.format(
                    "<hr/>" +
                            "<table>" +
                            "<tr><th style=\"margin: 0 20px;\">branch:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">commitId:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">shortCommitId:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">commitTime:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "</table>" +
                            "",
                    branch,
                    commitId,
                    shortCommitId,
                    DateTime.from(commitTime)
            );
            builder.append(buildInfoHtml);
        }

        if (buildProperties != null) {

            final String version = buildProperties.getVersion();
            final String artifact = buildProperties.getArtifact();
            final String group = buildProperties.getGroup();
            final String name = buildProperties.getName();
            final Instant buildTime = buildProperties.getTime();

            final String applicationInfoHtml = StrFormatter.format(
                    "<hr/>" +
                            "<table>" +
                            "<tr><th style=\"margin: 0 20px;\">version:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">artifact:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">group:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">name:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "<tr><th style=\"margin: 0 20px;\">buildTime:&nbsp;&nbsp; </th><td>{}</td></tr>" +
                            "</table>" +
                            "",
                    version,
                    artifact,
                    group,
                    name,
                    DateTime.from(buildTime)
            );
            builder.append(applicationInfoHtml);
        }

        return builder.append("<hr/>").toString();
    }

    private String[] getActiveProfiles() {
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    @Bean("docket")
    public Docket docket() {

        // 返回文档摘要信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securitySchemes())
                .globalOperationParameters(globalOperationParameters())
                ;
    }

    private List<Parameter> globalOperationParameters() {

        return Collections.singletonList(new ParameterBuilder()
                .name("Authorization")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .allowEmptyValue(true)
                .build());
    }


    // 生成接口信息，包括标题、联系人等
    private ApiInfo apiInfo() {

        String projectName;
        if (buildProperties != null) {
            projectName = buildProperties.getArtifact();
        } else if (applicationContext != null) {
            projectName = applicationContext.getApplicationName();
        } else {
            projectName = "java-chat-cloud";
        }

        return new ApiInfoBuilder()
                .title(StrFormatter.format("[{}]{}",
                        Stream.of(getActiveProfiles()).reduce((s, s2) -> s + "," + s2).orElse("default"),
                        projectName))
                .description(getApplicationDescription())
                .build();
    }

    private List<ApiKey> securitySchemes() {
        List<ApiKey> apiKeyList = Lists.newArrayListWithExpectedSize(1);
        apiKeyList.add(new ApiKey("Authorization", "认证参数", "header"));
        return apiKeyList;
    }
}
