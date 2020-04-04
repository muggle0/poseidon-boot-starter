package com.muggle.poseidon.util;

import com.muggle.poseidon.base.exception.SimplePoseidonException;
import com.muggle.poseidon.properties.SecurityMessageProperties;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: poseidon
 * @description: Token工具类
 * @author: muggle
 * @create: 2018-12-22 15:05
 **/

public class JwtTokenUtils {
    private static final long EXPIRATION = 3600000L;

    /**
     * 创建一个带过期时间的token experTime单位为小时
     * @param map
     * @param credential
     * @param experTime
     * @return
     */
    public static String createToken(Map<String, Object> map, String credential, Long experTime) {
        UUID uuid = UUID.randomUUID();
        map.put(SecurityMessageProperties.RANDOM,uuid.toString());
        String compact = Jwts.builder().signWith(SignatureAlgorithm.HS512, credential)
                .setIssuer(SecurityMessageProperties.ISSUER)
                .setSubject(SecurityMessageProperties.SUBJECT)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + experTime * EXPIRATION))
                .setClaims(map)
                .compact();
        return compact;
    }

    /**
     * 获得用户信息所使用的key
     * @param token
     * @param credential
     * @return
     */
    public static String getStoreKey(String token, String credential){
        Claims body = Jwts.parser()
                .setSigningKey(credential)
                .parseClaimsJws(token)
                .getBody();
        String key = body.get("key",String.class);
        return key;
    }

    /**
     * 获得版本号
     * @param token
     * @param credential
     * @return
     */
    public static String getRandom(String token,String credential){
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(credential)
                    .parseClaimsJws(token)
                    .getBody();
            String random = body.get(SecurityMessageProperties.RANDOM,String.class);
            return random;
        }catch (ExpiredJwtException e){
            throw new SimplePoseidonException("用户登录过期，请重新登录");
        }
    }

    /**
     * 根据key获得body 扩展性方法
     * @param token
     * @param credential
     * @param key
     * @return
     */
    public static String getBody(String token,String credential,String key){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(credential)
                    .parseClaimsJws(token)
                    .getBody();
            String body = claims.get(key,String.class);
            return body;
        }catch (ExpiredJwtException e){
            throw new SimplePoseidonException("用户登录过期，请重新登录");
        }

    }

    public static String createToken(String storeKey,Map<String, Object> body, String credential, Long experTime) {
        UUID uuid = UUID.randomUUID();
        body.put(SecurityMessageProperties.RANDOM,uuid.toString());
        body.put("key",storeKey);
        return createToken(body,credential,experTime);
    }
}
/**
 * token使用思路
 *1.token 的实效性
 * 创建token时带过期时间，每次先校验token是否过期
 * 2.token对应的用户信息存储
 * 存储在redis中
 * 3.登录再登出后作废token仍然生效。采用版本号
 * 4.token 的多人登录和单人登录要求灵活切换
 * 数据字典对应的系统设置
 * Redis信息多版本。
 *
 * */