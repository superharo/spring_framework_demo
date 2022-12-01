> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.iocoder.cn](https://www.iocoder.cn/Spring-Boot/Job/?yudao)

> 摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/Job/ 「芋道源码」欢迎转载，保留摘要，谢谢！ 1. 概述 2. 快速入门 Spring Task 3. 快速入......

<div id="locker" style="display: block;"> <div class="mask"></div> <div class="info"> <div> <p class="text-center" style="color: black;"> 扫码关注公众号：<span style="color: #E9405A; font-weight: bold;"> 芋道源码 </span></p> <p class="text-center"></p> <p class="text-center"> <span style="color: black;"> 发送：</span> <span class="token" style="color: #e9415a; font-weight: bold; font-size: 17px; margin-bottom: 45px;"> 百事可乐 </span> <br> <span style="color: black;"> 获取永久解锁本站全部文章的链接 </span> </p> </div> <div class="text-center"> <img class="code-img" style="width: 300px" src="/images/common/erweima.jpg"> </div> </div> </div>

摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/Job/ 「芋道源码」欢迎转载，保留摘要，谢谢！

*   [1. 概述](http://www.iocoder.cn/Spring-Boot/Job/)
*   [2. 快速入门 Spring Task](http://www.iocoder.cn/Spring-Boot/Job/)
*   [3. 快速入门 Quartz 单机](http://www.iocoder.cn/Spring-Boot/Job/)
*   [4. 再次入门 Quartz 集群](http://www.iocoder.cn/Spring-Boot/Job/)
*   [5. 快速入门 XXL-JOB](http://www.iocoder.cn/Spring-Boot/Job/)
*   [6. 快速入门 Elastic-Job](http://www.iocoder.cn/Spring-Boot/Job/)
*   [666. 彩蛋](http://www.iocoder.cn/Spring-Boot/Job/)

> 本文在提供完整代码示例，可见 [https://github.com/YunaiV/SpringBoot-Labs](https://github.com/YunaiV/SpringBoot-Labs) 的 [lab-28](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28) 目录。
> 
> 原创不易，给点个 [Star](https://github.com/YunaiV/SpringBoot-Labs/stargazers) 嘿，一起冲鸭！

在产品的色彩斑斓的黑的需求中，有存在一类需求，是需要去**定时执行**的，此时就需要使用到**定时任务**。例如说，每分钟扫描超时支付的订单，每小时清理一次日志文件，每天统计前一天的数据并生成报表，每个月月初的工资单的推送，每年一次的生日提醒等等。

> 其中，艿艿最喜欢 “每个月月初的工资单的推送”，你呢？

在 JDK 中，内置了两个类，可以实现定时任务的功能：

*   [`java.util.Timer`](https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/sun/misc/Timer.java) ：可以通过创建 [`java.util.TimerTask`](https://github.com/frohoff/jdk8u-jdk/blob/master/src/share/classes/sun/misc/TimerTask.java) 调度任务，在同一个线程中**串行**执行，相互影响。也就是说，对于同一个 Timer 里的多个 TimerTask 任务，如果一个 TimerTask 任务在执行中，其它 TimerTask 即使到达执行的时间，也只能排队等待。因为 Timer 是串行的，同时存在 [坑坑](https://blog.csdn.net/qincidong/article/details/82526458) ，所以后来 JDK 又推出了 ScheduledExecutorService ，Timer 也基本不再使用。
*   [`java.util.concurrent.ScheduledExecutorService`](https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/java/util/concurrent/ScheduledExecutorService.java) ：在 JDK 1.5 新增，基于线程池设计的定时任务类，每个调度任务都会被分配到线程池中**并发**执行，互不影响。这样，ScheduledExecutorService 就解决了 Timer 串行的问题。

在日常开发中，我们很少直接使用 Timer 或 ScheduledExecutorService 来实现定时任务的需求。主要有几点原因：

*   它们仅支持按照指定频率，不**直接**支持指定时间的定时调度，需要我们结合 Calendar 自行计算，才能实现复杂时间的调度。例如说，每天、每周五、2019-11-11 等等。
*   它们是进程级别，而我们为了实现定时任务的高可用，需要部署多个进程。此时需要等多考虑，多个进程下，同一个任务在相同时刻，不能重复执行。
*   项目可能存在定时任务较多，需要统一的管理，此时不得不进行二次封装。

所以，一般情况下，我们会选择专业的**调度任务中间件**。

> 关于 “**任务**” 的叫法，也有叫 “**作业**” 的。在英文上，有 Task 也有 Job 。本质是一样的，本文两种都会用。
> 
> 然后，一般来说是调度任务，定时执行。所以胖友会在本文，或者其它文章中，会看到 “调度” 或“定时”的字眼儿。

在 Spring 体系中，内置了两种定时任务的解决方案：

*   第一种，[Spring Framework](https://github.com/spring-projects/spring-framework) 的 [Spring Task](https://github.com/spring-projects/spring-framework/tree/master/spring-core/src/main/java/org/springframework/core/task) 模块，提供了**轻量级**的定时任务的实现。
    
*   第二种，[Spring Boot](https://github.com/spring-projects/spring-boot) 2.0 版本，整合了 [Quartz](http://www.quartz-scheduler.org/) 作业调度框架，提供了**功能强大**的定时任务的实现。
    
    > 注：Spring Framework 已经内置了 Quartz 的整合。Spring Boot 1.X 版本未提供 Quartz 的自动化配置，而 2.X 版本提供了支持。
    

在 Java 生态中，还有非常多优秀的开源的调度任务中间件：

*   [Elastic-Job](http://elasticjob.io/)
    
    > 唯品会基于 Elastic-Job 之上，演化出了 [Saturn](https://github.com/vipshop/Saturn) 项目。
    
*   [Apache DolphinScheduler](https://dolphinscheduler.apache.org/zh-cn/)
    
*   [XXL-JOB](https://www.xuxueli.com/xxl-job/)
    

目前国内采用 Elastic-Job 和 XXL-JOB 为主。从艿艿了解到的情况，使用 XXL-JOB 的团队会更多一些，主要是上手较为容易，运维功能更为完善。

本文，我们会按照 Spring Task、Quartz、XXL-JOB 的顺序，进行分别入门。而在文章的结尾，会简单聊聊分布式定时任务的实现原理。

> 示例代码对应仓库：[lab-28-task-demo](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-demo) 。

考虑到实际场景下，我们很少使用 Spring Task ，所以本小节会写的比较简洁。如果对 Spring Task 比较感兴趣的胖友，可以自己去阅读 [《Spring Framework Documentation —— Task Execution and Scheduling》](https://docs.spring.io/spring/docs/5.2.x/spring-framework-reference/integration.html#scheduling) 文档，里面有 Spring Task 相关的详细文档。

在本小节，我们会使用 Spring Task 功能，实现一个每 2 秒打印一行执行日志的定时任务。

2.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> 
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-28-task-demo</artifactId>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

</project>
```

因为 Spring Task 是 Spring Framework 的模块，所以在我们引入 `spring-boot-starter-web` 依赖后，无需特别引入它。

同时，考虑到我们希望让项目启动时，不自动结束 JVM 进程，所以我们引入了 `spring-boot-starter-web` 依赖。

2.2 ScheduleConfiguration
-------------------------

在 [`cn.iocoder.springboot.lab28.task.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/src/main/java/cn/iocoder/springboot/lab28/task/config/) 包路径下，创建 [ScheduleConfiguration](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/src/main/java/cn/iocoder/springboot/lab28/task/config/ScheduleConfiguration.java) 类，配置 Spring Task 。代码如下：

```
@Configuration
@EnableScheduling
public class ScheduleConfiguration {
}
```

*   在类上，添加 [`@EnableScheduling`](https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/scheduling/annotation/EnableScheduling.java) 注解，启动 Spring Task 的定时任务调度的功能。

2.3 DemoJob
-----------

在 [`cn.iocoder.springboot.lab28.task.job`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/src/main/java/cn/iocoder/springboot/lab28/task/job/) 包路径下，创建 [DemoJob](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob.java) 类，示例定时任务类。代码如下：

```
@Component
public class DemoJob {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    @Scheduled(fixedRate = 2000)
    public void execute() {
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
    }

}
```

*   在类上，添加 `@Component` 注解，创建 DemoJob Bean 对象。
*   创建 `#execute()` 方法，实现打印日志。同时，在该方法上，添加 [`@Scheduled`](https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/scheduling/annotation/Scheduled.java) 注解，设置每 2 秒执行该方法。

虽然说，`@Scheduled` 注解，可以添加在一个类上的多个方法上，但是艿艿的个人习惯上，还是一个 Job 类，一个定时任务。😈

2.4 Application
---------------

创建 [`Application.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-demo/src/main/java/cn/iocoder/springboot/lab28/task/Application.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

运行 Application 类，启动示例项目。输出日志精简如下：

```
# 初始化一个 ThreadPoolTaskScheduler 任务调度器
2019-11-30 18:02:58.415  INFO 83730 --- [           main] o.s.s.c.ThreadPoolTaskScheduler          : Initializing ExecutorService 'taskScheduler'

# 每 2 秒，执行一次 DemoJob 的任务
2019-11-30 18:02:58.449  INFO 83730 --- [ pikaqiu-demo-1] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (1) 次执行]
2019-11-30 18:03:00.438  INFO 83730 --- [ pikaqiu-demo-1] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (2) 次执行]
2019-11-30 18:03:02.442  INFO 83730 --- [ pikaqiu-demo-2] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (3) 次执行]
```

*   通过日志，我们可以看到，初始化一个 ThreadPoolTaskScheduler 任务调度器。之后，每 2 秒，执行一次 DemoJob 的任务。

至此，我们已经完成了 Spring Task 调度任务功能的入门。实际上，Spring Task 还提供了[异步任务](https://docs.spring.io/spring/docs/5.2.x/spring-framework-reference/integration.html#scheduling-annotation-support) ，这个我们在其它文章中，详细讲解。

> 下面[「2.5 @Scheduled」](#)和[「2.6 应用配置文件」](#)两个小节，是补充知识，建议看看。

2.5 @Scheduled
--------------

[`@Scheduled`](https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/scheduling/annotation/Scheduled.java) 注解，设置定时任务的执行计划。

**常用**属性如下：

*   `cron` 属性：Spring Cron 表达式。例如说，`"0 0 12 * * ?"` 表示每天中午执行一次，`"11 11 11 11 11 ?"` 表示 11 月 11 号 11 点 11 分 11 秒执行一次（哈哈哈）。更多示例和讲解，可以看看 [《Spring Cron 表达式》](https://blog.csdn.net/bingduanlbd/article/details/51740913) 文章。注意，以调用**完成时刻**为开始计时时间。
*   `fixedDelay` 属性：固定执行间隔，单位：毫秒。注意，以调用**完成时刻**为开始计时时间。
*   `fixedRate` 属性：固定执行间隔，单位：毫秒。注意，以调用**开始时刻**为开始计时时间。
*   这三个属性，有点雷同，可以看看 [《@Scheduled 定时任务的 fixedRate、fixedDelay、cron 的区别》](https://www.iteye.com/blog/guwq2014-2424520) ，一定要分清楚差异。

**不常用**属性如下：

*   `initialDelay` 属性：初始化的定时任务执行延迟，单位：毫秒。
*   `zone` 属性：解析 Spring Cron 表达式的所属的时区。默认情况下，使用服务器的本地时区。
*   `initialDelayString` 属性：`initialDelay` 的字符串形式。
*   `fixedDelayString` 属性：`fixedDelay` 的字符串形式。
*   `fixedRateString` 属性：`fixedRate` 的字符串形式。

2.6 应用配置文件
----------

在 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-demo/src/main/resources/application.yml) 中，添加 Spring Task 定时任务的配置，如下：

```
spring:
  task:
    
    scheduling:
      thread-name-prefix: pikaqiu-demo- 
      pool:
        size: 10 
      shutdown:
        await-termination: true 
        await-termination-period: 60
```

*   在 `spring.task.scheduling` 配置项，Spring Task 调度任务的配置，对应 [TaskSchedulingProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/task/TaskSchedulingProperties.java) 配置类。
*   Spring Boot [TaskSchedulingAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/task/TaskSchedulingAutoConfiguration.java) 自动化配置类，实现 Spring Task 的自动配置，创建 [ThreadPoolTaskScheduler](https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/scheduling/concurrent/ThreadPoolTaskScheduler.java) 基于线程池的任务调度器。本质上，ThreadPoolTaskScheduler 是基于 ScheduledExecutorService 的封装，增强在调度时间上的功能。

**注意**，`spring.task.scheduling.shutdown` 配置项，是为了实现 Spring Task 定时任务的优雅关闭。我们想象一下，如果定时任务在执行的过程中，如果应用开始关闭，把定时任务需要使用到的 Spring Bean 进行销毁，例如说数据库连接池，那么此时定时任务还在执行中，一旦需要访问数据库，可能会导致报错。

*   所以，通过配置 `await-termination = true` ，实现应用关闭时，等待定时任务执行完成。这样，应用在关闭的时，Spring 会优先等待 ThreadPoolTaskScheduler 执行完任务之后，再开始 Spring Bean 的销毁。
*   同时，又考虑到我们不可能无限等待定时任务全部执行结束，因此可以配置 `await-termination-period = 60` ，等待任务完成的最大时长，单位为秒。具体设置多少的等待时长，可以根据自己应用的需要。

> 示例代码对应仓库：[lab-28-task-quartz-memory](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-quartz-memory) 。

在艿艿最早开始实习的时候，公司使用 Quartz 作为任务调度中间件。考虑到我们要实现定时任务的高可用，需要部署多个 JVM 进程。比较舒服的是，Quartz 自带了集群方案。它通过将作业信息存储到关系数据库中，并使用关系数据库的**行锁**来实现执行作业的竞争，从而保证多个进程下，同一个任务在相同时刻，不能重复执行。

可能很多胖友对 Quartz 还不是很了解，我们先来看一段简介：

> FROM [https://www.oschina.net/p/quartz](https://www.oschina.net/p/quartz)
> 
> Quartz 是一个开源的作业调度框架，它完全由 Java 写成，并设计用于 J2SE 和 J2EE 应用中。它提供了巨大的灵活性而不牺牲简单性。你能够用它来为执行一个作业而创建简单的或复杂的调度。
> 
> 它有很多特征，如：数据库支持，**集群**，插件，EJB 作业预构建，JavaMail 及其它，支持 cron-like 表达式等等。

在 Quartz 体系结构中，有三个组件非常重要：

*   Scheduler ：调度器
*   Trigger ：触发器
*   Job ：任务

不了解的胖友，可以直接看看 [《Quartz 入门详解》](https://www.jianshu.com/p/7663f0ed486a) 文章。这里，艿艿就不重复赘述。

> FROM [https://medium.com/@ChamithKodikara/spring-boot-2-quartz-2-scheduler-integration-a8eaaf850805](https://medium.com/@ChamithKodikara/spring-boot-2-quartz-2-scheduler-integration-a8eaaf850805)
> 
> [Quartz 整体架构图](https://medium.com/@ChamithKodikara/spring-boot-2-quartz-2-scheduler-integration-a8eaaf850805)

Quartz 分成单机模式和集群模式。

*   本小节，我们先来学习下 Quartz 的单机模式，入门比较快。
*   下一下[「5. 再次入门 Quartz 集群」](#) ，我们再来学习下 Quartz 的集群模式。在生产环境下，**一定一定一定要使用 Quartz 的集群模式**，保证定时任务的高可用。

😈 下面，让我们开始遨游~

3.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> 
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-28-task-quartz-memory</artifactId>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>
    </dependencies>

</project>
```

具体每个依赖的作用，胖友自己认真看下艿艿添加的所有注释噢。

3.2 示例 Job
----------

在 [`cn.iocoder.springboot.lab28.task.config.job`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/job/) 包路径下，我们来创建示例 Job 。

创建 [DemoJob01](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob01.java) 类，示例定时任务 01 类。代码如下：

```
public class DemoJob01 extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    @Autowired
    private DemoService demoService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("[executeInternal][定时第 ({}) 次执行, demoService 为 ({})]", counts.incrementAndGet(),
                demoService);
    }

}
```

*   继承 [QuartzJobBean](https://github.com/spring-projects/spring-framework/blob/master/spring-context-support/src/main/java/org/springframework/scheduling/quartz/QuartzJobBean.java) 抽象类，实现 `#executeInternal(JobExecutionContext context)` 方法，执行自定义的定时任务的逻辑。
    
*   QuartzJobBean 实现了 [`org.quartz.Job`](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/Job.java) 接口，提供了 Quartz 每次创建 Job 执行定时逻辑时，将该 Job Bean 的依赖属性注入。例如说，DemoJob01 需要 `@Autowired` 注入的 [`demoService`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob01.java) 属性。核心代码如下：
    
    ```
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
            
            MutablePropertyValues pvs = new MutablePropertyValues();
            pvs.addPropertyValues(context.getScheduler().getContext());
            pvs.addPropertyValues(context.getMergedJobDataMap());
            bw.setPropertyValues(pvs, true);
    	} catch (SchedulerException ex) {
    		throw new JobExecutionException(ex);
    	}
    
        
        this.executeInternal(context);
    }
    
    protected abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;
    ```
    
    *   这样一看，是不是清晰很多。不要惧怕中间件的源码，好奇哪个类或者方法，就点进去看看。反正，又不花钱。
*   `counts` 属性，计数器。用于我们后面我们展示，**每次 DemoJob01 都会被 Quartz 创建出一个新的 Job 对象，执行任务**。这个很重要，也要非常小心。
    

创建 [DemoJob02](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob02.java) 类，示例定时任务 02 类。代码如下：

```
public class DemoJob02 extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("[executeInternal][我开始的执行了]");
    }

}
```

*   比较简单，为了后面演示案例之用。

3.3 ScheduleConfiguration
-------------------------

在 [`cn.iocoder.springboot.lab28.task.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/config/) 包路径下，创建 [ScheduleConfiguration](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/config/ScheduleConfiguration.java) 类，配置上述的两个示例 Job 。代码如下：

```
@Configuration
public class ScheduleConfiguration {

    public static class DemoJob01Configuration {

        @Bean
        public JobDetail demoJob01() {
            return JobBuilder.newJob(DemoJob01.class)
                    .withIdentity("demoJob01") 
                    .storeDurably() 
                    .build();
        }

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
```

*   内部创建了 DemoJob01Configuration 和 DemoJob02Configuration 两个配置类，分别配置 DemoJob01 和 DemoJob02 两个 Quartz Job 。
*   ========== **DemoJob01Configuration** ==========
*   `#demoJob01()` 方法，创建 DemoJob01 的 JobDetail Bean 对象。
*   `#demoJob01Trigger()` 方法，创建 DemoJob01 的 Trigger Bean 对象。其中，我们使用 [SimpleScheduleBuilder](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/SimpleScheduleBuilder.java) 简单的调度计划的构造器，创建了每 5 秒执行一次，无限重复的调度计划。
*   ========== **DemoJob2Configuration** ==========
*   `#demoJob2()` 方法，创建 DemoJob02 的 JobDetail Bean 对象。
*   `#demoJob02Trigger()` 方法，创建 DemoJob02 的 Trigger Bean 对象。其中，我们使用 [CronScheduleBuilder](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/CronScheduleBuilder.java) 基于 Quartz Cron 表达式的调度计划的构造器，创建了每**第** 10 秒执行一次的调度计划。这里，推荐一个 [Quartz/Cron/Crontab 表达式在线生成工具](http://www.bejson.com/othertools/cron/) ，方便帮我们生成 Quartz Cron 表达式，并计算出最近 5 次运行时间。

😈 因为 JobDetail 和 Trigger 一般是成双成对出现，所以艿艿习惯配置成一个 Configuration 配置类。

3.4 Application
---------------

创建 [`Application.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/Application.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

运行 Application 类，启动示例项目。输出日志精简如下：

```
2019-11-30 23:40:05.123  INFO 92812 --- [           main] org.quartz.impl.StdSchedulerFactory      : Using default implementation for ThreadExecutor
2019-11-30 23:40:05.130  INFO 92812 --- [           main] org.quartz.core.SchedulerSignalerImpl    : Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
2019-11-30 23:40:05.130  INFO 92812 --- [           main] org.quartz.core.QuartzScheduler          : Quartz Scheduler v.2.3.2 created.
2019-11-30 23:40:05.131  INFO 92812 --- [           main] org.quartz.simpl.RAMJobStore             : RAMJobStore initialized.
2019-11-30 23:40:05.132  INFO 92812 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler meta-data: Quartz Scheduler (v2.3.2) 'quartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

2019-11-30 23:40:05.132  INFO 92812 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler 'quartzScheduler' initialized from an externally provided properties instance.
2019-11-30 23:40:05.132  INFO 92812 --- [           main] org.quartz.impl.StdSchedulerFactory      : Quartz scheduler version: 2.3.2
2019-11-30 23:40:05.132  INFO 92812 --- [           main] org.quartz.core.QuartzScheduler          : JobFactory set to: org.springframework.scheduling.quartz.SpringBeanJobFactory@203dd56b
2019-11-30 23:40:05.158  INFO 92812 --- [           main] o.s.s.quartz.SchedulerFactoryBean        : Starting Quartz Scheduler now
2019-11-30 23:40:05.158  INFO 92812 --- [           main] org.quartz.core.QuartzScheduler          : Scheduler quartzScheduler_$_NON_CLUSTERED started.


2019-11-30 23:40:05.164  INFO 92812 --- [eduler_Worker-1] c.i.springboot.lab28.task.job.DemoJob01  : [executeInternal][定时第 (1) 次执行, demoService 为 (cn.iocoder.springboot.lab28.task.service.DemoService@23d75d74)]
2019-11-30 23:40:09.866  INFO 92812 --- [eduler_Worker-2] c.i.springboot.lab28.task.job.DemoJob01  : [executeInternal][定时第 (1) 次执行, demoService 为 (cn.iocoder.springboot.lab28.task.service.DemoService@23d75d74)]
2019-11-30 23:40:14.865  INFO 92812 --- [eduler_Worker-4] c.i.springboot.lab28.task.job.DemoJob01  : [executeInternal][定时第 (1) 次执行, demoService 为 (cn.iocoder.springboot.lab28.task.service.DemoService@23d75d74)]


2019-11-30 23:40:10.004  INFO 92812 --- [eduler_Worker-3] c.i.springboot.lab28.task.job.DemoJob02  : [executeInternal][我开始的执行了]
2019-11-30 23:40:20.001  INFO 92812 --- [eduler_Worker-6] c.i.springboot.lab28.task.job.DemoJob02  : [executeInternal][我开始的执行了]
2019-11-30 23:40:30.002  INFO 92812 --- [eduler_Worker-9] c.i.springboot.lab28.task.job.DemoJob02  : [executeInternal][我开始的执行了]
```

*   项目启动时，会创建了 Quartz QuartzScheduler 并启动。
*   考虑到阅读日志方便，艿艿这里把 DemoJob01 和 DemoJob02 的日志分开来了。
*   对于 DemoJob01 ，每 5 秒左右执行一次。同时我们可以看到，`demoService` 成功注入，而 `counts` 每次都是 1 ，说明每次 DemoJob01 都是新创建的。
*   对于 DemoJob02 ，每**第** 10 秒执行一次。

> 下面[「3.5 应用配置文件」](#)两个小节，是补充知识，建议看看。

3.5 应用配置文件
----------

在 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-quartz-memory/src/main/resources/application.yml) 中，添加 Quartz 的配置，如下：

```
spring:
  
  quartz:
    job-store-type: memory 
    auto-startup: true 
    startup-delay: 0 
    wait-for-jobs-to-complete-on-shutdown: true 
    overwrite-existing-jobs: false 
    properties: 
      org:
        quartz:
          threadPool:
            threadCount: 25 
            threadPriority: 5 
            class: org.quartz.simpl.SimpleThreadPool
```

*   在 `spring.quartz` 配置项，Quartz 的配置，对应 [QuartzProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/quartz/QuartzProperties.java) 配置类。
*   Spring Boot [QuartzAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/quartz/QuartzAutoConfiguration.java) 自动化配置类，实现 Quartz 的自动配置，创建 Quartz [Scheduler](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/Scheduler.java)(**调度器**) Bean 。

**注意**，`spring.quartz.wait-for-jobs-to-complete-on-shutdown` 配置项，是为了实现 Quartz 的优雅关闭，建议开启。关于这块，和我们在 Spring Task 的[「2.6 应用配置文件」](#) 提到的是一致的。

> 示例代码对应仓库：[lab-28-task-quartz-memory](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-quartz-memory) 。

实际场景下，我们必然需要考虑定时任务的**高可用**，所以基本上，肯定使用 Quartz 的集群方案。因此本小节，我们使用 Quartz 的 **JDBC** 存储器 [JobStoreTX](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/impl/jdbcjobstore/JobStoreTX.java) ，并是使用 MySQL 作为数据库。

如下是 Quartz 两种存储器的对比：

> FROM [https://blog.csdn.net/Evankaka/article/details/45540885](https://blog.csdn.net/Evankaka/article/details/45540885)

<table><thead><tr><th>类型</th><th>优点</th><th>缺点</th></tr></thead><tbody><tr><td>RAMJobStore</td><td>不要外部数据库，配置容易，运行速度快</td><td>因为调度程序信息是存储在被分配给 JVM 的内存里面，所以，当应用程序停止运行时，所有调度信息将被丢失。另外因为存储到 JVM 内存里面，所以可以存储多少个 Job 和 Trigger 将会受到限制</td></tr><tr><td>JDBC 作业存储</td><td>支持集群，因为所有的任务信息都会保存到数据库中，可以控制事物，还有就是如果应用服务器关闭或者重启，任务信息都不会丢失，并且可以恢复因服务器关闭或者重启而导致执行失败的任务</td><td>运行速度的快慢取决与连接数据库的快慢</td></tr></tbody></table>

> 艿艿：实际上，有方案可以实现兼具这两种方式的优点，我们在 [「666. 彩蛋」](#) 中来说。

另外，本小节提供的示例和 [「3. 快速入门 Quartz 单机」](#) 基本一致。😈 下面，让我们开始遨游~

4.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.10.RELEASE</version>
        <relativePath/> 
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-28-task-quartz-jdbc</artifactId>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency> 
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.48</version>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

</project>
```

*   和 [「3.1 引入依赖」](#) 基本一致，只是额外引入 `spring-boot-starter-test` 依赖，等会会写两个单元测试方法。

4.2 示例 Job
----------

在 [`cn.iocoder.springboot.lab28.task.config.job`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/job/) 包路径下，创建 [DemoJob01](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob01.java) 和 [DemoJob02](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob01.java) 类。代码如下：

```
@DisallowConcurrentExecution
public class DemoJob01 extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DemoService demoService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        logger.info("[executeInternal][我开始的执行了, demoService 为 ({})]", demoService);
    }

}



@DisallowConcurrentExecution
public class DemoJob02 extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) {
        logger.info("[executeInternal][我开始的执行了]");
    }

}
```

*   相比 [「3.2 示例 Job」](#) 来说，在类上添加了 Quartz 的 [`@DisallowConcurrentExecution`](https://github.com/elventear/quartz-scheduler/blob/master/quartz-core/src/main/java/org/quartz/DisallowConcurrentExecution.java) 注解，保证相同 [JobDetail](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/JobDetail.java) 在多个 JVM 进程中，有且仅有一个节点在执行。

**注意**，不是以 Quartz [Job](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/Job.java) 为维度，保证在多个 JVM 进程中，有且仅有一个节点在执行，**而是以 JobDetail 为维度**。虽然说，绝大多数情况下，我们会保证一个 Job 和 JobDetail 是**一一对应**。😈 所以，搞不清楚这个概念的胖友，最好搞清楚这个概念。实在有点懵逼，保证一个 Job 和 JobDetail 是**一一对应**就对了。

而 JobDetail 的**唯一标识**是 [JobKey](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/java/org/quartz/JobKey.java) ，使用 `name` + `group` 两个属性。一般情况下，我们只需要设置 `name` 即可，而 Quartz 会默认 `group = DEFAULT` 。

不过这里还有一点要补充，也是需要**注意的**，在 Quartz 中，**相同 Scheduler 名字**的节点，形成一个 Quartz 集群。在下文中，我们可以通过 `spring.quartz.scheduler-name` 配置项，设置 Scheduler 的名字。

**【重要】**为什么要说这个呢？因为我们要完善一下上面的说法：通过在 Job 实现类上添加 `@DisallowConcurrentExecution` 注解，实现在**相同** Quartz Scheduler 集群中，**相同** JobKey 的 JobDetail ，保证在多个 JVM 进程中，有且仅有一个节点在执行。

4.3 应用配置文件
----------

在 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-quartz-jdbc/src/main/resources/application.yml) 中，添加 Quartz 的配置，如下：

```
spring:
  datasource:
    user:
      url: jdbc:mysql://127.0.0.1:3306/lab-28-quartz-jdbc-user?useSSL=false&useUnicode=true&characterEncoding=UTF-8
      driver-class-name: com.mysql.jdbc.Driver
      username: root
      password:
    quartz:
      url: jdbc:mysql://127.0.0.1:3306/lab-28-quartz-jdbc-quartz?useSSL=false&useUnicode=true&characterEncoding=UTF-8
      driver-class-name: com.mysql.jdbc.Driver
      username: root
      password:

  
  quartz:
    scheduler-name: clusteredScheduler 
    job-store-type: jdbc 
    auto-startup: true 
    startup-delay: 0 
    wait-for-jobs-to-complete-on-shutdown: true 
    overwrite-existing-jobs: false 
    properties: 
      org:
        quartz:
          
          jobStore:
            
            dataSource: quartzDataSource 
            class: org.quartz.impl.jdbcjobstore.JobStoreTX 
            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
            tablePrefix: QRTZ_ 
            isClustered: true 
            clusterCheckinInterval: 1000
            useProperties: false
          
          threadPool:
            threadCount: 25 
            threadPriority: 5 
            class: org.quartz.simpl.SimpleThreadPool 
    jdbc: 
      initialize-schema: never
```

*   配置项比较多，我们主要对比 [「3.5 应用配置文件」](#) 来看看。
    
*   在 `spring.datasource` 配置项下，用于创建多个数据源的配置。
    
    *   `user` 配置，连接 `lab-28-quartz-jdbc-user` 库。目的是，为了模拟我们一般项目，使用到的业务数据库。
    *   `quartz` 配置，连接 `lab-28-quartz-jdbc-quartz` 库。目的是，Quartz 会使用单独的数据库。😈 如果我们有多个项目需要使用到 Quartz 数据库的话，**可以统一使用一个**，但是要注意配置 `spring.quartz.scheduler-name` 设置不同的 Scheduler 名字，形成不同的 Quartz 集群。
*   在 `spring.quartz` 配置项下，额外增加了一些配置项，我们逐个来看看。
    
    *   `scheduler-name` 配置，Scheduler 名字。这个我们在上文解释了很多次了，如果还不明白，请拍死自己。
    *   `job-store-type` 配置，设置了使用 `"jdbc"` 的 Job 存储器。
    *   `properties.org.quartz.jobStore` 配置，增加了 JobStore 相关配置。重点是，通过 `dataSource` 配置项，设置了使用名字为 `"quartzDataSource"` 的 DataSource 为数据源。😈 在 [「4.4 DataSourceConfiguration」](#) 中，我们会使用 `spring.datasource.quartz` 配置，来创建该数据源。
    *   `jdbc` 配置项，虽然名字叫这个，主要是为了设置使用 SQL 初始化 Quartz 表结构。这里，我们设置 `initialize-schema = never` ，我们手动创建表结构。

咳咳咳，配置项确实有点多。如果暂时搞不明白的胖友，可以先简单把 `spring.datasource` 数据源，修改成自己的即可。

4.4 初始化 Quartz 表结构
------------------

在 [Quartz Download](http://www.quartz-scheduler.org/downloads/) 地址，下载对应版本的发布包。解压后，我们可以在 `src/org/quartz/impl/jdbcjobstore/` 目录，看到各种数据库的 Quartz 表结构的初始化脚本。这里，因为我们使用 MySQL ，所以使用 [`tables_mysql_innodb.sql`](https://github.com/quartz-scheduler/quartz/blob/master/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql) 脚本。

在数据库中执行该脚本，完成初始化 Quartz 表结构。如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2019-12-05/01.png)

关于每个 Quartz 表结构的说明，可以看看 [《Quartz 框架（二）——JobStore 数据库表字段详解》](https://www.jianshu.com/p/b94ebb8780fa) 文章。😈 实际上，也可以不看，哈哈哈哈。

我们会发现，每个表都有一个 `SCHED_NAME` 字段，Quartz Scheduler 名字。这样，实现每个 Quartz 集群，数据层面的拆分。

4.5 DataSourceConfiguration
---------------------------

在 [`cn.iocoder.springboot.lab28.task.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/config/) 包路径下，创建 [DataSourceConfiguration](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-memory/src/main/java/cn/iocoder/springboot/lab28/task/config/ScheduleConfiguration.java) 类，配置数据源。代码如下：

```
@Configuration
public class DataSourceConfiguration {

    


    @Primary
    @Bean(name = "userDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.user") 
    public DataSourceProperties userDataSourceProperties() {
        return new DataSourceProperties();
    }

    


    @Primary
    @Bean(name = "userDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.user.hikari") 
    public DataSource userDataSource() {
        
        DataSourceProperties properties =  this.userDataSourceProperties();
        
        return createHikariDataSource(properties);
    }

    


    @Bean(name = "quartzDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.quartz") 
    public DataSourceProperties quartzDataSourceProperties() {
        return new DataSourceProperties();
    }

    


    @Bean(name = "quartzDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.quartz.hikari")
    @QuartzDataSource
    public DataSource quartzDataSource() {
        
        DataSourceProperties properties =  this.quartzDataSourceProperties();
        
        return createHikariDataSource(properties);
    }

    private static HikariDataSource createHikariDataSource(DataSourceProperties properties) {
        
        HikariDataSource dataSource = properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        
        if (StringUtils.hasText(properties.getName())) {
            dataSource.setPoolName(properties.getName());
        }
        return dataSource;
    }

}
```

*   基于 `spring.datasource.user` 配置项，创建了名字为 `"userDataSource"` 的 DataSource Bean 。并且，在其上我们添加了 `@Primay` 注解，表示其是**主**数据源。
*   基于 `spring.datasource.quartz` 配置项，创建了名字为 `"quartzDataSource"` 的 DataSource Bean 。并且，在其上我们添加了 [`@QuartzDataSource`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/quartz/QuartzDataSource.java) 注解，表示其是 **Quartz** 的数据源。😈 注意，一定要配置啊，这里艿艿卡了好久！！！！

4.6 定时任务配置
----------

完成上述的工作之后，我们需要配置 Quartz 的定时任务。目前，有两种方式：

*   方式一，[「4.6.1 Bean 自动设置」](#) 。
*   方式二，[「4.6.2 Scheduler 手动设置」](#) 。

### 4.6.1 Bean 自动设置

在 [`cn.iocoder.springboot.lab28.task.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/config/) 包路径下，创建 [ScheduleConfiguration](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/config/ScheduleConfiguration.java) 类，配置上述的两个示例 Job 。代码如下：

```
@Configuration
public class ScheduleConfiguration {

    public static class DemoJob01Configuration {

        @Bean
        public JobDetail demoJob01() {
            return JobBuilder.newJob(DemoJob01.class)
                    .withIdentity("demoJob01") 
                    .storeDurably() 
                    .build();
        }

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
```

*   和 [「3.3 ScheduleConfiguration」](#) 是一模一样的。

在 Quartz 调度器启动的时候，会根据该配置，自动调用如下方法：

*   `Scheduler#addJob(JobDetail jobDetail, boolean replace)` 方法，将 JobDetail 持久化到数据库。
*   `Scheduler#scheduleJob(Trigger trigger)` 方法，将 Trigger 持久化到数据库。

### 4.6.2 Scheduler 手动设置

一般情况下，艿艿**推荐**使用 Scheduler 手动设置。

创建 [QuartzSchedulerTest](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/test/java/cn/iocoder/springboot/lab28/task/QuartzSchedulerTest.java) 类，创建分别添加 DemoJob01 和 DemoJob02 的 Quartz 定时任务配置。代码如下：

```
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class QuartzSchedulerTest {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void addDemoJob01Config() throws SchedulerException {
        
        JobDetail jobDetail = JobBuilder.newJob(DemoJob01.class)
                .withIdentity("demoJob01") 
                .storeDurably() 
                .build();
        
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5) 
                .repeatForever(); 
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail) 
                .withIdentity("demoJob01Trigger") 
                .withSchedule(scheduleBuilder) 
                .build();
        
        scheduler.scheduleJob(jobDetail, trigger);

    }

    @Test
    public void addDemoJob02Config() throws SchedulerException {
        
        JobDetail jobDetail = JobBuilder.newJob(DemoJob02.class)
                .withIdentity("demoJob02") 
                .storeDurably() 
                .build();
        
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail) 
                .withIdentity("demoJob02Trigger") 
                .withSchedule(scheduleBuilder) 
                .build();
        
        scheduler.scheduleJob(jobDetail, trigger);

    }

}
```

*   创建 JobDetail 和 Trigger 的代码，其实和 [「4.6.1 Bean 自动设置」](#) 是一致的。
*   在每个单元测试方法的最后，调用 `Scheduler#scheduleJob(JobDetail jobDetail, Trigger trigger)` 方法，将 JobDetail 和 Trigger 持久化到数据库。
*   如果想要覆盖数据库中的 Quartz 定时任务的配置，可以调用 `Scheduler#scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace)` 方法，传入 `replace = true` 进行覆盖配置。

4.7 Application
---------------

创建 [`Application.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/Application.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

*   运行 Application 类，启动示例项目。具体的执行日志，和 [「3.4 Application」](#) 基本一致，艿艿这里就不重复罗列了。

如果胖友想要测试集群下的运行情况，可以再创建 创建 [`Application02.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/Application02.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application02 {

    public static void main(String[] args) {
        
        System.setProperty("server.port", "0");

        
        SpringApplication.run(Application.class, args);
    }

}
```

*   运行 Application02 类，再次启动一个示例项目。然后，观察输出的日志，可以看到启动的两个示例项目，都会有 DemoJob01 和 DemoJob02 的执行日志。

> 示例代码对应仓库：[lab-28-task-xxl-job](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28/lab-28-task-xxl-job) 。

虽然说，Quartz 的功能，已经能够满足我们对定时任务的诉求，但是距离生产可用、好用，还是有一定的距离。在艿艿最早开始实习的时候，因为 Quartz 只提供了任务调度的功能，不提供管理任务的管理与监控控制台，需要自己去做二次封装。当时，因为社区中找不到合适的实现这块功能的开源项目，所以我们就自己进行了简单的封装，满足我们的管理与监控的需求。

不过现在呢，开源社区中已经有了很多优秀的调度任务中间件。其中，比较有代表性的就是 [XXL-JOB](https://www.xuxueli.com/xxl-job/) 。其对自己的定义如下：

> XXL-JOB 是一个轻量级分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。

对于 XXL-JOB 的入门，艿艿已经在 [《芋道 XXL-JOB 极简入门》](http://www.iocoder.cn/XXL-JOB/install/?self) 中编写，胖友先跳转到该文章阅读。重点是，要先搭建一个 XXL-JOB 调度中心。😈 因为，本文我们是来在 Spring Boot 项目中，实现一个 XXL-JOB 执行器。

5.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-xxl-job/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.1.RELEASE</version>
        <relativePath/> 
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-28-task-xxl-job</artifactId>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-job-core</artifactId>
            <version>2.1.1</version>
        </dependency>
    </dependencies>

</project>
```

具体每个依赖的作用，胖友自己认真看下艿艿添加的所有注释噢。比较可惜的是，目前 XXL-JOB 官方并未提供 Spring Boot Starter 包，略微有点尴尬。不过，社区已经有人在提交 Pull Request 了，详细可见 [https://github.com/xuxueli/xxl-job/pull/820](https://github.com/xuxueli/xxl-job/pull/820) 。

5.2 应用配置文件
----------

在 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-28-task-xxl-job/src/main/resources/application.yml) 中，添加 Quartz 的配置，如下：

```
server:
  port: 9090 


xxl:
  job:
    admin:
      addresses: http://127.0.0.1:8080/xxl-job-admin 
    executor:
      appname: lab-28-executor 
      ip: 
      port: 6666 
      logpath: /Users/yunai/logs/xxl-job/lab-28-executor 
      logretentiondays: 30 
    accessToken: yudaoyuanma
```

*   具体每个参数的作用，胖友自己看下详细的注释哈。

5.3 XxlJobConfiguration
-----------------------

在 [`cn.iocoder.springboot.lab28.task.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-xxl-job/src/main/java/cn/iocoder/springboot/lab28/task/config/) 包路径下，创建 [DataSourceConfiguration](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-xxl-job/src/main/java/cn/iocoder/springboot/lab28/task/config/ScheduleConfiguration.java) 类，配置 XXL-JOB 执行器。代码如下：

```
@Configuration
public class XxlJobConfiguration {

    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;
    @Value("${xxl.job.executor.appname}")
    private String appName;
    @Value("${xxl.job.executor.ip}")
    private String ip;
    @Value("${xxl.job.executor.port}")
    private int port;
    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.executor.logpath}")
    private String logPath;
    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppName(appName);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        
        return xxlJobSpringExecutor;
    }

}
```

*   在 `#xxlJobExecutor()` 方法，创建了 Spring 容器下的 XXL-JOB 执行器 Bean 对象。要注意，方法上添加了的 `@Bean` 注解，配置了启动和销毁方法。

5.4 DemoJob
-----------

在 [`cn.iocoder.springboot.lab28.task.job`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-xxl-job/src/main/java/cn/iocoder/springboot/lab28/task/job/) 包路径下，创建 [DemoJob](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-xxl-job/src/main/java/cn/iocoder/springboot/lab28/task/job/DemoJob.java) 类，示例定时任务类。代码如下：

```
@Component
@JobHandler("demoJob")
public class DemoJob extends IJobHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicInteger counts = new AtomicInteger();

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        
        logger.info("[execute][定时第 ({}) 次执行]", counts.incrementAndGet());
        
        return ReturnT.SUCCESS;
    }

}
```

*   继承 XXL-JOB [IJobHandler](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-core/src/main/java/com/xxl/job/core/handler/IJobHandler.java) 抽象类，通过实现 `#execute(String param)` 方法，从而实现定时任务的逻辑。
*   在方法上，添加 [`@JobHandler`](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-core/src/main/java/com/xxl/job/core/handler/annotation/JobHandler.java) 注解，设置 JobHandler 的名字。后续，我们在调度中心的控制台中，新增任务时，需要使用到这个名字。

`#execute(String param)` 方法的返回结果，为 [ReturnT](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-core/src/main/java/com/xxl/job/core/biz/model/ReturnT.java) 类型。当返回值符合 `“ReturnT.code == ReturnT.SUCCESS_CODE”` 时表示任务执行成功，否则表示任务执行失败，而且可以通过 `“ReturnT.msg”` 回调错误信息给调度中心；从而，在任务逻辑中可以方便的控制任务执行结果。

`#execute(String param)` 方法的方法参数，为调度中心的控制台中，新增任务时，配置的 “任务参数”。一般情况下，不会使用到。

5.5 Application
---------------

创建 [`Application.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-28/lab-28-task-quartz-jdbc/src/main/java/cn/iocoder/springboot/lab28/task/Application.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

运行 Application 类，启动示例项目。输出日志精简如下：

```
# XXL-JOB 启动日志
2019-11-29 00:58:42.429  INFO 46957 --- [           main] c.xxl.job.core.executor.XxlJobExecutor   : >>>>>>>>>>> xxl-job register jobhandler success, name:demoJob, jobHandler:cn.iocoder.springboot.lab28.task.job.DemoJob@3af9aa66
2019-11-29 00:58:42.451  INFO 46957 --- [           main] c.x.r.r.provider.XxlRpcProviderFactory   : >>>>>>>>>>> xxl-rpc, provider factory add service success. serviceKey = com.xxl.job.core.biz.ExecutorBiz, serviceBean = class com.xxl.job.core.biz.impl.ExecutorBizImpl
2019-11-29 00:58:42.454  INFO 46957 --- [           main] c.x.r.r.provider.XxlRpcProviderFactory   : >>>>>>>>>>> xxl-rpc, provider factory add service success. serviceKey = com.xxl.job.core.biz.ExecutorBiz, serviceBean = class com.xxl.job.core.biz.impl.ExecutorBizImpl
2019-11-29 00:58:42.565  INFO 46957 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-11-29 00:58:42.629  INFO 46957 --- [       Thread-7] com.xxl.rpc.remoting.net.Server          : >>>>>>>>>>> xxl-rpc remoting server start success, nettype = com.xxl.rpc.remoting.net.impl.netty_http.server.NettyHttpServer, port = 6666
```

此时，因为我们并未在 XXL-JOB 调度中心进行相关的配置，所以 DemoJob 并不会执行。下面，让我们在 XXL-JOB 调度中心进行相应的配置。

5.6 新增执行器
---------

浏览器打开 [http://127.0.0.1:8080/xxl-job-admin/jobgroup](http://127.0.0.1:8080/xxl-job-admin/jobgroup) 地址，即「执行器管理」菜单。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/03.jpg)

点击「新增执行器」按钮，弹出「新增执行器」界面。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/04.jpg)

填写完 `"lab-28-executor"` 执行器的信息，点击「保存」按钮，进行保存。耐心等待一会，执行器会自动注册上来。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/05.jpg)

*   执行器列表中显示在线的执行器列表, 可通过 "OnLine 机器" 查看对应执行器的集群机器。

相同执行器，有且仅需配置一次即可。

5.7 新建任务
--------

浏览器打开 [http://127.0.0.1:8080/xxl-job-admin/jobinfo](http://127.0.0.1:8080/xxl-job-admin/jobinfo) 地址，即「任务管理」菜单。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/06.jpg)

点击最右边的「新增」按钮，弹出「新增」界面。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/07.jpg)

填写完 `"demoJob"` 任务的信息，点击「保存」按钮，进行保存。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/08.jpg)

点击 `"demoJob"` 任务的「操作」按钮，选择「启动」，确认后，该 `"demoJob"` 任务的状态就变成了 **RUNNING** 。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/09.jpg)

此时，我们打开执行器的 IDE 界面，可以看到 DemoJob 已经在每分钟执行一次了。日志如下：

```
2019-11-29 01:30:00.161  INFO 48374 --- [      Thread-18] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (1) 次执行]
2019-11-29 01:31:00.012  INFO 48374 --- [      Thread-18] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (2) 次执行]
2019-11-29 01:32:00.009  INFO 48374 --- [      Thread-18] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (3) 次执行]
2019-11-29 01:33:00.010  INFO 48374 --- [      Thread-18] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (4) 次执行]
2019-11-29 01:34:00.005  INFO 48374 --- [      Thread-18] c.i.springboot.lab28.task.job.DemoJob    : [execute][定时第 (5) 次执行]
```

并且，我们在调度中心的界面上，点击 `"demoJob"` 任务的「操作」按钮，选择「查询日志」，可以看到相应的调度日志。如下图：![](https://static.iocoder.cn/images/XXL-JOB/2019-11-28/10.jpg)

至此，我们已经完成了 XXL-JOB 执行器的入门。

可能很多胖友不了解 Elastic-Job 这个中间件。我们看一段其官方文档的介绍：

> Elastic-Job 是一个分布式调度解决方案，由两个相互独立的子项目 Elastic-Job-Lite 和 Elastic-Job-Cloud 组成。
> 
> Elastic-Job-Lite 定位为轻量级无中心化解决方案，使用 jar 包的形式提供分布式任务的协调服务。

Elastic-Job 基本是国内开源**最好**的调度任务中间件的几个中间件，可能没有之一，嘿嘿。目前处于有点 “断更” 的状态，具体可见 [https://github.com/elasticjob/elastic-job-lite/issues/616](https://github.com/elasticjob/elastic-job-lite/issues/616) 。

所以关于这块的示例，艿艿暂时先不提供。如果对 Elastic-Job 源码感兴趣的胖友，可以看看艿艿写的如下两个系列：

*   [《芋道 Elastic-Job-Lite 源码分析系列》](http://www.iocoder.cn/categories/Elastic-Job-Lite/?self)
*   [《芋道 Elastic-Job-Cloud 源码分析系列》](http://www.iocoder.cn/categories/Elastic-Job-Cloud/?self)

**① 如何选择？**

可能胖友希望了解下不同调度中间件的对比。表格如下：

<table><thead><tr><th>特性</th><th>quartz</th><th>elastic-job-lite</th><th>xxl-job</th><th>LTS</th></tr></thead><tbody><tr><td>依赖</td><td>MySQL、jdk</td><td>jdk、zookeeper</td><td>mysql、jdk</td><td>jdk、zookeeper、maven</td></tr><tr><td>高可用</td><td>多节点部署，通过竞争数据库锁来保证只有一个节点执行任务</td><td>通过 zookeeper 的注册与发现，可以动态的添加服务器</td><td>基于竞争数据库锁保证只有一个节点执行任务，支持水平扩容。可以手动增加定时任务，启动和暂停任务，有监控</td><td>集群部署, 可以动态的添加服务器。可以手动增加定时任务，启动和暂停任务。有监控</td></tr><tr><td>任务分片</td><td>×</td><td>√</td><td>√</td><td>√</td></tr><tr><td>管理界面</td><td>×</td><td>√</td><td>√</td><td>√</td></tr><tr><td>难易程度</td><td>简单</td><td>简单</td><td>简单</td><td>略复杂</td></tr><tr><td>高级功能</td><td>-</td><td>弹性扩容，多种作业模式，失效转移，运行状态收集，多线程处理数据，幂等性，容错处理，spring 命名空间支持</td><td>弹性扩容，分片广播，故障转移，Rolling 实时日志，GLUE（支持在线编辑代码，免发布）, 任务进度监控，任务依赖，数据加密，邮件报警，运行报表，国际化</td><td>支持 spring，spring boot，业务日志记录器，SPI 扩展支持，故障转移，节点监控，多样化任务执行结果支持，FailStore 容错，动态扩容。</td></tr><tr><td>版本更新</td><td>半年没更新</td><td>2 年没更新</td><td>最近有更新</td><td>1 年没更新</td></tr></tbody></table>

也推荐看看如下文章：

*   [《分布式定时任务调度系统技术选型》](https://www.expectfly.com/2017/08/15/%E5%88%86%E5%B8%83%E5%BC%8F%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1%E6%96%B9%E6%A1%88%E6%8A%80%E6%9C%AF%E9%80%89%E5%9E%8B/)
*   [《Azkaban、Xxl-Job 与 Airflow 对比分析》](https://my.oschina.net/centychen/blog/3044544)

目前的状况，如果真的不知道怎么选择，可以先尝试下 [XXL-JOB](https://www.xuxueli.com/xxl-job/) 。

**② 中心化 V.S 去中心化**

下面，让我们一起来简单聊聊分布式调度中间件的实现方式的分类。一个分布式的调度中间件，会存在两种角色：

*   调度器：负责调度任务，下发给执行器。
*   执行器：负责接收任务，执行具体任务。

那么，如果从**调度系统的角度**来看，可以分成两类：

*   中心化： 调度中心和执行器**分离**，调度中心统一调度，通知某个执行器处理任务。
*   去中心化：调度中心和执行器**一体化**，自己调度自己执行处理任务。

如此可知 XXL-Job 属于**中心化**的任务调度平台。目前采用这种方案的还有：

*   链家的 [kob](https://github.com/LianjiaTech/kob)
*   美团的 Crane（暂未开源）

**去中心化**的任务调度平台，目前有：

*   [Elastic Job](http://elasticjob.io/docs/elastic-job-lite/00-overview/)
*   唯品会的 [Saturn](https://github.com/vipshop/Saturn)
*   [Quartz](http://www.quartz-scheduler.org/) 基于数据库的集群方案
*   淘宝的 [TBSchedule](https://github.com/taobao/TBSchedule)（暂停更新，只能使用阿里云 [SchedulerX](https://cn.aliyun.com/aliware/schedulerx) 服务）

> 艿艿：如果胖友想要更加的理解，可以看看艿艿朋友写的 [《中心化 V.S 去中心化调度设计》](https://www.jianshu.com/p/989298cf5314)

**③ 任务竞争 V.S 任务预分配**

那么，如果从**任务分配的角度**来看，可以分成两类：

*   任务竞争：调度器会通过竞争任务，下发任务给执行器。
*   任务预分配：调度器预先分配任务给不同的执行器，无需进行竞争。

如此可知 XXL-Job 属于**任务竞争**的任务调度平台。目前采用这种方案的还有：

*   链家的 [kob](https://github.com/LianjiaTech/kob)
*   美团的 Crane（暂未开源）
*   [Quartz](http://www.quartz-scheduler.org/) 基于数据库的集群方案

**任务预分配**的任务调度平台，目前有：

*   [Elastic Job](http://elasticjob.io/docs/elastic-job-lite/00-overview/)
*   唯品会的 [Saturn](https://github.com/vipshop/Saturn)
*   淘宝的 [TBSchedule](https://github.com/taobao/TBSchedule)（暂停更新，只能使用阿里云 [SchedulerX](https://cn.aliyun.com/aliware/schedulerx) 服务）

一般来说，基于任务预分配的任务调度平台，都会选择使用 Zookeeper 来协调分配任务到不同的节点上。同时，任务调度平台必须是去中心化的方案，每个节点即是调度器又是执行器。这样，任务在预分配在每个节点之后，后续就自己调度给自己执行。

相比较而言，随着节点越来越多，基于任务竞争的方案会因为任务竞争，导致存在性能下滑的问题。而基于任务预分配的方案，则不会存在这个问题。并且，基于任务预分配的方案，性能会优于基于任务竞争的方案。

这里在推荐一篇 Elastic Job 开发者张亮的文章 [《详解当当网的分布式作业框架 elastic-job》](https://www.infoq.cn/article/dangdang-distributed-work-framework-elastic-job) ，灰常给力！

**④ Quartz 是个优秀的调度内核**

绝大多数情况下，我们不会直接使用 Quartz 作为我们的调度中间件的选择。但是，基本所有的分布式调度中间件，都将 Quartz 作为调度内核，因为 Quartz 在单纯任务调度本身提供了很强的功能。

不过呢，随着一个分布式调度中间件的逐步完善，又会逐步考虑抛弃 Quartz 作为调度内核，转而自研。例如说 XXL-JOB 在 2.1.0 RELEASE 的版本中，就已经更换成自研的调度模块。其替换的理由如下：

> XXL-JOB 最终选择自研调度组件（早期调度组件基于 Quartz）；
> 
> *   一方面，是为了精简系统降低冗余依赖。
> *   另一方面，是为了提供系统的可控度与稳定性。

在 Elastic-Job 3.X 的开发计划中，也有一项计划，就是自研调度组件，替换掉 Quartz 。