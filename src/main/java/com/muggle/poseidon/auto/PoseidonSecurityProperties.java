package com.muggle.poseidon.auto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-04
 **/

@ConfigurationProperties(prefix="poseidon")
public class PoseidonSecurityProperties {

    /** 放行的url **/
    private List<String> ignorePath;

    public List<String> getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(List<String> ignorePath) {
        this.ignorePath = ignorePath;
    }
}
