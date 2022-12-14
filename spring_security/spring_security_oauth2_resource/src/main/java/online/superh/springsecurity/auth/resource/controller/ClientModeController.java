package online.superh.springsecurity.auth.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description: 客户端模式
 * 客户端模式，指客户端以自己的名义，而不是以用户的名义，向授权服务器进行认证。
 * （A）客户端向授权服务器进行身份认证，并要求一个访问令牌。
 * （B）授权服务器确认无误后，向客户端提供访问令牌。
 * 例子：
 * 我们对接微信公众号时，就采用的客户端模式。我们的后端服务器就扮演“客户端”的角色，与微信公众号的后端服务器进行交互。
 * @date: 2022-12-14 12:41
 */
@RestController
public class ClientModeController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;

    @GetMapping("/clientlogin")
    public OAuth2AccessToken login() {
        // 创建 ClientCredentialsResourceDetails 对象
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
        // 创建 OAuth2RestTemplate 对象
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

}
