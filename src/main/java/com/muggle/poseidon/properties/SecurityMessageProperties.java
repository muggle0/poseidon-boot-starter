package com.muggle.poseidon.properties;

/**
 * @program: poseidon-cloud-starter
 * @description:
 * @author: muggle
 * @create: 2019-11-05
 **/

public interface SecurityMessageProperties {
    String BAD_TOKEN = "BAD_TOKEN";
    String SUBJECT = "POSEIDON_CLAIM";
    String ISSUER = "security";
    String RANDOM = "RANDOM";
    String LOGOUT = "/logout";
    String LOGIN_URL = "/sign_in";
}
