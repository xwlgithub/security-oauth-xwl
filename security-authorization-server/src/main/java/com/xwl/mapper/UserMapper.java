package com.xwl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwl.domain.SecurityUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xueWenLiang
 * @date 2021/6/30 15:25
 * @Description 描述信息
 */
@Mapper
public interface UserMapper extends BaseMapper {

    /**
     * 根据用户名获取用户信息
     * @param userName
     * @return
     */
    @Select("select *  from security_user where user_name=#{userName}")
    SecurityUser findSecurityUserbyName(@Param("userName")String userName);
}
