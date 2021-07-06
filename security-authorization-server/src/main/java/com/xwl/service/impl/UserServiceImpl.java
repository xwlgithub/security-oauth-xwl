package com.xwl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xwl.domain.SecurityUser;
import com.xwl.exception.ExceptionEnum;
import com.xwl.exception.XwlException;
import com.xwl.mapper.UserMapper;
import com.xwl.service.UserService;
import com.xwl.util.ServerUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author xueWenLiang
 * @date 2021/7/5 15:04
 * @Description 用户实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RedisTemplate redisTemplate;

    @Override
    public Boolean sendMobileMsg(String mobile) throws XwlException {
        if (!StringUtils.isEmpty(redisTemplate.opsForValue().get(mobile))) {
            throw new XwlException(ExceptionEnum.VERCODE_ISHAVE_SURE);
        }
        SecurityUser securityUser = userMapper.selectOne(Wrappers.<SecurityUser>lambdaQuery().eq(SecurityUser::getMobile, mobile));
        if (ObjectUtils.isEmpty(securityUser)) {
            throw new XwlException(ExceptionEnum.MOBILE_ISNULL);
        }
        //发送验证码
        String fourRandom = ServerUtils.getFourRandom();
        redisTemplate.opsForValue().set(mobile, fourRandom, 60, TimeUnit.SECONDS);
        return true;
    }
}
