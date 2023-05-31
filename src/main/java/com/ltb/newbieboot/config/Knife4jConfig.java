package com.ltb.newbieboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * knife4j配置类
 *
 * @author 小李哞哞
 * @date 2023/5/21
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public ApiInfo apiInfo() {
        Contact contact = new Contact("李天保", "https://blog.csdn.net/tranlit", "1330622668@qq.com");
        return new ApiInfo(
                "knife4j Demo",
                "API文档",
                "v1.0",
                "https://blog.csdn.net/tranlit",
                contact,
                "许可证",
                "许可证URL",
                new ArrayList<>()
        );
    }

    @Bean
    public Docket docket(ApiInfo apiInfo){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).build();
    }
}