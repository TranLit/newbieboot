package com.ltb.newbieboot.utils;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.ltb.newbieboot.entity.system.User;
import com.ltb.newbieboot.service.system.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Component
public class TokenUtil {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    /**
     * 生成Token
     * @return
     */
    public static String getToken(String id, String sign) {
        // 将id保存到token里作为载荷
        return JWT.create().withAudience(id)
                // 2小时后Token过期
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                // 以password为token的秘钥
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 获取当前登录的用户信息
     * @return
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String id = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(id);
            }
        } catch (JWTDecodeException e) {
            return null;
        }
        return null;
    }
}
