package com.muggle.poseidon.adapter;

import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import com.muggle.poseidon.handler.web.CodeController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description
 * Date 2021/3/28
 * Created by muggle
 */
@Configuration
@EnableConfigurationProperties(PoseidonSecurityProperties.class)
@ConditionalOnMissingBean(PoseidonAuthConfigAdapter.class)
public class DisableSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }

    @Bean
    @Profile("local")
    public CodeController codeController(){
        return new CodeController();
    }

    @Bean
    @Profile("local")
    public GeneratorWebMvcConfig generatorWebMvcConfig(){
        return new GeneratorWebMvcConfig();
    }

}
