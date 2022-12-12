package online.superh.redismq.test.pubsubtest;

import lombok.Data;
import online.superh.redismq.core.pubsub.AbstractChannelMessage;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 16:39
 */
@Data
public class LoginMessage extends AbstractChannelMessage {
    /**
     * 获得 Redis Channel
     *
     * @return Channel
     */
    @Override
    public String getChannel() {
        return "LoginMessage";
    }
}
