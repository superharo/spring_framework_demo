package online.superh.springboot.actuator.conf.info;

import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.info.MapInfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: haro
 * @description: 自定义应用信息
 * @date: 2022-12-12 10:56
 */
@Configuration
public class CustomizeInfoIndicator {

    @Bean
    public InfoContributor exampleInfo() {
        // return new SimpleInfoContributor("example",
        //         Collections.singletonMap("自定义SimpleInfoContributor", "用于提供一个应用的信息"));
        // MapInfoContributor，用于提供 map 结构的应用信息
        Map<String, Object> map = new HashMap<>();
        map.put("MapInfoContributor","用于提供 map 结构的应用信息");
        return new MapInfoContributor(map);
    }

}
