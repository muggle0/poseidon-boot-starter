package com.muggle.poseidon.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;
import com.muggle.poseidon.entity.AuthUrlPathDO;
import com.muggle.poseidon.entity.pojo.OaUrlInfo;
import com.muggle.poseidon.mapper.OaUrlInfoMapper;
import com.muggle.poseidon.mapper.OaUserInfoMapper;
import com.muggle.poseidon.service.IOaUrlInfoService;
import com.muggle.poseidon.service.TokenService;
import com.muggle.poseidon.service.helper.LoginHelper;
import com.muggle.poseidon.service.manager.UserInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

/**
* @Description:
* @Author: muggle
* @Date: 2020/11/11
**/
@Service
public class TokenServiceImpl implements TokenService {
private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
@Autowired
private UserInfoManager userInfoManager;
@Autowired
private OaUrlInfoMapper urlInfoMapper;
@Autowired
private Map<String, LoginHelper> loginHelperMap;

private AntPathMatcher antPathMatcher=new AntPathMatcher();

@Autowired
IOaUrlInfoService oaUrlInfoService;

@Override
public UserDetails getUserById(Long userId) {

return null;
}

@Override
public boolean rooleMatch(Collection<? extends GrantedAuthority> roles, String url) {
OaUserInfoMapper oaUserInfoMapper = userInfoManager.getOaUserInfoMapper();
List<String> collect = roles.stream().map(role -> role.getAuthority()).collect(Collectors.toList());
    List<String> auths = oaUserInfoMapper.findAuths(collect);
        boolean result=false;
        for (String auth : auths) {
        result=antPathMatcher.match(auth,url);
        }
        return result;
        }

        @Override
        public void processUrl(List<AuthUrlPathDO> list) {
            Set<String> urlSet = new HashSet<>();
                Iterator<AuthUrlPathDO> iterator = list.iterator();
                    while (iterator.hasNext()) {
                    AuthUrlPathDO next = iterator.next();
                    if (next.getMethodURL().contains("error") || next.getMethodURL().contains("swagger") || next.getClassUrl() == null) {
                    iterator.remove();
                    continue;
                    }

                    String url = next.getMethodURL();
                    urlSet.add(next.getClassUrl());
                    urlSet.add(url);
                    }
                    QueryWrapper<OaUrlInfo> urlInfoQuery = new QueryWrapper<>();
                        urlInfoQuery.in("url", urlSet);
                        List<OaUrlInfo> oaUrlInfos = urlInfoMapper.selectList(urlInfoQuery);
                            Map<String, OaUrlInfo> urlMap = oaUrlInfos.stream().collect(Collectors.toMap(OaUrlInfo::getUrl, bean -> bean));
                            List<OaUrlInfo> oaUrlInfoList = new ArrayList<>();
                                for (AuthUrlPathDO authUrlPathDO : list) {
                                String url = authUrlPathDO.getMethodURL();
                                OaUrlInfo dbOaUrlInfo = urlMap.get(url);
                                if (dbOaUrlInfo != null) {
                                continue;
                                }
                                OaUrlInfo parent = urlMap.get(authUrlPathDO.getClassUrl());
                                if (parent == null) {
                                OaUrlInfo parentOaurl = new OaUrlInfo();
                                parentOaurl.setUrl(authUrlPathDO.getClassUrl());
                                parentOaurl.setDescription(authUrlPathDO.getClassDesc());
                                parentOaurl.setGmtCreate(new Date());
                                parentOaurl.setEnable(true);
                                parentOaurl.setRequestType(authUrlPathDO.getRequestType());
                                parentOaurl.setClassName(authUrlPathDO.getClassName());
                                parentOaurl.setId(IdWorker.getId());
                                oaUrlInfoList.add(parentOaurl);
                                urlMap.put(authUrlPathDO.getClassUrl(), parentOaurl);
                                parent = parentOaurl;
                                }
                                OaUrlInfo oaUrlInfo = new OaUrlInfo();
                                oaUrlInfo.setUrl(url);
                                oaUrlInfo.setDescription(authUrlPathDO.getMethodDesc());
                                oaUrlInfo.setGmtCreate(new Date());
                                oaUrlInfo.setEnable(true);
                                oaUrlInfo.setRequestType(authUrlPathDO.getRequestType());
                                oaUrlInfo.setClassName(authUrlPathDO.getClassName());
                                oaUrlInfo.setMethodName(authUrlPathDO.getMethodName());
                                oaUrlInfo.setId(IdWorker.getId());
                                oaUrlInfo.setParentId(parent.getId());
                                oaUrlInfo.setParentUrl(parent.getUrl());
                                oaUrlInfoList.add(oaUrlInfo);
                                }
                                oaUrlInfoService.saveBatch(oaUrlInfoList);
                                }

                                @Override
                                public UserDetails login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws SimplePoseidonCheckException {
                                if (!httpServletRequest.getMethod().equals(HttpMethod.POST.name())) {
                                throw new SimplePoseidonCheckException("非法请求");
                                }
                                Object username =null;
                                Object password = null;
                                Object loginType = null;
                                try ( BufferedReader streamReader = new BufferedReader( new InputStreamReader(httpServletRequest.getInputStream(), "UTF-8"))){
                                StringBuilder responseStrBuilder = new StringBuilder();
                                String inputStr;
                                while ((inputStr = streamReader.readLine()) != null){
                                responseStrBuilder.append(inputStr);
                                }
                                JSONObject jsonObject = JSONObject.parseObject(responseStrBuilder.toString());
                                username=jsonObject.get("username");
                                password=jsonObject.get("password");
                                loginType=jsonObject.get("loginType");
                                } catch (IOException e) {
                                log.error("登录异常",e);
                                throw new SimplePoseidonCheckException("数据读取异常");
                                }
                                if (username == null) {
                                throw new SimplePoseidonCheckException("请填写用户名");
                                }
                                if (password == null) {
                                throw new SimplePoseidonCheckException("请填写密码");
                                }
                                if (loginType == null) {
                                throw new SimplePoseidonCheckException("请选择登录类型");
                                }
                                LoginHelper loginHelper = loginHelperMap.get(loginType.toString().concat("Helper"));
                                if (loginHelper == null) {
                                throw new SimplePoseidonCheckException("登录类型错误");
                                }
                                UserDetails login = loginHelper.login(username.toString(), password.toString());

                                return login;
                                }

                                @Override
                                public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                                return userInfoManager.loadUserByUsername(username);
                                }
                                }
