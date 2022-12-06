package online.superh.taskasync.conf;

import online.superh.taskasync.component.GlobalAsyncExcetionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @version: 1.0
 * @author: haro
 * @description: 异步配置类 实现 AsyncConfigurer 接口，实现异步相关的全局配置
 * @date: 2022-12-06 17:19
 */
@Configuration
@EnableAsync
public class AsynConf implements AsyncConfigurer {

    @Autowired
    private GlobalAsyncExcetionHandler exceptionHandler;

    /**
     * 返回 Spring Task 异步任务的默认执行器。这里，我们返回了 null ，并未定义默认执行器。
     * 所以最终会使用 TaskExecutionAutoConfiguration 自动化配置类创建出来的 ThreadPoolTaskExecutor 任务执行器，作为默认执行器
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    /**
     * 实现 #getAsyncUncaughtExceptionHandler() 方法，返回我们定义的 GlobalAsyncExceptionHandler 对象
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return exceptionHandler;
    }


    /**
     * 自定义异步任务执行
     */
    public static final String EXECUTOR_ONE_BEAN_NAME = "executor-one";
    public static final String EXECUTOR_TWO_BEAN_NAME = "executor-two";

    @Configuration
    public static class ExecutorOneConfiguration {

        @Bean(name = EXECUTOR_ONE_BEAN_NAME + "-properties")
        @Primary
        @ConfigurationProperties(prefix = "spring.task.execution-one")
        public TaskExecutionProperties taskExecutionProperties() {
            return new TaskExecutionProperties();
        }

        @Bean(name = EXECUTOR_ONE_BEAN_NAME)
        public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

            TaskExecutorBuilder builder = createTskExecutorBuilder(this.taskExecutionProperties());

            return builder.build();
        }

    }

    @Configuration
    public static class ExecutorTwoConfiguration {

        @Bean(name = EXECUTOR_TWO_BEAN_NAME + "-properties")
        @ConfigurationProperties(prefix = "spring.task.execution-two")
        public TaskExecutionProperties taskExecutionProperties() {
            return new TaskExecutionProperties();
        }

        @Bean(name = EXECUTOR_TWO_BEAN_NAME)
        public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

            TaskExecutorBuilder builder = createTskExecutorBuilder(this.taskExecutionProperties());

            return builder.build();
        }

    }

    private static TaskExecutorBuilder createTskExecutorBuilder(TaskExecutionProperties properties) {

        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        builder = builder.maxPoolSize(pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());

        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());

        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());

        return builder;
    }


}
