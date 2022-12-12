> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.iocoder.cn](https://www.iocoder.cn/Spring-Boot/Actuator/?self)

> 摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/Actuator/ 「芋道源码」欢迎转载，保留摘要，谢谢！ 1. 概述 2. 快速入门 3. 内置端点 4. he......

<div id="locker" style="display: block;"> <div class="mask"></div> <div class="info"> <div> <p class="text-center" style="color: black;"> 扫码关注公众号：<span style="color: #E9405A; font-weight: bold;"> 芋道源码 </span></p> <p class="text-center"></p> <p class="text-center"> <span style="color: black;"> 发送：</span> <span class="token" style="color: #e9415a; font-weight: bold; font-size: 17px; margin-bottom: 45px;"> 百事可乐 </span> <br> <span style="color: black;"> 获取永久解锁本站全部文章的链接 </span> </p> </div> <div class="text-center"> <img class="code-img" style="width: 300px" src="/images/common/erweima.jpg"> </div> </div> </div>

摘要: 原创出处 http://www.iocoder.cn/Spring-Boot/Actuator/ 「芋道源码」欢迎转载，保留摘要，谢谢！

*   [1. 概述](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [2. 快速入门](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [3. 内置端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [4. health 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [5. info 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [6. metrics 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [7. httptrace 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [8. auditevents 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [9. beans 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [10. conditions 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [11. env 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [12. configprops 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [13. mappings 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [14. loggers 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [15. threaddump 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [16. heapdump 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [17. shutdown 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [18. sessions 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [19. scheduledtasks 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [20. flyway 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [21. liquibase 端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [22. 自定义端点](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [23. 自定义端口](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [24. 安全认证](http://www.iocoder.cn/Spring-Boot/Actuator/)
*   [666. 彩蛋](http://www.iocoder.cn/Spring-Boot/Actuator/)

> 本文在提供完整代码示例，可见 [https://github.com/YunaiV/SpringBoot-Labs](https://github.com/YunaiV/SpringBoot-Labs) 的 [lab-34](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-34) 目录。
> 
> 原创不易，给点个 [Star](https://github.com/YunaiV/SpringBoot-Labs/stargazers) 嘿，一起冲鸭！

现在在阅读本文的胖友，估计绝大多数都是 Java 开发者。很多时候，我们只关心**开发**部分，但是应用在部署在**生产环境**下，我们还需要考虑应用的**管理与监控**。例如说，应用是否健康存活、应用的 JVM 监控信息、服务器的监控信息（CPU、内存、磁盘等等）。

如果我们为应用的管理与监控做相应的开发，是需要一定的成本的。幸运的是，在 Spring Boot 框架提供了一个非常重要的**新组件** [spring-boot-actuator](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-actuator) 。其文档介绍如下：

> FROM [《Spring Boot Actuator: Production-ready Features》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html)
> 
> Spring Boot includes a number of additional features to help you monitor and manage your application when you push it to production.  
> Spring Boot 包含许多附加功能，可以帮助您在将应用程序推向生产环境时对其进行监视和管理。
> 
> You can choose to manage and monitor your application by using HTTP endpoints or with JMX. 您可以选择使用 HTTP 端点或 JMX 来管理和监视应用程序。
> 
> Auditing, health, and metrics gathering can also be automatically applied to your application.
> 
> 审计（auditing）、健康状况（health）和指标（metrics）收集也可以自动应用到您的应用程序中。

可能对监控了解比较少的胖友，理解起来略微会有一点懵逼。简单的来说，Spring Boot Actuator 提供 HTTP API 接口，返回应用的审计（auditing）、健康状况（health）和指标（metrics）等数据。

「Talk is cheap. Show me the code」让我们先来快速入门，一起来感受下。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 。

本小节，我们来对 Actuator 做一个快速入门，以便我们对它有个直观的感受和认识。

2.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/pom.xml) 文件中，引入相关依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.2.RELEASE</version>
        <relativePath/> 
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-34-acturator-demo</artifactId>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

</project>
```

*   具体每个依赖的作用，胖友自己认真看下艿艿添加的所有注释噢。
*   另外，`spring-boot-starter-web` 依赖并不是必须的，仅仅是为了保证 Spring Boot 应用启动后持续运行。

2.2 配置文件
--------

在 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/src/main/resources/application.yaml) 中，添加 Actuator 配置，如下：

```
management:
  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude:
```

*   在 `management.endpoints.web` 配置项，设置 Actuator HTTP 配置项，对应 [WebEndpointProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/endpoint/web/WebEndpointProperties.java) 配置类。
*   Spring Boot 提供的 [WebEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/endpoint/web/WebEndpointAutoConfiguration.java) 自动化配置类，实现 Actuator HTTP 端点的配置。
*   重点，注意看下艿艿在 `include` 和 `exclude` 的注释。因为 Actuator 提供了多种端点 ( [Endpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint/annotation/Endpoint.java) )，可以通过配置来开启或关闭端点。这里，我们配置 `include: '*'` ，开启所有 Actuator 端点。

2.3 Application
---------------

创建 [`Application.java`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/Application.java) 类，配置 `@SpringBootApplication` 注解即可。代码如下：

```
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
}
```

2.4 简单测试
--------

打开浏览器，访问 [http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health) 地址，获得应用的健康信息。响应结果如下：

```
{
    "status": "UP"
}
```

*   表示应用处于**开启**状态。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/beans](http://127.0.0.1:8080/actuator/beans) 地址，获得应用的所有 Spring Bean 信息。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/01.png)

至此，我们已经完成了对 Spring Boot Actuator 的快速入门。

在 [`org.springframework.boot.actuate`](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate) 包下，我们可以看到 Actuator 已经和**多个框架**进行集成，提供**内置端点**。如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/02.png)

在[《Spring Boot Actuator: Production-ready Features》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints)文档中，官方列举整理端点如下表格：

<table><thead><tr><th>ID</th><th>Description</th></tr></thead><tbody><tr><td><code>auditevents</code></td><td>Exposes audit events information for the current application. Requires an <code>AuditEventRepository</code> bean.</td></tr><tr><td><code>beans</code></td><td>Displays a complete list of all the Spring beans in your application.</td></tr><tr><td><code>caches</code></td><td>Exposes available caches.</td></tr><tr><td><code>conditions</code></td><td>Shows the conditions that were evaluated on configuration and auto-configuration classes and the reasons why they did or did not match.</td></tr><tr><td><code>configprops</code></td><td>Displays a collated list of all <code>@ConfigurationProperties</code>.</td></tr><tr><td><code>env</code></td><td>Exposes properties from Spring’s <code>ConfigurableEnvironment</code>.</td></tr><tr><td><code>flyway</code></td><td>Shows any Flyway database migrations that have been applied. Requires one or more <code>Flyway</code> beans.</td></tr><tr><td><code>health</code></td><td>Shows application health information.</td></tr><tr><td><code>httptrace</code></td><td>Displays HTTP trace information (by default, the last 100 HTTP request-response exchanges). Requires an <code>HttpTraceRepository</code> bean.</td></tr><tr><td><code>info</code></td><td>Displays arbitrary application info.</td></tr><tr><td><code>integrationgraph</code></td><td>Shows the Spring Integration graph. Requires a dependency on <code>spring-integration-core</code>.</td></tr><tr><td><code>loggers</code></td><td>Shows and modifies the configuration of loggers in the application.</td></tr><tr><td><code>liquibase</code></td><td>Shows any Liquibase database migrations that have been applied. Requires one or more <code>Liquibase</code> beans.</td></tr><tr><td><code>metrics</code></td><td>Shows ‘metrics’ information for the current application.</td></tr><tr><td><code>mappings</code></td><td>Displays a collated list of all <code>@RequestMapping</code> paths.</td></tr><tr><td><code>scheduledtasks</code></td><td>Displays the scheduled tasks in your application.</td></tr><tr><td><code>sessions</code></td><td>Allows retrieval and deletion of user sessions from a Spring Session-backed session store. Requires a Servlet-based web application using Spring Session.</td></tr><tr><td><code>shutdown</code></td><td>Lets the application be gracefully shutdown. Disabled by default.</td></tr><tr><td><code>threaddump</code></td><td>Performs a thread dump.</td></tr></tbody></table>

下面，我们就逐个端点来瞅瞅，加深理解。

> 示例代码对应仓库：[lab-34-actuator-demo-health](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-health/) 。

`health` 端点，对应 `GET /actuator/health` 接口，对应 [HealthEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/HealthEndpoint.java) 类。

用于获取应用的健康状态。`health` 端点通过健康指示器 [HealthIndicator](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/HealthIndicator.java) ，获取不同的资源的健康信息。Actuator 内置了多个 HealthIndicator 实现，如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/03.jpg)

*   [DiskSpaceHealthIndicator](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/system/DiskSpaceHealthIndicator.java) ，基于磁盘空间的 HealthIndicator 实现类。默认配置下，剩余磁盘不足 10MB 时，则认为不健康。
*   [PingHealthIndicator](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/PingHealthIndicator.java) ，基于 Ping 的 HealthIndicator 实现类。因为能访问上 `GET /actuator/health` 接口，说明就是 Ping 的通，所以只返回健康。

当然，我们可以实现 HealthIndicator 接口，自定义自己的健康指示器。

在 `health` 端点中，一定定义了四种 [Status](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/Status.java) 状态：

*   `UP` ：可用。
*   `DOWN` ：不可用。
*   `OUT_OF_SERVICE` ：暂不提供服务，一般是开发者主动设置。可以认为是一种 “**特殊**” 的不可用。
*   `UNKNOWN` ：未知状态。

下面，我们就来看看 `health` 端点的示例，同时编写一个自定义的 HealthIndicator 实现类。考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-health](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-health/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

4.1 配置文件
--------

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-health/src/main/resources/application.yaml) 配置，增加 `health` 端点的配置。配置如下：

```
management:
  endpoint:
    
    health:
      enabled: true 
      show-details: ALWAYS 
      status:
        http-mapping: 
          DOWN: 503
        order: DOWN, OUT_OF_SERVICE, UP, UNKNOWN 
  health:
    
    diskspace:
      enabled: true 
      path: . 
      threshold: 
  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude:
```

额外增加 `management.endpoint.health` 配置项，设置 `health` 端点的配置项，对应 [HealthProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/health/HealthProperties.java) 配置类。

*   Spring Boot 提供的 [HealthEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/health/HealthEndpointAutoConfiguration.java) 自动化配置类，实现 `health` 端点的配置。
    
*   `enabled` ：是否开启。默认为 `true` 开启。虽然我们通过 `management.endpoints.web.web.exposure.include = *` 配置，开放所有端点，但是它仅仅是开放 `health` 端点的 **HTTP** ，实际还是需要配置 `enabled = true` 来开启 `health` 端点的**功能**。不过，因为 `enabled` 默认为 `true` ，所以也不需要主动去配置。
    
*   `show-details` ：何时显示完整的健康信息。它一共有三种选择项 [HealthProperties.Show](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/health/HealthProperties.java)：
    
    *   `NEVER` ：**默认值**，都不展示。这样，`health` 端点只会返回应用的**汇总**健康状态，不会包含完整的健康信息，正如我们在[「2.4 简单测试」](#)所看到的。
    *   `ALWAYS` ：总是展示。本示例我们采用该选择项，这样我们就可以看到每一个 HealthIndicator 提供的健康信息。
    *   `WHEN_AUTHORIZED` ：经过授权的用户，可看到完整的健康信息。
*   `status.http-mapping` ：设置不同健康状态 (`status`) 对应的响应状态码。例如说，在我们配合 [Tengine](https://tengine.taobao.org/) 的 [健康检查功能](https://tengine.taobao.org/document_cn/http_upstream_check_cn.html) 时，通过检测 `health` 端点，发现应用的健康状态为 `DOWN` 时，进行摘除。不过因为 Tengine 只能基于**响应的状态码**判断应用是否健康，所以我们需要将 `DOWN` 状态的应用，`health` 端点的返回 503 资源不可用。此时，我们就需要通过 `status.http-mapping` 来配置了。
    
    > 艿艿后来查询了下官方文档，默认情况下，`UP` 和 `UNKNOWN` 状态返回 200 响应码，`DOWN` 和 `OUT_OF_SERVICE` 返回 503 状态码 (SERVICE_UNAVAILABLE) 。
    > 
    > 所以，这里我们也是无需配置的。
    
*   `status.order` ：健康状态排序值。这里我们配置的 `DOWN, OUT_OF_SERVICE, UP, UNKNOWN` 就是**默认**的排序。因为多个 HealthIndicator 可能会返回不同的结果，最终需要**汇总**成一个健康状态。此时，以结果中按照 `status.order` 来排序之后，排在第一个的结果为**汇总**状态。如果有点懵逼的胖友，我们可以在[「4.3 简单测试」](#)中，进一步理解。
    
*   这里，艿艿主要是为了让胖友了解 `management.endpoint.health` 配置项。一般情况下，我们无需**主动**配置它，默认即可。
    

额外增加 `management.health.diskspace` 配置项，设置 DiskSpaceHealthIndicator 的配置项，对应 [DiskSpaceHealthIndicatorProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/system/DiskSpaceHealthIndicatorProperties.java) 配置类。

*   Spring Boot 提供的 [DiskSpaceHealthContributorAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/system/DiskSpaceHealthContributorAutoConfiguration.java) 自动化配置类，实现 DiskSpaceHealthIndicator 的配置。
*   `enabled` 属性：是否开启。默认为 `true` 开启。
*   `path` 属性：目录。默认为 `.` 当前目录。
*   `threshold` 属性：剩余空间的阀值。默认为 10M 。
*   这里，艿艿主要是为了让胖友了解 `management.health.diskspace` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

4.2 DemoHealthIndicator
-----------------------

在本小节，我们自定义一个 HealthIndicator 实现类。在 [`cn.iocoder.springboot.lab34.actuatordemo.actuate`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-health/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/actuate/) 包下，创建 [DemoHealthIndicator](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-health/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/actuate/DemoHealthIndicator.java) 类，继承 [AbstractHealthIndicator](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/AbstractHealthIndicator.java) 抽象类，示例 HealthIndicator 。代码如下：

```
@Component
public class DemoHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        
        boolean success = checkSuccess();

        
        if (success) {
            builder.up().build();
            return;
        }

        
        builder.down().withDetail("msg", "我就是做个示例而已");
    }

    private boolean checkSuccess() {
        return false;
    }

}
```

*   AbstractHealthIndicator 是由 Actuator 提供的抽象基类，方便实现 HealthIndicator ，我们仅需实现 `#doHealthCheck(Health.Builder builder)` 方法即可。当然，内置的 HealthIndicator ，也是继承 AbstractHealthIndicator 抽象类。
*   在类上，添加 `@Component` 注解，保证能够被 `health` 端点获取到。😈 不然，咱不就白自定义实现 HealthIndicator 了嘛。
*   `<1>` 处，调用 `#checkSuccess()` 方法，判断是否健康。这里因为是示例，我们先直接返回 `false` 。
*   `<2>` 处，如果健康，则调用 [`Health.Builder#up()`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/Health.java#L288-L294) 方法，标记健康状态为 `Status.UP` 。
*   `<3>` 处，如果不健康，则调用 [`Health.Builder#down()`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/Health.java#L237-L248) 方法，标记健康状态为 `Status.DOWN` 。同时，调用 [`Health.Builder#withDetail(String key, Object value)`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/Health.java#L237-L248) 方法，添加一组键值对，设置到详细信息中。

4.3 简单测试
--------

**① 第一轮**

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health) 地址，获得应用的健康信息。响应结果如下：

```
{
  "status": "DOWN",
  "components": {
    "demo": {
      "status": "DOWN",
      "details": {
        "msg": "我就是做个示例而已"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 173201915904,
        "threshold": 10485760
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

因为我们配置了 `management.endpoint.health.show-details=ALWAYS` ，所以可以看到完整的健康信息。这里，展示了 3 个 HealthIndicator 的健康状态的结果：

*   `"demo"` 对应 DemoHealthIndicator ，因为我们在 `#checkSuccess()` 方法中强制返回了 `false` ，所以返回的健康状态就是 `DOWN` 。同时，我们也在 `details` 中看到了我们设置的 `msg` 键值对。
*   `"diskSpace"` 对应 DiskSpaceHealthIndicator ，返回的健康状态的结果是 `UP` 。同时，我们在 `details` 中可以看到 DiskSpaceHealthIndicator 设置的详细信息。
*   `"ping"` 对应 PingHealthIndicator ，返回的健康状态的结果是 `UP` 。因为 DiskSpaceHealthIndicator 没有设置详细信息，所以看不到 `details` 。

最终返回的健康状态是 `DOWN` 。我们回过头看下我们在 `management.endpoint.health.status.order` 的讲解。

*   这里一共返回了 `[UP, UP, DOWN]` 三个健康状态，按照 `management.endpoint.health.status.order` 排序之后，结果是 `[DOWN, UP, UP]` 。
*   取排序后的健康状态结果的**第一个** `DOWN` ，成为最终的**汇总**状态进行返回。

上述的逻辑，通过 [SimpleStatusAggregator](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/health/SimpleStatusAggregator.java) 的 `#getAggregateStatus(Set<Status> statuses)` 来聚合。代码如下：

```
private final List<String> order;

private final Comparator<Status> comparator = new StatusComparator();

@Override
public Status getAggregateStatus(Set<Status> statuses) {
	return statuses.stream()
	.filter(this::contains) 
	.sorted(this.comparator) 
	.findFirst() 
	.orElse(Status.UNKNOWN); 
}
```

*   `<1>` 处，过滤掉多个 HealthIndicator 健康状态集合中，不包含在状态排序数组 `order` 的状态。
*   `<2>` 处，按照 `comparator` 进行排序。该 StatusComparator 排序器，基于状态排序数组 `order` 进行升序。
*   `<3>` 处，获得排序结果中的第一个状态。
*   `<4>` 处，如果因为 `<1>` 处过滤后的结果集合为空，则返回 `Status.UNKNOWN` 状态。
*   😈 这里算是 Java8 Stream API 的经典用法，胖友在项目中也可以尝试一波。

**① 第二轮**

修改 DemoHealthIndicator 的 `#checkSuccess()` 方法，强制返回 `true` 。然后，重启 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/health](http://127.0.0.1:8080/actuator/health) 地址，获得应用的健康信息。响应结果如下：

```
{
  "status": "UP",
  "components": {
    "demo": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 174055534592,
        "threshold": 10485760
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

因为 3 个 HealthIndicator 的健康状态的结果都是 `UP` ，所以最终返回的健康状态也为 `UP` 。

> **重要**：更多关于 `health` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 2.8. Health Information》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-health)文档。

> 示例代码对应仓库：[lab-34-actuator-demo-info](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/) 。

`info` 端点，对应 `GET /actuator/info` 接口，对应 [InfoEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/InfoEndpoint.java) 类。

用于获取应用的信息。`info` 端点通过信息贡献者 [InfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/InfoContributor.java) ，获取不同的来源的信息。Actuator 内置了多个 InfoContributor 实现，如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/04.png)

当然，我们可以实现 InfoContributor 接口，自定义自己的信息贡献者。

下面，我们就来看看 `info` 端点的示例，同时编写一个自定义的 InfoContributor 实现类。考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-info](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

注意，下面的每一个小节，都对应一个 InfoContributor 的使用示例。

5.1 EnvironmentInfoContributor
------------------------------

[EnvironmentInfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/EnvironmentInfoContributor.java)，提供 来自 `info` 配置下的应用信息。

### 5.1.1 配置文件

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/resources/application.yaml) 配置，增加 EnvironmentInfoContributor 的配置。配置如下：

```
management:
  endpoint:
    
    info:
      enabled: true 
  info:
    
    env:
      enabled: true

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude: 


info:
  app:
    java:
      source: @java.version@
      target: @java.version@
    encoding: UTF-8
    version: @project.version@
```

额外增加 `management.endpoint.info` 配置项，设置 `info` 端点的配置项，无对应配置类。

*   Spring Boot 提供的 [InfoEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/info/InfoEndpointAutoConfiguration.java) 自动化配置类，实现 `info` 端点的配置。
*   `enabled` ：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.endpoint.info` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

额外增加 `management.info.env` 配置项，设置 EnvironmentInfoContributor 的配置项，无对应配置类。

*   Spring Boot 提供的 [InfoContributorAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/info/InfoContributorAutoConfiguration.java) 自动化配置类，实现 EnvironmentInfoContributor 的配置。
*   `enabled` 属性：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.info.env` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

额外增加 `info` 配置项，设置应用的信息，无对应配置类。比较特别的是，我们可以通过 `"@...@"` 来读取 Maven 的属性。例如说：`@java.version@` 读取 Maven 构建项目的 Java 版本。

### 5.1.2 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，获得应用的信息。响应结果如下：

```
{
  "app": {
    "java": {
      "source": "1.8.0_144",
      "target": "1.8.0_144"
    },
    "encoding": "UTF-8",
    "version": "2.2.2.RELEASE"
  }
}
```

*   `"app"` 响应结果，就是我们在 `info` 配置项下添加的。

此时，我们打开构建结构 `target/classes/application.yaml` 配置文件，会发现 `info` 配置项，在构建时候，已经被重写。该部分配置下：

```
info:
  app:
    java:
      source: 1.8.0_144
      target: 1.8.0_144
    encoding: UTF-8
    version: 2.2.2.RELEASE
```

5.2 BuildInfoContributor
------------------------

[BuildInfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/BuildInfoContributor.java)，提供来自构建信息 `META-INF/build-info.properties` 的应用信息。

### 5.2.1 配置文件

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/resources/application.yaml) 配置，增加 BuildInfoContributor 的配置。配置如下：

```
management:
  endpoint:
    
    info:
      enabled: true 
  info:
    
    env:
      enabled: true
    
    build:
      enabled: true

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude: 


info:
  app:
    java:
      source: @java.version@
      target: @java.version@
    encoding: UTF-8
    version: @project.version@
```

额外增加 `management.info.build` 配置项，设置 BuildInfoContributor 的配置项，无对应配置类。

*   Spring Boot 提供的 [InfoContributorAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/info/InfoContributorAutoConfiguration.java) 自动化配置类，实现 BuildInfoContributor 的配置。
*   `enabled` 属性：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.info.build` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

### 5.2.2 构建信息

构建信息 `META-INF/build-info.properties` ，我们可以使用 Spring Boot 提供的 [spring-boot-maven-plugin](https://docs.spring.io/spring-boot/docs/current/maven-plugin/usage.html) 插件，帮我们生成。

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/pom.xml) 文件中，引入该插件。增加部分如下：

```
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>build-info</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

手动执行 `spring-boot-maven-plugin` 插件的 `build-info` 任务。操作如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/05.png)

执行完成后，打开构建结构 `target/classes/` 目录，会发现 `META-INF/build-info.properties` 被自动生成。内容如下：

```
build.artifact=lab-34-acturator-demo-info
build.group=org.springframework.boot
build.name=lab-34-acturator-demo-info
build.time=2019-12-21T03\:37\:23.860Z
build.version=2.2.2.RELEASE
```

*   生成的构建信息，对应 [BuildProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/info/BuildProperties.java) 配置类。

### 5.2.3 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，获得应用的信息。响应结果如下：

```
{
    // ... 省略其它的
    
    "build": {
        "version": "2.2.2.RELEASE",
        "artifact": "lab-34-acturator-demo-info",
        "name": "lab-34-acturator-demo-info",
        "group": "org.springframework.boot",
        "time": "2019-12-21T03:49:19.060Z"
    }
}
```

*   `"build"` 响应结果，就是 `META-INF/build-info.properties` 构建信息。

5.3 GitInfoContributor
----------------------

[GitInfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/GitInfoContributor.java)，提供自来 Git 的版本信息。

### 5.3.1 配置文件

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/resources/application.yaml) 配置，增加 GitInfoContributor 的配置。配置如下：

```
management:
  endpoint:
    
    info:
      enabled: true 
  info:
    
    env:
      enabled: true
    
    build:
      enabled: true
    
    git:
      enabled: true
      mode: SIMPLE 

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude: 


info:
  app:
    java:
      source: @java.version@
      target: @java.version@
    encoding: UTF-8
    version: @project.version@
```

额外增加 `management.info.git` 配置项，设置 GitInfoContributor 的配置项，对应 [InfoContributorProperties.Git](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/info/InfoContributorProperties.java#L37-L52) 配置类。

*   Spring Boot 提供的 [InfoContributorAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/info/InfoContributorAutoConfiguration.java) 自动化配置类，实现 BuildInfoContributor 的配置。
*   `enabled` 属性：是否开启。默认为 `true` 开启。
*   `mode` 属性：Git 信息展示模式。具体的解释，看下艿艿在配置上的注释。这里，我们采用 `SIMPLE` 模式，只展示精简的 Git 版本信息。
*   这里，艿艿主要是为了让胖友了解 `management.info.git` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

### 5.3.2 Git 版本信息

Git 版本信息，我们可以使用 [git-commit-id-plugin](https://github.com/git-commit-id/maven-git-commit-id-plugin) 插件，帮我们生成。

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/pom.xml) 文件中，引入该插件。增加部分如下：

```
<plugin>
    <groupId>pl.project13.maven</groupId>
    <artifactId>git-commit-id-plugin</artifactId>
    <executions>
        <execution>
            <goals>
                <goal>revision</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

手动执行 `git-commit-id-plugin` 插件的 `git-commit-id:revision` 任务。操作如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/06.png)

执行完成后，打开构建结构 `target/classes/` 目录，会发现 `git.properties` 被自动生成。内容如下：

```
#Generated by Git-Commit-Id-Plugin
#Sat Dec 21 12:30:20 CST 2019
git.branch=master
git.build.host=MacBook-Pro-8
git.build.time=2019-12-21T12\:30\:20+0800
git.build.user.email=
git.build.user.name=YunaiV
git.build.version=2.2.2.RELEASE
git.closest.tag.commit.count=
git.closest.tag.name=
git.commit.id=ff43301283d09b78d531857d4e22c31d060e8f45
git.commit.id.abbrev=ff43301
git.commit.id.describe=ff43301-dirty
git.commit.id.describe-short=ff43301-dirty
git.commit.message.full=\u589E\u52A0 actuate \u793A\u4F8B
git.commit.message.short=\u589E\u52A0 actuate \u793A\u4F8B
git.commit.time=2019-12-21T10\:28\:25+0800
git.commit.user.email=
git.commit.user.name=YunaiV
git.dirty=true
git.local.branch.ahead=NO_REMOTE
git.local.branch.behind=NO_REMOTE
git.remote.origin.url=https\://github.com/YunaiV/SpringBoot-Labs.git
git.tags=
git.total.commit.count=197
```

*   生成的 Git 版本信息，对应 [GitProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/info/GitProperties.java) 配置类。

### 5.3.3 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，获得应用的信息。响应结果如下：

```
{
    // ... 省略其它的
    
    "git": {
        "commit": {
            "time": "2019-12-21T02:28:25Z",
            "id": "ff43301"
        },
        "branch": "master"
    }
}
```

*   `"git"` 响应结果，就是 `git.properties` 版本信息。因为是 `SIMPLE` 模式，所以只展示部分 Git 版本信息。

5.4 SimpleInfoContributor
-------------------------

[SimpleInfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/SimpleInfoContributor.java)，用于提供**一个**应用的信息。

### 5.4.1 ActuateConfig

在 [`cn.iocoder.springboot.lab34.actuatordemo.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/) 包下，创建 [ActuateConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/ActuateConfig.java) 配置类，配置一个 SimpleInfoContributor Bean。代码如下：

```
@Configuration
public class ActuateConfig {

    @Bean
    public InfoContributor exampleInfo() {
        return new SimpleInfoContributor("example",
                Collections.singletonMap("key", "value"));
    }

}
```

*   比较简单，胖友自己看下就懂。

### 5.4.2 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，获得应用的信息。响应结果如下：

```
{
    // ... 省略其它的

    "example": {
        "key": "value"
    }
}
```

5.5 MapInfoContributor
----------------------

[MapInfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/MapInfoContributor.java)，用于提供 map 结构的应用信息。

使用方式和[「5.4 SimpleInfoContributor」](#)类似，就不重复赘述。

5.6 DemoInfoContributor
-----------------------

在本小节，我们自定义一个 InfoContributor 实现类。在 [`cn.iocoder.springboot.lab34.actuatordemo.demo`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/demo/) 包下，创建 [DemoInfoContributor](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-info/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/demo/DemoInfoContributor.java) 类，实现 [InfoContributor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/info/InfoContributor.java) 接口，示例 InfoContributor 。代码如下：

```
@Component
public class DemoInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("demo",
                Collections.singletonMap("key", "value"));
    }

}
```

*   在类上，添加 `@Component` 注解，保证能够被 `info` 端点获取到。😈 不然，咱不就白自定义实现 InfoContributor 了嘛。
*   比较简单，胖友自己看下就懂。效果上，和[「5.4 SimpleInfoContributor」](#)是一致的。

### 5.6.1 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，获得应用的信息。响应结果如下：

```
{
    // ... 省略其它的

    "demo": {
        "key": "value"
    }
}
```

> **重要**：更多关于 `info` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 2.9. Application Information》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-application-info)文档。

> 示例代码对应仓库：[lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/) 。

`metrics` 端点，对应 `GET /actuator/metrics` 和 `GET /actuator/metrics/{name}` 接口，对应 [MetricsEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/metrics/MetricsEndpoint.java) 类。

用于获取应用的指标 (Metrics) 信息。例如说，JVM 内存信息、JVM GC 信息、线程信息等等。

通过不断获取采集应用的指标信息，我们可以存储数据到存储器中。之后，我们可以该数据形成相应的监控报表。如下是基于 [Prometheus](https://github.com/prometheus/prometheus) + [Grafana](https://grafana.com/) 实现的 JVM 报表的示例：

> FROM [https://grafana.com/grafana/dashboards/4701](https://grafana.com/grafana/dashboards/4701)
> 
> ![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/07.png)

同时，在应用的指标到达指定阀值时，监控系统发出告警，从而让我们可以快速介入，进行问题的排查。如下是阿里云的监控系统在钉钉上的提示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/08.png)

下面，我们就来看看 `metrics` 端点的示例，同时编写一个自定义的指标的示例。考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

6.1 内置指标
--------

在 Actuator 中，已经内置多个常用指标，所以在本小节中，我们来一起瞅瞅。

### 6.1.1 配置文件

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/src/main/resources/application.yaml) 配置，增加 `metrics` 端点的相关配置。配置如下：

```
management:
  endpoint:
    
    metrics:
      enabled: true 
  
  metrics:
    
    enable:
      xxx: false
    
    tags:
      application: demo-application

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude:
```

额外增加 `management.endpoint.metrics` 配置项，设置 `metrics` 端点的配置项，无对应配置类。

*   Spring Boot 提供的 [MetricsEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/metrics/MetricsEndpointAutoConfiguration.java) 自动化配置类，实现 `metrics` 端点的配置。
*   `enabled` ：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.endpoint.metrics` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

额外增加 `management.info.metrics` 配置项，设置 Metrics 的具体配置项，对应 [MetricsProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/metrics/MetricsProperties.java) 配置类。

*   Spring Boot 提供的 [MetricsAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/metrics/MetricsAutoConfiguration.java) 自动化配置类，实现 Metrics 的配置。
*   `enabled` 属性：设置**指定前缀**的指标是否开启，默认都为 `true` 开启。例如说，胖友不希望 JVM 相关的指标被提供查出来，则可以将这里的 `xxx` 改成 `jvm` 即可。
*   `tags` 属性：指标的**通用** Tag 标签。这里，我们配置了一个通过用标签键为 `application` ，值为 `demo-application` 。我们来试着想下，应用 A 和应用 B 都有相同的 Metrics 名，那么如果我们需要去区分它们，则需要通过给 Metrics 打上不同的标签来区分，而一般情况下，我们会选择 `application` 作为标签。如果胖友有使用过 Prometheus + Grafana 来做监控报表，会发现也是推荐这么实践的。
*   这里，艿艿主要是为了让胖友了解 `management.info.metrics` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

### 6.1.2 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/metrics](http://127.0.0.1:8080/actuator/metrics) 地址，获得**指标列表**。注意，这里仅仅只返回指标的**名字**，不返回指标的具体**数值**。响应结果如下：

```
{
    "names": [
        // JVM memory
        "jvm.memory.max",
        "jvm.memory.used",
        "jvm.memory.committed",
        // JVM buffer
        "jvm.buffer.count",
        "jvm.buffer.total.capacity",
        "jvm.buffer.memory.used",
        // JVM gc
        "jvm.gc.memory.allocated",
        "jvm.gc.memory.promoted",
        "jvm.gc.max.data.size",
        "jvm.gc.pause",
        "jvm.gc.live.data.size",
        // JVM threads
        "jvm.threads.states",
        "jvm.threads.daemon",
        "jvm.threads.live",
        "jvm.threads.peak",
        // JVM classes
        "jvm.classes.loaded",
        "jvm.classes.unloaded",
        
        // Process
        "process.files.open",
        "process.files.max",
        "process.uptime",
        "process.start.time",
        "process.cpu.usage",
        
        // System
        "system.load.average.1m",
        "system.cpu.count",
        "system.cpu.usage",

        // Logback
        "logback.events",
        
        // Tomcat
        "tomcat.sessions.active.max",
        "tomcat.sessions.active.current",
        "tomcat.sessions.alive.max",
        "tomcat.sessions.created",
        "tomcat.sessions.rejected",
        "tomcat.sessions.expired",
        
        // HTTP
        "http.server.requests"
    ]
}
```

*   艿艿把指标做了下梳理，默认情况下会有 JVM、Process、System、Logback、Tomcat、HTTP 大类的指标。每个指标的含义，胖友可以后续感兴趣自己去研究一下。
*   如果胖友引入数据库连接池，则会看到 Datasource 相关的指标。

继续使用浏览器，访问 [http://127.0.0.1:8080/actuator/metrics/jvm.memory.max](http://127.0.0.1:8080/actuator/metrics/jvm.memory.max) 地址，获得 `jvm.memory.max` 指标的数值。响应结果如下：

```
{
    "name": "jvm.memory.max",
    "description": "The maximum amount of memory in bytes that can be used for memory management",
    "baseUnit": "bytes",
    "measurements": [
        {
            "statistic": "VALUE",
            "value": 9893314559
        }
    ],
    "availableTags": [
        {
            "tag": "area",
            "values": [
                "heap",
                "nonheap"
            ]
        },
        {
            "tag": "application",
            "values": [
                "demo-application"
            ]
        },
        {
            "tag": "id",
            "values": [
                "Compressed Class Space",
                "PS Old Gen",
                "PS Survivor Space",
                "Metaspace",
                "PS Eden Space",
                "Code Cache"
            ]
        }
    ]
}
```

*   对应 [MetricsEndpoint.MetricResponse](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/metrics/MetricsEndpoint.java#L181-L249) 响应类。
*   `name` ：指标的名字。
*   `description` ：指标的描述。
*   `measurements` ：指标的测量数值。
*   `baseUnit` ：指标的单位。
*   `availableTags` ：指标的标签数组。这里，可以看到我们定义的通用标签 `application = demo-application` 。

其它指标的数值，胖友自行访问 [http://127.0.0..1:8080/actuator/metrics/{name}](http://127.0.0..1:8080/actuator/metrics/%7Bname%7D) 地址。

6.2 自定义指标
---------

在 Spring Boot 2.X 的版本，其指标收集器 (Metrics Collector) 是基于 [Micrometer](https://github.com/micrometer-metrics/micrometer) 来实现。这里，我们来看看 Micrometer 的简介：

> FROM [《使用 Micrometer 记录 Java 应用性能指标》](http://www.iocoder.cn/Fight/j-using-micrometer-to-record-java-metric/?self)
> 
> Micrometer 为 Java 平台上的性能数据收集提供了一个通用的 API，应用程序只需要使用 Micrometer 的通用 API 来收集性能指标即可。Micrometer 会负责完成与不同监控系统的适配工作。这就使得切换监控系统变得很容易。Micrometer 还支持推送数据到多个不同的监控系统。
> 
> 在 Java 应用中使用 Micrometer 非常的简单。只需要在 Maven 或 Gradle 项目中添加相应的依赖即可。Micrometer 包含如下三种模块，分组名称都是 [`io.micrometer`](https://mvnrepository.com/artifact/io.micrometer)：
> 
> *   包含数据收集 SPI 和基于内存的实现的核心模块 [micrometer-core](https://github.com/micrometer-metrics/micrometer/tree/master/micrometer-core)。
> *   针对不同监控系统的实现模块，如针对 Prometheus 的 [micrometer-registry-prometheus](https://github.com/micrometer-metrics/micrometer/tree/master/implementations/micrometer-registry-prometheus)。
> *   与测试相关的模块 [micrometer-test](https://github.com/micrometer-metrics/micrometer/tree/master/micrometer-test)。

在 Micrometer 中，定义了 [Meter](https://github.com/micrometer-metrics/micrometer/blob/master/micrometer-core/src/main/java/io/micrometer/core/instrument/Meter.java) 接口，用于收集应用中的 Metrics 数据的接口。其有 5 种实现大类：Counter、Timer、LongTaskTimer、Gauge、DistributionSummary。具体如何使用，我们可见如下两篇文章：

*   [《使用 Micrometer 记录 Java 应用性能指标》](http://www.iocoder.cn/Fight/j-using-micrometer-to-record-java-metric/?self)
*   [《JVM 应用度量框架 Micrometer 实战》](http://www.iocoder.cn/Fight/jvm-micrometer-prometheus/?self)

本小节，我们以 Counter 举例子。Counter 作为计数器，用于收集指标 Metrics 的总数。

*   适用于**累加**情况下的统计。例如，下单总数、支付次数、HTTP 请求总量等等。
*   可以通过 **Tag** 可以区分相同 Metrics 在不同的场景的计数。例如：
    *   对于下单，可以使用不同的 Tag 标记不同的业务来源或者是按日期划分。
    *   对于 HTTP 请求总量记录，可以使用 Tag 区分不同的 URL 。

下面，我们来实现一个收集某个 API 接口的请求总数的 Counter 。😈 还是在 [lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/) 项目上进行修改。

### 6.2.1 DemoController

在 [`cn.iocoder.springboot.lab34.actuatordemo.controller`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/controller/) 包路径下，创建 [DemoController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/controller/DemoController.java) 类，提供测试 API 接口。代码如下：

```
@RestController
@RequestMapping("/demo")
public class DemoController {

    


     
    private static final Counter METRICS_DEMO_COUNT = Counter
            .builder("demo.visit.count") 
            .description("demo 访问次数") 
            .baseUnit("次") 
            .tag("test", "nicai") 
            .register(Metrics.globalRegistry); 


    @GetMapping("/visit")
    public String visit() {
        
        METRICS_DEMO_COUNT.increment();
        return "Demo 示例";
    }

}
```

*   `<1>` 处，使用 [`Counter.Builder`](https://github.com/micrometer-metrics/micrometer/blob/master/micrometer-core/src/main/java/io/micrometer/core/instrument/Counter.java#L57-L130) 构建一个 [Counter](https://github.com/micrometer-metrics/micrometer/blob/master/micrometer-core/src/main/java/io/micrometer/core/instrument/Counter.java) 。
*   `<2>` 处，在每次 `/demo/visit` 接口被调用时，都增加一次 `METRICS_DEMO_COUNT` 计数。

😈 是不是自定义一个 Counter 并进行计数，还是蛮简单的，嘿嘿。

### 6.2.2 简单测试

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

首先，打开浏览器，访问 [http://127.0.0.1:8080/demo/visit](http://127.0.0.1:8080/demo/visit) 地址，让指标 `"demo.visit.count"` 的计数增加 2 次。

然后，继续浏览器，访问 [http://127.0.0.1:8080/actuator/metrics/demo.visit.count](http://127.0.0.1:8080/actuator/metrics/demo.visit.count) 地址，获得 `"demo.visit.count"` 指标的数值。响应结果如下：

```
{
    "name": "demo.visit.count",
    "description": "demo 访问次数",
    "baseUnit": "次",
    "measurements": [
        {
            "statistic": "COUNT",
            "value": 2
        }
    ],
    "availableTags": [
        {
            "tag": "test",
            "values": [
                "nicai"
            ]
        },
        {
            "tag": "application",
            "values": [
                "demo-application"
            ]
        }
    ]
}
```

*   看到 `measurements` 数值为 2 ，成功。

> **重要**：更多关于 `metrics` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 6. Metrics》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-metrics)文档。

> 示例代码对应仓库：[lab-34-actuator-demo-httptrace](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-httptrace/) 。

`httptrace` 端点，对应 `GET /actuator/httptrace` 接口，对应 [HttpTraceEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/http/HttpTraceEndpoint.java) 类。

用于获取应用的最近的 HTTP 跟踪信息 ( [HttpTrace](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/http/HttpTrace.java) )。`httptrace` 端点通过 [TraceRepository](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/http/HttpTrace.java) 获取最近的 HttpTrace。Actuator 内置了 [InMemoryHttpTraceRepository](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/http/InMemoryHttpTraceRepository.java) 实现类，存储**最近的 100 条** HttpTrace 到**内存**中。

在 SpringMVC 中，通过 [HttpTraceFilter](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/trace/servlet/HttpTraceFilter.java) 过滤器，拦截请求，记录 HttpTrace 到 TraceRepository 中。

下面，我们就来看看 `httptrace` 端点的示例，考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-httptrace](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-httptrace/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

7.1 配置文件
--------

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-httptrace/src/main/resources/application.yaml) 配置，增加 `httptrace` 端点的配置。配置如下：

```
management:
  endpoint:
    
    httptrace:
      enabled: true 
  
  trace:
    http:
      enabled: true 
      include: 

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude:
```

额外增加 `management.endpoint.httptrace` 配置项，设置 `httptrace` 端点的配置项，无对应配置类。

*   Spring Boot 提供的 [HttpTraceEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/trace/http/HttpTraceEndpointAutoConfiguration.java) 自动化配置类，实现 `httptrace` 端点的配置。
*   `enabled` ：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.endpoint.httptrace` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

额外增加 `management.trace.http` 配置项，设置 HttpTrace 的具体配置项，对应 [HttpTraceProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/trace/http/HttpTraceProperties.java) 配置类。

*   Spring Boot 提供的 [HttpTraceAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/trace/http/HttpTraceAutoConfiguration.java) 自动化配置类，实现 HttpTrace 相关组件的配置，例如说 HttpTraceFilter 过滤器。
*   `enabled` 属性：是否开启。默认为 `true` 开启。
*   `include` 属性：包含的 [Include](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/http/Include.java) 的数组。默认情况下，考虑安全性，不包含 `COOKIE_HEADERS`、`AUTHORIZATION_HEADER` 项。
*   这里，艿艿主要是为了让胖友了解 `management.trace.http` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

7.2 ActuateConfig
-----------------

在 [`cn.iocoder.springboot.lab34.actuatordemo.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-httptrace/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/) 包下，创建 [ActuateConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-httptrace/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/ActuateConfig.java) 配置类，配置一个 HttpTraceRepository Bean。代码如下：

```
@Configuration
public class ActuateConfig {

    @Bean
    public HttpTraceRepository httpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }

}
```

*   内部创建的是 InMemoryHttpTraceRepository 对象。

为什么我们要配置一个 HttpTraceRepository 呢？HttpTraceEndpointAutoConfiguration 自动化配置类，创建 HttpTraceEndpoint 的前提是，需要有一个 HttpTraceRepository Bean 。因此，我们在 ActuateConfig 中，配置了 InMemoryHttpTraceRepository 对象。

7.3 简单测试
--------

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/httptrace](http://127.0.0.1:8080/actuator/httptrace) 地址，获得应用的最近的 HTTP 跟踪信息。响应结果如下：

```
{
    "traces": [
        {
            "timestamp": "2019-12-21T18:45:35.145Z",
            "principal": null,
            "session": null,
            "request": {
                "method": "GET",
                "uri": "http://127.0.0.1:8080/actuator/httptrace",
                "headers": {},
                "remoteAddress": null
            },
            "response": {
                "status": 200,
                "headers": {}
            },
            "timeTaken": null
        }
        
        // ... 省略其它
    ]
}
```

*   `principal` ：请求主体。
*   `session` ：Session 编号。
*   `request` ：请求信息。
*   `response` ：响应信息。
*   `timestamp` ：请求时间。
*   `timeTaken` ：消耗时间，单位：毫秒。

如果胖友希望存储 HttpTrace 到数据库中，可以自定义实现 HttpTraceRepository。

> **重要**：更多关于 `httptrace` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 8. HTTP Tracing》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-http-tracing)文档。

> 示例代码对应仓库：[lab-34-actuator-demo-auditevents](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/) 。

`auditevents` 端点，对应 `GET /actuator/auditevents` 接口，对应 [AuditEventsEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/AuditEventsEndpoint.java) 类。

用于获取应用的最近的审计事件 ( [AuditEvents](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/AuditEvent.java) )。`httptrace` 端点通过 [AuditEventRepository](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/AuditEventRepository.java) 获取最近的 AuditEvents。Actuator 内置了 [InMemoryAuditEventRepository](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/InMemoryAuditEventRepository.java) 实现类，存储**最近的 1000 条** AuditEvents 到**内存**中。

在 Actuator 中，通过 [AuditListener](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/audit/listener/AuditListener.java) 监听器，监听 AuditEvent 事件，记录 AuditEvent 到 AuditEventRepository 中。

下面，我们就来看看 `auditevents` 端点的示例，考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-auditevents](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

8.1 引入依赖
--------

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/pom.xml) 文件中，**增加**引入 `spring-boot-starter-security` 相关依赖。

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.2.0.RELEASE</version> 
</dependency>
```

在 Spring Security 中，认证成功或失败时，[AuthenticationAuditListener](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/security/AuthenticationAuditListener.java) 会监听 AuthenticationSuccessEvent 或 AbstractAuthenticationFailureEvent 事件，从而发布 AuditEvent 事件。

8.2 配置文件
--------

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/src/main/resources/application.yaml) 配置，增加 `auditevents` 端点的配置。配置如下：

```
management:
  endpoint:
    
    auditevents:
      enabled: true 

  endpoints:
    
    web:
      base-path: /actuator 
      exposure:
        include: '*' 
        exclude: 

spring:
  
  security:
    
    user:
      name: user 
      password: user
```

额外增加 `management.endpoint.auditevents` 配置项，设置 `auditevents` 端点的配置项，无对应配置类。

*   Spring Boot 提供的 [AuditEventsEndpointAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/audit/AuditEventsEndpointAutoConfiguration.java) 自动化配置类，实现 `auditevents` 端点的配置。
*   `enabled` ：是否开启。默认为 `true` 开启。
*   这里，艿艿主要是为了让胖友了解 `management.endpoint.auditevents` 配置项。一般情况下，我们无需**主动**配置它，默认即可。

额外增加 `spring.security` 配置项，增加「user/user」账号。

8.3 ActuateConfig
-----------------

在 [`cn.iocoder.springboot.lab34.actuatordemo.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/) 包下，创建 [ActuateConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-auditevents/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/ActuateConfig.java) 配置类，配置一个 AuditEventRepository Bean。代码如下：

```
@Configuration
public class ActuateConfig {

    @Bean
    public AuditEventRepository auditEventRepository() {
        return new InMemoryAuditEventRepository();
    }

}
```

*   内部创建的是 InMemoryAuditEventRepository 对象。

为什么我们要配置一个 AuditEventRepository 呢？AuditEventsEndpointAutoConfiguration 自动化配置类，创建 AuditEventsEndpoint 的前提是，需要有一个 AuditEventRepository Bean 。因此，我们在 ActuateConfig 中，配置了 InMemoryAuditEventRepository 对象。

8.4 简单测试
--------

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

首先，打开浏览器，访问 [http://127.0.0.1:8080/login](http://127.0.0.1:8080/login) 地址，使用「user/user」账号，进行登录。

然后，继续浏览器，访问 [http://127.0.0.1:8080/actuator/auditevents](http://127.0.0.1:8080/actuator/auditevents) 地址，获取应用的最近的 AuditEvents 。响应结果如下：

```
{
  "events": [
    {
      "timestamp": "2019-12-22T02:25:35.035Z",
      "principal": "anonymousUser",
      "type": "AUTHORIZATION_FAILURE",
      "data": {
        "details": {
          "remoteAddress": "127.0.0.1",
          "sessionId": null
        },
        "type": "org.springframework.security.access.AccessDeniedException",
        "message": "Access is denied"
      }
    },
    {
      "timestamp": "2019-12-22T02:25:44.015Z",
      "principal": "user",
      "type": "AUTHENTICATION_SUCCESS",
      "data": {
        "details": {
          "remoteAddress": "127.0.0.1",
          "sessionId": "5F55B4F20774FE5AADA04FBECD0437A3"
        }
      }
    }
  ]
}
```

*   `type` ：审计事件的类型。
*   `timestamp` ：审计事件的时间。
*   `principal` ：请求主体。
*   `data` ：明细数据。

> **重要**：更多关于 `auditevents` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 7. Auditing》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-auditing)文档。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 。

`beans` 端点，对应 `GET /actuator/beans` 接口，对应 [BeansEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/beans/BeansEndpoint.java) 类。

用于获得应用的所有 Spring Bean 信息。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/beans](http://127.0.0.1:8080/actuator/beans) 地址，获得应用的所有 Spring Bean 信息。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/09.png)

*   响应的结果，每个 `beans` 的元素，对应 [BeansEndpoint.BeanDescriptor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/beans/BeansEndpoint.java#L146-L190) 类。
*   `beans` 的 Key ：Bean 的名字。
*   `aliases` ：Bean 的别名数组。
*   `type` ：Bean 的类型。
*   `resource` ：创建该 Bean 的资源。通过该属性，我们知道该 Bean 是如何创建的。
*   `dependencies` ：创建该 Bean 依赖的其它 Bean 的名字。
*   `scope` ：Bean 的作用域。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 。

`conditions` 端点，对应 `GET /actuator/conditions` 接口，对应 [ConditionsReportEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java) 类。

用于获得 Spring Boot 基于 [Condition](https://reflectoring.io/spring-boot-conditionals/) 条件创建 Bean 的情况。通过它，我们可以知道：

*   如果一个 Bean 被创建，是因为**满足**了什么条件。
*   如果一个 Bean 未被创建，是因为**不满足**什么条件。

也就是说，通过 `conditions` 端点，我们可以排查基于 Condition 的 Bean 为什么被创建，为什么没被创建。😈 在 Spring Boot 提供自动化配置的遍历的同时，也会带给我们为什么某个 Bean 被创建或未被创建的困扰，通过 `conditions` 端点就可以很好的排查和解决了。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/conditions](http://127.0.0.1:8080/actuator/conditions) 地址，获得应用的所有 Spring Bean 信息。响应结果如下：

```
{
  "contexts": {
    "application": {
      "positiveMatches": {
        "AuditEventsEndpointAutoConfiguration": [
          {
            "condition": "OnAvailableEndpointCondition",
            "message": "@ConditionalOnAvailableEndpoint no property management.endpoint.auditevents.enabled found so using endpoint default; @ConditionalOnAvailableEndpoint marked as exposed by a 'management.endpoints.web.exposure' property"
          }
          // ... 省略其它
        ]
      },
      "negativeMatches": {
        "RabbitHealthContributorAutoConfiguration": {
          "notMatched": [
            {
              "condition": "OnClassCondition",
              "message": "@ConditionalOnClass did not find required class 'org.springframework.amqp.rabbit.core.RabbitTemplate'"
            }
          ],
          "matched": []
        }
        // ... 省略其它
      },
      "unconditionalClasses": [
        "org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration",
        // ... 省略其它
      ]
    }
  }
}
```

*   整个响应结果，对应 [ConditionsReportEndpoint.ContextConditionEvaluation](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java#L99-L157) 类。
*   `positiveMatches` ：**成功**被创建 Bean 的条件明细 MultiValueMap 。
    *   KEY ：对应每个 Bean 的名字。
    *   VALUE ： 条件明细数组，每个数组元素对应 [MessageAndCondition](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java#L187-L217) 类。
        *   `condition` ：条件类型。
        *   `message` ：条件描述。
*   `negativeMatches` ：**未成功**被创建 Bean 的条件明细 Map 。
    *   KEY ：对应每个 Bean 的名字。
    *   VALUE ： 匹配条件和未匹配的条件明细，对应 [MessageAndConditions](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java#L159-L185) 类。
        *   `notMatched` ：匹配条件的明细数组，每个数组元素对应 [MessageAndCondition](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java#L187-L217) 类。😈 酱紫，我们就可以通过 `notMatched` 来知道该 Bean 未被创建，是因为不满足什么条件。例如这里，RabbitHealthContributorAutoConfiguration 未被自动化配置的原因，是因为 RabbitTemplate 类不存在。
        *   `matched` ：不匹配条件的明细数组，每个数组元素对应 [MessageAndCondition](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/condition/ConditionsReportEndpoint.java#L187-L217) 类。
*   `unconditionalClasses` ：不包含任何**类**级别的条件的自动配置类，也就是说，类始终会被自动加载。
*   `exclusions` ：应用明确排除的配置类。本示例中，我们并没有配置，所以看不到。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 。

`env` 端点，对应 `GET /actuator/env` 和 `GET /actuator/env/{name}` 接口，对应 [EnvironmentEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/env/EnvironmentEndpoint.java) 类。

用于获取应用中所有**可用的** Spring [PropertySource](https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/core/env/PropertySource.java) 配置来源。Spring Boot 对应的 PropertySource 非常多：

*   应用的 YAML 配置文件、Properties 配置文件。例如说，`application.yaml`、`application.properties` 。
*   命令行指定的参数。例如 `java -jar springboot.jar --server.port=9090` 。
*   Java 系统变量，即 `System#getProperties()` 。
*   操作系统环境变量。
*   更多的可以看看 [《Spring Boot Features —— 2. Externalized Configuration》](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config) 。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/env](http://127.0.0.1:8080/actuator/env) 地址，获取应用中所有**可用的** Spring PropertySource 配置来源。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/10.png)

*   整个响应结果，对应 [EnvironmentEndpoint.EnvironmentEntryDescriptor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/env/EnvironmentEndpoint.java#L239-L270) 类。
*   `activeProfiles` ：激活的 Profile 数组。
*   `propertySources` ：[EnvironmentEndpoint.PropertySourceDescriptor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/env/EnvironmentEndpoint.java#L297-L319) 数组。
    *   `name` ：PropertySource 的名字。
    *   `properties` ：具体配置项 Map ，每个配置项对应 [EnvironmentEndpoint.PropertyValueDescriptor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/env/EnvironmentEndpoint.java#L346-L369) 类。
        *   KEY ：配置项的键。
        *   `value` ：配置项的值。
        *   `origin` ：配置项的来源。例如说，在 YAML 配置文件的第几行。

另外考虑到安全性，对于配置项 KEY 包含 `"password"`、`secret`、`key`、`token` 等时，其 `value` 会使用加密成 `"******"` 字符串。感兴趣的胖友，可以看看 [Sanitizer](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint/Sanitizer.java) 类的代码。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 。

`configprops` 端点，对应 `GET /actuator/configprops` 接口，对应 [ConfigurationPropertiesReportEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/context/properties/ConfigurationPropertiesReportEndpoint.java) 类。

用于获取应用中所有**有效的** Spring [`@ConfigurationProperties`](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/context/properties/ConfigurationProperties.java) 注解的配置属性类。例如说，`"spring.jdbc"` 配置对应的 [JdbcProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jdbc/JdbcProperties.java) 配置属性类。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/configprops](http://127.0.0.1:8080/actuator/configprops) 地址，获取应用中所有**有效的** Spring `@ConfigurationProperties` 注解的配置属性类。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/11.png)

*   整个响应结果，对应 [ConfigurationPropertiesReportEndpoint.ApplicationConfigurationProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/context/properties/ConfigurationPropertiesReportEndpoint.java#L476-L493) 类。
*   `beans` ：[ConfigurationPropertiesReportEndpoint.ConfigurationPropertiesBeanDescriptor](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/context/properties/ConfigurationPropertiesReportEndpoint.java#L522-L553) Map 。
    *   KEY ：配置属性类的全类名。
    *   `prefix` ：配置属性的前缀。
    *   `properties` ：具体配置项 Map ，每个配置项对应一个键值对。

另外考虑到安全性，对于 `properties` 的每个配置项 KEY 包含 `"password"`、`secret`、`key`、`token` 等时，其 VALUE 会使用加密成 `"******"` 字符串。感兴趣的胖友，可以看看 [Sanitizer](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/endpoint/Sanitizer.java) 类的代码。

> 示例代码对应仓库：[lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/) 。

`mappings` 端点，对应 `GET /actuator/mappings` 接口，对应 [MappingsEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/MappingsEndpoint.java) 类。

用于获取应用的 HTTP 请求匹配 (Mapping) 信息。`mappings` 端点通过 [MappingDescriptionProvider](https://github.com/Diffblue-benchmarks/Spring-projects-spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/MappingDescriptionProvider.java) ，获取不同类型的 HTTP 请求匹配信息。Actuator 内置了多个 MappingDescriptionProvider 实现，如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/12.png)

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics) 项目即可。😈 因为我们在里面创建了 [DemoController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/controller/DemoController.java) 类。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/mappings](http://127.0.0.1:8080/actuator/mappings) 地址，获取应用的 HTTP 请求匹配 (Mapping) 信息。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/13.png)

*   整个响应结果，对应 [MappingsEndpoint.ApplicationMappings](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/MappingsEndpoint.java#L64-L80) 类。
*   `dispatcherServlets` ：由 [DispatcherServletsMappingDescriptionProvider](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/DispatcherServletsMappingDescriptionProvider.java) 提供，针对 SpringMVC [DispatcherServlet](https://github.com/spring-projects/spring-framework/blob/master/spring-webmvc/src/main/java/org/springframework/web/servlet/DispatcherServlet.java) 。`dispatcherServlets` Map 的每个元素，对应一个 SpringMVC DispatcherServlet 。其 KEY 为 DispatcherServlet 的名字，VALUE 为 Mapping 数组，对应 [DispatcherServletMappingDescription](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/DispatcherServletMappingDescription.java) 类。我们来简单看看 DispatcherServletMappingDescription 的每个字段：
    *   `handler` ：（简单）对应 Handler 的类与方法。
    *   `mappings` ：（简单）对应的匹配的请求 Method 与 URI 条件。
    *   `details.handlerMethod` ：（详细）对应 Handler 的类与方法。
    *   `details.requestMappingConditions` ：（详细）对应的匹配的完整条件。例如说，我们在 `@RequestMapping` 注解上添加的匹配条件。
*   `servlets` ：由 [ServletsMappingDescriptionProvider](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/ServletsMappingDescriptionProvider.java) 提供，针对 J2EE Servlet 。`servlets` 数组的每个元素，对应 [ServletRegistrationMappingDescription](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/ServletRegistrationMappingDescription.java) 类，比较简单，胖友自己去瞅瞅即可。
*   `filters` ：由 [FiltersMappingDescriptionProvider](https://github.com/Diffblue-benchmarks/Spring-projects-spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/FiltersMappingDescriptionProvider.java) 提供，针对 J2EE Filter 。`filters` 数组的每个元素，对应 [FilterRegistrationMappingDescription](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/web/mappings/servlet/FilterRegistrationMappingDescription.java) 类，比较简单，胖友自己去瞅瞅即可。

> 示例代码对应仓库：[lab-34-actuator-demo-metrics](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-metrics/) 。

`loggings` 端点，对应 [LoggersEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/logging/LoggersEndpoint.java) 类。对应接口如下：

*   `GET /actuator/loggers` 接口，返回所有 Logger 的配置信息。
*   `GET /actuator/loggers/{name}` 接口，获取指定 Logger 的配置信息。
*   `POST /actuator/loggers/{name}` 接口，修改指定 Logger 的日志级别。

`loggers` 端点通过 [LoggingSystem](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/java/org/springframework/boot/logging/LoggingSystem.java) ，获取不同的日志库的 Logger 的配置信息。Actuator 内置了多个 LoggingSystem 实现，如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/14.png)

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/loggers](http://127.0.0.1:8080/actuator/loggers) 地址，获取应用中所有 Logger 的配置信息。响应结果如下：

```
{
    "levels": ["OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE" ],
    "loggers": {
        "ROOT": {
            "configuredLevel": "INFO",
            "effectiveLevel": "INFO"
        },
        // ... 省略其它 logger 
    },
    "groups": {
        "web": {
            "configuredLevel": null,
            "members": [
                "org.springframework.core.codec",
                "org.springframework.http",
                "org.springframework.web",
                "org.springframework.boot.actuate.endpoint.web",
                "org.springframework.boot.web.servlet.ServletContextInitializerBeans"
            ]
        },
        // ... 省略其它 group
    }
}
```

*   `levels` ：支持的日志级别数组。
*   `loggers` ：所有 Logger 的 Map 。
    *   KEY ：Logger 名字。
    *   `configuredLevel` ：配置的日志级别。
    *   `effectiveLevel` ：生效的日志级别。
*   `groups` ：所有 Logger 分组的 Map 。Logger 分组是 Spring Boot 自定义的功能，不了解的胖友，可以看看 [《芋道 Spring Boot 日志集成 Logging 入门》](http://www.iocoder.cn/Spring-Boot/Logging/?self) 文章。
    *   KEY ：Logger 分组的名字。
    *   `configuredLevel` ：配置的日志级别。
    *   `members` ：包含的 Logger 数组。

另外，通过 `POST /actuator/loggers/{name}` 接口，可以动态实现 Logger 的日志级别修改。具体如何实现，我门在 [《芋道 Spring Boot 日志集成 Logging 入门》](http://www.iocoder.cn/Spring-Boot/Logging/?self) 文章中看看。

> **重要**：更多关于 `loggers` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 5. Loggers》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-loggers)文档。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 。

`threaddump` 端点，对应 `GET /actuator/threaddump` 接口，对应 [ThreadDumpEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/management/ThreadDumpEndpoint.java) 类。

用于获取应用的所有**存活的**线程信息。`threaddump` 端点通过调用 [`ThreadMXBean#dumpAllThreads(boolean lockedMonitors, boolean lockedSynchronizers)`](https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/java/lang/management/ThreadMXBean.java#L768-L805) 方法，进行线程信息的获取。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/mappings](http://127.0.0.1:8080/actuator/mappings) 地址，获取线程信息。响应结果如下图：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/15.png)

*   每一个线程，对应 [`java.lang.management.ThreadInfo`](https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.management/share/classes/java/lang/management/ThreadInfo.java) 类。具体字段的解释，见 [ThreadInfo (Java Platform SE 8)](http://www.pbteach.com/resource/jdkapi/jdk-8_doc-api-cn/java/lang/management/ThreadInfo.html) 。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 。

`heapdump` 端点，对应 `GET /actuator/heapdump` 接口，对应 [HeapDumpWebEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/management/HeapDumpWebEndpoint.java) 类。

用于获取应用的 JVM Heap Dump（堆转储文件）。`heapdump` 端点通过调用 [`HotSpotDiagnosticMXBean#dumpHeap(String outputFile, boolean live)`](https://github.com/openjdk-mirror/jdk7u-jdk/blob/master/src/share/classes/com/sun/management/HotSpotDiagnosticMXBean.java#L52-L70) 方法，进行 JVM Heap Dump 的获取。

> 关于 JVM Heap Dump 的拓展知识，胖友可以看看 [《JVM 故障分析及性能优化系列之六：JVM Heap Dump（堆转储文件）的生成和 MAT 的使用》](https://www.javatang.com/archives/2017/10/30/53562102.html) 文章。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/heapdump](http://127.0.0.1:8080/actuator/heapdump) 地址，获取应用的 JVM Heap Dump。因为是堆转储**文件**，所以会下载一个 heapdump 文件。后续，胖友可以使用 [Memory Analyzer (MAT)](https://www.eclipse.org/mat/) 工具，进行 JVM 堆内存的分析。

> 示例代码对应仓库：[lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 。

`shutdown` 端点，对应 `POST /actuator/shutdown` 接口，对应 [ShutdownEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/context/ShutdownEndpoint.java) 类。

用于远程**优雅**关闭应用。`shutdown` 端点通过调用 Spring [`ConfigurableApplicationContext#close()`](https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/ConfigurableApplicationContext.java#L183-L192) 方法，进行应用的优雅关闭。

因为 `shutdown` 端点可以实现应用的关闭，是个相对敏感的操作，所以默认情况下是关闭的。所以，我们需要在配置文件中，配置 `management.endpoint.shutdown.enabled = true` 来进行开启。另外，在生产环境下，一定要做好 `shutdown` 端点的安全防护措施，例如：使用 Spring Security 进行安全认证、仅允许内网 IP 访问等等。

这个端点比较简单，我们就不单独搭建项目，而是直接使用 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo) 项目即可。

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

使用 [Postman](https://www.getpostman.com/)，**POST** 请求 [http://127.0.0.1:8080/actuator/shutdown](http://127.0.0.1:8080/actuator/shutdown) 地址，进行应用的优雅关闭。执行完成后，我们会看到控制台输出如下日志，最终应用被关闭。

```
2019-12-23 08:17:21.596  INFO 4178 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-12-23 08:17:21.597  INFO 4178 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-12-23 08:17:21.601  INFO 4178 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 4 ms
2019-12-23 08:17:22.159  INFO 4178 --- [       Thread-3] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'

Process finished with exit code 0
```

`sessions` 端点，对应 [SessionsEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/session/SessionsEndpoint.java) 类。对应接口如下：

*   `GET /actuator/sessions/?username=` 接口，获取指定 `username` 对应的 Session 。
*   `GET /actuator/sessions/{sessionId}` 接口，获取指定 `sessionId` 对应的 Session 。
*   `DELETE /actuator/sessions/{sessionId}` 接口，删除指定 `sessionId` 对应的 Session 。

`sessions` 端点，是基于 [Spring Session](https://github.com/spring-projects/spring-session) 进行实现。暂时不提供示例，感兴趣的胖友，自己去尝试一波。

想要对 Spring Session 进行了解的胖友，欢迎阅读[《芋道 Spring Boot 分布式 Session 入门》](http://www.iocoder.cn/Spring-Boot/Distributed-Session/?self)文章。

`scheduledtasks` 端点，对应 `GET /actuator/scheduledtasks` 接口，对应 [ScheduledTasksEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/scheduling/ScheduledTasksEndpoint.java) 类。

用于获取应用中的定时（调度）任务。

暂时不提供示例，感兴趣的胖友，自己去尝试一波。

想要对 Spring 定时任务进行了解的胖友，欢迎阅读[《芋道 Spring Boot 定时任务入门》](http://www.iocoder.cn/Spring-Boot/Job/?self)文章。

`flyway` 端点，对应 `GET /actuator/flyway` 接口，对应 [FlywayEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/flyway/FlywayEndpoint.java) 类。

用于获取应用中的已执行的 [Flyway](https://flywaydb.org/) 数据库迁移。

暂时不提供示例，感兴趣的胖友，自己去尝试一波。

想要对 Flyway 进行了解的胖友，欢迎阅读[《芋道 Spring Boot 数据库版本管理入门》](http://www.iocoder.cn/Spring-Boot//database-version-control/?self)文章的[「2. Flyway」](#)小节。

`liquibase` 端点，对应 `GET /actuator/liquibase` 接口，对应 [LiquibaseEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/liquibase/LiquibaseEndpoint.java) 类。

用于获取应用中的已执行的 [Liquibase](https://www.liquibase.org/) 数据库迁移。

暂时不提供示例，感兴趣的胖友，自己去尝试一波。

想要对 Liquibase 进行了解的胖友，欢迎阅读[《芋道 Spring Boot 数据库版本管理入门》](http://www.iocoder.cn/Spring-Boot//database-version-control/?self)文章的[「3. Liquibase」](#)小节。

> 示例代码对应仓库：[lab-34-actuator-demo-custom-endpoint](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-custom-endpoint/) 。

在上述小节的内容，我们看了 Actuator 提供的很多内置端点。本小节，我们来实现一个 Actuator **自定义**端点。

考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-custom-endpoint](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-custom-endpoint/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

22.1 DemoEndPoint
-----------------

在 [`cn.iocoder.springboot.lab34.actuatordemo.endpoint`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-34/lab-34-actuator-demo-custom-endpoint/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/endpoint/) 包路径下，创建 [DemoEndPoint](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-custom-endpoint/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/endpoint/DemoEndPoint.java) 类，示例 EndPoint。代码如下：

```
@Component
@Endpoint(id = "demo")
public class DemoEndPoint {

    @ReadOperation
    public Map<String, String> hello() {
        Map<String, String> result = new HashMap<>();
        result.put("作者", "yudaoyuanma");
        result.put("秃头", "true");
        return result;
    }

}
```

*   在类上，添加 `@Component` 注解，保证创建一个 Bean ，被 Spring 扫描到。
*   在类上，添加 `@Endpoint` 注解，表示它是一个 Actuator Endpoint 端点。同时，设置 `id = "demo"` ，则可以使用 `/actuator/demo` 地址进行访问。
*   在 `#hello()` 方法上，添加 `@ReadOperation` 注解，表示它是读操作，对应 `GET` 请求。

补充：

*   如果胖友有修改操作，可以使用 `@WriteOperation` 注解，对应 `POST` 请求。
*   如果胖友有修改操作，可以使用 `@DeleteOperation` 注解，对应 `DELETE` 请求。
*   如果胖友想要接收参数，可以参考 [LoggersEndpoint](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/logging/LoggersEndpoint.java) 端点的代码。例如说，`@Selector` 注解，接收路径参数。

22.2 简单测试
---------

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/demo](http://127.0.0.1:8080/actuator/demo) 地址，获得应用的信息。响应结果如下：

```
{
    "作者": "yudaoyuanma",
    "秃头": "true"
}
```

> **重要**：更多关于自定义端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 2.7. Implementing Custom Endpoints》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints-custom)文档。

默认情况下，Actuator 服务使用配置项 `server.port` 的端口。那么，如果我们使用 Nginx 代理后端应用，暴露 API 接口时，可能同时也将 Actuator 的端点也暴露出去了。

此时，比较简单的解决方案，让 Actuator 服务不使用 `server.port` 的端口即可。我们仅仅需要配置 `management.server.port` 使用其它端口，就可以实现 Actuator 自定义端口。这样，就能避免我们将 Actuator 的端点也暴露出去了。

具体的示例艿艿就不提供了，胖友可以在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目上改动下试试，很简单。

> **重要**：Actuator 还可以自定义每个 Endpoint 的访问路径，SSL 证书等等。
> 
> 感兴趣的胖友，可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 3. Monitoring and Management over HTTP》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-monitoring)文档。

虽然说，在[「23. 自定义端口」](#) 小节中，我们已经通过自定义端口 方式，避免 Actuator 的端点暴露到外网。但是本着更高的安全级别，内网也不一定可靠的原则，我们需要给 Actuator 的端点，增加**安全认证**。🙂 毕竟，服务器可能被黑掉，黑客就可以连入内网，那么 Actuator 的端点就会被轻易的访问。

我们可以通过整合 Spring Security 框架，快速的实现安全认证的功能。如果没有学习过 Spring Security 框架的胖友，后续可以看看[《芋道 Spring Boot 安全框架 Spring Security 入门》](http://www.iocoder.cn/Spring-Boot/Spring-Security/?self)文章。

下面，我们就来进行本小节的示例，考虑到不污染[「2. 快速入门」](#) 的示例，我们在 [lab-34-actuator-demo](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo/) 项目的基础上，复制出一个 [lab-34-actuator-demo-security](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-security/) 项目。😈 酱紫，我们也能少写点代码，哈哈哈~

24.1 引入依赖
---------

修改 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-security/pom.xml) 文件中，**额外**引入 `spring-boot-starter-security` 相关依赖。

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

24.2 配置文件
---------

修改 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-security/src/main/resources/application.yaml) 配置，增加 Spring Security 的配置。配置如下：

```
spring:
  
  security:
    
    user:
      name: user 
      password: user 
      roles: ADMIN
```

*   我们添加了一个账号「user/user」，角色为 **ADMIN** 。

24.3 SecurityConfig
-------------------

在 [`cn.iocoder.springboot.lab34.actuatordemo.config`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-security/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/) 包下，创建 [SecurityConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-34/lab-34-actuator-demo-security/src/main/java/cn/iocoder/springboot/lab34/actuatordemo/config/SecurityConfig.java) 配置类，配置 Actuator 的端点的安全认证。代码如下：

```
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests((requests) ->
                requests.anyRequest().hasRole("ADMIN"));
        
        http.httpBasic();
    }

}
```

*   `<1>` 处，设置 Actuator 的端点的访问，需要经过认证，并且拥有 ADMIN 角色。
*   `<2>` 处，设置使用 [HTTP Basic authentication](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Authentication)。

24.4 简单测试
---------

调用 `Application#main(Object[] args)` 方法，启动 Spring Boot 应用。

打开浏览器，访问 [http://127.0.0.1:8080/actuator/info](http://127.0.0.1:8080/actuator/info) 地址，被安全拦截，弹出登录窗口。如下图所示：![](https://static.iocoder.cn/images/Spring-Boot/2020-02-01/16.png)

输入「user/user」账号后，登录成功，才可以访问该端点。

> **重要**：更多关于 `health` 端点的内容，胖友可以补充阅读[《Spring Boot Actuator: Production-ready Features —— 2.3. Securing HTTP Endpoints》](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-features.html#production-ready-endpoints-security)文档。

至此，我们已经完成了 Spring Boot Actuator 的学习。通过它提供的**内置**的端点，我们可以很方便的监控与管理应用。同时，其它框架也可以实现其**框架的端点**。并且，我们同样可以**自定义的端点**。相当于说，**Spring Boot 定义了一套统一的应用的监控与管理体系**。

实际生产环境下，我们并不可能直接通过调用 Actuator 的端点，进行应用的管控与管理，毕竟应用节点的数量是非常多的，😈 我们也记不住那么多应用的地址，嘿嘿。所以，我们需要有**控制台**。目前比较主流的，有两个解决方案：

*   Spring Boot Admin ，可阅读[《芋道 Spring Boot 监控工具 Admin 入门》](http://www.iocoder.cn/Spring-Boot/Admin/?self)文章
*   Prometheus + Grafana ，可阅读[《芋道 Spring Boot 监控平台 Prometheus + Grafana 入门》](http://www.iocoder.cn/Spring-Boot/Prometheus-and-Grafana/?self)文章

考虑到很多胖友的英语跟艿艿一样渣渣，可以使用[《SpringBoot 2.X 中文文档 —— 五、Spring Boot Actuator: 生产就绪功能》](https://docshome.gitbooks.io/springboot/content/pages/production-ready.html#production-ready)文档。