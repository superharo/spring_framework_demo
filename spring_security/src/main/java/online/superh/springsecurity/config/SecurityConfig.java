package online.superh.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

/**
 * @version: 1.0
 * @author: haro
 * @description: 安全配置类
 * @date: 2022-12-13 17:53
 */
@Configuration
// 开启对 Spring Security 注解的方法，进行权限验证
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 重写 #configure(AuthenticationManagerBuilder auth) 方法，实现 AuthenticationManager 认证管理器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.//使用内存中的 InMemoryUserDetailsManager
                inMemoryAuthentication()
                //不使用 PasswordEncoder 密码编码器
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                //配置 admin 用户
                .withUser("admin").password("admin123").roles("ADMIN")
                //配置 normal 用户
                .and().withUser("normal").password("normal").roles("NORMAL");
    }

    /**
     * 们重写 #configure(HttpSecurity http) 方法，主要配置 URL 的权限控制
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http//配置请求地址的权限
                .authorizeRequests()
                // .antMatchers("/test/echo").permitAll() // 所有用户可访问
                // .antMatchers("/test/admin").hasRole("ADMIN") // 需要 ADMIN 角色
                // .antMatchers("/test/normal").access("hasRole('ROLE_NORMAL')") // 需要 NORMAL 角色。
                // 任何请求，访问的用户都需要经过认证
                .anyRequest().authenticated()
                .and()
                //设置 Form 表单登录
                .formLogin()
//                    .loginPage("/login") // 登录 URL 地址
                .permitAll() // 所有用户可访问
                .and()
                // 配置退出相关
                .logout()
//                    .logoutUrl("/logout") // 退出 URL 地址
                .permitAll(); // 所有用户可访问
    }
}
