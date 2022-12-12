package online.superh.springboot.actuator.conf.info;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

import java.util.Collections;

/**
 * @version: 1.0
 * @author: haro
 * @description: 自定义信息
 * @date: 2022-12-12 11:07
 */
public class CustomizeInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("demo",
                Collections.singletonMap("key", "value"));
    }
}
