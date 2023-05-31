package com.ltb.newbieboot.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Component
public class CaptchaUtil {

    private static DefaultKaptcha kaptcha;

    private String captchaText;

    static {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.length", "4");

        Config config = new Config(properties);
        kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
    }

    public BufferedImage generateCaptchaImage() {
        return kaptcha.createImage(captchaText);
    }

    public String getCaptchaText() {
        this.captchaText = kaptcha.createText();
        return this.captchaText;
    }
}
