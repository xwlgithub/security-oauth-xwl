package com.xwl.config.granter;

import com.xwl.config.auth.vercode.CaptchaAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xueWenLiang
 * @date 2021/9/15 17:00
 * @Description 描述信息
 */
public class MobileQuickLoginGranter extends AbstractTokenGranter {
    //手机号
    private final String MOBILE_PARAM = "mobile";
    //验证码
    private final String VERFICATION_PARAM = "verificationCode";
    //认证处理
    private final AuthenticationManager authenticationManager;
    protected AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();


    protected MobileQuickLoginGranter(AuthorizationServerTokenServices tokenServices,
                                      ClientDetailsService clientDetailsService,
                                      OAuth2RequestFactory requestFactory,
                                      String grantType,AuthenticationManager authenticationManager) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authenticationManager=authenticationManager;
    }
    protected MobileQuickLoginGranter(AuthorizationServerTokenServices tokenServices,
                                      ClientDetailsService clientDetailsService,
                                      OAuth2RequestFactory requestFactory,AuthenticationManager authenticationManager) {
       this(tokenServices, clientDetailsService, requestFactory, "MOBILE_QUICK",authenticationManager);
    }

    /**
     * 自定义授权业务处理
     * @param client
     * @param tokenRequest
     * @return
     */
    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        //获取请求域
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
       //获取参数
        String thisPhone = request.getParameter(MOBILE_PARAM);
        String verificationCode = request.getParameter(VERFICATION_PARAM);
        //构建待请求认证
        CaptchaAuthenticationToken captchaAuthenticationToken = new CaptchaAuthenticationToken(thisPhone, verificationCode);
        //设置请求体
        captchaAuthenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        //认证完成
        Authentication authenticate = authenticationManager.authenticate(captchaAuthenticationToken);
        //构建token基础信息体
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        //认证token信息体
        return new OAuth2Authentication(storedOAuth2Request, authenticate);
    }
}
