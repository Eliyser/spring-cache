package com.haien;

import com.haien.sping.cache.entity.User;
import net.sf.ehcache.CacheManager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class SpringCacheTest {
    @Test
    public void test() throws IOException{
        //2. 把底层cache设置到底层CacheManager；这里使用net.sf.ehcache的CacheManager
        CacheManager ehcacheManager=new CacheManager(
                new ClassPathResource("ehcache.xml").getInputStream()); //1. 创建底层cache
        //3. 把底层CacheManager设置到spring的CacheManager
        EhCacheCacheManager cacheCacheManager=new EhCacheCacheManager();
        cacheCacheManager.setCacheManager(ehcacheManager);

        //根据缓存名字获取缓存;使用spring的cache;“user”cache定义在ehcache.xml中
        Cache cache=cacheCacheManager.getCache("user");

        //往缓存写数据
        Long id=1L;
        User user=new User(id,"zhang","zhang@gmail.com");
        cache.put(id,user); //id为键，user等下被转为json作值
        //从缓冲读数据
        Assert.assertNotNull(cache.get(id,User.class)); //根据id找值，再根据User类信息转json为类
    }
}










































