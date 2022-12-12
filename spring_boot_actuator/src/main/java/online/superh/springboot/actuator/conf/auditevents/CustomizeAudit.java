package online.superh.springboot.actuator.conf.auditevents;

import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 12:03
 */
@Configuration
public class CustomizeAudit {

    /**
     * 为什么我们要配置一个 AuditEventRepository 呢？AuditEventsEndpointAutoConfiguration 自动化配置类，
     * 创建 AuditEventsEndpoint 的前提是，需要有一个 AuditEventRepository Bean 。
     * 因此，我们在 ActuateConfig 中，配置了 InMemoryAuditEventRepository 对象。
     * @return
     */
    @Bean
    public AuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository();
    }

}
