package com.xwl.config.common;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xueWenLiang
 * @date 2021/7/6 16:50
 * @Description 配置类
 */
@Configuration
@Data
public class EnvMentConfiguration {
    @Value("${signKey}")
    private String signKey;


}
