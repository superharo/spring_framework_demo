package online.superh.redismq.test.pubsubtest;


import online.superh.redismq.core.RedisMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Menu 菜单相关消息的 Producer
 */
@Component
public class LoginProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送消息
     */
    public void sendLoginMessage() {
        LoginMessage message = new LoginMessage();
        redisMQTemplate.send(message);
    }

}
