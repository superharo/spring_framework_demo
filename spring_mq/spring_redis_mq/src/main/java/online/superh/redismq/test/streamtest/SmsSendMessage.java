package online.superh.redismq.test.streamtest;

import lombok.Data;
import online.superh.redismq.core.stream.AbstractStreamMessage;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 16:40
 */
@Data
public class SmsSendMessage extends AbstractStreamMessage {

    /**
     * 获得 Redis Stream Key
     *
     * @return Channel
     */
    @Override
    public String getStreamKey() {
        return "SmsSendMessage";
    }
}
