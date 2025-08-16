package com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName UploadUrlResponse
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/27 11:11
 */
@Data
@Accessors(chain = true)
public class UploadUrlResponse {
    // 上传文件的地址
    public String uploadUrl;

    // 下载文件的地址
    public String downloadUrl;
}
