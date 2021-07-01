package com.xwl.config.auth;

import com.xwl.config.common.Oauth2Constant;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author xueWenLiang
 * @date 2021/6/30 17:06
 * @Description 自定义客户端认证查询 -
 */
@Service
@Configuration
@Setter
@Slf4j
public class ClientDetailServiceImpl  extends JdbcClientDetailsService {
    @Autowired
    private  DataSource dataSource;
    public ClientDetailServiceImpl(DataSource dataSource) {
       super(dataSource);
    }

    /**
     * 自定义查询ClientDetails对象体
     * @param clientId
     * @return
     * @throws InvalidClientException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        return super.loadClientByClientId(clientId);
    }

    /**
     * 加载数据库DataSource
     * @return
     */
    @Primary
    @Bean
    public ClientDetailServiceImpl clientDetailService() {
        ClientDetailServiceImpl clientDetailsService = new ClientDetailServiceImpl(dataSource);
        return clientDetailsService;
    }




}
