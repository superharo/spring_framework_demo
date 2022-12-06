package online.superh.quartz.job;

import lombok.extern.slf4j.Slf4j;
import online.superh.quartz.service.DemoService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 14:56
 */
@Slf4j
public class DemoJob01 extends QuartzJobBean {

    /**
     * QuartzJobBean 实现了 org.quartz.Job 接口，提供了 Quartz 每次创建 Job 执行定时逻辑时，将该 Job Bean 的依赖属性注入。
     */

    //每次 DemoJob01 都会被 Quartz 创建出一个新的 Job 对象，执行任务
    private final AtomicInteger counts = new AtomicInteger();

    @Autowired
    private DemoService demoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("[executeInternal][定时第 ({}) 次执行, demoService 为 ({})]",
                counts.incrementAndGet(),
                demoService
        );
    }
}
