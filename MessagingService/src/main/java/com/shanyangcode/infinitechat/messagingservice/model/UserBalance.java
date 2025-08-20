package com.shanyangcode.infinitechat.messagingservice.model;

/**
 * @ClassName UserBalance
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 19:15
 */

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户余额表
 */
@Data
@TableName("user_balance")
@Accessors(chain = true)
public class UserBalance {

    /**
     * 用户ID
     */
    @TableId("user_id")
    private Long userId;

    /**
     * 余额
     */
    @TableField("balance")
    private BigDecimal balance;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
