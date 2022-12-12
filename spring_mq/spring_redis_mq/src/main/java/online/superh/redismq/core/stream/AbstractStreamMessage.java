package online.superh.redismq.core.stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.superh.redismq.core.message.AbstractRedisMessage;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 15:57
 */
public abstract class AbstractStreamMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Stream Key
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化
    public abstract String getStreamKey();

}
