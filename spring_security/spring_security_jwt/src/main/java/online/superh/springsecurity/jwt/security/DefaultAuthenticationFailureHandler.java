package online.superh.springsecurity.jwt.security;

import com.alibaba.fastjson.JSON;
import online.superh.springsecurity.jwt.common.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 13:43
 */
@Component
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        CommonResult result = new CommonResult();
        result.setStatus("400");
        result.setMsg("Login Failure!");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
