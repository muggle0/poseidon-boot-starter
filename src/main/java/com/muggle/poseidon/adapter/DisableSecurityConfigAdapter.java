package com.muggle.poseidon.adapter;

import com.muggle.poseidon.auto.PoseidonSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description
 * Date 2021/3/28
 * Created by muggle
 */
@Configuration
@EnableConfigurationProperties(PoseidonSecurityProperties.class)
@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "false", matchIfMissing = false)
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

}
