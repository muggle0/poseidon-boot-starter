package com.muggle.poseidon.auto;


import com.muggle.poseidon.handler.web.WebResultHandler;
import com.muggle.poseidon.handler.web.WebUrlHandler;
import com.muggle.poseidon.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 二次开发的扩展配置类
 * muggle
 */

@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(TokenService.class)
@Configuration
public class ExpansibilityConfig {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ExpansibilityConfig.class);

    @Bean
    public WebResultHandler webResultHandler(){
        return new WebResultHandler();
    }

    @Bean
    public WebUrlHandler webUrlHandler(){
        return new WebUrlHandler();
    }

}
