package com.xwl.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:12
 * @Description 自定义异常枚举类
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
public enum  ExceptionEnum {

    RUN_EXCEPTION(500,"服务器异常"),
    RUN_SUCCESS(200,"操作成功"),


    //业务异常
    USER_NAME_ISNULL(204,"用户不存在,请确认"),
    MOBILE_ISNULL(204,"手机号不存在,请确认"),
    VERCODE_ISFALSE(404,"手机号或验证码错误,请确认"),
    VERCODE_ISHAVE_SURE(204,"验证码已发送过,请勿重发!"),
    DATA_MSG_ISNOTNULL(404,"请补全信息,请确认");

    private int code;
    private String message;
}
