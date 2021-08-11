package com.muggle.poseidon.config;

import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.entity.pojo.OaUserInfo;
import com.muggle.poseidon.mapper.OaUserInfoMapper;
import com.muggle.poseidon.store.SecurityStore;
import com.muggle.poseidon.tool.UserInfoTool;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
* @Description:
* @Author: muggle
* @Date: 2020/11/11
**/
@Component
public class SecurityStoreImpl implements SecurityStore {
@Autowired
RedissonClient redissonClient;
@Autowired
OaUserInfoMapper userInfoMapper;


@Override
public UserDetails getUserdetail(String token) throws BasePoseidonCheckException {
OaUserInfo userInfo= UserInfoTool.parserToken(token);
return userInfo;
}

@Override
public String signUserMessage(UserDetails userDetails) {
String s = UserInfoTool.creakeToken((OaUserInfo) userDetails);
return s;
}

@Override
public Boolean cleanToken(String username) {
return true;
}
}
