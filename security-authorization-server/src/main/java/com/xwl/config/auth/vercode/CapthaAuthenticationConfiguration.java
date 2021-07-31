package com.xwl.config.auth.vercode;
import com.alibaba.nacos.client.utils.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.MapUtils;
import com.xwl.config.auth.ClientDetailServiceImpl;
import com.xwl.service.impl.UserDetailServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
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
public class CapthaAuthenticationConfiguration {
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private ClientDetailServiceImpl clientDetailService;
    private static CapthaAuthenticationConfiguration cn=new CapthaAuthenticationConfiguration();
    @PostConstruct
    public void init(){
       cn.userDetailService=this.userDetailService;
       cn.redisTemplate=this.redisTemplate;
       cn.tokenEndpoint=this.tokenEndpoint;
       cn.clientDetailService=this.clientDetailService;
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
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return (httpServletRequest, httpServletResponse, authentication) -> {
            ObjectMapper objectMapper=new ObjectMapper();
            TokenRequest tokenRequest = new TokenRequest(null, "vercode-01", Arrays.asList("all"), "authorization_code,password,refresh_token,client_credentials");
            ClientDetails clientDetails = cn.clientDetailService.loadClientByClientId("vercode-01");
            OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

           // OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(oAuth2Authentication));
//            ObjectMapper objectMapper=new ObjectMapper();
//            httpServletResponse.setStatus(HttpStatus.OK.value());
//            httpServletResponse.getWriter().println(objectMapper.writeValueAsString(authentication));
        };
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

}
