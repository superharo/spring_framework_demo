package online.superh.springsecurity.jwt.security;

import com.alibaba.fastjson.JSON;
import online.superh.springsecurity.jwt.common.CommonResult;
import online.superh.springsecurity.jwt.util.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 13:44
 */
@Component
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        CommonResult result = new CommonResult();
        result.setStatus("200");
        result.setMsg("Login Success!");
        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();
        System.out.println(userDetails.getUsername());
        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), 300, "_secret");
        result.setJwtToken(jwtToken);
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
