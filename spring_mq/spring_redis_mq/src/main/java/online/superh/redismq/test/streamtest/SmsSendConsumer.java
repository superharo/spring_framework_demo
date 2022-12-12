package online.superh.redismq.test.streamtest;

import lombok.extern.slf4j.Slf4j;
import online.superh.redismq.core.stream.AbstractStreamMessageListener;
import org.springframework.stereotype.Component;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 16:42
 */
@Slf4j
@Component
public class SmsSendConsumer extends AbstractStreamMessageListener<SmsSendMessage> {
    /**
     * 处理消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(SmsSendMessage message) {
        log.info("SmsSendConsumer----------onMessage消费");
    }
}
