package online.superh.redismq.controller;

import online.superh.redismq.test.pubsubtest.LoginProducer;
import online.superh.redismq.test.streamtest.SmsSendProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-12 16:51
 */
@RestController
@RequestMapping("/mq")
public class MqTestController {

    @Autowired
    private LoginProducer loginProducer;
    @Autowired
    private SmsSendProducer smsSendProducer;

    @RequestMapping("/LoginSend")
    public void loginSend(){
        loginProducer.sendLoginMessage();
    }

    @RequestMapping("/smsSend")
    public void smsSend(){
        smsSendProducer.sendSmsSendMessage();
    }

}
