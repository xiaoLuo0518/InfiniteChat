package com.shanyangcode.infinitechat.messagingservice.feign;

import com.shanyangcode.infinitechat.messagingservice.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


//远程调用http请求
@FeignClient("ContactService")
public interface ContactServiceFeign {
    @GetMapping("/api/v1/contact/user")
    Result<?> getUser();
}
