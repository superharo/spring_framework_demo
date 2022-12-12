// package online.superh.springboot.actuator.conf;
//
// import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
// /**
//  * @version: 1.0
//  * @author: haro
//  * @description: 安全认证配置类
//  * @date: 2022-12-12 14:13
//  */
// @Configuration
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//     @Override
//     protected void configure(HttpSecurity http) throws Exception {
//         // <1> 访问 EndPoint 地址，需要经过认证，并且拥有 ADMIN 角色
//         http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests((requests) ->
//                 requests.anyRequest().hasRole("ADMIN"));
//         // <2> 开启 Basic Auth
//         http.httpBasic();
//     }
//
// }