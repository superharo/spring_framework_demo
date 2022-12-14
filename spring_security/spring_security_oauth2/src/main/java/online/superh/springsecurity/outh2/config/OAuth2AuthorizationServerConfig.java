package online.superh.springsecurity.outh2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 10:36
 */
@Configuration
// 声明开启 OAuth 授权服务器的功能
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // //因为客户端模式下，无需 Spring Security 提供用户的认证功能
    // /**
    //  * 用户认证 Manager
    //  */
    // @Autowired
    // private AuthenticationManager authenticationManager;
    //
    // /**
    //  * 配置使用的 AuthenticationManager 实现用户认证的功能
    //  * AuthenticationManager 是 Spring Security 的配置类
    //  * @param endpoints the endpoints configurer
    //  * @throws Exception
    //  */
    // @Override
    // public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //     endpoints.authenticationManager(authenticationManager);
    // }


    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    /**
     * 设置 /oauth/check_token 端点，通过认证后可访问
     * /oauth/check_token 端点对应 CheckTokenEndpoint 类，用于校验访问令牌的有效性
     * 这里的认证，指的是使用 client-id + client-secret 进行的客户端认证，不要和用户认证混淆
     * @param oauthServer a fluent configurer for security features
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.checkTokenAccess("isAuthenticated()");
    }

    /**
     * 进行 Client 客户端的配置。
     * 设置使用基于内存的 Client 存储器。实际情况下，最好放入数据库中，方便管理。
     * @param clients the client details configurer
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // <4.1>
                // 就是 client-id 和 client-secret
                .withClient("clientapp").secret("112233") // <4.2> Client 账号、密码。
                // OAuth2.0 定义了四种授权方式：
                // 授权码模式（Authorization Code）
                // 密码模式（Resource Owner Password Credentials）
                // 简化模式（Implicit）
                // 客户端模式（Client Credentials）
                // .authorizedGrantTypes("password") // <4.2> 密码模式
                // .authorizedGrantTypes("authorization_code")//授权模式
                // .redirectUris("http://127.0.0.1:9090/callback")//回调地址
                // .authorizedGrantTypes("implicit") // 简化模式
                // .redirectUris("http://127.0.0.1:9090/callback02")
                .authorizedGrantTypes("client_credentials") //客户端模式
                .scopes("read_userinfo", "read_contacts") // <4.2> 可授权的 Scope
//                .and().withClient() // <4.3> 可以继续配置新的 Client
        ;
    }

}
