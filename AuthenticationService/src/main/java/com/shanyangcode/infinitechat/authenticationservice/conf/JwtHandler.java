package com.shanyangcode.infinitechat.authenticationservice.conf;

import com.alibaba.fastjson.JSON;
import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName Jwt
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/28 16:59
 */
@Component
public class JwtHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            refuseResult(response);
            return false;
        }

        return true;
    }

    public void refuseResult(HttpServletResponse httpServletResponse) throws Exception{
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        Result<Object> result = new Result<>().setCode(40101).setMessage("签名验证失败");
        httpServletResponse.getWriter().print(JSON.toJSONString(result));
        httpServletResponse.getWriter().flush();
    }
}

