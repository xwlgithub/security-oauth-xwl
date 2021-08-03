//package com.xwl.config.common;
//
//import com.xwl.exception.XwlException;
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
//import org.springframework.boot.web.servlet.error.ErrorAttributes;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author xueWenLiang
// * @date 2021/7/6 16:19
// * @Description 拦截filter日志抛出
// */
//@RestController
//@Component
//public class ToErrorController extends BasicErrorController {
//    private static final String ERROR_MESSAGE = "error";
//    public ToErrorController(ErrorAttributes errorAttributes) {
//        super(errorAttributes, new ErrorProperties());
//    }
//
//    /**
//     * 响应客户端->信息提示
//     * @param request
//     * @return
//     */
//    @Override
//    @RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
//    public ResponseEntity error(HttpServletRequest request) {
//        //从域中获取
//        XwlException myException = (XwlException) request.getAttribute(ERROR_MESSAGE);
//        //返回信息
//        Map<String,Object> map=new HashMap<>();
//        map.put("code",myException.getExceptionEnum().getCode());
//        map.put("message",myException.getExceptionEnum().getMessage());
//        return ResponseEntity.ok(map);
//    }
//}
