package com.shanyangcode.infinitechat.authenticationservice.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @ClassName Interceptors
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/28 16:39
 */
@Configuration
public class Interceptors implements WebMvcConfigurer {
    @Autowired
    private SourceHandler sourceHandler;

    @Autowired
    private JwtHandler jwtHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器
        registry.addInterceptor(sourceHandler)
                .addPathPatterns("/**");


        registry.addInterceptor(jwtHandler)
                .addPathPatterns(new ArrayList<String>(Arrays.asList("api/v1/user/avatar","")));
    }
}
