package com.muggle.poseidon.util;

import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import com.muggle.poseidon.base.exception.SimplePoseidonCheckException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @program: poseidon-cloud-starter
 * @description: 获取用户信息
 * @author: muggle
 * @create: 2019-11-06
 **/

public class UserInfoUtils {

    public static UserDetails getUserInfo() throws BasePoseidonCheckException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication==null||authentication.getDetails()==null){
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public String getPassword() {
                    return null;
                }

                @Override
                public String getUsername() {
                    return "无用户信息";
                }

                @Override
                public boolean isAccountNonExpired() {
                    return false;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return false;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return false;
                }

                @Override
                public boolean isEnabled() {
                    return false;
                }
            };
        }
        if (!(authentication.getDetails() instanceof UserDetails )){
            throw new SimplePoseidonCheckException("用户登陆信息异常");
        }
        return (UserDetails)authentication.getDetails();
    }
}