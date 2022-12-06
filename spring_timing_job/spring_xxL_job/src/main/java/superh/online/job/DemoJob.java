package superh.online.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 15:57
 */
@Slf4j
@Component
@JobHandler("demoJob")
public class DemoJob extends IJobHandler {

    /**
     * 继承 XXL-JOB IJobHandler 抽象类，通过实现 #execute(String param) 方法，从而实现定时任务的逻辑。
     * 在方法上，添加 @JobHandler 注解，设置 JobHandler 的名字。后续，我们在调度中心的控制台中，新增任务时，需要使用到这个名字。
     * #execute(String param) 方法的返回结果，为 ReturnT 类型。当返回值符合 “ReturnT.code == ReturnT.SUCCESS_CODE” 时表示任务执行成功，否则表示任务执行失败，而且可以通过 “ReturnT.msg” 回调错误信息给调度中心；从而，在任务逻辑中可以方便的控制任务执行结果。
     * #execute(String param) 方法的方法参数，为调度中心的控制台中，新增任务时，配置的 “任务参数”。一般情况下，不会使用到。
     */

    private final AtomicInteger counts = new AtomicInteger();

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        log.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
        return ReturnT.SUCCESS;
    }
}
