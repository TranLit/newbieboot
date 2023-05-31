package com.ltb.newbieboot.interceptor;

import com.ltb.newbieboot.annotation.DemoEnabled;
import com.ltb.newbieboot.common.StandardCode;
import com.ltb.newbieboot.config.NewbieBootConfigurationProperties;
import com.ltb.newbieboot.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 小李哞哞
 * @date 2023/5/31
 */
@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Autowired
    private NewbieBootConfigurationProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean demoEnabled = properties.getDemoEnabled();
        // 演示模式未开启就放行
        if (!demoEnabled) {
            return true;
        }

        // 判断请求方法上是否有@DemoEnabled注解
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 有则表示该方法支持演示，放行
            if (handlerMethod.getMethod().isAnnotationPresent(DemoEnabled.class)) {
                return true;
            }
            throw new ServiceException(StandardCode.CODE_600, "演示模式，禁止操作");
        }

        // 其他情况放行
        return true;
    }
}
