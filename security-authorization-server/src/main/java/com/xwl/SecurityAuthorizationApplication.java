package com.xwl;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author xueWenLiang
 * @date 2021/6/29 17:45
 * @Description 授权服务器启动类
 */
@SpringCloudApplication
@MapperScan({"com.xwl.mapper"})
public class SecurityAuthorizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthorizationApplication.class, args);
    }
}
