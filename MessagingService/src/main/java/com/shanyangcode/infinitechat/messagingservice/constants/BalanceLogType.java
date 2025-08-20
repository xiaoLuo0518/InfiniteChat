package com.shanyangcode.infinitechat.messagingservice.constants;

/**
 * @ClassName BalanceLogType
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 20:28
 */
public enum BalanceLogType {
    SEND_RED_PACKET(1, "发送红包"),
    RECEIVE_RED_PACKET(2, "领取红包"),
    REFUND_RED_PACKET(3, "红包退回");

    private final int type;
    private final String description;

    BalanceLogType(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
