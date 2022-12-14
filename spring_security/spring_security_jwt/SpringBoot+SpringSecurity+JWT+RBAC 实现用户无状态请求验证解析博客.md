> 本文由 [简悦 SimpRead](http://ksria.com/simpread/) 转码， 原文地址 [www.iocoder.cn](https://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/?self)

> 摘要: 原创出处 blog.csdn.net/larger5/article/details/81063438 「larger5」欢迎转载，保留摘要，谢谢！ 一、前言 二、代码 1. pom 2. ......

<div id="locker" style="display: block;"> <div class="mask"></div> <div class="info"> <div> <p class="text-center" style="color: black;"> 扫码关注公众号：<span style="color: #E9405A; font-weight: bold;"> 芋道源码 </span></p> <p class="text-center"></p> <p class="text-center"> <span style="color: black;"> 发送：</span> <span class="token" style="color: #e9415a; font-weight: bold; font-size: 17px; margin-bottom: 45px;"> 百事可乐 </span> <br> <span style="color: black;"> 获取永久解锁本站全部文章的链接 </span> </p> </div> <div class="text-center"> <img class="code-img" style="width: 300px" src="/images/common/erweima.jpg"> </div> </div> </div>

摘要: 原创出处 blog.csdn.net/larger5/article/details/81063438 「larger5」欢迎转载，保留摘要，谢谢！

*   [一、前言](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
*   [二、代码](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [1. pom](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [2. AjaxResponseBody](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [3. AjaxAccessDeniedHandler](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [4. AjaxAuthenticationEntryPoint](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [5. AjaxAuthenticationFailureHandler](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [6. AjaxAuthenticationSuccessHandler](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [7. AjaxLogoutSuccessHandler](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [8. JwtAuthenticationTokenFilter](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [9. RbacAuthorityService](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [10. SelfUserDetails](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [11. SelfUserDetailsService](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [12. SpringSecurityConf](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [13. JwtTokenUtil](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
*   [三、其他](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
*   [四、测试示例（2018/8/29 更新）](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [1. 登录示例](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
    *   [2. 访问内部示例](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)
*   [五、关于 Token（2019.9.13 更新）](http://www.iocoder.cn/Fight/Separate-SpringBoot-SpringSecurity-JWT-RBAC-from-front-and-rear-to-achieve-user-stateless-request-authentication/)

修改自前文，十分贴近公司开发的生产环境

前后端分离 SpringBoot + SpringSecurity 权限解决方案

RBAC（Role-Based Access Control，基于角色的访问控制）

代码已经放在 github 上了：https://github.com/larger5/SpringBoot_SpringSecurity_JWT_RBAC.git

![](https://static.iocoder.cn/cc9e361bf492dd31d75bef42a57634ca)

1. pom
------

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>


<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.36</version>
</dependency>


<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.0</version>
</dependency>
```

2. AjaxResponseBody
-------------------

```
package com.cun.security3.bean;

import java.io.Serializable;

public class AjaxResponseBody implements Serializable{

    private String status;
    private String msg;
    private Object result;
    private String jwtToken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
```

3. AjaxAccessDeniedHandler
--------------------------

```
package com.cun.security3.config;

import com.alibaba.fastjson.JSON;
import com.cun.security3.bean.AjaxResponseBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("300");
        responseBody.setMsg("Need Authorities!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
```

4. AjaxAuthenticationEntryPoint
-------------------------------

```
package com.cun.security3.config;

import com.alibaba.fastjson.JSON;
import com.cun.security3.bean.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("000");
        responseBody.setMsg("Need Authorities!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
```

5. AjaxAuthenticationFailureHandler
-----------------------------------

```
package com.cun.security3.config;

import com.alibaba.fastjson.JSON;
import com.cun.security3.bean.AjaxResponseBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("400");
        responseBody.setMsg("Login Failure!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
```

6. AjaxAuthenticationSuccessHandler
-----------------------------------

```
package com.cun.security3.config;

import com.alibaba.fastjson.JSON;
import com.cun.security3.bean.AjaxResponseBody;
import com.cun.security3.utils.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("200");
        responseBody.setMsg("Login Success!");

        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();

        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), 300, "_secret");
        responseBody.setJwtToken(jwtToken);

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
```

7. AjaxLogoutSuccessHandler
---------------------------

```
package com.cun.security3.config;

import com.alibaba.fastjson.JSON;
import com.cun.security3.bean.AjaxResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("100");
        responseBody.setMsg("Logout Success!");

        httpServletResponse.getWriter().write(JSON.toJSONString(responseBody));
    }
}
```

8. JwtAuthenticationTokenFilter
-------------------------------

```
package com.cun.security3.config;

import com.cun.security3.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    SelfUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String authToken = authHeader.substring("Bearer ".length());

            String username = JwtTokenUtil.parseToken(authToken, "_secret");

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}
```

9. RbacAuthorityService
-----------------------

```
package com.cun.security3.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("rbacauthorityservice")
public class RbacAuthorityService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object userInfo = authentication.getPrincipal();

        boolean hasPermission  = false;

        if (userInfo instanceof UserDetails) {

            String username = ((UserDetails) userInfo).getUsername();

            
            Set<String> urls = new HashSet();
            urls.add("/common/**"); 
            Set set2 = new HashSet();
            Set set3 = new HashSet();

            AntPathMatcher antPathMatcher = new AntPathMatcher();

            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }

            return hasPermission;
        } else {
            return false;
        }
    }
}
```

10. SelfUserDetails
-------------------

```
package com.cun.security3.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;




public class SelfUserDetails implements UserDetails, Serializable {
    private String username;
    private String password;
    private Set<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() { 
        return this.password;
    }

    @Override
    public String getUsername() { 
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```

11. SelfUserDetailsService
--------------------------

```
package com.cun.security3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SelfUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        

        SelfUserDetails userInfo = new SelfUserDetails();
        userInfo.setUsername(username);
        userInfo.setPassword(new BCryptPasswordEncoder().encode("123"));

        Set authoritiesSet = new HashSet();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authoritiesSet.add(authority);
        userInfo.setAuthorities(authoritiesSet);

        return userInfo;
    }
}
```

12. SpringSecurityConf
----------------------

```
package com.cun.security3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SpringSecurityConf extends WebSecurityConfigurerAdapter {

    @Autowired
    AjaxAuthenticationEntryPoint authenticationEntryPoint;  

    @Autowired
    AjaxAuthenticationSuccessHandler authenticationSuccessHandler;  

    @Autowired
    AjaxAuthenticationFailureHandler authenticationFailureHandler;  

    @Autowired
    AjaxLogoutSuccessHandler  logoutSuccessHandler;  

    @Autowired
    AjaxAccessDeniedHandler accessDeniedHandler;    

    @Autowired
    SelfUserDetailsService userDetailsService; 

    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter; 

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 
                .and()

                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)

                .and()
                .authorizeRequests()

                .anyRequest()
                .access("@rbacauthorityservice.hasPermission(request,authentication)") 

                .and()
                .formLogin()  
                .successHandler(authenticationSuccessHandler) 
                .failureHandler(authenticationFailureHandler) 
                .permitAll()

                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService).tokenValiditySeconds(300);

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler); 
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class); 

    }
}
```

13. JwtTokenUtil
----------------

```
package com.cun.security3.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

public class JwtTokenUtil {

    private static InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("jwt.jks"); 
    private static PrivateKey privateKey = null;
    private static PublicKey publicKey = null;

    static { 
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS"); 
            keyStore.load(inputStream, "123456".toCharArray());
            privateKey = (PrivateKey) keyStore.getKey("jwt", "123456".toCharArray()); 
            publicKey = keyStore.getCertificate("jwt").getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateToken(String subject, int expirationSeconds, String salt) {
        return Jwts.builder()
                .setClaims(null)
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))

                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public static String parseToken(String token, String salt) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()

                    .setSigningKey(publicKey)
                    .parseClaimsJws(token).getBody();
            subject = claims.getSubject();
        } catch (Exception e) {
        }
        return subject;
    }

}
```

<table><thead><tr><th>不足</th><th>解决</th></tr></thead><tbody><tr><td>没有注销 token 登录</td><td>每次登录，生成 token 放到 Redis 数据库里边，调用接口的时候，先查有没有这个 token，注销时把 token 删除</td></tr></tbody></table>

由于网友对 `jwt` 的使用方式存在疑问，这里更新一下测试方法， 借助了 `postman`，每次请求都是 `ajax` 方式，注意 `get/post` 等类型

1. 登录示例
-------

①注意是 `post` 请求，请求 `/login`，存好返回给你的 `jwtToken`，以后每次请求都要带上它

![](https://static.iocoder.cn/57b77e2d0e6420f589ce96ffb5efd531)

2. 访问内部示例
---------

① 注意任何请求要带上 `jwtToken`，不像之前的基于 `Session`，一登录成功就完事。

② 若还是请求失败，看 `RbacAuthorityService` 是否开放了该 `url` 。

![](https://static.iocoder.cn/28eab7b7e9919e56d53dec3da1b344e8)

实际开发中，没有必要引入 jjwt，token 存于 Redis 即可（自带有效期），性能更好。