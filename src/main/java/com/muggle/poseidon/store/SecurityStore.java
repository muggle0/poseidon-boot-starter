package com.muggle.poseidon.store;

import com.muggle.poseidon.base.exception.BasePoseidonCheckException;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public interface SecurityStore {

    List<String> ACCESS_PATHS = new CopyOnWriteArrayList();


    /**
     * @throws
     * @author muggle
     * @Description: 根据token获取用户信息，先通过凭证解析token,然后获得storeKey,random,,自定义body,然后根据key 和random(版本号)获取用户信息
     * @Param:
     * @return:
     * @date 2019/11/5 11:40
     */
    UserDetails getUserdetail(String token) throws BasePoseidonCheckException;

    /**
     * @throws
     * @author muggle
     * @Description: 将用户的登陆信息保存到token仓库中（比如Redis 或者mysql）
     * @Param:
     * @return:
     * @date 2019/11/5 11:40
     */
    String signUser(UserDetails userDetails);

    /**
    * @author muggle
    * @Description: 清除用户登录信息（登出）
    * @Param:
    * @return:
    * @date 2019/11/5 11:42
    */
    Boolean cleanToken(String username);

    /**
    * @author muggle
    * @Description: 保存放行的url
    * @Param:
    * @return:
    * @date 2019/11/5 11:44
    */
    static void saveAccessPath(List<String> paths) {
        ACCESS_PATHS.addAll(paths);
    }
}
