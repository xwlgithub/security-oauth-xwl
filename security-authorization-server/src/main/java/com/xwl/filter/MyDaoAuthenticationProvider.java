package com.xwl.filter;

import com.xwl.domain.SecurityUser;
import com.xwl.exception.ExceptionEnum;
import com.xwl.exception.XwlException;
import com.xwl.mapper.UserMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author xueWenLiang
 * @date 2021/7/5 17:57
 * @Description 自定义 验证码验证Provider
 */
public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {
    private  UserMapper userMapper;

    public MyDaoAuthenticationProvider(UserDetailsService userDetailsService,UserMapper userMapper){
        super();
        setUserDetailsService(userDetailsService);
        this.userMapper=userMapper;
    }
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        SecurityUser securityUser = userMapper.findSecurityUserbyName(userDetails.getUsername());
        if (securityUser.getPassword().equals(userDetails.getPassword())) {
            return;
        }
        throw new XwlException(ExceptionEnum.RUN_EXCEPTION);
    }


}
