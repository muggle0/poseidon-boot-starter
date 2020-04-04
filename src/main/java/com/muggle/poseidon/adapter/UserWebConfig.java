//package com.muggle.poseidon.adapter;
//
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.support.config.FastJsonConfig;
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//import com.muggle.poseidon.base.config.interceptor.RequestLockInterceptor;
//import com.muggle.poseidon.helper.RequestLockHelper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.server.ErrorPage;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Configuration
//public class UserWebConfig implements WebMvcConfigurer {
//
//    @Autowired
//    List<RequestLockHelper> helpers;
//
//
//
//    //    解析器
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        // 1.需要先定义一个convert 转换消息的对象
//        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
//        // 2.添加fastJson的配置信息,比如，是否需要格式化返回的json数据
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        // 空值特别处理
//        // WriteNullListAsEmpty 将Collection类型字段的字段空值输出为[]
//        // WriteNullStringAsEmpty 将字符串类型字段的空值输出为空字符串 ""
//        // WriteNullNumberAsZero 将数值类型字段的空值输出为0
//        // WriteNullBooleanAsFalse 将Boolean类型字段的空值输出为false
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteNullStringAsEmpty);
//        // 处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastConverter.setSupportedMediaTypes(fastMediaTypes);
//        // 3.在convert中添加配置信息
//        fastConverter.setFastJsonConfig(fastJsonConfig);
//        // 4.将convert添加到converters当中
//        converters.add(fastConverter);
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RequestLockInterceptor(helpers)).addPathPatterns("/**");
//
//    }
//
//    //    配置错误页面
//    @Bean
//    public WebServerFactoryCustomizer containerCustomizer() {
//        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>() {
//            @Override
//            public void customize(ConfigurableServletWebServerFactory factory) {
//                factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/public/test"));
//            }
//        };
//    }
//}
