package online.superh.redismq.test.streamtest;


import lombok.extern.slf4j.Slf4j;
import online.superh.redismq.core.RedisMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Sms 短信相关消息的 Producer
 *
 * @author zzf
 * @date 2021/3/9 16:35
 */
@Slf4j
@Component
public class SmsSendProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送消息
     */
    public void sendSmsSendMessage() {
        SmsSendMessage message = new SmsSendMessage();
        redisMQTemplate.send(message);
    }

}
