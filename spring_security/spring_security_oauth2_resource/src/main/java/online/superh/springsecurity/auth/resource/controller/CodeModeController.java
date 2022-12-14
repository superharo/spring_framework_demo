package online.superh.springsecurity.auth.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description: 授权码模式
 * 授权码模式，是功能最完整、流程最严密的授权模式。它的特点就是通过客户端的后台服务器，与授权务器进行互动。
 * （A）用户访问客户端，后者将前者跳转到到授权服务器。
 * （B）用户选择是否给予客户端授权。
 * （C）假设用户给予授权，授权服务器将跳转到客户端事先指定的"重定向 URI"（Redirection URI），同时附上一个授权码。
 * （D）客户端收到授权码，附上早先的"重定向 URI"，向认证服务器申请令牌。这一步是在客户端的后台的服务器上完成的，对用户不可见。
 * （E）认证服务器核对了授权码和重定向 URI，确认无误后，向客户端发送访问令牌。
 * @date: 2022-12-14 11:38
 */
@RestController
public class CodeModeController {

    @Autowired
    private OAuth2ClientProperties oauth2ClientProperties;

    @Value("${security.oauth2.access-token-uri}")
    private String accessTokenUri;

    @GetMapping("/callback")
    public OAuth2AccessToken login(@RequestParam("code") String code) {
        // 创建 AuthorizationCodeResourceDetails 对象
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(oauth2ClientProperties.getClientId());
        resourceDetails.setClientSecret(oauth2ClientProperties.getClientSecret());
        // 创建 OAuth2RestTemplate 对象
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode(code); // <1> 设置 code
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setPreservedState("http://127.0.0.1:9090/callback"); // <2> 通过这个方式，设置 redirect_uri 参数
        restTemplate.setAccessTokenProvider(new AuthorizationCodeAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

}
