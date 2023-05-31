package com.ltb.newbieboot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解应用于处理器方法上，表示这个方法可在演示模式下调用
 *
 * @author 小李哞哞
 * @date 2023/5/31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DemoEnabled {
}
