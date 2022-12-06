> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.iocoder.cn](https://www.iocoder.cn/XXL-JOB/install/?self)

> 摘要: 原创出处 http://www.iocoder.cn/XXL-JOB/install/ 「芋道源码」欢迎转载，保留摘要，谢谢！ 1. 概述 2. 特性 3. 架构设计 3.1 设计思想 3.......

<div id="locker" style="display: block;"> <div class="mask"></div> <div class="info"> <div> <p class="text-center" style="color: black;"> 扫码关注公众号：<span style="color: #E9405A; font-weight: bold;"> 芋道源码 </span></p> <p class="text-center"></p> <p class="text-center"> <span style="color: black;"> 发送：</span> <span class="token" style="color: #e9415a; font-weight: bold; font-size: 17px; margin-bottom: 45px;"> 百事可乐 </span> <br> <span style="color: black;"> 获取永久解锁本站全部文章的链接 </span> </p> </div> <div class="text-center"> <img class="code-img" style="width: 300px" src="/images/common/erweima.jpg"> </div> </div> </div>

摘要: 原创出处 http://www.iocoder.cn/XXL-JOB/install/ 「芋道源码」欢迎转载，保留摘要，谢谢！

*   [1. 概述](http://www.iocoder.cn/XXL-JOB/install/)
*   [2. 特性](http://www.iocoder.cn/XXL-JOB/install/)
*   [3. 架构设计](http://www.iocoder.cn/XXL-JOB/install/)
    *   [3.1 设计思想](http://www.iocoder.cn/XXL-JOB/install/)
    *   [3.2 系统组成](http://www.iocoder.cn/XXL-JOB/install/)
    *   [3.3 架构图](http://www.iocoder.cn/XXL-JOB/install/)
    *   [3.4 高可用](http://www.iocoder.cn/XXL-JOB/install/)
    *   [3.5 同类框架比较](http://www.iocoder.cn/XXL-JOB/install/)
*   [4. 搭建调度中心](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.1 克隆源码](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.2 初始化 XXL-JOB 表结构](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.3 修改配置文件](http://www.iocoder.cn/XXL-JOB/install/)
    *   [xxl-job, triggerpool max size 调度线程池最大线程配置](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.4 修改日志配置文件](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.5 IDEA 启动调度中心](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.6 编译源码](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.7 命令行启动调度中心](http://www.iocoder.cn/XXL-JOB/install/)
    *   [4.8 搭建集群](http://www.iocoder.cn/XXL-JOB/install/)
*   [5. 搭建执行器](http://www.iocoder.cn/XXL-JOB/install/)
*   [666. 彩蛋](http://www.iocoder.cn/XXL-JOB/install/)

> 推荐阅读如下 XXL-JOB 文章：
> 
> *   [《芋道 Spring Boot 定时任务入门》](http://www.iocoder.cn/Spring-Boot/Job/?self)

> 艿艿：XXL-JOB 的文档非常全，所以本文艿艿是基于 [《分布式任务调度平台 XXL-JOB 官方文档》](https://www.xuxueli.com/xxl-job/) ，同时参考 [《一文读懂分布式任务调度平台 XXL-JOB》](https://juejin.im/post/5d8b76436fb9a04e1325ca45#heading-9) 文章，进行整理和编写。
> 
> 顺便吐槽一句，新版文档的排版，左边不是文档目录，好难用啊。

XXL-JOB 是一个**轻量级**分布式任务调度平台，其核心设计目标是开发迅速、学习简单、轻量级、易扩展。

从它登记的[接入公司列表](https://www.xuxueli.com/xxl-job/#1.4%20%E5%8F%91%E5%B1%95) ，可以看到拍拍贷、优信二手车、京东、哈啰出行等知名的互联网公司正在使用中。所以，胖友们是可以放心大胆的在项目中使用。😈 目前，艿艿的项目的任务调度平台也是使用它，美滋滋。

XXL-JOB 提供了 35 点[特性](https://www.xuxueli.com/xxl-job/#1.3%20%E7%89%B9%E6%80%A7)列表，我们将其重新整理如下：

> 归类不一定绝对严谨。
> 
> 简单喵一眼，反正也记不住。在使用中，慢慢感受吧。

**① 功能强大**

*   1、简单：支持通过 Web 页面对任务进行 CRUD 操作，操作简单，一分钟上手；
*   2、动态：支持动态修改任务状态、启动 / 停止任务，以及终止运行中任务，即时生效；
*   15、事件触发：除了”Cron 方式” 和” 任务依赖方式” 触发任务执行之外，支持基于事件的触发任务方式。调度中心提供触发任务单次执行的 API 服务，可根据业务事件灵活触发。
*   18、GLUE：提供 Web IDE，支持在线开发任务逻辑代码，动态发布，实时编译生效，省略部署上线的过程。支持 30 个版本的历史版本回溯。 19、脚本任务：支持以 GLUE 模式开发和运行脚本任务，包括 Shell、Python、NodeJS、PHP、PowerShell 等类型脚本;
*   20、命令行任务：原生提供通用命令行任务 Handler（Bean 任务，”CommandJobHandler”）；业务方只需要提供命令行即可；
*   21、任务依赖：支持配置子任务依赖，当父任务执行结束且执行成功后将会主动触发一次子任务的执行, 多个子任务用逗号分隔；
*   23、自定义任务参数：支持在线配置调度任务入参，即时生效；
*   25、数据加密：调度中心和执行器之间的通讯进行数据加密，提升调度信息安全性；
*   30、跨平台：原生提供通用 HTTP 任务 Handler（Bean 任务，”HttpJobHandler”）；业务方只需要提供 HTTP 链接即可，不限制语言、平台；
*   31、国际化：调度中心支持国际化设置，提供中文、英文两种可选语言，默认为中文；

**② 高性能**

*   13、分片广播任务：执行器集群部署时，任务路由策略选择” 分片广播” 情况下，一次任务调度将会广播触发集群中所有执行器执行一次任务，可根据分片参数开发分片任务；
*   14、动态分片：分片广播任务以执行器为维度进行分片，支持动态扩容执行器集群从而动态增加分片数量，协同进行业务处理；在进行大数据量业务操作时可显著提升任务处理能力和速度。
*   24、调度线程池：调度系统多线程触发调度运行，确保调度精确执行，不被堵塞；
*   29、全异步：任务调度流程全异步化设计实现，如异步调度、异步运行、异步回调等，有效对密集调度进行流量削峰，理论上支持任意时长任务的运行；
*   33、线程池隔离：调度线程池进行隔离拆分，慢任务自动降级进入 ”Slow” 线程池，避免耗尽调度线程，提高系统稳定性；

**③ 高可用**

*   3、调度中心 HA（中心式）：调度采用中心式设计，“调度中心” 自研调度组件并支持集群部署，可保证调度中心 HA；
*   4、执行器 HA（分布式）：任务分布式执行，任务” 执行器” 支持集群部署，可保证任务执行 HA；
*   7、路由策略：执行器集群部署时提供丰富的路由策略，包括：第一个、最后一个、轮询、随机、一致性 HASH、最不经常使用、最近最久未使用、故障转移、忙碌转移等；
*   8、故障转移：任务路由策略选择” 故障转移” 情况下，如果执行器集群中某一台机器故障，将会自动 Failover 切换到一台正常的执行器发送调度请求。
*   9、阻塞处理策略：调度过于密集执行器来不及处理时的处理策略，策略包括：单机串行（默认）、丢弃后续调度、覆盖之前调度；
*   10、任务超时控制：支持自定义任务超时时间，任务运行超时将会主动中断任务；
*   11、任务失败重试：支持自定义任务失败重试次数，当任务失败时将会按照预设的失败重试次数主动进行重试；其中分片任务支持分片粒度的失败重试；
*   22、一致性：“调度中心” 通过 DB 锁保证集群分布式调度的一致性, 一次任务调度只会触发一次执行；

**④ 监控治理**

*   5、注册中心: 执行器会周期性自动注册任务, 调度中心将会自动发现注册的任务并触发执行。同时，也支持手动录入执行器地址；
*   6、弹性扩容缩容：一旦有新执行器机器上线或者下线，下次调度时将会重新分配任务；
*   12、任务失败告警；默认提供邮件方式失败告警，同时预留扩展接口，可方便的扩展短信、钉钉等告警方式；
*   16、任务进度监控：支持实时监控任务进度；
*   17、Rolling 实时日志：支持在线查看调度结果，并且支持以 Rolling 方式实时查看执行器输出的完整的执行日志；
*   26、邮件报警：任务失败时支持邮件报警，支持配置多邮件地址群发报警邮件；
*   27、推送 Maven 中央仓库: 将会把最新稳定版推送到 Maven 中央仓库, 方便用户接入和使用;
*   28、运行报表：支持实时查看运行数据，如任务数量、调度次数、执行器数量等；以及调度报表，如调度日期分布图，调度成功分布图等；
*   32、容器化：提供官方 Docker 镜像，并实时更新推送 Docker Hub，进一步实现产品开箱即用；
*   34、用户管理：支持在线管理系统用户，存在管理员、普通用户两种角色；
*   35、权限控制：执行器维度进行权限控制，管理员拥有全量权限，普通用户需要分配执行器权限后才允许相关操作；

> 艿艿：如果胖友不想看本小节，可以直接跳到[「4. 搭建调度中心」](#) 。

3.1 设计思想
--------

*   将调度行为抽象形成 “**调度中心**” 公共平台，而平台自身并不承担业务逻辑，“**调度中心**” 负责发起调度请求。
*   将任务抽象成分散的 [JobHandler](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-core/src/main/java/com/xxl/job/core/handler/IJobHandler.java) ，交由 “**执行器**” 统一管理，“**执行器**” 负责接收调度请求并执行对应的 JobHandler 中 业务逻辑。

因此，“调度”和 “任务” 两部分可以相互解耦，提高系统整体稳定性和扩展性。

如果胖友对分布式任务调度平台有一定了解的话，如果从调度系统的角度来看，可以分成两类：

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

3.2 系统组成
--------

整个 XXL-JOB 系统，由**调度中心**和**执行器**两个角色组成，分别处于不同的进程中。

**调度中心**

*   负责管理调度信息，按照调度配置发出调度请求，自身不承担业务代码。
*   调度系统与任务解耦，提高了系统可用性和稳定性，同时调度系统性能不再受限于任务模块。
*   支持可视化、简单且动态的管理调度信息，包括任务新建，更新，删除， GLUE 开发和任务报警等，所有上述操作都会实时生效。
*   支持监控调度结果以及执行日志，支持执行器 Failover 。

**执行器**

*   负责接收调度请求并执行任务逻辑。任务模块专注于任务的执行等操作，开发和维护更加简单和高效。
*   接收 “调度中心” 的执行请求、终止请求和日志请求等。

一般来说，XXL-JOB 执行器可以**内嵌**到应用服务里。例如说，一个提供 Restful API 的 Spring Boot 项目中，引入 `xxl-job-core` 依赖，同时也作为一个 XXL-JOB 执行器。😈 本质上，每次 Restful API 是请求任务，而每次任务调度是定时任务。

3.3 架构图
-------

整体架构如下图所示：

![](https://static.iocoder.cn/fe0e3739426128436d7402b028026c4e.jpg)

> 注意，`【】` 中填写调度中心和执行器；`[]` 中填写组件名。

*   注意，左边是调度中心，右边是执行器。
*   【执行器】：**[注册线程]** 根据配置的【调度中心】的地址，自动注册到【调度中心】。
*   【调度中心】：达到任务触发条件，【调度中心】下发任务给【执行器】。
*   【执行器】：基于 **[任务线程池]** 执行任务，并把执行结果放入 **[内存队列]** 中、把 **[执行日志]** 写入 Log 日志文件中。
*   【执行器】：**[回调线程]** 消费 **[内存队列]** 中的调度结果，主动上报给【调度中心】。
*   当用户在【调度中心】查看 **[Rolling 任务日志]**，【调度中心】请求【执行器】，【执行器】读取 Log 日志文件并返回日志详情。

3.4 高可用
-------

XXL-JOB 的高可用，需要考虑调度中心的高可用、以及执行器的高可用。

**注意**，虽然说 XXL-JOB 执行的是后台任务，即使挂掉，用户的感知度也比较低，但是考虑高可用是一种良好的习惯，**在高性能之前请做好高可用**。

### 3.4.1 调度中心的高可用

调度中心支持多节点部署，基于**数据库行锁**，保证触发器的名称和执行时间相同，则只且仅有一个调度中心节点去下发任务给执行器。

核心代码可见 XXL-JOB 的 [`JobScheduleHelper#start()`](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-admin/src/main/java/com/xxl/job/admin/core/thread/JobScheduleHelper.java#L37-L270) 方法：

```
conn = XxlJobAdminConfig.getAdminConfig().getDataSource().getConnection();
connAutoCommit = conn.getAutoCommit();
conn.setAutoCommit(false);
preparedStatement = conn.prepareStatement(  "select * from xxl_job_lock where lock_name = 'schedule_lock' for update" );
preparedStatement.execute();




conn.commit();
```

*   😈 XXL-JOB 的这块方法，小 200 行，有点头铁。

### 3.4.2 执行器的高可用

执行器支持多节点部署，通过调度中心选择其中的执行器，下发任务来执行。

当任务”路由策略”选择”故障转移 (FAILOVER)” 时，当调度中心每次发起调度请求时，会按照顺序对执行器发出心跳检测请求，第一个检测为存活状态的执行器将会被选定并发送调度请求。

**① 路由策略**

调度中心基于路由策略，选择一个执行器节点下发任务，从而让执行器执行任务。XXL-JOB 提供了如下路由策略保证任务调度高可用：

*   忙碌转移 (BUSYOVER) 策略：当调度中心每次发起调度请求时，会按照顺序对执行器发出**空闲检测**请求，第一个检测为空闲状态的执行器将会被选定并发送调度请求。具体代码，见 [ExecutorRouteFailover](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-admin/src/main/java/com/xxl/job/admin/core/route/strategy/ExecutorRouteBusyover.java) 类。
*   故障转移 (FAILOVER) 策略：当调度中心每次发起调度请求时，会按照顺序对执行器发出**心跳检测**请求，第一个检测为存活状态的执行器将会被选定并发送调度请求。具体代码，见 [ExecutorRouteFailover](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-admin/src/main/java/com/xxl/job/admin/core/route/strategy/ExecutorRouteFailover.java) 类。

还有第一个 (FIRST)、LAST(最后一个)、轮询(ROUND)、随机(RANDOM)、CONSISTENT_HASH(一致性 HASH)、最不经常使用(LEAST_FREQUENTLY_USED)、最近最久未使用(LEAST_RECENTLY_USED)、分片广播(SHARDING_BROADCAST) 路由策略，胖友可以看看 [`com.xxl.job.admin.core.route.strategy/`](https://github.com/xuxueli/xxl-job/tree/master/xxl-job-admin/src/main/java/com/xxl/job/admin/core/route/strategy) 包下的代码。

> 艿艿：严格来说，②、③、④ 不算实现执行器的高可用的点，列出来只是不知道放在哪里，又觉得用户需要了解。

**② 阻塞处理策略**

当调度过于密集时，执行器来不及处理时，则当执行器节点存在多个相同任务编号的任务未执行完成，则需要基于阻塞处理策略对任务进行取舍：

*   单机串行（默认）：调度请求进入单机执行器后，调度请求进入 FIFO 队列并以串行方式运行。
*   丢弃后续调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败。
*   覆盖之前调度：调度请求进入单机执行器后，发现执行器存在运行的调度任务，将会终止运行中的调度任务并清空队列，然后运行本地调度任务。

具体可见 [`ExecutorBizImpl#run()`](https://github.com/xuxueli/xxl-job/blob/master/xxl-job-core/src/main/java/com/xxl/job/core/biz/impl/ExecutorBizImpl.java#L144-L172) 方法的代码。

**③ 故障转移 & 失败重试**

一次完整任务流程包括” 调度（调度中心） + 执行（执行器）” 两个阶段。

*   “故障转移” 发生在调度阶段，在执行器集群部署时，如果某一台执行器发生故障，该策略支持自动进行 Failover 切换到一台正常的执行器机器并且完成调度请求流程。
*   “失败重试” 发生在” 调度 + 执行” 两个阶段，支持通过自定义任务失败重试次数，当任务失败时，将会按照预设的失败重试次数主动进行重试。

**④ 任务超时时间**

支持设置任务超时时间，任务运行超时的情况下，将会**主动中断任务**。

需要注意的是，任务超时中断时与任务终止机制类似，也是通过 “interrupt” 中断任务，**因此业务代码需要将 InterruptedException 外抛**，否则功能不可用。

3.5 同类框架比较
----------

<table><thead><tr><th>特性</th><th>quartz</th><th>elastic-job-lite</th><th>xxl-job</th><th>LTS</th></tr></thead><tbody><tr><td>依赖</td><td>MySQL、jdk</td><td>jdk、zookeeper</td><td>mysql、jdk</td><td>jdk、zookeeper、maven</td></tr><tr><td>高可用</td><td>多节点部署，通过竞争数据库锁来保证只有一个节点执行任务</td><td>通过 zookeeper 的注册与发现，可以动态的添加服务器</td><td>基于竞争数据库锁保证只有一个节点执行任务，支持水平扩容。可以手动增加定时任务，启动和暂停任务，有监控</td><td>集群部署, 可以动态的添加服务器。可以手动增加定时任务，启动和暂停任务。有监控</td></tr><tr><td>任务分片</td><td>×</td><td>√</td><td>√</td><td>√</td></tr><tr><td>管理界面</td><td>×</td><td>√</td><td>√</td><td>√</td></tr><tr><td>难易程度</td><td>简单</td><td>简单</td><td>简单</td><td>略复杂</td></tr><tr><td>高级功能</td><td>-</td><td>弹性扩容，多种作业模式，失效转移，运行状态收集，多线程处理数据，幂等性，容错处理，spring 命名空间支持</td><td>弹性扩容，分片广播，故障转移，Rolling 实时日志，GLUE（支持在线编辑代码，免发布）, 任务进度监控，任务依赖，数据加密，邮件报警，运行报表，国际化</td><td>支持 spring，spring boot，业务日志记录器，SPI 扩展支持，故障转移，节点监控，多样化任务执行结果支持，FailStore 容错，动态扩容。</td></tr><tr><td>版本更新</td><td>半年没更新</td><td>2 年没更新</td><td>最近有更新</td><td>1 年没更新</td></tr></tbody></table>

也推荐看看如下文章：

*   [《分布式定时任务调度系统技术选型》](https://www.expectfly.com/2017/08/15/%E5%88%86%E5%B8%83%E5%BC%8F%E5%AE%9A%E6%97%B6%E4%BB%BB%E5%8A%A1%E6%96%B9%E6%A1%88%E6%8A%80%E6%9C%AF%E9%80%89%E5%9E%8B/)
*   [《Azkaban、Xxl-Job 与 Airflow 对比分析》](https://my.oschina.net/centychen/blog/3044544)

本小节，我们来搭建一个调度中心。XXL-JOB 暂未提供直接直接启动的 `jar` 包，所以需要自己编译源码。

考虑到降低大家的学习成本，我们使用 IDEA 进行操作。

4.1 克隆源码
--------

使用 IDEA ，从码云 [https://gitee.com/xuxueli0323/xxl-job](https://gitee.com/xuxueli0323/xxl-job) 克隆源码。从码云克隆的原因是，速度比较快。

> 如果胖友想直接下载源码，可以到 [XXL-JOB Releases](https://github.com/xuxueli/xxl-job/releases) 下载需要的版本。

克隆完成后，耐心等待下载完依赖。完成后，整体项目结构如下图：[项目结构](http://www.iocoder.cn/images/XXL-JOB/2019-11-28/1.jpg)

*   `xxl-job-core` 模块：XXL-JOB 核心。后续我们在编写执行器时，会引入该模块。
*   `xxl-job-admin` 模块：调度中心。
*   `xxl-job-executor-samples` 模块：提供了在 Spring、Spring Boot、JFinal、Nutz 等框架下的使用示例。

这里，我们需要编译的主要是 `xxl-job-admin` 模块，即调度中心。

4.2 初始化 XXL-JOB 表结构
-------------------

在 [`doc/db/tables_xxl_job.sql`](https://gitee.com/xuxueli0323/xxl-job/blob/master/doc/db/tables_xxl_job.sql) 地址，是 XXL-JOB 表结构的初始化脚本。我们需要在数据库中执行该脚本，完成初始化 XXL-JOB 表结构。如下图所示：[XXL-JOB 表结构](http://www.iocoder.cn/images/XXL-JOB/2019-11-28/02.jpg)

*   xxl_job_lock：任务调度锁表；
*   xxl_job_group：执行器信息表，维护任务执行器信息；
*   xxl_job_info：调度扩展信息表： 用于保存 XXL-JOB 调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等；
*   xxl_job_log：调度日志表： 用于保存 XXL-JOB 任务调度的历史信息，如调度结果、执行结果、调度入参、调度机器和执行器等等；
*   xxl_job_log_report：调度日志报表：用户存储 XXL-JOB 任务调度日志的报表，调度中心报表功能页面会用到；
*   xxl_job_logglue：任务 GLUE 日志：用于保存 GLUE 更新历史，用于支持 GLUE 的版本回溯功能；
*   xxl_job_registry：执行器注册表，维护在线的执行器和调度中心机器地址信息；
*   xxl_job_user：系统用户表；

自 XXL-JOB 2.1.0 Release 版本，去除对 Quartz 的依赖，所以我们就看不到 Quartz 相关的表哈。

4.3 修改配置文件
----------

打开 `xxl-job-admin` 模块，修改 `src/main/resources/application.properties` 配置文件。如下：

```
### web # Web 服务器
server.port=8080
server.context-path=/xxl-job-admin

### actuator
management.context-path=/actuator
management.health.mail.enabled=false

### resources
spring.mvc.static-path-pattern=/static/**
spring.resources.static-locations=classpath:/static/

### freemarker
spring.freemarker.templateLoaderPath=classpath:/templates/
spring.freemarker.suffix=.ftl
spring.freemarker.charset=UTF-8
spring.freemarker.request-context-attribute=request
spring.freemarker.settings.number_format=0.##########

### mybatis
mybatis.mapper-locations=classpath:/mybatis-mapper/*Mapper.xml

### xxl-job, datasource 调度中心 JDBC 链接
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/xxl_job?Unicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root_pwd
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.type=org.apache.tomcat.jdbc.pool.DataSource
spring.datasource.tomcat.max-wait=10000
spring.datasource.tomcat.max-active=30
spring.datasource.tomcat.test-on-borrow=true
spring.datasource.tomcat.validation-query=SELECT 1
spring.datasource.tomcat.validation-interval=30000

### xxl-job email 报警邮箱
spring.mail.host=smtp.qq.com
spring.mail.port=25
spring.mail.username=xxx@qq.com
spring.mail.password=xxx
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory

### xxl-job, access token 调度中心通讯TOKEN [选填]：非空时启用；调度中心国际化配置 [选填]： 默认为空，表示中文; "en" 表示英文；
xxl.job.accessToken=

### xxl-job, i18n (default empty as chinese, "en" as english)
xxl.job.i18n=

## xxl-job, triggerpool max size 调度线程池最大线程配置
xxl.job.triggerpool.fast.max=200
xxl.job.triggerpool.slow.max=100

### xxl-job, log retention days 调度中心日志表数据保存天数 [必填]：过期日志自动清理；限制大于等于7时生效，否则, 如-1，关闭自动清理功能；
xxl.job.logretentiondays=30
```

可以看到 XXL-JOB 使用了 Spring Boot ，嘿嘿。配置项比较多，艿艿说下必须要改的项：

*   `server.port` ：XXL-JOB 调度中心的服务器地址。可以根据自己的需要，修改该端口。
*   `spring.datasource` ：XXL-JOB 调度中心的数据源地址，**必须**修改成自己准备提供给 XXL-JOB 的数据库地址。
*   `spring.mail` ：报警邮箱，生产环境下**必须**配置，不然定时任务执行报错都不知道，简直要命。😈 一般来说下，建议有时间的胖友，修改下 XXL-JOB 的源码，把钉钉告警接入。
*   `xxl.job.accessToken` ：调度中心通讯令牌，**建议**填写。虽然说，内网一般很安全，但是以防万一，并且又没啥成本，直接给整上。

4.4 修改日志配置文件
------------

打开 `xxl-job-admin` 模块，修改 `src/main/resources/logback.xml` 配置文件。如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false" scan="true" scanPeriod="1 seconds">

    <contextName>logback</contextName>
    <property />

    <appender >
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} %contextName [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <appender >
        <file>${log.path}</file>
        <rollingPolicy>
            <fileNamePattern>${log.path}.%d{yyyy-MM-dd}.zip</fileNamePattern>
        </rollingPolicy>
        <encoder>
            <pattern>%date %level [%thread] %logger{36} [%file : %line] %msg%n
            </pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="console"/>
        <appender-ref ref="file"/>
    </root>

</configuration>
```

*   默认情况下，日志输出的地址是 `/data/applogs/xxl-job/xxl-job-admin.log` 。可以根据自己的需要，进行调整。

4.5 IDEA 启动调度中心
---------------

在开始编译源码之前，我们先直接使用 XxlJobAdminApplication 类，运行启动调度中心。这样，避免我们后面编译源码，进行打包查出来的 `jar` 包，结果配置文件不对的尴尬。

当看到如下日志，代表启动成功：

```
23:19:11.950 logback [main] INFO  o.a.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
23:19:12.026 logback [main] INFO  o.a.tomcat.util.net.NioSelectorPool - Using a shared selector for servlet write/read
23:19:12.181 logback [main] INFO  o.s.b.c.e.t.TomcatEmbeddedServletContainer - Tomcat started on port(s): 8080 (http)
```

启动成功后，浏览器 [http://127.0.0.1:8080/xxl-job-admin](http://127.0.0.1:8080/xxl-job-admin) 地址，并使用默认 "admin/123456" 进行登录。如果登录成功，说明我们已经配置正确啦。

4.6 编译源码
--------

```
$ cd /Users/yunai/Java/xxl-job2/


$ mvn clean package -pl xxl-job-admin -am -DskipTests
```

打包完成后，在 `xxl-job-admin/target/xxl-job-admin-2.1.2-SNAPSHOT.jar` 地址下，就是我们要启动的 XXL-JOB 调度中心的 `jar` 包。

4.7 命令行启动调度中心
-------------

```
$ cd xxl-job-admin/target/


$ jar -jar xxl-job-admin-2.1.2-SNAPSHOT.jar
```

当看到如下日志，代表启动成功：

```
23:19:11.950 logback [main] INFO  o.a.coyote.http11.Http11NioProtocol - Starting ProtocolHandler ["http-nio-8080"]
23:19:12.026 logback [main] INFO  o.a.tomcat.util.net.NioSelectorPool - Using a shared selector for servlet write/read
23:19:12.181 logback [main] INFO  o.s.b.c.e.t.TomcatEmbeddedServletContainer - Tomcat started on port(s): 8080 (http)
```

启动成功后，浏览器 [http://127.0.0.1:8080/xxl-job-admin](http://127.0.0.1:8080/xxl-job-admin) 地址，并使用默认 "admin/123456" 进行登录。如果登录成功，说明我们已经配置正确啦。

另外，启动完成之后，记得去「用户管理」菜单，修改下管理员的密码哟，不然嘿嘿嘿。

4.8 搭建集群
--------

> 艿艿：如果胖友仅仅是简单入门，这步为可选项。

在生产环境下，一定要部署 XXL-JOB 调度中心的集群，提升调度系统容灾和可用性。

调度中心集群部署时，几点要求和建议：

*   DB 配置保持一致；
*   集群机器时钟保持一致（单机集群忽视）；
*   推荐通过 Nginx 为调度中心集群做负载均衡，分配域名。调度中心访问、执行器回调配置、调用 API 服务等操作均通过该域名进行。

另外，如果胖友想要使用 Docker 镜像方式搭建调度中心，可以自行参看 XXL-JOB 的官方文档。

在 [《芋道 Spring Boot 定时任务入门》](http://www.iocoder.cn/Spring-Boot/Job/?self) 的 [「5. 快速入门 XXL-JOB」](#) 小节，我们提供了如何在 Spring Boot 中，使用 XXL-JOB 执行器。嘻嘻。

暂时木有彩蛋，后续艿艿在补充下我们对 XXL-JOB 的使用的最佳实践和改造。例如说：

*   接入钉钉告警
*   修改成 Spring Boot 2.X 版本
*   监控 XXL-JOB 调度中心
*   等等

建议的话，有时间在细细看看 [《分布式任务调度平台 XXL-JOB 官方文档》](https://www.xuxueli.com/xxl-job/) 吧，查漏补缺，嘿嘿。。