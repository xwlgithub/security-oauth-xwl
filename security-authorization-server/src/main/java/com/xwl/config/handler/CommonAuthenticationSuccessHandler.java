package com.xwl.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author xueWenLiang
 * @date 2021/9/17 9:53
 * @Description 表单认证成功统一处理
 */
@Slf4j
@Component
public class CommonAuthenticationSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler {

    private final String THE_CUSTOM_CLIENT="vercode-01";
    private final String THE_CUSTOM_CLIENT_SECURITE="client-a-secret";
    @Autowired
    private @NonNull
    ClientDetailsService clientDetailsService;

    @Autowired
    private @NonNull
    PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private @NonNull
    AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private @NonNull
    ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login succeed！");
        // 1. 获取客户端认证信息
        String header = request.getHeader("Authorization");
        String clientId = null;
        String clientSecret=null;
        //走认证授权回执成功信息
        if (!StringUtils.isEmpty(header)) {
            if (header == null || !header.toLowerCase().startsWith("basic ")) {
                throw new UnapprovedClientAuthenticationException("请求头中无客户端信息");
            }

            // 解密请求头
            String[] client = extractAndDecodeHeader(header);
            if (client.length != 2) {
                throw new BadCredentialsException("Invalid basic authentication token");
            }
             clientId = client[0];
             clientSecret = client[1];
        }else {
            clientId=THE_CUSTOM_CLIENT;
            clientSecret=THE_CUSTOM_CLIENT_SECURITE;
        }
        // 获取客户端信息进行对比判断
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null) {
            throw new UnapprovedClientAuthenticationException("客户端信息不存在：" + clientId);
        } else if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("客户端密钥不匹配" + clientSecret);
        }
        // 2. 构建令牌请求
        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(0), clientId, clientDetails.getScope(), clientDetails.getAuthorizedGrantTypes().toString());
        // 3. 创建 oauth2 令牌请求
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        // 4. 获取当前用户信息（省略，前面已经获取过了）
        // 5. 构建用户授权令牌 (省略，已经传过来了)
        // 6. 构建 oauth2 身份验证令牌
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        // 7. 创建令牌
        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        // 直接结束
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(accessToken));
    }
    /**
     * 对请求头进行解密以及解析
     *
     * @param header 请求头
     * @return 客户端信息
     */
    private String[] extractAndDecodeHeader(String header) {
        byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode basic authentication token");
        }
        String token = new String(decoded, StandardCharsets.UTF_8);
        int delimiter = token.indexOf(":");

        if (delimiter == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delimiter), token.substring(delimiter + 1)};
    }
}
