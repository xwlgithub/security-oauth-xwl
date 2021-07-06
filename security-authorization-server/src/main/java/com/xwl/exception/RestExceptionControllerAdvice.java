package com.xwl.exception;


import com.xwl.util.R;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:20
 * @Description 自定义异常拦截统一返回
 */
@Import(RestControllerAdvice.class)
public class RestExceptionControllerAdvice   {


    @ExceptionHandler(XwlException.class)
    public R xwlExceptionHandler(HttpServletRequest request, XwlException e){
        return R.exceptionReturn(e.getExceptionEnum());
    }
}
