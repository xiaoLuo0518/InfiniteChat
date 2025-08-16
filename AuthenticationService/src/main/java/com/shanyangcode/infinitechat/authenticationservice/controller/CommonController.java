package com.shanyangcode.infinitechat.authenticationservice.controller;

import com.shanyangcode.infinitechat.authenticationservice.common.Result;
import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;
import com.shanyangcode.infinitechat.authenticationservice.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName CommonController
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 10:57
 */


@RestController()
@RequestMapping("/api/v1/user/common")
public class CommonController {
    private final CommonService commonService;

    @Autowired
    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }


    @GetMapping("/sms")
    public Result<SMSResponse> sendSms(@RequestBody SMSRequest smsRequest) {
        SMSResponse smsResponse = commonService.sendSms(smsRequest);
        return Result.OK(smsResponse);
    }

    @GetMapping("/uploadUrl")
    public Result<UploadUrlResponse> getUploadUrl(@Valid UploadUrlRequest request) throws Exception {
        UploadUrlResponse response = commonService.uploadUrl(request);
        return Result.OK(response);
    }

}
