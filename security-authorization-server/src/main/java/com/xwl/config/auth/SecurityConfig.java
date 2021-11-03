package com.xwl.config.auth;

import com.xwl.config.auth.vercode.CapthaOauthSecurityConfig;
import com.xwl.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author xueWenLiang
 * @date 2021/6/30 15:07
 * @Description (认证授权)安全配置中心
 */
@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("all")
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailServiceImpl userDetailService;
    private final CapthaOauthSecurityConfig capthaOauthSecurityConfig;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 静态资源放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/error/**","/h2-console/**")
                //放行静态资源
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        ;
    }

    /**
     * http请求配置过滤
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .mvcMatchers("/oauth/**", "/login/**", "/logout/**").permitAll()
//                .and()
//                .httpBasic() //Basic提交
//                .and().addFilterAt(customizeAuthenticationProcessFilter, UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable(); //关跨域保护"/oauth/**""/author/sendMobileMsg/**"

        http
                .formLogin()
                .loginProcessingUrl("/login")
//                .successHandler(commonAuthenticationSuccessHandler)
                //.failureHandler(authenticationFailureHandler())
                .and()
                .authorizeRequests()
                .mvcMatchers("/login/**","/dpk/logins/**", "/logout/**","/author/**","/capthaLogin/**","/callback","/xwl-oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(capthaOauthSecurityConfig)
                .and()
                .httpBasic()
                .and()
//                .addFilterBefore(capthaAuthenticationConfiguration
//                .capthaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }
}
