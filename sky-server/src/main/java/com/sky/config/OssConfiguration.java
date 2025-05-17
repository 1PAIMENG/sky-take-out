package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean//当容器中没有这个bean的时候才创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("初始化阿里云OSS工具类{}", aliOssProperties);
       return  new AliOssUtil(aliOssProperties.getEndpoint(),
               aliOssProperties.getAccessKeyId(),
               aliOssProperties.getAccessKeySecret(),
               aliOssProperties.getBucketName());
    }
}
