import online.superh.redisson.RedisRedissonApplication;
import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-13 13:50
 */

@SpringBootTest(classes = RedisRedissonApplication.class)
public class RedisSessionTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testStringSetKey() {
        stringRedisTemplate.opsForValue().set("superh", "shuaiPro");
    }

    /**
     *  分布式锁
     */
    private static final String LOCK_KEY = "anylock";

    @Test
    public void testLock() throws InterruptedException {
        // <2.1> 启动一个线程 A ，去占有锁
        new Thread(() -> {
            // 加锁以后 10 秒钟自动解锁
            // 无需调用 unlock 方法手动解锁
            final RLock lock = redissonClient.getLock(LOCK_KEY);
            lock.lock(10, TimeUnit.SECONDS);
        }).start();
        // <2.2> 简单 sleep 1 秒，保证线程 A 成功持有锁
        Thread.sleep(1000L);

        // <3> 尝试加锁，最多等待 100 秒，上锁以后 10 秒自动解锁
        System.out.println(String.format("准备开始获得锁时间：%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        final RLock lock = redissonClient.getLock(LOCK_KEY);
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            System.out.println(String.format("实际获得锁时间：%s", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        } else {
            System.out.println("加锁失败");
        }
    }

    /**
     * 限流器
     * Redisson 提供的是基于滑动窗口 RateLimiter 的实现
     */
    @Test
    public void testLimit() throws InterruptedException {
        // 创建 RRateLimiter 对象
        RRateLimiter rateLimiter = redissonClient.getRateLimiter("myRateLimiter");
        // 初始化：最大流速 = 每 1 秒钟产生 2 个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < 5; i++) {
            System.out.println(String.format("%s：获得锁结果(%s)", simpleDateFormat.format(new Date()), rateLimiter.tryAcquire()));
            Thread.sleep(250L);
        }
    }

}
