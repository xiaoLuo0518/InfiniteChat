package com.shanyangcode.infinitechat.realtimecommunicationservice.webSocket;

import cn.hutool.json.JSONUtil;
import com.shanyangcode.infinitechat.realtimecommunicationservice.Utils.JwtUtil.JwtUtil;
import com.shanyangcode.infinitechat.realtimecommunicationservice.constants.MessageTypeEnum;
import com.shanyangcode.infinitechat.realtimecommunicationservice.constants.UserConstants;
import com.shanyangcode.infinitechat.realtimecommunicationservice.exception.MessageTypeException;
import com.shanyangcode.infinitechat.realtimecommunicationservice.model.AckData;
import com.shanyangcode.infinitechat.realtimecommunicationservice.model.LogOutData;
import com.shanyangcode.infinitechat.realtimecommunicationservice.model.MessageDTO;
import io.jsonwebtoken.Claims;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.InetAddress;


/**
 * @ClassName MessageInBoundHandler
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/29 10:40
 */
@Getter
@Slf4j
@ChannelHandler.Sharable
//处理管道消息
public class MessageInBoundHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final StringRedisTemplate redisTemplate;


    public MessageInBoundHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //长连接的消息
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) throws Exception {
        log.info("服务端收到了消息{}", msg.text());
        //将获得的消息反序列化为一个对象
        MessageDTO messageDTO = JSONUtil.toBean(msg.text(), MessageDTO.class);

        //获取消息的类型
        MessageTypeEnum typeEnum = MessageTypeEnum.of(messageDTO.getType());

        //ACK HEART_BEAT LOG_OUT
        switch (typeEnum) {
            case ACK:
                processACK(messageDTO);
                break;
            case LOG_OUT:
                processLogOut(channelHandlerContext, messageDTO);
                break;
            case HEART_BEAT:
                processHeartBeat(channelHandlerContext, messageDTO);
                break;
            case ILLEGAL:
                processIllegal(messageDTO);
                break;
        }
    }


    //处理客户端成功返回的消息
    private void processACK(MessageDTO msg) {
        AckData ackData = JSONUtil.toBean(msg.getData().toString(), AckData.class);
        log.info("ackData:{}", ackData);
        log.info("推送消息成功！");
    }

    //处理退出请求
    private void processLogOut(ChannelHandlerContext ctx, MessageDTO msg) {
        LogOutData logOutData = JSONUtil.toBean(msg.getData().toString(), LogOutData.class);
        Integer userUuid = logOutData.getUserUuid();
        log.info("请求断开用户{}的连接...", userUuid);
        offline(ctx);
        log.info("断开连接成功！");
    }

    //处理心跳请求
    private void processHeartBeat(ChannelHandlerContext ctx, MessageDTO msg) {
        log.info("收到心跳包");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(MessageTypeEnum.HEART_BEAT.getCode());
        TextWebSocketFrame frame = new TextWebSocketFrame(JSONUtil.toJsonStr(messageDTO));
        ctx.channel().writeAndFlush(frame);
    }

    //处理非法请求
    private void processIllegal(MessageDTO msg) {
        throw new MessageTypeException("不支持的消息格式！");
    }


    //管道打开的时候会自动调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("websocket has bean created.......");
    }

    //管道关闭的时候会自动调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        offline(ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //处理心跳
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                //读超时
                case READER_IDLE:
                    log.error("读空闲超时，关闭连接...{},用户id{}", ctx.channel().remoteAddress(), ChannelManager.getUserByChannel(ctx.channel()));
                    offline(ctx);
                    break;
                case WRITER_IDLE:
                case ALL_IDLE:
            }
        }

        //此时已经升级为websocket协议  在这里验证一下token是否能进行后续的操作
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //token的信息已经被NettyUtils贴到了管道中
            String token = NettyUtils.getAttr(ctx.channel(), NettyUtils.TOKEN);
            String userUuid = NettyUtils.getAttr(ctx.channel(), NettyUtils.UID);
            //对token进行验证，不通过直接关闭连接
            if (!validateToken(userUuid, token)) {
                log.info("token invalid");
                ctx.close();
            } else {
                //将登录信息放入到 redis，用户与 netty 服务器的映射
                redisTemplate.opsForValue().set(UserConstants.USER_SESSION + userUuid, InetAddress.getLocalHost().getHostAddress());

                //放置一个用户产生多个连接
                Channel channel = ChannelManager.getChannelByUserId(userUuid);
                if (channel != null) {
                    ChannelManager.removeUserChannel(userUuid);
                    ChannelManager.removeChannelUser(channel);
                    channel.close();
                }

                //再将新的channel放入到其中
                ChannelManager.addUserChannel(userUuid, ctx.channel());
                ChannelManager.addChannelUser(ctx.channel(), userUuid);
                log.info("客户端连接成功，用户id:{},客户端地址为:{}", userUuid, ctx.channel().remoteAddress());

            }


        }

    }

    //关闭连接
    private void offline(ChannelHandlerContext ctx) {
        String userUuid = ChannelManager.getUserByChannel(ctx.channel());
        try {
            ChannelManager.removeChannelUser(ctx.channel());
            if (userUuid != null) {
                ChannelManager.removeUserChannel(userUuid);
                log.info("客户端关闭连接UserId:{},客户端地址为:{}", userUuid, ctx.channel().remoteAddress());
            }
        } catch (Exception e) {
            log.error("处理退出登录异常", e);
        } finally {
            // 关闭通道
            if (ctx.channel() != null) {
                ctx.channel().close();
            }
            // 在redis中删除对应的key
            redisTemplate.opsForValue().getAndDelete(UserConstants.USER_SESSION + userUuid);
        }
    }


    private boolean validateToken(String userUuid, String token) {
        Claims claims = JwtUtil.parse(token);
        String userID = claims.getSubject();

        //校验不通过直接返回false
        return userID != null && userID.equals(userUuid);

    }


    //异常的捕获   只要你的 handler 所在 pipeline 中的某个 handler 抛出了异常，而这个异常没有被它自己处理掉，最终就会被传递到 exceptionCaught 来处理。
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("捕获到异常", cause);
        try {
            offline(ctx);
        } catch (Exception e) {
            log.error("关闭管道失败", e);
        }


    }

}