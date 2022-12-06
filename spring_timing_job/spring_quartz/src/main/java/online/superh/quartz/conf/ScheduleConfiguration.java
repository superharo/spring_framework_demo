package online.superh.quartz.conf;

import online.superh.quartz.job.DemoJob01;
import online.superh.quartz.job.DemoJob02;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-06 15:09
 */
@Configuration
public class ScheduleConfiguration {

    /**
     *  因为 JobDetail 和 Trigger 一般是成双成对出现，习惯配置成一个 Configuration 配置类
     */
    public static class DemoJob01Configuration {

        /**
         *  创建 DemoJob01 的 JobDetail Bean 对象。
         * @return
         */
        @Bean
        public JobDetail demoJob01() {
            return JobBuilder.newJob(DemoJob01.class)
                    .withIdentity("demoJob01")
                    .storeDurably()
                    .build();
        }

        /**
         * 创建 DemoJob01 的 Trigger Bean 对象。
         * 其中，我们使用 SimpleScheduleBuilder 简单的调度计划的构造器，创建了每 5 秒执行一次，无限重复的调度计划。
         * @return
         */
        @Bean
        public Trigger demoJob01Trigger() {

            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5)
                    .repeatForever();

            return TriggerBuilder.newTrigger()
                    .forJob(demoJob01())
                    .withIdentity("demoJob01Trigger")
                    .withSchedule(scheduleBuilder)
                    .build();
        }

    }

    public static class DemoJob02Configuration {

        @Bean
        public JobDetail demoJob02() {
            return JobBuilder.newJob(DemoJob02.class)
                    .withIdentity("demoJob02")
                    .storeDurably()
                    .build();
        }

        @Bean
        public Trigger demoJob02Trigger() {

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");

            return TriggerBuilder.newTrigger()
                    .forJob(demoJob02())
                    .withIdentity("demoJob02Trigger")
                    .withSchedule(scheduleBuilder)
                    .build();
        }

    }


}
