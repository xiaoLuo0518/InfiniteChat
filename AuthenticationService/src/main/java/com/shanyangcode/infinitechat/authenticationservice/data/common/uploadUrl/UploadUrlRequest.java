package com.shanyangcode.infinitechat.authenticationservice.data.common.uploadUrl;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * @ClassName UploadUrlRequest
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/27 11:11
 */
@Data
@Accessors(chain = true)
public class UploadUrlRequest {
    @NotEmpty(message = "文件名称不能为空")
    private String fileName;
}
