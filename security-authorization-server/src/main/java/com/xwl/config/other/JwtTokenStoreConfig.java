package com.xwl.config.other;

import com.xwl.config.common.EnvMentConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.PostConstruct;

/**
 * @author xueWenLiang
 * @date 2021/7/2 15:30
 * @Description 使用Jwt存储token的配置
 */
@Configuration
@SuppressWarnings("all")
public class JwtTokenStoreConfig {
    @Autowired
    private EnvMentConfiguration envMentConfiguration;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        //配置JWT使用的秘钥
        accessTokenConverter.setSigningKey(envMentConfiguration.getSignKey());
        return accessTokenConverter;
    }
}
