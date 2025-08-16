package com.shanyangcode.infinitechat.authenticationservice.service.impl;

import com.shanyangcode.infinitechat.authenticationservice.constants.config.OSSConstant;
import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;
import com.shanyangcode.infinitechat.authenticationservice.factory.VerifyCodeKeyStrategyFactory;
import com.shanyangcode.infinitechat.authenticationservice.service.CommonService;
import com.shanyangcode.infinitechat.authenticationservice.utils.OSSUtils;
import com.shanyangcode.infinitechat.authenticationservice.utils.RandomNumUtil;
import com.shanyangcode.infinitechat.authenticationservice.utils.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * @ClassName CommonService
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 11:12
 */
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OSSUtils oSSUtils;


    @Override
    public SMSResponse sendSms(SMSRequest smsRequest) {
        String type = smsRequest.getType();
        String phone = smsRequest.getPhone();
        String code = RandomNumUtil.getRandomCode();

        //将生成的验证码放入redis 并设置3分钟的过期时间
        redisTemplate.opsForValue().set(VerifyCodeKeyStrategyFactory.getStrategy(type) + phone,code,3, TimeUnit.MINUTES);

        //向用户手机发送验证码
        try {
            VerifyCodeUtil.sendSMS(code,phone);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new SMSResponse().setCode(code).setPhone(phone);

    }

    @Override
    public UploadUrlResponse uploadUrl(UploadUrlRequest request) {
        String fileName = request.getFileName();

        String uploadUrl = oSSUtils.uploadUrl(OSSConstant.BUCKET_NAME, fileName, OSSConstant.PICTURE_EXPIRE_TIME);
        String downUrl = oSSUtils.downUrl(OSSConstant.BUCKET_NAME, fileName);

        UploadUrlResponse response = new UploadUrlResponse();
        response.setUploadUrl(uploadUrl)
                .setDownloadUrl(downUrl);

        return response;
    }
}
