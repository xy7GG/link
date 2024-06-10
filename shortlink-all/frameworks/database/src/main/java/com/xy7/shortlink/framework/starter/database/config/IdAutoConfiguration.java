package com.xy7.shortlink.framework.starter.database.config;

import com.xy7.shortlink.framework.starter.bases.ApplicationContextHolder;
import com.xy7.shortlink.framework.starter.database.snowflake.RandomWorkIdChoose;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ApplicationContextHolder.class)
public class IdAutoConfiguration {

    /**
     * 随机数构建雪花 WorkId 选择器。如果项目未使用 Redis，使用该选择器
     */
    @Bean
    public RandomWorkIdChoose randomWorkIdChoose() {
        return new RandomWorkIdChoose();
    }

}
