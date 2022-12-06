package online.superh.taskasync.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-07 11:48
 */
@Slf4j
@Component
public class GlobalAsyncExcetionHandler implements AsyncUncaughtExceptionHandler {

    /**
     * AsyncUncaughtExceptionHandler 只能拦截返回类型非 Future 的异步调用方法。
     * 返回类型为 Future 的异步调用方法，需要通过「异步回调」来处理
     * @param ex
     * @param method
     * @param params
     */
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("[handleUncaughtException][method({}) params({}) 发生异常]",
                method, params, ex);
    }
}
