package online.superh.taskasync.service;

import java.util.concurrent.Future;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 17:20
 */
public interface DemoService {

    Integer execute01();

    Integer execute02();

    Future<Integer> execute01AsyncWithFuture();

    Future<Integer> execute02AsyncWithFuture();

}
