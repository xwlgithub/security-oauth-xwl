package com.xwl.service.impl;

import com.xwl.domain.SecurityUser;
import com.xwl.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:25
 * @Description 描述信息
 */
@Configuration
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private  UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        SecurityUser securityUser = userMapper.findSecurityUserbyName(userName);
        if (ObjectUtils.isEmpty(securityUser)) {
            throw new RuntimeException("用户不存在");
        }
        ;
        return securityUser;
    }
}
