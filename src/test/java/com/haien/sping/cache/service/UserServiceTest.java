package com.haien.sping.cache.service;

import com.haien.sping.cache.AnnotCacheConfWithKeyGenertor;
import com.haien.sping.cache.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AnnotCacheConfWithKeyGenertor.class)
public class UserServiceTest {
    @Resource
    private CacheManager cacheManager;
    @Resource
    private UserService userService;
    private Long id=1L;
    private String username="zhang";
    private User user=new User(id,username,"zhang@gmail.com");

    @Before
    public void setUp(){
        userService.save(user);
    }

    @Test
    public void save() {
        Cache cache=cacheManager.getCache("user");
        Assert.assertNotNull(cache.get(id,User.class));
    }

    @Test
    public void delete() {
        userService.delete(user);
        Cache cache=cacheManager.getCache("user");
        Assert.assertNull(cache.get(id));
    }

    @Test
    public void findById(){
        userService.findById(id);
    }

    @Test
    public void findByUsername(){
        userService.findByUsername(username);
    }
}