package online.superh.springsecurity.auth.resource.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: 1.0
 * @author: haro
 * @description: 简化模式
 * 简化模式，不通过第三方应用程序的服务器，直接在浏览器中向授权服务器申请令牌，跳过了“授权码”这个步骤，因此得名。所有步骤在浏览器中完成，令牌对访问者是可见的，且客户端不需要授权。
 * （A）用户访问客户端，后者将前者跳转到到授权服务器。
 * （B）用户选择是否给予客户端授权。
 * （C）假设用户给予授权，授权服务器将用户导向客户端指定的"重定向URI"，并在 URI 的 Hash 部分包含了访问令牌。
 * （D）浏览器向资源服务器发出请求，其中不包括上一步收到的 Hash 值。
 * （E）资源服务器返回一个网页，其中包含的代码可以获取 Hash 值中的令牌。
 * （F）浏览器执行上一步获得的脚本，提取出令牌。
 * （G）浏览器将令牌发给客户端。
 * @date: 2022-12-14 11:58
 */
@RestController
public class SimpleModeController {

    @GetMapping("/callback02")
    public String callback02() {
        return "callback02";
    }

}
