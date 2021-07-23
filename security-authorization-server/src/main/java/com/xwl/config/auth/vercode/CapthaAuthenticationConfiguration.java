package com.xwl.config.auth.vercode;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwl.service.impl.UserDetailServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * @author xueWenLiang
 * @date 2021/7/12 15:24
 * @Description 验证码认证配置
 */
@Configuration
@SuppressWarnings("all")
public class CapthaAuthenticationConfiguration implements Ordered {
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    private static CapthaAuthenticationConfiguration cn=new CapthaAuthenticationConfiguration();
    @PostConstruct
    public void init(){
       cn.userDetailService=this.userDetailService;
       cn.redisTemplate=this.redisTemplate;
       cn.tokenEndpoint=this.tokenEndpoint;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (httpServletRequest, httpServletResponse, authentication) -> {
            ObjectMapper objectMapper=new ObjectMapper();
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.getWriter().println(objectMapper.writeValueAsString(authentication));
        };
    }
    /**
     * 验证码过滤器构建
     * @return
     */
    @Bean
    public CapthaAuthenticationFilter capthaAuthenticationFilter(){
        CapthaAuthenticationFilter capthaAuthenticationFilter = new CapthaAuthenticationFilter();
        ProviderManager providerManager = new ProviderManager(Collections.singletonList(captchaAuthenticationProvider()));
        capthaAuthenticationFilter.setAuthenticationManager(providerManager);
        capthaAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        capthaAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return capthaAuthenticationFilter;
    }
    @Bean
    public CaptchaAuthenticationProvider captchaAuthenticationProvider(){
        return new CaptchaAuthenticationProvider(cn.userDetailService, cn.redisTemplate);
    }
    /**
     * 认证失败处理
     * @return
     */
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest,httpServletResponse,authenticationException) ->{
            ObjectMapper om=new ObjectMapper();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding("utf-8");
            Map<String, Object> map = new HashMap<>();
            map.put("title","认证失败");
            map.put("details",authenticationException.getMessage());
            httpServletResponse.getWriter().println(om.writeValueAsString(map));
        };
    }

//    /**
//     * 手机号验证码登录成功返回token ->
//     * @return
//     */
//    private AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return (httpServletRequest, httpServletResponse, authentication) -> {
//            ObjectMapper om=new ObjectMapper();
//            Map<String, Object> data = new HashMap<>();
//            Map<String, String> params = new HashMap<>();
//            params.put("grant_type", "client_credentials");
//            params.put("scope", "all");
//            JSONObject jsonObj=new JSONObject(params);
//            String bodys = jsonObj.toString();
//        };
//    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
