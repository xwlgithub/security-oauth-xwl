package com.xwl.config.other;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import sun.plugin2.util.SystemUtil;

import java.net.InetAddress;

/**
 * @author xueWenLiang
 * @date 2021/7/6 9:52
 * @Description 启动生成器
 */
@Configuration
@Slf4j
public class ApplicationInitRunner  implements ApplicationRunner, Ordered {
    private final ApplicationContext applicationContext;
    public ApplicationInitRunner(ApplicationContext applicationContext){
        this.applicationContext=applicationContext;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment env = applicationContext.getEnvironment();
        String contextPath = env.getProperty("server.address") == null ? "" : env.getProperty("server.address");
        log.info("\n______________________________________________________________\n\t" +
                "Java Version: {} \n\t" +
                "Application: {} is running! \n\t" +
                "访问连接: http://{}:{}/{}\n\t" +
                "API接口文档：http://{}:{}/doc.html\n" +
                "______________________________________________________________",
                SystemUtil.getJavaHome(),
                env.getProperty("spring.application.name"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                contextPath,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

    /**
     * Redis 乱码控制
     * @param factory
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE+1;
    }
}
