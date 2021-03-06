package com.xwl.filter;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xwl.domain.SecurityUser;
import com.xwl.exception.ExceptionEnum;
import com.xwl.exception.XwlException;
import com.xwl.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author xueWenLiang
 * @date 2021/7/5 14:53
 * @Description 自定义认证过滤器
 */
@SuppressWarnings("all")
public class CustomizeAuthenticationProcessFilter extends UsernamePasswordAuthenticationFilter {
    private final String MOBILE_PARAM = "mobile";
    private final String VERFICATION_PARAM = "verificationCode";
    private final String REQUEST_TYPE = "POST";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;

    /**
     * 自定义验证-手机号登录校验
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws XwlException {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        String thisPhone = request.getParameter(MOBILE_PARAM);
        String verificationCode = request.getParameter(VERFICATION_PARAM);
        if (!ObjectUtils.isEmpty(thisPhone) && !ObjectUtils.isEmpty(verificationCode) && request.getMethod().equals(REQUEST_TYPE)) {
            //验证码比对
            String cpde = (String) redisTemplate.opsForValue().get(thisPhone);
            boolean isSuccess = false;
            try {
                isSuccess = cpde.equalsIgnoreCase(verificationCode);
                if (isSuccess==false) {
                    request.setAttribute("error", new XwlException(ExceptionEnum.VERCODE_ISFALSE));
                }
            } catch (Exception e) {
                request.setAttribute("error", new XwlException(ExceptionEnum.VERCODE_ISFALSE));
            }
            if (isSuccess) {
                SecurityUser securityUser = userMapper.selectOne(Wrappers.<SecurityUser>lambdaQuery().eq(SecurityUser::getMobile, thisPhone));
                if (ObjectUtils.isEmpty(securityUser)) {
                    request.setAttribute("error", new XwlException(ExceptionEnum.MOBILE_ISNULL));
                }
                usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(securityUser.getUsername(), securityUser.getPassword());
                this.setDetails(request, usernamePasswordAuthenticationToken);
            }
        } else {
            request.setAttribute("error", new XwlException(ExceptionEnum.DATA_MSG_ISNOTNULL));
        }

        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
