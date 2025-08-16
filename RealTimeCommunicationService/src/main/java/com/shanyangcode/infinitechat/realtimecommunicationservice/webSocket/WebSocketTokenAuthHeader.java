package com.shanyangcode.infinitechat.realtimecommunicationservice.webSocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Optional;

/**
 * @ClassName WebSocketTokenAuthHeader
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/29 21:15
 */
public class WebSocketTokenAuthHeader extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;

            //从http请求获取userUuid
            String userUuid = Optional.ofNullable(httpRequest.headers().get("userUuid"))
                    .map(CharSequence::toString)
                    .orElse("");
            //从http请求获取token
            String token = Optional.ofNullable(httpRequest.headers().get("token"))
                    .map(CharSequence::toString)
                    .orElse(" ");

            //将uid和token赋给channel
            NettyUtils.setAttr(ctx.channel(), NettyUtils.TOKEN, token);
            NettyUtils.setAttr(ctx.channel(), NettyUtils.UID, userUuid);

            ctx.pipeline().remove(this);
            ctx.fireChannelRead(httpRequest);

        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
