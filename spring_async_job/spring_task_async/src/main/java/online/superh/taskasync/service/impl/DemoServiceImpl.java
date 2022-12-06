package online.superh.taskasync.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.superh.taskasync.service.DemoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 17:22
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Async
    @Override
    public Integer execute01() {
        log.info("[execute01]");
        log.info("---------ThreadName:{}",Thread.currentThread().getName());
        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    @Async
    @Override
    public Integer execute02() {
        log.info("[execute02]");
        log.info("---------ThreadName:{}",Thread.currentThread().getName());
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 2;
    }

    @Async
    @Override
    public Future<Integer> execute01AsyncWithFuture() {
        return AsyncResult.forValue(this.execute01());
    }

    @Async
    @Override
    public Future<Integer> execute02AsyncWithFuture() {
        return AsyncResult.forValue(this.execute02());
    }
}
