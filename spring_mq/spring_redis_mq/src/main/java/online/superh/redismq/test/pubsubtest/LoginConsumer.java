package online.superh.redismq.test.pubsubtest;

import lombok.extern.slf4j.Slf4j;
import online.superh.redismq.core.pubsub.AbstractChannelMessageListener;
import org.springframework.stereotype.Component;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 16:43
 */
@Slf4j
@Component
public class LoginConsumer extends AbstractChannelMessageListener<LoginMessage> {

    /**
     * 处理消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(LoginMessage message) {
        log.info("LoginConsumer----onMessage消费");
    }
}
