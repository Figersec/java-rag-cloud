package com.kailin.dao.config;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author chengpuhui
 * @Date 2022/1/25
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
                this.strictInsertFill(metaObject, "isDelete", Boolean.class, false);
//                if (metaObject.hasSetter("createUserId") && metaObject.getValue("createUserId") == null) {
//                    log.debug("开始填充创建人......");
//                    this.strictInsertFill(metaObject, "createUserId", String.class, currentUserId());
//                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                // 默认传入不为空则不填充，故这里置为空再填充
                metaObject.setValue("updateTime", null);
                this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
//                if (metaObject.hasSetter("updateUserId") && metaObject.getValue("updateUserId") == null) {
//                    metaObject.setValue("updateUserId", null);
//                    log.debug("开始填充修改人......");
//                    this.strictUpdateFill(metaObject, "updateUserId", String.class, currentUserId());
//                }
            }

        };
    }

    /***************************** 以下为多数据源动态数据源切换配置，不使用可以不配置 *****************************/

    /**
     * 主库
     * @return
     */
    @Bean(name = "chatMainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.chatmain" )
    public DataSource chatMainDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 从库
     * @return
     */
    @Bean(name = "chatSlaveDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.chatslave" )
    public DataSource chatSlaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源切换切面注入</br>
     * 需要配置默认主库，供多库多实例场景使用
     * @return
     */
    @Bean
    public DataSourceAspect dsAspect() {
        return DataSourceAspect.getInstance("chatMainDataSource");
    }

    /**
     * 多数据源配置
     * @param chatMainDataSource
     * @param chatSlaveDataSource
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("chatMainDataSource") DataSource chatMainDataSource, @Qualifier("chatSlaveDataSource") DataSource chatSlaveDataSource) {
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("chatMainDataSource", chatMainDataSource);
        targetDataSources.put("chatSlaveDataSource", chatSlaveDataSource);
        //添加数据源
        multipleDataSource.setTargetDataSources(targetDataSources);
        //设置默认数据源
        multipleDataSource.setDefaultTargetDataSource(chatMainDataSource);
        return multipleDataSource;
    }
}
