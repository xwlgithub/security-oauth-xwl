package com.xwl.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xwl.filter.CustomizeAuthenticationProcessFilter;
import com.xwl.filter.MyDaoAuthenticationProvider;
import com.xwl.mapper.UserMapper;
import com.xwl.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:07
 * @Description 安全配置中心
 */
@EnableWebSecurity(debug = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailServiceImpl userDetailService;
    private final UserMapper userMapper;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 认证选择配置
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /**
     * 自定义验证过滤器
     * @return
     */
//    @Bean
//    DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
//        daoAuthenticationProvider.setUserDetailsService(userDetailService);
//        return daoAuthenticationProvider;
//    }
    @Bean
    MyDaoAuthenticationProvider daoAuthenticationProvider() {
        MyDaoAuthenticationProvider daoAuthenticationProvider = new MyDaoAuthenticationProvider(userDetailService,userMapper);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        return daoAuthenticationProvider;
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

    /**
     * 认证成功处理
     * @return
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (httpServletRequest, httpServletResponse, authentication) -> {
            ObjectMapper objectMapper=new ObjectMapper();
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.getWriter().println(objectMapper.writeValueAsString(authentication));
        };
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
     * Username 认证过滤器
     * @return
     * @throws Exception
     */
    @Bean
    public CustomizeAuthenticationProcessFilter customizeAuthenticationProcessFilter() throws Exception {
        CustomizeAuthenticationProcessFilter filter = new CustomizeAuthenticationProcessFilter();
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        filter.setFilterProcessesUrl("/api/xwlLogin");
        filter.setAuthenticationManager(authenticationManager());
        return filter;
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
//                .csrf().disable(); //关跨域保护
        http
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler())
                .and()
                .authorizeRequests()
                .mvcMatchers("/oauth/**", "/login/**", "/logout/**","/author/sendMobileMsg/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and().addFilterAt(customizeAuthenticationProcessFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
    }
}
