package com.kailin.dao.config;

/**
 * 当前线程使用的数据源名称。
 */
public final class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    private DataSourceContextHolder() {
    }

    public static void set(String dataSource) {
        CONTEXT.set(dataSource);
    }

    public static String get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
