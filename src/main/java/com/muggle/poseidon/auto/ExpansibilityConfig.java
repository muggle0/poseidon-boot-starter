package com.muggle.poseidon.auto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * 二次开发的扩展配置类
 * muggle
 */

@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "true", matchIfMissing = false)
@Configuration
//@ConditionalOnProperty(TokenService.class)
public class ExpansibilityConfig {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(ExpansibilityConfig.class);

    /**
     * 当使用者 不注入 PoseidonLocker 实现类的时候启动该实例，使用者应该使用redis锁来实现 PoseidonLocker 类
     * @return
     */
  /*  @Bean
    @ConditionalOnMissingBean(PoseidonLocker.class)
    public PoseidonLocker getLocker(){
        log.warn("注入 基于内存简单全局锁（PoseidonLocker），该锁是不安全的 建议替换为 Redis 锁");
        return new SimpleLocker();
    }*/
}
