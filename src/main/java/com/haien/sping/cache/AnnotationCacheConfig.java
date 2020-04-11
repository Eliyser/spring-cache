package com.haien.sping.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @Author haien
 * @Description 开启注解的CacheManager配置类，代替spring-cache.xml
 * @Date 2019/3/22
 **/
@Configuration
@ComponentScan(basePackages = "com.haien.sping.cache.service")
@EnableCaching(proxyTargetClass = true) //效果同<cache:annotation-driven/>一致，开启注解
public class AnnotationCacheConfig {
    @Bean
    public CacheManager cacheManager(){
        try {
            //2.
            net.sf.ehcache.CacheManager ehcacheCacheManager=new net.sf.ehcache.CacheManager(
                    new ClassPathResource("ehcache.xml").getInputStream()); //1.
            //3.
            EhCacheCacheManager cacheCacheManager=new EhCacheCacheManager(ehcacheCacheManager);

            return cacheCacheManager;
        } catch (IOException e) { //getInputStream()抛出的
            throw new RuntimeException(e);
        }
    }
}
