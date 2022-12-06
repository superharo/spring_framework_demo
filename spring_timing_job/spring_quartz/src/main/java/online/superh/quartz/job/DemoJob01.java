package online.superh.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 14:56
 */
public class DemoJob01 extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }
}
