package com.kailin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * <h2>自定义注入RestTemplate</h2>
 *
 * @author Daizc-kl
 * @date 2020/08/07
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RestTemplateAutoConfiguration.class)
public class RestTemplateConfig {

    /**
     * <h3>每个项目均可注入自定义的RestTemplate</h3>
     *
     * @param restTemplateBuilder RestTemplateBuilder
     * @return RestTemplate
     */
    @Bean
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }


    @Bean
    public RestTemplateRequestCustomizer<?> restTemplateRequestLogCustomizer() {

        return (RestTemplateRequestCustomizer<ClientHttpRequest>) request -> {
            log.info("RestTemplate发起{}请求=>[{}]", request.getMethod(), request.getURI());
        };
    }

    /**
     * <h3>RestTemplate构建器</h3>
     *
     * <p>注入<code>RestTemplateRequestCustomizer<?></code>和<code>RestTemplateCustomizer</code>的实现类将会自动配置在注入在使用此Builder构建器RestTemplate上</p>
     * <p>RestTemplateCustomizer用于配置RestTemplate</p>
     * <p>RestTemplateRequestCustomizer用于在发起请求前配置Request</p>
     *
     * @param messageConverters              自动注入所有的HttpMessageConverters实现类
     * @param restTemplateCustomizers        自动注入所有的RestTemplateCustomizer实现类
     * @param restTemplateRequestCustomizers 自动注入所有的RestTemplateRequestCustomizer实现类
     * @return RestTemplateBuilder
     */
    @Bean
    public RestTemplateBuilder restTemplateBuilder(ObjectProvider<HttpMessageConverters> messageConverters,
                                                   ObjectProvider<RestTemplateCustomizer> restTemplateCustomizers,
                                                   ObjectProvider<RestTemplateRequestCustomizer<?>> restTemplateRequestCustomizers) {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        HttpMessageConverters converters = messageConverters.getIfUnique();
        if (converters != null) {
            builder = builder.messageConverters(converters.getConverters());
        }
        // 自定义配置
        // 请注意 此处默认注入了 MetricsRestTemplateCustomizer
        builder = addCustomizers(builder, restTemplateCustomizers, RestTemplateBuilder::customizers);
        // 自定义配置
        builder = addCustomizers(builder, restTemplateRequestCustomizers, RestTemplateBuilder::requestCustomizers);

        builder = builder.connectTimeout(Duration.ofSeconds(3));
        builder = builder.readTimeout(Duration.ofSeconds(20));
        return builder;
    }


    private <T> RestTemplateBuilder addCustomizers(RestTemplateBuilder builder, ObjectProvider<T> objectProvider,
                                                   BiFunction<RestTemplateBuilder, Collection<T>, RestTemplateBuilder> method) {
        List<T> customizers = objectProvider.orderedStream().collect(Collectors.toList());
        if (!customizers.isEmpty()) {
            return method.apply(builder, customizers);
        }
        return builder;
    }

}
