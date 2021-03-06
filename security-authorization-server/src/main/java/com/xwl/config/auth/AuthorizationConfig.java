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
 * @Description ???????????????
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
     * TODO ????????????AuthenticationManager?????????
     * ??????1???Security??????????????? ??????AuthenticationManage (Bean)??????
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
     * ????????????
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // ????????????????????????
                .allowFormAuthenticationForClients()
                // spel????????? ?????????????????????/auth/token_key???????????????
                .tokenKeyAccess("isAuthenticated()")
                // spel????????? ???????????????????????????/auth/check_token???????????????
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * ?????????????????????-jdbc??????
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
     * ?????????->???????????????????????????
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        //token?????????
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer));

        //????????????
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userDetailService)
                //????????????????????????
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter);
    }
}
