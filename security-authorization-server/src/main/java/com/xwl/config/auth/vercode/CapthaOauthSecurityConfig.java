package com.xwl.config.auth.vercode;

import com.xwl.config.handler.CommonAuthenticationFalureHandler;
import com.xwl.config.handler.CommonAuthenticationSuccessHandler;
import com.xwl.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author xueWenLiang
 * @date 2021/9/16 16:46
 * @Description 快捷登录配置
 */
@Component
@SuppressWarnings("all")
public class CapthaOauthSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CommonAuthenticationSuccessHandler commonAuthenticationSuccessHandler;
    @Autowired
    private CommonAuthenticationFalureHandler commonAuthenticationFalureHandler;


    @Override
    public void configure(HttpSecurity builder) throws Exception {
        CapthaAuthenticationFilter capthaAuthenticationFilter = new CapthaAuthenticationFilter();
        capthaAuthenticationFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        capthaAuthenticationFilter.setAuthenticationSuccessHandler(commonAuthenticationSuccessHandler);
        capthaAuthenticationFilter.setAuthenticationFailureHandler(commonAuthenticationFalureHandler);
        CaptchaAuthenticationProvider captchaAuthenticationProvider=new CaptchaAuthenticationProvider(userDetailService,redisTemplate);
        builder.authenticationProvider(captchaAuthenticationProvider)
                .addFilterBefore(capthaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
;    }
}
