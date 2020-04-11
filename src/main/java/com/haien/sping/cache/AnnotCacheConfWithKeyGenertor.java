package com.haien.sping.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackages = "com.haien.sping.cache.service")
@EnableCaching(proxyTargetClass = true)
public class AnnotCacheConfWithKeyGenertor implements CachingConfigurer {
    @Bean
    public CacheManager cacheManager() {
        try {
            net.sf.ehcache.CacheManager ehcacheCacheManager=new net.sf.ehcache.CacheManager(
                    new ClassPathResource("ehcache.xml").getInputStream()
            );
            EhCacheCacheManager cacheCacheManager=new EhCacheCacheManager(ehcacheCacheManager);

            return cacheCacheManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }
}
