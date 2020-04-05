//package com.muggle.poseidon.listener;
//
//import com.muggle.poseidon.properties.DingParamProperties;
//import com.muggle.poseidon.properties.DingSendEnum;
//import com.muggle.poseidon.util.DingUtil;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
///**
// * @Description:
// * @Author: muggle
// * @Date: 2020/4/2$
// **/
//
//
//@Component
//public class ExceptionListener implements ApplicationListener<ExceptionEvent> {
//
//
//    @Override
//    public void onApplicationEvent(ExceptionEvent event) {
//        // todo
//        int i=0;
//        while (i<3) {
//            try {
//                String message = event.getMessage();
//                break;
//            } catch (Exception e) {
//                i++;
//            }
//        }
//    }
//}
