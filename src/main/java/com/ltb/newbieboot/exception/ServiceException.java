package com.ltb.newbieboot.exception;

import lombok.Getter;

/**
 * 自定义异常类
 * 业务逻辑异常
 *
 * @author 小李哞哞
 * @date 2023/5/23
 */
@Getter
public class ServiceException extends RuntimeException{
    private String code;
    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }
}
