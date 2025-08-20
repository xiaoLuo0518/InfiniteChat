package com.shanyangcode.infinitechat.messagingservice.constants;

import lombok.Getter;

/**
 * 红包状态枚举。
 */
@Getter
public enum RedPacketStatus {
    UNCLAIMED(1, "未领取完"),
    CLAIMED(2, "已领取完"),
    EXPIRED(3, "已过期");

    private final int status;
    private final String description;

    RedPacketStatus(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public static RedPacketStatus fromStatus(int status) {
        for (RedPacketStatus rs : RedPacketStatus.values()) {
            if (rs.getStatus() == status) {
                return rs;
            }
        }
        throw new IllegalArgumentException("未知的红包状态: " + status);
    }
}