// package online.superh.springsecurity.outh2.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.BeanIds;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
// import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//
// /**
//  * @version: 1.0
//  * @author: haro
//  * @description:
//  * @date: 2022-12-14 10:12
//  */
// // 因为客户端模式下，无需 Spring Security 提供用户的认证功能
// @Configuration
// @EnableWebSecurity
// public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//     @Override
//     @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
//     public AuthenticationManager authenticationManagerBean() throws Exception {
//         return super.authenticationManagerBean();
//     }
//
//     @Bean
//     public static NoOpPasswordEncoder passwordEncoder() {
//         return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
//     }
//
//     @Override
//     protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//         auth.
//                 // 使用内存中的 InMemoryUserDetailsManager
//                         inMemoryAuthentication()
//                 // 不使用 PasswordEncoder 密码编码器
//                 .passwordEncoder(passwordEncoder())
//                 // 配置 yunai 用户
//                 .withUser("admin").password("admin123").roles("ADMIN");
//     }
//
// }
