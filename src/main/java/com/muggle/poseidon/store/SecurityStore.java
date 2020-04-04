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
    String saveUser(UserDetails userDetails, long expirationTime, String key);

    /**
    * @author muggle
    * @Description: 清除用户登录信息（登出）,根据key清除版本号
    * @Param:
    * @return:
    * @date 2019/11/5 11:42
    */
    Boolean cleanToken(String token);

    /**
    * @author muggle
    * @Description: 设置用户信息（非登录过期）过期时间 （2020.3.4 废弃）
    * @Param:
    * @return:
    * @date 2019/11/5 11:43
    */
    @Deprecated
    void setExpirationTime(long expirationTime, String token);

    /**
    * @author muggle
    * @Description: 对token进行二次处理
    * @Param:
    * @return:
    * @date 2019/11/5 11:43
    */
    UserDetails processToken(UserDetails userDetails, String token, Function<UserDetails, UserDetails> function);

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
