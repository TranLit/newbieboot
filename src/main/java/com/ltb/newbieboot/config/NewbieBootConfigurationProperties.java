package com.ltb.newbieboot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 小李哞哞
 * @date 2023/5/31
 */
@Component
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "moumou")
@Data
public class NewbieBootConfigurationProperties {
    @Value("${demoEnabled}")
    private Boolean demoEnabled;
}
