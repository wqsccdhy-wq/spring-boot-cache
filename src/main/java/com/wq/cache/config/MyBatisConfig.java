package com.wq.cache.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:13
 * @since 2020-2-8 16:13
 */
@Configuration
public class MyBatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {

            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                // MyBatis映射数据库表字段驼峰标识
                configuration.setMapUnderscoreToCamelCase(true);
            }
        };
    }
}
