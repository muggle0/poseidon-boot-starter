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
    /** 凭证，用于校验token 要和鉴权平台的令牌一致**/
    private String credential;

    /** 放行的url **/
    private List<String> ignorePath;

    public List<String> getIgnorePath() {
        return ignorePath;
    }

    public void setIgnorePath(List<String> ignorePath) {
        this.ignorePath = ignorePath;
    }

    private Long experTime;

    public Long getExperTime() {
        return experTime;
    }

    public void setExperTime(Long experTime) {
        this.experTime = experTime;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
