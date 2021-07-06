package com.xwl.util;

import com.xwl.exception.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:15
 * @Description 描述信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private int code;
    private String message;
    private Boolean isSuccess;
    private T data;


    /**
     * 无参成功返回体
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> OK() {
        return result(null, ExceptionEnum.RUN_SUCCESS.getCode(), ExceptionEnum.RUN_SUCCESS.getMessage(), true);
    }

    /**
     * 有数据返回体
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> OK(T data) {
        return result(data, ExceptionEnum.RUN_SUCCESS.getCode(), ExceptionEnum.RUN_SUCCESS.getMessage(), true);
    }

    /**
     * 无备注服务器异常
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> serverError() {
        return result(null, ExceptionEnum.RUN_EXCEPTION.getCode(), ExceptionEnum.RUN_EXCEPTION.getMessage(), false);
    }

    /**
     * 自定义异常回执信息
     *
     * @param <T>
     * @return
     */
    public static <T> R<T> exceptionReturn(ExceptionEnum exceptionEnum) {
        return result(null, exceptionEnum.getCode(), exceptionEnum.getMessage(), false);
    }

    /**
     * 统一封装异常体
     *
     * @param data
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T> R<T> result(T data, int code, String message, Boolean isSuccess) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(message);
        r.setIsSuccess(isSuccess);
        return r;
    }
}
