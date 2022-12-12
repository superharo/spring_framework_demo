package online.superh.springboot.actuator.conf.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * @version: 1.0
 * @author: haro
 * @description: 自定义健康指示器
 * @date: 2022-12-12 10:18
 */

@Component
public class CustomizeHealthIndicator extends AbstractHealthIndicator {


    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // <1> 判断是否健康
        boolean success = checkSuccess();

        // <2> 如果健康，则标记状态为 UP
        if (success) {
            builder.up().build();
            return;
        }

        // <3> 如果不健康，则标记状态为 DOWN
        builder.down().withDetail("msg", "自定义健康指示器示例");
    }

    private boolean checkSuccess() {
        return true;
    }
}
