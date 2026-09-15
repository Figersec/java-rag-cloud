package com.kailin.dao.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 保留多数据源切面入口，默认走主库。
 */
@Aspect
@Order(0)
public class DataSourceAspect {

    private final String defaultDataSource;

    public DataSourceAspect() {
        this("chatMainDataSource");
    }

    public DataSourceAspect(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public static DataSourceAspect getInstance(String defaultDataSource) {
        return new DataSourceAspect(defaultDataSource);
    }

    public String getDefaultDataSource() {
        return defaultDataSource;
    }
}
