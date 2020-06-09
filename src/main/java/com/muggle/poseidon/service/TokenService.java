package com.muggle.poseidon.service;

import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;
import com.muggle.poseidon.entity.AuthUrlPathDO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TokenService extends UserDetailsService {

    UserDetails getUserById(Long id);

    /**
    * @author muggle
    * @Description: 权限匹配接口
    * @Param:  path为请求的 url  roleCodes 权限编码集合
    * @return:
    * @date 2019/11/5 18:05
    */
    boolean rooleMatch(Collection<? extends GrantedAuthority> authorities, String path);

    /**
     * 项目初始化url处理方法
     * @param list
     */
    void processUrl(List<AuthUrlPathDO> list);

    /**
     * 登录方法（可以自定义登录逻辑，比如加验证码等等）
     * @param request
     * @param response
     * @return
     * @throws
     */
    UserDetails login(HttpServletRequest request, HttpServletResponse response) throws SimplePoseidonCheckException;
}
