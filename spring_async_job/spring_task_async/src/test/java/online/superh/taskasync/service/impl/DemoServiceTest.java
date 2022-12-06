package online.superh.taskasync.service.impl;

import lombok.extern.slf4j.Slf4j;
import online.superh.taskasync.TaskAsyncApplication;
import online.superh.taskasync.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@SpringBootTest(classes = TaskAsyncApplication.class)
class DemoServiceTest {

    @Autowired
    private DemoService demoService;

    /**
     * 异步
     */
    @Test
    public void test01() {
        long now = System.currentTimeMillis();
        log.info("[test01][开始执行]");

        demoService.execute01();
        demoService.execute02();

        log.info("[test01][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

    /**
     * 异步 + 等待结果
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test02() throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        log.info("[test02][开始执行]");

        Future<Integer> execute01Result = demoService.execute01AsyncWithFuture();
        Future<Integer> execute02Result = demoService.execute02AsyncWithFuture();

        // 阻塞等待结果
        execute01Result.get();
        execute02Result.get();

        log.info("[test02][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

    /**
     * 异步 + 回调
     */
    @Test
    public void test03() throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        log.info("[task04][开始执行]");

        ListenableFuture<Integer> execute01Result = demoService.execute01AsyncWithListenableFuture();
        log.info("[task04][execute01Result 的类型是：({})]",execute01Result.getClass().getSimpleName());
        /*
         * 增加成功的回调和失败的回调
         * addCallback(SuccessCallback<? super T> successCallback, FailureCallback failureCallback);
         */
        execute01Result.addCallback(
                result -> log.info("[onSuccess][result: {}]", result),
                ex -> log.info("[onFailure][发生异常]", ex)
        );

        /*
         * 增加成功和失败的统一回调
         * addCallback(ListenableFutureCallback<? super T> callback);
         */
        execute01Result.addCallback(new ListenableFutureCallback<Integer>() {

            @Override
            public void onSuccess(Integer result) {
                log.info("[全局 onSuccess][result: {}]", result);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("[全局 onFailure][发生异常]", ex);
            }
        });
        // 获取结果
        execute01Result.get();
        log.info("[task04][结束执行，消耗时长 {} 毫秒]", System.currentTimeMillis() - now);
    }

    @Test
    public void testAsyncExceptionHanlder(){
        demoService.throwException();
    }

    @Test
    public void testExecute01Customize(){
        demoService.execute01Customize();
    }

}