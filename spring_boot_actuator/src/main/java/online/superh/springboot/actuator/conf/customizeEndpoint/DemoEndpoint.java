package online.superh.springboot.actuator.conf.customizeEndpoint;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: haro
 * @description:  Actuator 自定义端点
 * @date: 2022-12-12 14:06
 */

@Component
// 在类上，添加 @Endpoint 注解，表示它是一个 Actuator Endpoint 端点。同时，设置 id = "demo" ，则可以使用 /actuator/demo 地址进行访问。
@Endpoint(id = "demo")
public class DemoEndpoint {

    /**
     * 在 #hello() 方法上，添加 @ReadOperation 注解，表示它是读操作，对应 GET 请求。
     * 可以使用 @WriteOperation 注解，对应 POST 请求。
     * 可以使用 @DeleteOperation 注解，对应 DELETE 请求。
     * @return
     */
    @ReadOperation
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();
        result.put("作者", "haro");
        result.put("帅哥", "true");
        return result;
    }

}
