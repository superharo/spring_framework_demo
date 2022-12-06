> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.jianshu.com](https://www.jianshu.com/p/7663f0ed486a)

入门简介：
-----

基本上任何公司都会用到调度这个功能， 比如我们公司需要定期执行调度生成报表， 或者比如博客什么的定时更新之类的，都可以靠 Quartz 来完成。正如官网所说，小到独立应用大到大型电子商务网站， Quartz 都能胜任。

Quartz 体系结构：
------------

明白 Quartz 怎么用，首先要了解 Scheduler(调度器)、Job(任务) 和 Trigger(触发器) 这 3 个核心的概念。

1.  Job： 是一个接口，只定义一个方法 execute(JobExecutionContext context)，在实现接口的 execute 方法中编写所需要定时执行的 Job(任务)， JobExecutionContext 类提供了调度应用的一些信息。Job 运行时的信息保存在 JobDataMap 实例中；
    
2.  JobDetail： **Quartz 每次调度 Job 时， 都重新创建一个 Job 实例， 所以它不直接接受一个 Job 的实例，相反它接收一个 Job 实现类 (JobDetail: 描述 Job 的实现类及其它相关的静态信息，如 Job 名字、描述、关联监听器等信息)，以便运行时通过 newInstance() 的反射机制实例化 Job。
    
3.  Trigger： ** 是一个类，描述触发 Job 执行的时间触发规则。主要有 SimpleTrigger 和 CronTrigger 这两个子类。当且仅当需调度一次或者以固定时间间隔周期执行调度，SimpleTrigger 是最适合的选择；而 CronTrigger 则可以通过 Cron 表达式定义出各种复杂时间规则的调度方案：如工作日周一到周五的 15：00~16：00 执行调度等；  
    [具体 cron 语法参考这篇文章 http://www.jianshu.com/p/9027d067ac5b](https://www.jianshu.com/p/9027d067ac5b)
    

4.  Calendar：**org.quartz.Calendar 和 java.util.Calendar 不同， 它是一些日历特定时间点的集合（可以简单地将 org.quartz.Calendar 看作 java.util.Calendar 的集合——java.util.Calendar 代表一个日历时间点，无特殊说明后面的 Calendar 即指 org.quartz.Calendar）。 一个 Trigger 可以和多个 Calendar 关联， 以便排除或包含某些时间点。假设，我们安排每周星期一早上 10:00 执行任务，但是如果碰到法定的节日，任务则不执行，这时就需要在 Trigger 触发机制的基础上使用 Calendar 进行定点排除。针对不同时间段类型，Quartz 在 org.quartz.impl.calendar 包下提供了若干个 Calendar 的实现类，如 AnnualCalendar、MonthlyCalendar、WeeklyCalendar 分别针对每年、每月和每周进行定义；
    
5.  Scheduler： ** 代表一个 Quartz 的独立运行容器， Trigger 和 JobDetail 可以注册到 Scheduler 中， 两者在 Scheduler 中拥有各自的组及名称， 组及名称是 Scheduler 查找定位容器中某一对象的依据， Trigger 的组及名称必须唯一， JobDetail 的组和名称也必须唯一（但可以和 Trigger 的组和名称相同，因为它们是不同类型的）。Scheduler 定义了多个接口方法， 允许外部通过组及名称访问和控制容器中 Trigger 和 JobDetail。Scheduler 可以将 Trigger 绑定到某一 JobDetail 中， 这样当 Trigger 触发时， 对应的 Job 就被执行。一个 Job 可以对应多个 Trigger， 但一个 Trigger 只能对应一个 Job。可以通过 SchedulerFactory 创建一个 Scheduler 实例。Scheduler 拥有一个 SchedulerContext，它类似于 ServletContext，保存着 Scheduler 上下文信息，Job 和 Trigger 都可以访问 SchedulerContext 内的信息。SchedulerContext 内部通过一个 Map，以键值对的方式维护这些上下文数据，SchedulerContext 为保存和获取数据提供了多个 put() 和 getXxx() 的方法。可以通过 Scheduler# getContext() 获取对应的 SchedulerContext 实例；
    
6.  ThreadPool： **Scheduler 使用一个线程池作为任务运行的基础设施，任务通过共享线程池中的线程提高运行效率。Job 有一个 StatefulJob 子接口，代表有状态的任务，该接口是一个没有方法的标签接口，其目的是让 Quartz 知道任务的类型，以便采用不同的执行方案。无状态任务在执行时拥有自己的 JobDataMap 拷贝，对 JobDataMap 的更改不会影响下次的执行。而有状态任务共享共享同一个 JobDataMap 实例，每次任务执行对 JobDataMap 所做的更改会保存下来，后面的执行可以看到这个更改，也即每次执行任务后都会对后面的执行发生影响。正因为这个原因，无状态的 Job 可以并发执行，而有状态的 StatefulJob 不能并发执行，这意味着如果前次的 StatefulJob 还没有执行完毕，下一次的任务将阻塞等待，直到前次任务执行完毕。有状态任务比无状态任务需要考虑更多的因素，程序往往拥有更高的复杂度，因此除非必要，应该尽量使用无状态的 Job。如果 Quartz 使用了数据库持久化任务调度信息，无状态的 JobDataMap 仅会在 Scheduler 注册任务时保持一次，而有状态任务对应的 JobDataMap 在每次执行任务后都会进行保存。Trigger 自身也可以拥有一个 JobDataMap，其关联的 Job 可以通过 JobExecutionContext#getTrigger().getJobDataMap() 获取 Trigger 中的 JobDataMap。不管是有状态还是无状态的任务，在任务执行期间对 Trigger 的 JobDataMap 所做的更改都不会进行持久，也即不会对下次的执行产生影响。Quartz 拥有完善的事件和监听体系，大部分组件都拥有事件，如任务执行前事件、任务执行后事件、触发器触发前事件、触发后事件、调度器开始事件、关闭事件等等，可以注册相应的监听器处理感兴趣的事件。
    

下图描述了 Scheduler 的内部组件结构，SchedulerContext 提供 Scheduler 全局可见的上下文信息，每一个任务都对应一个 JobDataMap，虚线表达的 JobDataMap 表示对应有状态的任务：

![](http://upload-images.jianshu.io/upload_images/2131552-672a08be2c807b36)

废话不多说， 上代码：

1.  最简单的 Job 代码 (就打印 Hello Quartz ！)：

```
package com.ruixunyun.www.quartz;  
   
import org.quartz.Job;  
import org.quartz.JobExecutionContext;  
import org.quartz.JobExecutionException;  
   
public class HelloQuartz  implements Job {  
   
    public void execute(JobExecutionContext arg0) throws JobExecutionException {  
        System.out.println("Hello Quartz !");                 
    }         
}
```

2.  设置触发器

```
package com.ruixunyun.www.quartz;
    
import org.quartz.CronScheduleBuilder;    
import org.quartz.JobBuilder;    
import org.quartz.JobDetail;    
import org.quartz.Scheduler;    
import org.quartz.SchedulerException;  
import org.quartz.SchedulerFactory;    
import org.quartz.SimpleScheduleBuilder;  
import org.quartz.Trigger;    
import org.quartz.TriggerBuilder;    
import org.quartz.impl.StdSchedulerFactory;    
    
public class QuartzStartListener {    
   public static void main(String[] args) throws InterruptedException {    
       
       //通过schedulerFactory获取一个调度器    
       SchedulerFactory schedulerfactory = new StdSchedulerFactory();    
       Scheduler scheduler=null;    
       try{    
           // 通过schedulerFactory获取一个调度器    
           scheduler = schedulerfactory.getScheduler();    
               
            // 创建jobDetail实例，绑定Job实现类    
            // 指明job的名称，所在组的名称，以及绑定job类    
           JobDetail job = JobBuilder.newJob(HelloQuartz.class).withIdentity("JobName", "JobGroupName").build();    
             
               
            // 定义调度触发规则    
                           
            // SimpleTrigger   
//      Trigger trigger=TriggerBuilder.newTrigger().withIdentity("SimpleTrigger", "SimpleTriggerGroup")    
//                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(3).withRepeatCount(6))    
//                    .startNow().build();    
             
            //  corn表达式  每五秒执行一次  
              Trigger trigger=TriggerBuilder.newTrigger().withIdentity("CronTrigger1", "CronTriggerGroup")    
              .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))    
              .startNow().build();     
               
            // 把作业和触发器注册到任务调度中    
           scheduler.scheduleJob(job, trigger);    
               
           // 启动调度    
           scheduler.start();    
             
           Thread.sleep(10000);  
             
           // 停止调度  
           scheduler.shutdown();  
               
       }catch(SchedulerException e){    
           e.printStackTrace();    
       }    
           
   }    
}
```

输出 (设置了 sleep10 秒， 故在 0 秒调度一次， 5 秒一次， 10 秒最后一次)：

![](http://upload-images.jianshu.io/upload_images/2131552-bf9bc52ade6924d9)

Spring 中使用 Quartz
-----------------

maven 中加入 quartz 的 jar 包

```
<!-- https://mvnrepository.com/artifact/org.quartz-scheduler/quartz -->
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz</artifactId>
    <version>2.2.1</version>
 </dependency>
```

2.  web.xml 中添加监听器, 监听 QuartzStartListener.class  
    备注: 放到 spring 中, SchedulerTest.class 去掉了 main 方法, 并且继承了 ServletContextListener 类, 实现了他的两个方法: contextInitialized , contextDestroyed

```
package com.ruixunyun.www.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class QuartzStartListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //通过schedulerFactory获取一个调度器
        SchedulerFactory schedulerfactory = new StdSchedulerFactory();
        Scheduler scheduler = null;
        try {
            // 通过schedulerFactory获取一个调度器
            scheduler = schedulerfactory.getScheduler();

            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            JobDetail job = JobBuilder.newJob(HelloQuartz.class).withIdentity("JobName", "JobGroupName").build();

            // 定义调度触发规则  corn表达式  一分钟执行一次
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("CronTrigger1", "CronTriggerGroup")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * * * ?"))
                    .startNow().build();

            // 把作业和触发器注册到任务调度中
            scheduler.scheduleJob(job, trigger);
            // 启动调度
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
```

### web.xml 的监听配置

我的

```
<listener>
        <listener-class>com.ruixunyun.www.quartz.QuartzStartListener</listener-class>
</listener>
```