package online.superh.springsecurity.auth.resource.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 11:17
 */
@Configuration
//开启 OAuth 资源服务器的功能
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 设置 /login 无需权限访问
                .antMatchers("/login").permitAll()
                // 设置 /callback 无需权限访问
                .antMatchers("/callback").permitAll()
                // 设置 /callback02 无需权限访问
                .antMatchers("/callback02").permitAll()
                // 设置 /callback02 无需权限访问
                .antMatchers("/clientlogin").permitAll()
                // 设置其它请求，需要认证后访问
                .anyRequest().authenticated()
        ;
    }

}
