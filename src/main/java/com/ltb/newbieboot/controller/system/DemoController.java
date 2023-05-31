package com.ltb.newbieboot.controller.system;

import com.ltb.newbieboot.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 测试接口类
 *
 * @author 小李哞哞
 * @date 2023/5/21
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/testRedis")
    public String testRedis() {
        if (!redisUtil.hasKey("test:01")) {
            redisUtil.set("test:01", "这是一个测试数据");
        }
        System.out.println(redisUtil.get("test:01"));
        return "success";
    }
}
