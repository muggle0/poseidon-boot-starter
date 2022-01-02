package com.muggle.poseidon.util;

import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/6/5
 **/
public class IStringUtils extends StringUtils {
    static final String charStr = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static String getCode(int length) {
        Random random = new Random();
        StringBuffer valSb = new StringBuffer();
        int charLength = charStr.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charLength);
            valSb.append(charStr.charAt(index));
        }
        return valSb.toString();
    }
}
