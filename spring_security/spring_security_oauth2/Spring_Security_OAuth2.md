> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.iocoder.cn](https://www.iocoder.cn/Spring-Security/OAuth2-learning/?self)

> 摘要: 原创出处 http://www.iocoder.cn/Spring-Security/OAuth2-learning/ 「芋道源码」欢迎转载，保留摘要，谢谢！ 1. 概述 2. 密码模式 3.......

<div id="locker" style="display: block;"> <div class="mask"></div> <div class="info"> <div> <p class="text-center" style="color: black;"> 扫码关注公众号：<span style="color: #E9405A; font-weight: bold;"> 芋道源码 </span></p> <p class="text-center"></p> <p class="text-center"> <span style="color: black;"> 发送：</span> <span class="token" style="color: #e9415a; font-weight: bold; font-size: 17px; margin-bottom: 45px;"> 百事可乐 </span> <br> <span style="color: black;"> 获取永久解锁本站全部文章的链接 </span> </p> </div> <div class="text-center"> <img class="code-img" style="width: 300px" src="/images/common/erweima.jpg"> </div> </div> </div>

摘要: 原创出处 http://www.iocoder.cn/Spring-Security/OAuth2-learning/ 「芋道源码」欢迎转载，保留摘要，谢谢！

*   [1. 概述](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [2. 密码模式](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [3. 授权码模式](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [4. 简化模式](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [5. 客户端模式](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [6. 合并服务器](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [7. 刷新令牌](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [8. 删除令牌](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)
*   [666. 彩蛋](http://www.iocoder.cn/Spring-Security/OAuth2-learning/)

> 本文在提供完整代码示例，可见 [https://github.com/YunaiV/SpringBoot-Labs](https://github.com/YunaiV/SpringBoot-Labs) 的 [lab-68-spring-security-oauth](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-68-spring-security-oauth) 目录。
> 
> 原创不易，给点个 [Star](https://github.com/YunaiV/SpringBoot-Labs/stargazers) 嘿，一起冲鸭！

在[《芋道 Spring Boot 安全框架 Spring Security 入门》](http://www.iocoder.cn/Spring-Boot/Spring-Security/?self)文章中，艿艿分享了如何使用 Spring Security 实现认证与授权的功能，获得广大女粉丝的好评。

于是乎，艿艿准备再来分享一波 [Spring Security OAuth](https://github.com/spring-projects/spring-security-oauth) 框架，看看在 Spring Security 如何实现 OAuth2.0 实现**授权**的功能。

> 旁白君：实际上艿艿很早写了一篇关于 Spring Security OAuth 的[文章](http://www.iocoder.cn/Spring-Security/OAuth2-learning-learning/)，考虑到版本太老，提供的示例又过于简单，所以本文也是该文章的升级版。

可能有胖友对 OAuth2.0 不是很了解，所以我们先来简单介绍下它。可能胖友看 OAuth2.0 的概念会有点懵逼，不要担心，后续看完艿艿提供的示例代码，会突然清晰的哈。

另外，阮一峰提供了几篇关于 OAuth2.0 非常不错的文章，推荐胖友去从瞅瞅。同时，本文也会直接引用它的内容，方便胖友统一理解。

*   [《理解 OAuth2.0》](http://www.iocoder.cn/Fight/ruanyifeng-oauth_2_0/?self)
*   [《OAuth2.0 的一个简单解释》](http://www.iocoder.cn/Fight/ruanyifeng-oauth_design/?self)
*   [《OAuth2.0 的四种方式》](http://www.iocoder.cn/Fight/ruanyifeng-oauth-grant-types/?self)
*   [《GitHub OAuth 第三方登录示例教程》](http://www.iocoder.cn/Fight/ruanyifeng-github-oauth/?self)

1.1 OAuth2.0 是什么？
-----------------

> FROM [《维基百科 —— 开放授权》](https://zh.wikipedia.org/wiki/%E5%BC%80%E6%94%BE%E6%8E%88%E6%9D%83)

OAuth(Open Authorization) 是一个开放标准，允许用户让第三方应用访问该用户在某一网站上存储的私密的资源（如照片，视频，联系人列表），**而无需将用户名和密码提供给第三方应用**。

> 旁白君：很多团队，内部会采用 OAuth2.0 实现一个**授权**服务，避免每个上层应用或者服务重复开发。

OAuth 允许用户提供一个**令牌**，而不是用户名和密码来访问他们存放在特定服务提供者的数据。

每一个令牌授权一个特定的网站（例如，视频编辑网站) 在特定的时段（例如，接下来的 2 小时内）内访问**特定的资源**（例如仅仅是某一相册中的视频）。这样，OAuth 让用户可以授权第三方网站访问他们存储在另外服务提供者的某些特定信息，而非所有内容。

> 旁白君：如果胖友对接过[微信网页授权](https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html)功能，就会发现分成两种方式：**静默**授权、**手动**授权。前者只能获取到用户的 **openid**，而后者可以获取到用户的**基本信息**。

OAuth2.0 是用于授权的**行业标准协议**。OAuth2.0 为简化客户端开发提供了特定的授权流，包括 Web 应用、桌面应用、移动端应用等。

> 旁白君：OAuth 1.0 协议体系本身存在一些问题，现已被各大开发平台逐渐废弃。

1.2 OAuth2.0 角色解释
-----------------

在 OAuth2.0 中，有如下角色：

① **Authorization** Server：**认证**服务器，用于认证用户。如果客户端认证通过，则发放访问资源服务器的**令牌**。

② **Resource** Server：**资源**服务器，拥有受保护资源。如果请求包含正确的**访问令牌**，则可以访问资源。

> 友情提示：提供管理后台、客户端 API 的服务，都可以认为是 Resource Server。

③ **Client**：客户端。它请求资源服务器时，会带上**访问令牌**，从而成功访问资源。

> 友情提示：Client 可以是浏览器、客户端，也可以是内部服务。

④ Resource **Owner**：资源拥有者。最终用户，他有访问资源的**账号**与**密码**。

> 友情提示：可以简单把 Resource Owner 理解成人，她在使用 Client 访问资源。

1.3 OAuth 2.0 运行流程
------------------

如下是 OAuth 2.0 的**授权码模式**的运行流程：

![](https://static.iocoder.cn/6a92a862da97a4692c755c7e186dfd07.jpg)

> *   （A）用户打开客户端以后，客户端要求用户给予授权。
> *   （B）用户同意给予客户端授权。
> *   （C）客户端使用上一步获得的授权，向认证服务器申请令牌。
> *   （D）认证服务器对客户端进行认证以后，确认无误，同意发放令牌。
> *   （E）客户端使用令牌，向资源服务器申请获取资源。
> *   （F）资源服务器确认令牌无误，同意向客户端开放资源。

上述的六个步骤，**B 是关键**，即用户如何给客户端进行**授权**。有了授权之，客户端就可以获取**令牌**，进而凭令牌获取**资源**。

> 友情提示：如果胖友有对接过三方开放平台，例如说微信、QQ、微博等三方登录，就会很容易理解这个步骤过程。
> 
> 这个时候的资源，资源主要指的是三方开放平台的用户资料等等。

1.4 OAuth 2.0 授权模式
------------------

客户端必须得到用户的授权（Authorization Grant），才能获得访问令牌（Access Token）。

OAuth2.0 定义了四种授权方式：

*   授权码模式（Authorization Code）
*   密码模式（Resource Owner Password Credentials）
*   简化模式（Implicit）
*   客户端模式（Client Credentials）

其中，**密码模式**和**授权码模式**比较常用。至于如何选择，艿艿这里先提前剧透下，后续慢慢细品。

> FROM [《深度剖析 OAuth2 和微服务安全架构》](https://portal.qiniu.com/bucket/blog/resource)
> 
> ![](https://static.iocoder.cn/images/Spring-Security/2018_10_01/15.png)

当然，对于**黄框**部分，对于笔者还是比较困惑的。笔者认为，第三方的单页应用 SPA ，也是适合采用 Authorization Code Grant 授权模式的。例如，[《微信网页授权》](https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842) ：

> 具体而言，网页授权流程分为四步：
> 
> *   1、引导用户进入授权页面同意授权，获取 code
> *   2、通过 code 换取网页授权 access_token（与基础支持中的 access_toke n 不同）
> *   3、如果需要，开发者可以刷新网页授权 access_token，避免过期
> *   4、通过网页授权 access_token 和 openid 获取用户基本信息（支持 UnionID 机制）

所以，艿艿猜测，之所以图中画的是 Implicit Grant 的原因是，受 Google 的 [《OAuth 2.0 for Client-side Web Applications》](https://developers.google.com/identity/protocols/OAuth2UserAgent) 一文中，推荐使用了 Implicit Grant 。

当然，具体使用 Implicit Grant 还是 Authorization Code Grant 授权模式，没有定论。笔者，偏向于使用 **Authorization Code Grant**，对于第三方客户端的场景。

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/)
> *   资源服务器：[`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)

本小节，我们来学习**密码模式（Resource Owner Password Credentials Grant）**。

密码模式，用户向客户端提供自己的**用户名和密码**。客户端使用这些信息，向**授权服务器**索要授权。

在这种模式中，用户必须把自己的密码给客户端，但是客户端不得储存密码。这通常用在用户对客户端高度信任的情况下，比如客户端是操作系统的一部分，或者由一个著名公司出品。而授权服务器只有在其他授权模式无法执行的情况下，才能考虑使用这种模式。

> 旁白君：如果客户端和授权服务器都是自己公司的，显然符合。

![](https://static.iocoder.cn/1e7d96e9ed5ab025afd37c1ca97d1b39.jpg)

> *   （A）用户向客户端提供用户名和密码。
> *   （B）客户端将**用户名和密码**发给授权服务器，向后者**请求令牌**。
> *   （C）授权服务器确认无误后，向客户端提供访问令牌。

下面，我们来新建两个项目，搭建一个密码模式的使用示例。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/01.png)

*   [`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/)：授权服务器。
*   [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)：资源服务器。

2.1 搭建授权服务器
-----------

创建 [`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/) 项目，搭建授权服务器。

### 2.1.1 引入依赖

创建 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/pom.xml) 文件，引入 Spring Security OAuth 依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>lab-68</artifactId>
        <groupId>cn.iocoder.springboot.labs</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-68-demo02-authorization-server-with-resource-owner-password-credentials</artifactId>

    <properties>
        
        <spring.boot.version>2.2.4.RELEASE</spring.boot.version>
        
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.source>1.8</maven.compiler.source>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.security.oauth.boot</groupId>
            <artifactId>spring-security-oauth2-autoconfigure</artifactId>
            <version>${spring.boot.version}</version>
        </dependency>
    </dependencies>

</project>
```

添加 [`spring-security-oauth2-autoconfigure`](https://www.iocoder.cn/Spring-Security/OAuth2-learning/spring-security-oauth2-autoconfigure) 依赖，引入 Spring Security OAuth 并实现自动配置。同时，它也引入了 Spring Security 依赖。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/02.png)

### 2.1.2 SecurityConfig

创建 [SecurityConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/SecurityConfig.java) 配置类，提供一个账号密码为「yunai/1024」的用户。代码如下：

```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                
                inMemoryAuthentication()
                
                .passwordEncoder(passwordEncoder())
                
                .withUser("yunai").password("1024").roles("USER");
    }

}
```

我们通过 **Spring Security 提供认证功能**，所以这里需要配置一个用户。

> 友情提示：看不懂这个配置的胖友，后续可回[《芋道 Spring Boot 安全框架 Spring Security 入门》](http://www.iocoder.cn/Spring-Boot/Spring-Security/?self)重造下。

### 2.1.3 OAuth2AuthorizationServerConfig

创建 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 配置类，进行**授权**服务器。代码如下：

```
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    


    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() 
                .withClient("clientapp").secret("112233") 
                .authorizedGrantTypes("password") 
                .scopes("read_userinfo", "read_contacts") 

                ;
    }

}
```

① 在类上添加 [`@EnableAuthorizationServer`](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/config/annotation/web/configuration/EnableAuthorizationServer.java) 注解，声明开启 OAuth **授权**服务器的功能。

同时，继承 [AuthorizationServerConfigurerAdapter](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/config/annotation/web/configuration/AuthorizationServerConfigurerAdapter.java) 类，进行 OAuth **授权**服务器的配置。

② `#configure(AuthorizationServerEndpointsConfigurer endpoints)` 方法，配置使用的 AuthenticationManager 实现**用户认证**的功能。其中，`authenticationManager` 是由[「2.1.2 SecurityConfig」](#)创建，Spring Security 的配置类。

③ `#configure(AuthorizationServerSecurityConfigurer oauthServer)` 方法，设置 `/oauth/check_token` 端点，通过认证后可访问。

> 友情提示：这里的认证，指的是使用 `client-id` + `client-secret` 进行的**客户端**认证，不要和**用户**认证混淆。

其中，`/oauth/check_token` 端点对应 [CheckTokenEndpoint](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/endpoint/CheckTokenEndpoint.java) 类，用于校验访问令牌的有效性。

*   在客户端访问资源服务器时，会在请求中带上**访问令牌**。
*   在资源服务器收到客户端的请求时，会使用请求中的**访问令牌**，找授权服务器确认该**访问令牌**的有效性。

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/03.png)

④ `#configure(ClientDetailsServiceConfigurer clients)` 方法，进行 Client 客户端的配置。

`<4.1>` 处，设置使用基于**内存**的 Client 存储器。实际情况下，最好放入**数据库**中，方便管理。

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/04.png)

`<4.2>` 处，创建一个 Client 配置。如果要继续添加另外的 Client 配置，可以在 `<4.3>` 处使用 `#and()` 方法继续拼接。注意，这里的 `.withClient("clientapp").secret("112233")` 代码段，就是 `client-id` 和 `client-secret`。

> 补充知识：可能会有胖友会问，为什么要创建 Client 的 `client-id` 和 `client-secret` 呢？
> 
> 通过 `client-id` 编号和 `client-secret`，授权服务器可以知道调用的来源以及正确性。这样，即使 “坏人” 拿到 Access Token ，但是没有 `client-id` 编号和 `client-secret`，也不能和授权服务器发生**有效**的交互。

### 2.1.4 AuthorizationServerApplication

创建 [AuthorizationServerApplication](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/AuthorizationServerApplication.java) 类，授权服务器的启动类。代码如下：

```
@SpringBootApplication
public class AuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }

}
```

### 2.1.5 简单测试

执行 AuthorizationServerApplication 启动授权服务器。下面，我们使用 **Postman 模拟一个 Client**。

① `POST` 请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用密码模式进行**授权**。如下图所示：

请求说明：

*   通过 [Basic Auth](https://en.wikipedia.org/wiki/Basic_access_authentication) 的方式，填写 `client-id` + `client-secret` 作为用户名与密码，实现 Client 客户端有效性的认证。
*   请求参数 `grant_type` 为 `"password"`，表示使用**密码模式**。
*   请求参数 `username` 和 `password`，表示**用户**的用户名与密码。

响应说明：

*   响应字段 `access_token` 为**访问令牌**，后续客户端在访问资源服务器时，通过它作为身份的标识。
*   响应字段 `token_type` 为**令牌类型**，一般是 `bearer` 或是 `mac` 类型。
*   响应字段 `expires_in` 为访问令牌的**过期时间**，单位为秒。
*   响应字段 `scope` 为**权限范围**。

> 友情提示：`/oauth/token` 对应 [TokenEndpoint](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/endpoint/TokenEndpoint.java) 端点，提供 OAuth2.0 的四种授权模式。感兴趣的胖友，可以后续去撸撸。

② `POST` 请求 [http://localhost:8080/oauth/check_token](http://localhost:8080/oauth/check_token) 地址，校验访问令牌的有效性。如下图所示：

请求和响应比较简单，胖友自己瞅瞅即可。

2.2 搭建资源服务器
-----------

创建 [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/) 项目，搭建资源服务器。

### 2.2.1 引入依赖

创建 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/pom.xml) 文件，引入 Spring Security OAuth 依赖。

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>lab-68</artifactId>
        <groupId>cn.iocoder.springboot.labs</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>lab-68-demo02-resource-server</artifactId>

    <properties>
        
        <spring.boot.version>2.2.4.RELEASE</spring.boot.version>
        
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.source>1.8</maven.compiler.source>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>${spring.boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        
        <dependency>
            <groupId>org.springframework.security.oauth.boot</groupId>
            <artifactId>spring-security-oauth2-autoconfigure</artifactId>
            <version>${spring.boot.version}</version>
        </dependency>
    </dependencies>

</project>
```

> 友情提示：和[「2.1.1 引入依赖」](#)小节，是一致的哈。

### 2.2.2 配置文件

创建 [`application.yml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/resources/application.yml) 配置文件，添加 Spring Security OAuth 相关配置。

```
server:
  port: 9090

security:
  oauth2:
    
    client:
      client-id: clientapp
      client-secret: 112233
    
    resource:
      token-info-uri: http://127.0.0.1:8080/oauth/check_token 
    
    access-token-uri: http://127.0.0.1:8080/oauth/token
```

① `security.oauth2.client` 配置项，OAuth2 Client 配置，对应 [OAuth2ClientProperties](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/security/oauth2/client/OAuth2ClientProperties.java) 类。在这个配置项中，我们添加了客户端的 `client-id` 和 `client-secret`。

为什么要添加这个配置项呢？因为资源服务器会调用授权服务器的 `/oauth/check_token` 接口，而考虑到安全性，我们配置了该接口需要进过**客户端认证**。

> 友情提示：这里艿艿偷懒了，其实**单独**给资源服务器配置一个 Client 的 `client-id` 和 `client-secret`。我们可以把资源服务器理解成授权服务器的**一个特殊的客户端**。

② `security.oauth2.resource` 配置项，OAuth2 Resource 配置，对应 [ResourceServerProperties](https://github.com/spring-projects/spring-security-oauth2-boot/blob/master/spring-security-oauth2-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/security/oauth2/resource/ResourceServerProperties.java) 类。

这里，我们通过 `token-info-uri` 配置项，设置使用授权服务器的 `/oauth/check_token` 接口，校验访问令牌的有效性。

③ `security.access-token-uri` 配置项，是**我们自定义**的，设置授权服务器的 `oauth/token` 接口，获取访问令牌。因为稍后我们将在 [LoginController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/LoginController.java) 中，实现一个 `/login` 登录接口。

### 2.2.3 OAuth2ResourceServerConfig

创建 [OAuth2ResourceServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/config/OAuth2ResourceServerConfig.java) 类，进行**资源**服务器。代码如下：

```
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            
            .antMatchers("/login").permitAll()
            
            .anyRequest().authenticated()
            ;
    }

}
```

① 在类上添加 [`@EnableResourceServer`](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/config/annotation/web/configuration/EnableResourceServer.java) 注解，声明开启 OAuth **资源**服务器的功能。

同时，继承 [ResourceServerConfigurerAdapter](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/config/annotation/web/configuration/ResourceServerConfigurerAdapter.java) 类，进行 OAuth **资源**服务器的配置。

② `#configure(HttpSecurity http)` 方法，设置 HTTP 权限。这里，我们设置 `/login` 接口**无需**权限访问，其它接口**认证**后可访问。

这样，客户端在访问资源服务器时，其请求中的**访问令牌**会被**资源**服务器调用**授权**服务器的 `/oauth/check_token` 接口，进行校验访问令牌的正确性。

### 2.2.4 ExampleController

创建 [ExampleController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/ExampleController.java) 类，提供 `/api/example/hello` 接口，表示一个资源。代码如下：

```
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @RequestMapping("/hello")
    public String hello() {
        return "world";
    }

}
```

### 2.2.5 ResourceServerApplication

创建 [ResourceServerApplication](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/ResourceServerApplication.java) 类，资源服务器的启动类。代码如下：

```
@SpringBootApplication
public class ResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }

}
```

### 2.2.6 简单测试（第一弹）

执行 ResourceServerApplication 启动资源服务器。下面，我们来请求服务器的 <127.0.0.1:9090/api/example/hello> 接口，进行相应的测试。

① 首先，请求 <127.0.0.1:9090/api/example/hello> 接口，**不带**访问令牌，则请求会被**拦截**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/09.png)

② 然后，请求 <127.0.0.1:9090/api/example/hello> 接口，带上**错误**的访问令牌，则请求会被**拦截**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/10.png)

> 友情提示：访问令牌需要在请求头 `"Authorization"` 上设置，并且以 `"Bearer "` 开头。

③ 最后，请求 <127.0.0.1:9090/api/example/hello> 接口，带上**正确**的访问令牌，则请求会被**通过**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/11.png)

### 2.2.7 LoginController

创建 [LoginController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/LoginController.java) 类，提供 `/login` 登录接口。代码如下：

```
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;

    @PostMapping("/login")
    public OAuth2AccessToken login(@RequestParam("username") String username,
                                   @RequestParam("password") String password) {
        
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
        
        return restTemplate.getAccessToken();
    }

}
```

在 `/login` 接口中，**资源**服务器扮演的是一个 OAuth **客户端**的角色，调用授权服务器的 `/oauth/token` 接口，使用**密码模式**进行授权，获得**访问令牌**。

① `<1>` 处，创建 [ResourceOwnerPasswordResourceDetails](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/client/token/grant/password/ResourceOwnerPasswordResourceDetails.java) 对象，填写**密码模式**授权需要的**请求**参数。

② `<2>` 处，创建 [OAuth2RestTemplate](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/client/OAuth2RestTemplate.java) 对象，它是 Spring Security OAuth 封装的工具类，用于请求**授权**服务器。

同时，将 [ResourceOwnerPasswordAccessTokenProvider](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/client/token/grant/password/ResourceOwnerPasswordAccessTokenProvider.java) 设置到其中，表示使用**密码模式**授权。

> 友情提示：这一步非常重要，艿艿在这里卡了非常非常非常久，一度自闭要放弃。

③ `<3>` 处，调用 OAuth2RestTemplate 的 `#getAccessToken()` 方法，调用授权服务器的 `/oauth/token` 接口，进行**密码**模式的授权。

注意，OAuth2RestTemplate 是**有状态**的工具类，所以需要每次都**重新**创建。

### 2.2.8 简单测试（第二弹）

重新执行 ResourceServerApplication 启动资源服务器。下面，我们来进行 `/login` 接口的测试。

① 首先，请求 [http://127.0.0.1:9090/login](http://127.0.0.1:9090/login) 接口，使用**用户**的**用户名**与**密码**进行登录，获得访问令牌。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/12.png)

响应结果和授权服务器的 `/oauth/token` 接口是一致的，因为就是调用它，嘿嘿~

② 然后，请求 <127.0.0.1:9090/api/example/hello> 接口，带**刚刚的**访问令牌，则请求会被通过。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/13.png)

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/)
> *   资源服务器：[`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)

本小节，我们来学习**授权码模式（Authorization Code）**。

授权码模式，是功能最完整、流程最严密的授权模式。它的特点就是通过客户端的**后台**服务器，与**授权**务器进行互动。

> 旁白君：一般情况下，**在有客户端**的情况下，我们与第三方平台常常采用这种方式。

![](https://static.iocoder.cn/8a16a81fbba3d6ba1002921bf6b4feff.jpg)

> *   （A）用户访问客户端，后者将前者跳转到到**授权**服务器。
> *   （B）用户选择是否给予客户端授权。
> *   （C）假设用户给予授权，授权服务器将跳转到客户端事先指定的 "重定向 URI"（Redirection URI），同时附上一个**授权码**。
> *   （D）客户端收到授权码，附上早先的 "重定向 URI"，向认证服务器申请**令牌**。这一步是在客户端的后台的服务器上完成的，对用户不可见。
> *   （E）认证服务器核对了**授权码**和**重定向 URI**，确认无误后，向客户端发送**访问令牌**。

下面，我们来新建两个项目，搭建一个授权码模式的使用示例。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/21.png)

*   [`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/)：授权服务器。
*   [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)：资源服务器。

3.1 搭建授权服务器
-----------

复制出 [`lab-68-demo02-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-resource-owner-password-credentials/) 项目，**修改**搭建授权服务器。改动点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/22.png)

仅仅需要修改 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-authorization-code/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 类，设置使用 `"authorization_code"` 授权码模式，并设置回调地址。

🙂 注意，这里设置的回调地址，稍后我们会在[「3.2 搭建资源服务器」](#)中实现。

### 3.1.1 简单测试

执行 AuthorizationServerApplication 启动授权服务器。

① 使用**浏览器**，访问 [http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback&response_type=code&scope=read_userinfo](http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback&response_type=code&scope=read_userinfo) 地址，获取**授权**。请求参数说明如下：

*   `client_id` 参数，**必传**，为我们在 OAuth2AuthorizationServer 中配置的 Client 的编号。
*   `redirect_uri` 参数，**可选**，回调地址。当然，如果 `client_id` 对应的 Client 未配置 `redirectUris` 属性，会报错。
*   `response_type` 参数，**必传**，返回结果为 `code` **授权码**。
*   `scope` 参数，**可选**，申请授权的 Scope 。如果多个，使用逗号分隔。
*   `state` 参数，**可选**，表示客户端的当前状态，可以指定任意值，授权服务器会原封不动地返回这个值。

> 友情提示：`state` 参数，未在上述 URL 中体现出来。

因为我们并未**登录**授权服务器，所以被拦截跳转到登录界面。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/23.png)

② 输入用户的账号密码「yunai/1024」进行登录。登录完成后，进入授权界面。如下图所示：

> 旁白君：和我们日常使用的腾讯 QQ、微信、微博等等三方登录，是一模一样的，除了丑了点，嘿嘿~

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/24.png)

③ 选择 `scope.read_userinfo` 为 Approve 允许，点击「Authorize」按钮，完成**授权**操作。浏览器自动重定向到 Redirection URI 地址，并且在 URI 上可以看到 `code` 授权码。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/25.png)

> 友情提示：`/oauth/authorize` 对应 [AuthorizationEndpoint](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/endpoint/AuthorizationEndpoint.java) 端点。

④ 因为我们暂时没有启动**资源**服务器，所以显示无法访问。这里，我们先使用 Postman 模拟请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用**授权码**获取到**访问令牌**。如下图所示：

请求说明：

*   通过 [Basic Auth](https://en.wikipedia.org/wiki/Basic_access_authentication) 的方式，填写 `client-id` + `client-secret` 作为用户名与密码，实现 Client 客户端有效性的认证。
*   请求参数 `grant_type` 为 `"authorization_code"`，表示使用**授权码模式**。
*   请求参数 `code`，从授权服务器获取到的**授权码**。
*   请求参数 `redirect_uri`，Client 客户端的 **Redirection URI** 地址。

注意，授权码**仅能使用一次**，重复请求会报 `Invalid authorization code:` 错误。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/27.png)

3.2 搭建资源服务器
-----------

复用 [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/) 项目，主要是提供回调地址。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/28.png)

① 新建 [CallbackController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/CallbackController.java) 类，提供 `/callback` 回调地址。

② 在 [OAuth2ResourceServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/config/OAuth2ResourceServerConfig.java) 配置类中，设置 `/callback` 回调地址无需权限验证，不然回调都跳转不过来哈。

### 3.2.1 CallbackController

创建 [CallbackController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/CallbackController.java) 类，提供 `/callback` 回调地址，在获取到**授权码**时，请求**授权**服务器，通过**授权码**获取**访问令牌**。代码如下：

```
@RestController
@RequestMapping("/")
public class CallbackController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;

    @GetMapping("/callback")
    public OAuth2AccessToken login(@RequestParam("code") String code) {
        
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
        
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode(code); 
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setPreservedState("http://127.0.0.1:9090/callback"); 
        restTemplate.setAccessTokenProvider(new AuthorizationCodeAccessTokenProvider());
        
        return restTemplate.getAccessToken();
    }

}
```

代码比较简单，还是使用 OAuth2RestTemplate 进行请求授权服务器，胖友自己瞅瞅哈。

需要注意的是 `<1>` 和 `<2>` 处，设置请求授权服务器需要的 `code` 和 `redirect_uri` 参数。

### 3.2.2 简单测试

执行 ResourceServerApplication 启动资源服务器。

重复[「3.2.1 简单测试」](#)的过程，成功获取到**访问令牌**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/29.png)

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo02-authorization-server-with-implicit`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-implicit/)
> *   资源服务器：[`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)

本小节，我们来学习**简化模式（Implicit）**。

简化模式，不通过第三方应用程序的服务器，直接在浏览器中向**授权**服务器申请令牌，**跳过**了 “授权码” 这个步骤，因此得名。所有步骤在浏览器中完成，令牌对访问者是**可见**的，且客户端不需要授权。

![](https://static.iocoder.cn/202e8e80d8b1740f38bde7a3d889e088.jpg)

> *   （A）用户访问客户端，后者将前者跳转到到**授权**服务器。
> *   （B）用户选择是否给予客户端授权。
> *   （C）假设用户给予授权，授权服务器将用户导向客户端指定的 "重定向 URI"，并在 URI 的 **Hash 部分**包含了**访问令牌**。
> *   （D）浏览器向资源服务器发出请求，其中不包括上一步收到的 Hash 值。
> *   （E）资源服务器返回一个网页，其中包含的代码可以获取 Hash 值中的令牌。
> *   （F）浏览器执行上一步获得的脚本，提取出令牌。
> *   （G）浏览器将令牌发给客户端。

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/31.png)

*   [`lab-68-demo02-authorization-server-with-implicit`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-implicit/)：授权服务器。
*   [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)：资源服务器。

4.1 搭建授权服务器
-----------

复制出 [`lab-68-demo02-authorization-server-with-implicit`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-implicit/) 项目，**修改**搭建授权服务器。改动点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/32.png)

仅仅需要修改 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-implicit/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 类，设置使用 `"implicit"` 简化模式，并设置回调地址。

🙂 注意，这里设置的回调地址，稍后我们会在[「4.2 搭建资源服务器」](#)中实现。

4.2 搭建资源服务器
-----------

复用 [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/) 项目，主要是提供回调地址。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/33.png)

① 新建 [Callback02Controller](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/Callback02Controller.java) 类，提供 `/callback02` 回调地址。代码如下：

```
@RestController
@RequestMapping("/")
public class Callback02Controller {

    @GetMapping("/callback02")
    public String login() {
        return "假装这里有一个页面";
    }

}
```

> 友情提示：考虑到暂时不想做页面，所以这里先假装一下，嘿嘿。

② 在 [OAuth2ResourceServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/config/OAuth2ResourceServerConfig.java) 配置类中，设置 `/callback02` 回调地址无需权限验证，不然回调都跳转不过来哈。

4.3 简单测试
--------

执行 AuthorizationServerApplication 启动授权服务器。  
执行 ResourceServerApplication 启动资源服务器。

① 使用**浏览器**，访问 [http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback02&response_type=token&scope=read_userinfo](http://127.0.0.1:8080/oauth/authorize?client_id=clientapp&redirect_uri=http://127.0.0.1:9090/callback02&response_type=token&scope=read_userinfo) 地址，获取**授权**。请求参数说明如下：

*   `client_id` 参数，**必传**，为我们在 OAuth2AuthorizationServer 中配置的 Client 的编号。
*   `redirect_uri` 参数，**可选**，回调地址。当然，如果 `client_id` 对应的 Client 未配置 `redirectUris` 属性，会报错。
*   `response_type` 参数，**必传**，返回结果为 `token` **访问令牌**。
*   `scope` 参数，**可选**，申请授权的 Scope 。如果多个，使用逗号分隔。
*   `state` 参数，**可选**，表示客户端的当前状态，可以指定任意值，授权服务器会原封不动地返回这个值。

> 友情提示：`state` 参数，未在上述 URL 中体现出来。

因为我们并未**登录**授权服务器，所以被拦截跳转到登录界面。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/23.png)

② 输入用户的账号密码「yunai/1024」进行登录。登录完成后，进入授权界面。如下图所示：

> 旁白君：和我们日常使用的腾讯 QQ、微信、微博等等三方登录，是一模一样的，除了丑了点，嘿嘿~

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/34.png)

③ 选择 `scope.read_userinfo` 为 Approve 允许，点击「Authorize」按钮，完成**授权**操作。浏览器自动重定向到 Redirection URI 地址，并且在 URI 上的 **Hash 部分**可以看到 `access_token` 访问令牌。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/35.png)

后续，可以通过编写 Javascript 脚本的代码，获取 URI 上的 **Hash 部分**的访问令牌。

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo02-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-client-credentials/)
> *   资源服务器：[`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)

本小节，我们来学习**客户端模式（Client Credentials）**。

客户端模式，指客户端以自己的名义，而不是以用户的名义，向授权服务器进行认证。

严格地说，客户端模式并不属于 OAuth 框架所要解决的问题。在这种模式中，用户直接向客户端注册，客户端以自己的名义要求授权服务器提供服务，其实不存在授权问题。

> 旁白君：我们对接微信公众号时，就采用的客户端模式。我们的后端服务器就扮演 “客户端” 的角色，与微信公众号的后端服务器进行交互。

![](https://static.iocoder.cn/062fff24b9564ca9e0cbf4f702af9ee6.jpg)

> *   （A）客户端向授权服务器进行身份认证，并要求一个**访问令牌**。
> *   （B）授权服务器确认无误后，向客户端提供访问令牌。

下面，我们来新建两个项目，搭建一个客户端模式的使用示例。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/41.png)

*   [`lab-68-demo02-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-client-credentials/)：授权服务器。
*   [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/)：资源服务器。

5.1 搭建授权服务器
-----------

复制出 [`lab-68-demo02-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-client-credentials/) 项目，**修改**搭建授权服务器。改动点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/42.png)

① 删除 SecurityConfig 配置类，因为客户端模式下，无需 Spring Security 提供用户的认证功能。

但是，Spring Security OAuth 需要一个 PasswordEncoder Bean，否则会报错，因此我们在 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-client-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 类的 `#passwordEncoder()` 方法进行创建。

② 修改 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-authorization-server-with-implicit/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 类，设置使用 `"client_credentials"` 客户端模式。

### 5.1.1 简单测试

执行 AuthorizationServerApplication 启动授权服务器。下面，我们使用 **Postman 模拟一个 Client**。

① `POST` 请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用客户端模式进行**授权**。如下图所示：

请求说明：

*   通过 [Basic Auth](https://en.wikipedia.org/wiki/Basic_access_authentication) 的方式，填写 `client-id` + `client-secret` 作为用户名与密码，实现 Client 客户端有效性的认证。
*   请求参数 `grant_type` 为 `"client_credentials"`，表示使用**客户端模式**。

响应就是访问令牌，胖友自己瞅瞅即可。

5.2 搭建资源服务器
-----------

复用 [`lab-68-demo02-resource-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/) 项目，修改点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/44.png)

① 新建 [ClientLoginController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/controller/ClientLoginController.java) 类，提供 `/client-login` 接口，实现调用**授权**服务器，进行**客户端**模式的授权，获得访问令牌。代码如下：

```
@RestController
@RequestMapping("/")
public class ClientLoginController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;

    @PostMapping("/client-login")
    public OAuth2AccessToken login() {
        
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
        
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());
        
        return restTemplate.getAccessToken();
    }

}
```

代码比较简单，还是使用 OAuth2RestTemplate 进行请求授权服务器，胖友自己瞅瞅哈。

② 在 [OAuth2ResourceServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo02-resource-server/src/main/java/cn/iocoder/springboot/lab68/resourceserverdemo/config/OAuth2ResourceServerConfig.java) 配置类中，设置 `/client-login` 接口无需权限验证，不然无法调用哈。

### 5.2.1 简单测试

执行 ResourceServerApplication 启动资源服务器。

① 使用[「5.1.1 简单测试」](#)小节获得的**访问令牌**，请求 <127.0.0.1:9090/api/example/hello> 接口时**带上**，则请求会被**通过**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/45.png)

② 请求 [http://127.0.0.1:9090/clientlogin](http://127.0.0.1:9090/clientlogin) 接口，使用**客户端模式**进行授权，获得访问令牌。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/46.png)

响应结果和授权服务器的 `/oauth/token` 接口是一致的，因为就是调用它，嘿嘿~

> 旁白君：这个小节的标题，艿艿有点不知道怎么取了，就先叫合并服务器吧 = =！

在项目比较小时，考虑到节省服务器资源，会考虑将**授权**服务器和**资源**服务器**合并**到一个项目中，避免启动多个 Java 进程。良心的艿艿，编写了四种授权模式的示例，如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/51.png)

*   基于**密码**模式的示例：[`lab-68-demo01-resource-owner-password-credentials-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo01-resource-owner-password-credentials-server/)
*   基于**授权码**模式的示例：[`lab-68-demo01-authorization-code-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo01-authorization-code-server/)
*   基于**简化**模式的示例：[`lab-68-demo01-implicit-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo01-implicit-server/)
*   基于**客户端**模式的示例：[`lab-68-demo01-client-credentials-server`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo01-client-credentials-server/)

具体的代码实现，实际和上述每个授权模式对应的小节是基本一致的，只是说将代码 “**放**” 在了一个项目中。嘿嘿~

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo03-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials)

在 OAuth2.0 中，一共有**两类**令牌：

*   **访问**令牌（Access Token）
*   **刷新**令牌（Refresh Token）

在**访问**令牌过期时，我们可以使用**刷新**令牌向**授权**服务器获取一个**新**的访问令牌。

可能会胖友有疑惑，为什么会有**刷新**令牌呢？每次请求资源服务器时，都会在请求上带上**访问**令牌，这样它的泄露风险是**相对**高的。

因此，出于**安全性**的考虑，访问令牌的过期时间**比较短**，刷新令牌的过期时间**比较长**。这样，如果访问令牌即使被盗用走，那么在一定的时间后，访问令牌也能在较短的时间吼过期。当然，安全也是相对的，如果使用刷新令牌后，获取到新的访问令牌，访问令牌**后续**又**可能**被盗用。

艿艿整理了下，大家常用开放平台的令牌过期时间，让大家更好的理解：

<table><thead><tr><th>开放平台</th><th>Access Token 有效期</th><th>Refresh Token 有效期</th></tr></thead><tbody><tr><td><a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&amp;t=resource/res_list&amp;verify=1&amp;id=open1419316505&amp;token=&amp;lang=zh_CN" rel="external nofollow noopener noreferrer" target="_blank">微信开放平台</a></td><td>2 小时</td><td>未知</td></tr><tr><td><a href="http://wiki.open.qq.com/wiki/website/%E8%8E%B7%E5%8F%96Access_Token" rel="external nofollow noopener noreferrer" target="_blank">腾讯开放平台</a></td><td>90 天</td><td>未知</td></tr><tr><td><a href="https://dev.mi.com/docs/passport/access-token-life-cycle/" rel="external nofollow noopener noreferrer" target="_blank">小米开放平台</a></td><td>90 天</td><td>10 年</td></tr></tbody></table>

7.1 示例项目
--------

下面，复制出 [`lab-68-demo03-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials) 项目，搭建**提供访问令牌**的**授权**服务器。改动点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/61.png)

① 在 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 的 `#configure(ClientDetailsServiceConfigurer clients)` 方法中，在配置的 Client 的授权模式中，额外新增 `"refresh_token"` 刷新令牌。

通过 `#accessTokenValiditySeconds(int accessTokenValiditySeconds)` 方法，设置**访问**令牌的有效期。  
通过 `#refreshTokenValiditySeconds(int refreshTokenValiditySeconds)` 方法，设置**刷新**令牌的有效期。

② 在 [OAuth2AuthorizationServerConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/OAuth2AuthorizationServerConfig.java) 的 `#configure(AuthorizationServerEndpointsConfigurer endpoints)` 方法中，设置使用的 `userDetailsService` 用户详情 Service。

而该 `userDetailsService` 是在 [SecurityConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/SecurityConfig.java) 的 `#userDetailsServiceBean()` 方法创建的 UserDetailsService Bean。

> 友情提示：如果不进行 UserDetailsService 的设置，在使用**刷新**令牌获取新的**访问**令牌时，会抛出异常。

7.2 简单测试
--------

执行 AuthorizationServerApplication 启动授权服务器。下面，我们使用 **Postman 模拟一个 Client**。

① `POST` 请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用**密码**模式进行**授权**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/62.png)

**额外**多返回了 `refresh_token` 刷新令牌。

② `POST` 请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用**刷新令牌**模式进行**授权**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/63.png)

请求说明：

*   通过 [Basic Auth](https://en.wikipedia.org/wiki/Basic_access_authentication) 的方式，填写 `client-id` + `client-secret` 作为用户名与密码，实现 Client 客户端有效性的认证。
*   请求参数 `grant_type` 为 `"refresh_token"`，表示使用**刷新令牌模式**。
*   请求参数 `refresh_token`，表示**刷新令牌**。

在响应中，返回了**新的** `access_token` **访问**令牌。注意，**老的** `access_token` **访问**令牌会**失效**，无法继续使用。

> 示例代码对应仓库：
> 
> *   授权服务器：[`lab-68-demo03-authorization-server-with-client-credentials`](https://github.com/YunaiV/SpringBoot-Labs/tree/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials)

在用户**登出**系统时，我们会有**删除**令牌的需求。虽然说，可以通过客户端**本地**删除令牌的方式实现。但是，考虑到真正的彻底的实现删除令牌，必然服务端**自身**需要删除令牌。

> 友情提示：客户端**本地**删除令牌的方式实现，指的是清楚本地 Cookie、localStorage 的令牌缓存。

在 Spring Security OAuth2 中，**并没有提供内置的接口**，所以需要自己去实现。笔者参看 [《Spring Security OAuth2 – Simple Token Revocation》](https://www.baeldung.com/spring-security-oauth-revoke-tokens) 文档，实现删除令牌的 API 接口。

具体的实现，通过调用 [ConsumerTokenServices](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/ConsumerTokenServices.java) 的 `#revokeToken(String tokenValue)` 方法，删除**访问**令牌和**刷新**令牌。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/71.png)

8.1 示例项目
--------

下面，我们直接在**授权**服务器 [`lab-68-demo03-authorization-server-with-resource-owner-password-credentials`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/) 项目，修改接入删除令牌的功能。改动点如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/72.png)

① 创建 [TokenDemoController](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/controller/TokenDemoController.java) 类，提供 `/token/demo/revoke` 接口，调用 ConsumerTokenServices 的 `#revokeToken(String tokenValue)` 方法，删除**访问**令牌和**刷新**令牌。代码如下：

```
@RestController
@RequestMapping("/token/demo")
public class TokenDemoController {

    @Autowired
    private ConsumerTokenServices tokenServices;

    @PostMapping(value = "/revoke")
    public boolean revokeToken(@RequestParam("token") String token) {
        return tokenServices.revokeToken(token);
    }

}
```

② 在 [SecurityConfig](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-68-spring-security-oauth/lab-68-demo03-authorization-server-with-resource-owner-password-credentials/src/main/java/cn/iocoder/springboot/lab68/authorizationserverdemo/config/SecurityConfig.java) 配置类，设置 `/token/demo/revoke` 接口**无需授权**，方便测试。代码如下：

```
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
            .authorizeRequests()
            
            .mvcMatchers("/token/demo/revoke").permitAll()
            
            .anyRequest().authenticated();
}
```

8.2 简单测试
--------

执行 AuthorizationServerApplication 启动授权服务器。下面，我们使用 **Postman 模拟一个 Client**。

① `POST` 请求 [http://localhost:8080/oauth/token](http://localhost:8080/oauth/token) 地址，使用**密码**模式进行**授权**。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/73.png)

② `POST` 请求 [http://localhost:8080/token/demo/revoke](http://localhost:8080/token/demo/revoke) 地址，删除令牌。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/74.png)

删除成功。后续，胖友可以自己调用**授权**服务器的 `oauth/check_token` 接口，测试**访问**令牌是否已经被删除。

至此，我们完整学习 Spring Security OAuth 框架。不过 Spring 团队宣布该框架处于 Deprecation **废弃**状态。如下图所示：

![](http://www.iocoder.cn/images/Spring-Security/2020-01-01/81.png)

同时，Spring 团队正在实现新的 [Spring Authorization Server](https://github.com/spring-projects-experimental/spring-authorization-server) **授权**服务器，目前还处于 Experimental **实验**状态。

实际项目中，根据艿艿了解到的情况，很少项目会直接采用 Spring Security OAuth 框架，而是**自己参考它进行 OAuth2.0 的实现**。并且，一般只会实现**密码**授权模式。

在本文中，我们采用基于**内存**的 [InMemoryTokenStore](https://github.com/spring-projects/spring-security-oauth/blob/master/spring-security-oauth2/src/main/java/org/springframework/security/oauth2/provider/token/store/InMemoryTokenStore.java)，实现**访问**令牌和**刷新**令牌的存储。它会存在两个明显的**缺点**：

*   **重启**授权服务器时，令牌信息会**丢失**，导致用户需要重新授权。
*   **多个**授权服务器时，令牌信息无法**共享**，导致用户一会授权成功，一会授权失败。

因此，下一篇[《芋道 Spring Security OAuth2 存储器》](http://www.iocoder.cn/Spring-Security/OAuth2-learning-store/?self)文章，我们来学习 Spring Security OAuth 提供的基于**数据库**和 **Redis** 的存储器。走起~