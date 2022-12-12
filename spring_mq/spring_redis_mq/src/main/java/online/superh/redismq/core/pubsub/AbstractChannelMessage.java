package online.superh.redismq.core.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.superh.redismq.core.message.AbstractRedisMessage;

/**
 * @version: 1.0
 * @author: haro
 * @description: Redis Channel Message 抽象类
 * @date: 2022-12-12 15:55
 */
public abstract class AbstractChannelMessage extends AbstractRedisMessage {

    /**
     * 获得 Redis Channel
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化。原因是，Redis 发布 Channel 消息的时候，已经会指定。
    public abstract String getChannel();

}
