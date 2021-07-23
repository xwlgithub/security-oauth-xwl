package com.xwl.config.auth.vercode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xueWenLiang
 * @date 2021/7/12 14:26
 * @Description 验证码过滤器
 */
public class CapthaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final String MOBILE_PARAM = "mobile";
    private final String VERFICATION_PARAM = "verificationCode";
    private final String REQUEST_TYPE = "POST";

    public CapthaAuthenticationFilter() {
        super(new AntPathRequestMatcher("/capthaLogin", "POST"));
    }

    /**
     * 过滤器验证-验证码
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals(REQUEST_TYPE)) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String thisPhone = request.getParameter(MOBILE_PARAM);
        String verificationCode = request.getParameter(VERFICATION_PARAM);
        CaptchaAuthenticationToken captchaAuthenticationToken = new CaptchaAuthenticationToken(thisPhone, verificationCode);
        setDetails(request, captchaAuthenticationToken);
        return this.getAuthenticationManager().authenticate(captchaAuthenticationToken);
    }
    protected void setDetails(HttpServletRequest request,
                              CaptchaAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }
//    @Autowired
//    @Override
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        super.setAuthenticationManager(authenticationManager);
//    }
}
