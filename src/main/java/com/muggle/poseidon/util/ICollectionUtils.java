package com.muggle.poseidon.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 集合处理工具集
 * @Author: muggle
 * @Date: 2020/7/28
 **/
public class ICollectionUtils extends CollectionUtils {         

    /**
     * 寻求优化空间 如自定义map  自定义list
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> map=new HashMap<>();
        List<String> list=new ArrayList<>();
        List<String> collect = map.values().stream().collect(Collectors.toList());
        Map<String, String> collect1 = list.stream().collect(Collectors.toMap(key -> key, value -> value, (k1, k2) -> k2));
    }

}
