package com.xwl.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xueWenLiang
 * @date 2021/9/17 10:56
 * @Description 表单认证失败错误统一处理
 */
@Slf4j
@SuppressWarnings("all")
@Component
public class CommonAuthenticationFalureHandler  implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("认证失败",request);
        ObjectMapper om=new ObjectMapper();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("title","认证失败");
        map.put("details",exception.getMessage());
        response.getWriter().println(om.writeValueAsString(map));
    }
}
