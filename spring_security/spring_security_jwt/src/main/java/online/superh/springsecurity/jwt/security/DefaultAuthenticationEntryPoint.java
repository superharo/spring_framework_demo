package online.superh.springsecurity.jwt.security;

import com.alibaba.fastjson.JSON;
import online.superh.springsecurity.jwt.common.CommonResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 13:42
 */
@Component
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        CommonResult result = new CommonResult();
        result.setStatus("000");
        result.setMsg("Need Authorities!");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
