package online.superh.springsecurity.jwt.security;

import com.alibaba.fastjson.JSON;
import online.superh.springsecurity.jwt.common.CommonResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2022-12-14 13:50
 */
@Component
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        CommonResult result = new CommonResult();
        result.setStatus("100");
        result.setMsg("Logout Success!");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
