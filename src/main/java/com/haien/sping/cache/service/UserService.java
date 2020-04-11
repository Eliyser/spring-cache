package com.haien.sping.cache.service;

import com.haien.sping.cache.entity.User;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    //假设这是数据库
    Set<User> users=new HashSet<User>();

    @CachePut(value = "user",key = "#user.id") //调用该方法时，会把user.id作为key，返回值为键放入value指定的缓存
    public User save(User user){
        users.add(user);
        return user;
    }

    @Cacheable(value = "user",key="#id") //调用方法前先从缓存中读取，没有再调用方法并把数据放进缓存
    public User findById(final Long id){
        System.out.println("cache miss,invoke find by id,id="+id);
        for(User user:users){
            if(user.getId().equals(id))
                return user;
        }

        return null;
    }

    @CacheEvict(value = "user",key = "#user.id") //移除指定key的数据
    public void delete(User user){
        users.remove(user);
    }

    @CacheEvict(value = "user",allEntries = true) //移除所有数据
    public void deleteAll(){
        users.clear();
    }

    /**
     * @Author haien
     * @Description 若不改变CacheAspectSupport，更改何时获取result（以放入缓存）的条件的话，
     *              下面@Cacheable是绝对不会从缓存中拿的。
     * @Date 2019/3/24
     * @Param [username]
     * @return com.haien.sping.cache.entity.User
     **/
    @Caching(
            //此次查询过后缓存中将有username-user，但没有以id、email为键的条目
            cacheable = {
                    @Cacheable(value = "user",key = "#username")
            },
            //所以同时再存两条进去，这样后面按id来查时就可从缓存查了
            put={
                    @CachePut(value = "user",key = "#result.id",condition = "#result!=null"),
                    @CachePut(value = "user",key = "#result.email",condition = "#result!=null")
            }
    )
    public User findByUsername(final String username){
        System.out.println("cache miss,invoke find by username="+username);

        for(User user:users){
            if(user.getUsername().equals(username))
                return user;
        }

        return null;
    }

    @Caching(
            evict={
                @CacheEvict(value = "user",key = "#user.id",
                  condition = "#root.target.canEvict(#root.caches[0],#user.id,#user.username)",
                  beforeInvocation = true)
            }
    )
    public void conditionalUpdate(User user){
        users.remove(user);
        users.add(user);
    }

    /**
     * @Author haien
     * @Description 判断是否需要删除缓存
     * @Date 2019/3/24
     * @Param [userCache名为“user”的cache, id, username]
     * @return boolean
     **/
    public boolean canEvict(Cache userCache,Long id,String username) {
        //根据id查出条目
        User user=userCache.get(id,User.class);
        //查不到则不用删缓存
        if(user==null)
            return false;

        //查到了但username不是指定的那个也不用删除
        return !user.getUsername().equals(username);
    }
}






































