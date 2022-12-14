package online.superh.springsecurity.jwt.security;

import com.alibaba.fastjson.JSON;
import online.superh.springsecurity.jwt.common.CommonResult;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: haro
 * @description: 访问拒绝拦截器
 * @date: 2022-12-14 13:37
 */

@Component
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        CommonResult result = new CommonResult();
        result.setStatus("300");
        result.setMsg("Need Authorities!");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
