package com.xwl.config.auth;

//import com.xwl.config.auth.vercode.CaptchaTokenGranter;
import com.xwl.config.common.Oauth2Constant;
import com.xwl.config.other.JwtTokenEnhancer;
import com.xwl.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author xueWenLiang
 * @date 2021/6/30 16:32
 * @Description 授权服务器
 */
@Configuration
@EnableAuthorizationServer
@SuppressWarnings("all")
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private ClientDetailServiceImpl clientDetailService;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    /**
     * TODO 需要注入AuthenticationManager才好使
     * 疑点1：Security配置文件中 注入AuthenticationManage (Bean)对象
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Qualifier("jwtTokenStore")
    private TokenStore tokenStore;
    @Autowired
    private JwtTokenEnhancer jwtTokenEnhancer;
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全适配
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 允许表单认证请求
                .allowFormAuthenticationForClients()
                // spel表达式 访问公钥端点（/auth/token_key）需要认证
                .tokenKeyAccess("isAuthenticated()")
                // spel表达式 访问令牌解析端点（/auth/check_token）需要认证
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 客户端访问授权-jdbc查询
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clientDetailService.setSelectClientDetailsSql(Oauth2Constant.SELECT_CLIENT_DETAIL_SQL);
        clientDetailService.setFindClientDetailsSql(Oauth2Constant.FIND_CLIENT_DETAIL_SQL);
        clients.withClientDetails(clientDetailService);
    }

    /**
     * 授权时->流程节点增强处理器
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        //token增强链
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer));

        //认证校验
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                //配置令牌存储策略
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);
    }

    /**
     * 授权方式->()
     * 获取常用两种授权方式(客户端秘钥方式，授权码模式)->
     * @param endpoints
     * @return
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints){
        //获取已经存在的授权方式
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        //添加自定义授权模式
        return  new CompositeTokenGranter(granters);
    }
}
