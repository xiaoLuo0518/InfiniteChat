package com.shanyangcode.infinitechat.contactservice.data.User;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @ClassName User
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 16:35
 */
@Data
@Accessors(chain = true)
public class User {
    private String name;
}
