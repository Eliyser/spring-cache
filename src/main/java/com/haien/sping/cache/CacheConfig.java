package com.haien.sping.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @Author haien
 * @Description 替代spring-cache.xml
 * @Date 2019/3/22
 **/
@Configuration //配置spring容器
@EnableCaching(proxyTargetClass = true)
public class CacheConfig {
    @Bean //往容器注册bean实例
    public CacheManager cacheManager() {
        try {
            //2. 创建底层CacheManager
            net.sf.ehcache.CacheManager ehcacheCacheManager=new net.sf.ehcache.CacheManager(
                    new ClassPathResource("ehcache.xml").getInputStream()); //1. 创建底层cache
            //3. 创建spring的CacheManager
            EhCacheCacheManager cacheCacheManager=new EhCacheCacheManager(ehcacheCacheManager);

            return cacheCacheManager;
        } catch (IOException e) { //getInputStream()抛出的
            throw new RuntimeException(e);
        }
    }
}
