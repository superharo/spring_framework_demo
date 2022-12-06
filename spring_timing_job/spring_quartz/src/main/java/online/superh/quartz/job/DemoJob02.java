package online.superh.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 15:07
 */
@Slf4j
public class DemoJob02 extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("[executeInternal][我开始的执行了]");
    }
}
