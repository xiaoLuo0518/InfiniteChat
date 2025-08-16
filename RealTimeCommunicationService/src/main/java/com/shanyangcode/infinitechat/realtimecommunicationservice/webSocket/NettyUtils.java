package com.shanyangcode.infinitechat.realtimecommunicationservice.webSocket;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName NettyUtils
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/29 21:10
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyUtils {
    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static AttributeKey<String> UID = AttributeKey.valueOf("userUuid");
    public static AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    public static <T> void setAttr(Channel channel, AttributeKey<T> attributeKey, T data) {
        Attribute<T> attr = channel.attr(attributeKey);
        attr.set(data);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        return channel.attr(key).get();
    }
}
