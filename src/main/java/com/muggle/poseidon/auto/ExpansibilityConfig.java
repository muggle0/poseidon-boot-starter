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


}
