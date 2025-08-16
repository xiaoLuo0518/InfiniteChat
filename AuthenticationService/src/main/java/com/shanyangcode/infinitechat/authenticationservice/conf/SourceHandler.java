package com.shanyangcode.infinitechat.authenticationservice.conf;

import com.alibaba.fastjson.JSON;
import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @ClassName SouceHandler
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/28 16:43
 */
@Component
public class SourceHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("X-Request-Source");
        if (!Objects.equals(header, "InfiniteChat-GateWay")) {
            refuseResult(response);
            return false;
        }
        return true;
    }

    //拒绝后的返回体
    private void refuseResult(HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        Result<Object> result = new Result<>().setCode(40301).setMessage("非法请求");
        response.getWriter().print(JSON.toJSONString(result));
        response.getWriter().flush();


    }


}
