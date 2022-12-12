package online.superh.springboot.actuator.conf.httptrace;

import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 11:53
 */
@Configuration
public class CustomizeHttpTrace {

    /**
     * 为什么我们要配置一个 HttpTraceRepository 呢？HttpTraceEndpointAutoConfiguration 自动化配置类，
     * 创建 HttpTraceEndpoint 的前提是，需要有一个 HttpTraceRepository Bean 。
     * 因此，我们在 ActuateConfig 中，配置了 InMemoryHttpTraceRepository 对象。
     * @return
     */
    @Bean
    public HttpTraceRepository httpTraceRepository() {
        // 如果希望存储 HttpTrace 到数据库中，可以自定义实现 HttpTraceRepository。
        return new InMemoryHttpTraceRepository();
    }

}
