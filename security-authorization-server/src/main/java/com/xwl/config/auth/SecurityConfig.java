package com.xwl.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwl.config.auth.vercode.CaptchaAuthenticationProvider;
import com.xwl.config.auth.vercode.CapthaAuthenticationConfiguration;
import com.xwl.config.auth.vercode.CapthaAuthenticationFilter;
import com.xwl.filter.CustomizeAuthenticationProcessFilter;
import com.xwl.filter.MyDaoAuthenticationProvider;
import com.xwl.mapper.UserMapper;
import com.xwl.service.impl.UserDetailServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:07
 * @Description (????????????)??????????????????
 */
@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
@SuppressWarnings("all")
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailServiceImpl userDetailService;
    private final CapthaAuthenticationConfiguration capthaAuthenticationConfiguration;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * ??????????????????
     *.authenticationProvider(capthaAuthenticationConfiguration.captchaAuthenticationProvider())
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProviderSource());
    }

    /**
     * ????????????????????????
     * @return
     */
    @Bean
    DaoAuthenticationProvider daoAuthenticationProviderSource() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        return daoAuthenticationProvider;
    }
    /**
     * ??????????????????
     * @return
     */
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (httpServletRequest,httpServletResponse,authenticationException) ->{
            ObjectMapper om=new ObjectMapper();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setCharacterEncoding("utf-8");
            Map<String, Object> map = new HashMap<>();
            map.put("title","????????????");
            map.put("details",authenticationException.getMessage());
            httpServletResponse.getWriter().println(om.writeValueAsString(map));
        };
    }

    /**
     * ??????????????????
     * @return
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            System.out.println("??????");
            ObjectMapper objectMapper=new ObjectMapper();
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.getWriter().println(objectMapper.writeValueAsString(authentication));
        };
    }

    /**
     * ??????????????????
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/error/**","/h2-console/**")
                //??????????????????
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
        ;
    }

    /**
     * http??????????????????
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
//                .httpBasic() //Basic??????
//                .and().addFilterAt(customizeAuthenticationProcessFilter, UsernamePasswordAuthenticationFilter.class)
//                .csrf().disable(); //???????????????"/oauth/**""/author/sendMobileMsg/**"

        http
                .formLogin()
                .loginProcessingUrl("/login")
                //.successHandler(authenticationSuccessHandler())
                //.failureHandler(authenticationFailureHandler())
                .and()
                .authorizeRequests()
                .mvcMatchers("/login/**",
                        "/dpk/logins/**",
                        "/logout/**",
                        "/author/sendMobileMsg/**",
                        "/capthaLogin/**",
                        "/callback",
                        "/oauth/authorize/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and().addFilterBefore(capthaAuthenticationConfiguration
                .capthaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }
}
