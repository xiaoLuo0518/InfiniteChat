package com.shanyangcode.infinitechat.messagingservice.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @ClassName Friend
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 20:24
 */
@Data
@Accessors(chain = true)
@TableName("friend")
public class Friend {
    @TableId
    private Long id;

    private Long userId;

    private Long friendId;

    //1:好友  2：拉黑  3.删除
    private Integer status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
