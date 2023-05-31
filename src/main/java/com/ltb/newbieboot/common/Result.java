package com.ltb.newbieboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(StandardCode.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(StandardCode.CODE_200, "", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(StandardCode.CODE_200, msg, data);
    }

    public static Result error() {
        return new Result(StandardCode.CODE_500, "系统错误", null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }
}
