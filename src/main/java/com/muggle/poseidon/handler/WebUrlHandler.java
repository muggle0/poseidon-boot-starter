package com.muggle.poseidon.handler;

import com.muggle.poseidon.base.ResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: poseidon-cloud-core
 * @description:
 * @author: muggle
 * @create: 2020-03-12 09:54
 */
@RestController
@ConditionalOnProperty(prefix = "poseidon", name = "auto", havingValue = "true", matchIfMissing = false)
public class WebUrlHandler implements ErrorController {

    public WebUrlHandler() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> WebUrlHandler init <<<<<<<<<<<<<<<<<<<<");
    }

    private static final Logger log = LoggerFactory.getLogger(WebUrlHandler.class);

    @RequestMapping(value = "/public/notfound", produces = "application/json;charset=UTF-8")
    public ResultBean notfund(HttpServletRequest request) {
        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>  客户端访问了错误的页面  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return ResultBean.error("找不到页面", 404);
    }

    @Override
    public String getErrorPath() {
        return "/error_message";
    }


    @RequestMapping("/")
    public ResultBean getMessage() {
        log.debug("请求发起 访问路径： /");
        return ResultBean.successData("poseidon-boot-starter");
    }

    @RequestMapping(value = "/error", produces = "application/json;charset=UTF-8")
    public ResultBean error(HttpServletRequest request) {
        log.error(">>>>>>>>>>>>>>>>>>>  error <<<<<<<<<<<<<<<<<<<<<<<<");
        return ResultBean.error("请求未响应，请稍后再试");
    }
}
