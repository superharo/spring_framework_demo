package online.superh.redismq.core.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: 1.0
 * @author: haro
 * @description: 消息抽象基类
 * @date: 2022-12-12 15:53
 */
public abstract class AbstractRedisMessage {

    /**
     * 头
     */
    private Map<String, String> headers = new HashMap<>();

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

}
