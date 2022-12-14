package online.superh.springsecurity.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 9:41
 */
@Configuration
@EnableRedisHttpSession // 自动化配置 Spring Session 使用 Redis 作为数据源
public class SessionConfig {

}
