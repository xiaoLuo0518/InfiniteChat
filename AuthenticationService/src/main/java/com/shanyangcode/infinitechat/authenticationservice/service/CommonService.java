package com.shanyangcode.infinitechat.authenticationservice.service;

import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.sms.SMSResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl.UploadUrlResponse;

public interface CommonService {
    SMSResponse sendSms(SMSRequest smsRequest);
    UploadUrlResponse uploadUrl(UploadUrlRequest uploadUrlRequest);
}
