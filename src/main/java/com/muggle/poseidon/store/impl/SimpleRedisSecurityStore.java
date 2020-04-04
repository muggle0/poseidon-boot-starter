//package com.muggle.poseidon.store.impl;
//
//import com.muggle.poseidon.store.SecurityStore;
//import org.springframework.cglib.core.internal.Function;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * @program: poseidon-cloud-starter
// * @description: redis存储token的仓库
// * @author: muggle
// * @create: 2019-11-04
// **/
//
//public class SimpleRedisSecurityStore implements SecurityStore {
//    private RedisTemplate<String, UserDetails> redisTemplate;
//
//
//
//    @Override
//    public UserDetails getUserdetail(String token) {
//        UserDetails userDetails = redisTemplate.opsForValue().get(token);
//        return userDetails;
//    }
//
//    @Override
//    public void saveUser(UserDetails userDetails, long expirationTime, String token) {
//        redisTemplate.opsForValue().set(token,userDetails,expirationTime);
//    }
//
//    @Override
//    public Boolean cleanToken(String token) {
//        Boolean delete = redisTemplate.delete(token);
//        return delete;
//    }
//
//    @Override
//    public void setExpirationTime(long expirationTime, String token) {
//        redisTemplate.expire(token,expirationTime, TimeUnit.MILLISECONDS);
//    }
//
//    @Override
//    public UserDetails processToken(UserDetails userDetails, String token, Function<UserDetails, UserDetails> function) {
//        return null;
//    }
//
//}
