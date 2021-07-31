package com.xwl.config.auth.vercode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * @author xueWenLiang
 * @date 2021/7/12 14:03
 * @Description 自定义验证码Provider
 */
@Slf4j
@RequiredArgsConstructor
public class CaptchaAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final RedisTemplate redisTemplate;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    /**
     * 验证码->验证是否正确
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(CaptchaAuthenticationToken.class, authentication,
                () -> messages.getMessage(
                        "CaptchaAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));
        CaptchaAuthenticationToken captchaAuthenticationToken = (CaptchaAuthenticationToken) authentication;
        String phone = captchaAuthenticationToken.getName();
        String rawCode = (String) captchaAuthenticationToken.getCredentials();
        UserDetails userDetails = userDetailsService.loadUserByUsername(phone);
        if (Objects.isNull(userDetails)){
            throw new BadCredentialsException("Bad credentials");
        }
        Object verCode = redisTemplate.opsForValue().get(phone);
        if (StringUtils.isEmpty(verCode)){
            throw new RuntimeException("验证码已过期");
        }else {
            if (!((String)verCode).equals(rawCode)){
               throw new RuntimeException("验证码不正确,请确认");
            }
        }

        return createSuccessAuthentication(authentication,userDetails);
    }
    /**
     * 认证成功将非授信凭据转为授信凭据.
     * 封装用户信息 角色信息。
     *
     * @param authentication the authentication
     * @param user           the user
     * @return the authentication
     */
    protected Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {

        Collection<? extends GrantedAuthority> authorities = authoritiesMapper.mapAuthorities(user.getAuthorities());
        CaptchaAuthenticationToken authenticationToken = new CaptchaAuthenticationToken(user, null, authorities);
        authenticationToken.setDetails(authentication.getDetails());

        return authenticationToken;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
