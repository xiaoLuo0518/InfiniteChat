package com.shanyangcode.infinitechat.messagingservice.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.constants.ConfigEnum;
import com.shanyangcode.infinitechat.messagingservice.constants.SessionType;
import com.shanyangcode.infinitechat.messagingservice.constants.UserConstants;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.AppMessage;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgRequest;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgResponse;
import com.shanyangcode.infinitechat.messagingservice.exception.ServiceException;
import com.shanyangcode.infinitechat.messagingservice.mapper.FriendMapper;
import com.shanyangcode.infinitechat.messagingservice.mapper.MessageMapper;
import com.shanyangcode.infinitechat.messagingservice.mapper.UserMapper;
import com.shanyangcode.infinitechat.messagingservice.model.Friend;
import com.shanyangcode.infinitechat.messagingservice.model.Message;
import com.shanyangcode.infinitechat.messagingservice.model.Session;
import com.shanyangcode.infinitechat.messagingservice.model.User;
import com.shanyangcode.infinitechat.messagingservice.service.MessageService;
import com.shanyangcode.infinitechat.messagingservice.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName MessageServiceImpl
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 17:48
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final long KEEP_ALIVE_TIME = 60L; // 60秒
    private static final int QUEUE_CAPACITY = 100;
    private static final int STATUS_ACTIVE = 1;
    private static final String DEFAULT_SESSION_AVATAR = "";
    private static final String TIME_ZONE_SHANGHAI = "Asia/Shanghai";

    private final UserServiceImpl userService;
    private final FriendMapper friendMapper;
    private final UserSessionService userSessionService;
    private final DiscoveryClient discoveryClient;
    private final UserMapper userMapper;
    private final SessionServiceimpl sessionService;
    private final StringRedisTemplate redisTemplate;
    private final OkHttpClient httpClient = new OkHttpClient();


    private final ThreadPoolExecutor groupMessageExecutor;

    @Autowired
    public MessageServiceImpl(UserServiceImpl userService, FriendMapper friendMapper, UserSessionService userSessionService, DiscoveryClient discoveryClient, UserMapper userMapper, SessionServiceimpl sessionService, StringRedisTemplate redisTemplate) {
        this.userService = userService;
        this.friendMapper = friendMapper;
        this.userSessionService = userSessionService;
        this.discoveryClient = discoveryClient;
        this.userMapper = userMapper;
        this.sessionService = sessionService;
        this.redisTemplate = redisTemplate;
        this.groupMessageExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        ;
    }

    @Override
    public SendMsgResponse sendMessage(SendMsgRequest sendMsgRequest) {
        //1.校验发送者
        validateSender(sendMsgRequest);
        //2.判断是群聊还是单聊，群聊的话获取用户名单
        List<Long> receiverIds = this.getReceiverUserIds(sendMsgRequest);

        //3.构建消息
        validateReceiver(receiverIds);
        AppMessage appMessage = this.buildMessage(sendMsgRequest, receiverIds);
        Long messageId = this.generateMessageId();
        Date created = new Date();

        //TODO:发送到kafka
        //TODO:转发给RealTimeCommunicationService
        appMessage.setMessageId(messageId);
        appMessage.setCreatedAt(formatDate(created));


        //4.通过redis查询接收者对应的websocket服务地址
        sendRealTimeMessage(sendMsgRequest, appMessage, created);
        return buildResponseMsgVo(appMessage);

    }

    private SendMsgResponse buildResponseMsgVo(AppMessage appMessage) {
        SendMsgResponse responseMsgVo = new SendMsgResponse();
        BeanUtils.copyProperties(appMessage, responseMsgVo);
        responseMsgVo.setSessionId(String.valueOf(appMessage.getSessionId()));
        responseMsgVo.setCreatedAt(appMessage.getCreatedAt());
        return responseMsgVo;
    }

    private void sendRealTimeMessage(SendMsgRequest sendMsgRequest, AppMessage appMessage, Date created) {
        //1.将消息转换成json格式
        String json = JSON.toJSONString(appMessage);
        String nettyServerIP = Objects.requireNonNull(redisTemplate.opsForValue().get(UserConstants.USER_SESSION + sendMsgRequest.getReceiverUserId())).toString();

        //2.构建http请求请求体
        RequestBody requestBody = RequestBody.create(
                MediaType.parse(ConfigEnum.MEDIA_TYPE.getValue()),
                json
        );

        //3.通过discoveryClient查找RealTimeCommunicationService实例
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("RealTimeCommunicationService");
        if (serviceInstances.isEmpty()) {
            {
                throw new ServiceException("没有可用的RealTimeCommunicationService实例");
            }
        }

        //4.根据会话类型转发消息
        if (Objects.equals(sendMsgRequest.getSessionType(), SessionType.SINGLE.getValue())) {
            sendSingleMessage(sendMsgRequest, requestBody, nettyServerIP);
        } else {
            sendGroupMessage(serviceInstances, requestBody);
        }


    }

    private void sendSingleMessage(SendMsgRequest sendMsgRequest, RequestBody requestBody, String nettyServerIP) {
        String receiveUserId = String.valueOf(sendMsgRequest.getReceiverUserId());
        try {
            if (nettyServerIP != null) {
                Request request = new Request.Builder()
                        .url("http://" + nettyServerIP + ":8083" + ConfigEnum.MSG_URL.getValue())
                        .post(requestBody)
                        .build();
                //发送请求
                executeHttpRequest(request);
            } else {
                log.info("接收者已下线: {}", receiveUserId);
            }
        } catch (Exception e) {
            log.error("发送单聊消息失败: {}", e.getMessage());
            throw new ServiceException("发送单聊消息失败");
        }
    }

    private void sendGroupMessage(List<ServiceInstance> serviceInstances, RequestBody requestBody) {
        for (ServiceInstance instance : serviceInstances) {
            groupMessageExecutor.submit(() -> {
                String url = instance.getUri().toString();
                Request request = new Request.Builder()
                        .url(url + ConfigEnum.MSG_URL.getValue())
                        .post(requestBody)
                        .build();
                try {
                    executeHttpRequest(request);
                    log.info("成功发送群聊消息到 {}", url);
                } catch (Exception e) {
                    log.error("发送群聊消息到 {} 失败: {}", url, e.getMessage());
                    // 根据需求，可以在此处添加重试机制或其他错误处理逻辑
                }
            });
        }
    }

    private void executeHttpRequest(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("HTTP请求失败: " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String responseString = responseBody.string();
                // 处理响应内容（根据业务需求）
                log.info("HTTP响应: {}", responseString);
            }
        }
    }


    //对日期进行格式调整
    private String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE_SHANGHAI));
        return formatter.format(date);
    }

    //构建发给netty的消息
    private AppMessage buildMessage(SendMsgRequest sendMsgRequest, List<Long> receiverIds) {
        AppMessage appMessage = new AppMessage();
        BeanUtils.copyProperties(sendMsgRequest, appMessage);
        appMessage.setBody(sendMsgRequest.getData());
        appMessage.setReceiveUserIds(receiverIds);

        User sendUser = userMapper.selectById(sendMsgRequest.getSendUserId());
        appMessage.setAvatar(sendUser.getAvatar());
        appMessage.setUserName(sendUser.getUserName());

        Session session = sessionService.getById(sendMsgRequest.getSessionId());
        log.info("会话id:{}", session.getId());
        log.info("会话信息:{}", session);
        //单聊逻辑
        if (appMessage.getSessionType().equals(SessionType.SINGLE.getValue())) {
            //单聊的话不用设置（群聊）头像和名称
            appMessage.setSessionAvatar(null);
            appMessage.setSessionName(null);

        } else {
            appMessage.setSessionName(DEFAULT_SESSION_AVATAR);
            appMessage.setSessionName(session.getName());
        }

        return appMessage;

    }

    //校验接收者的合法性
    private void validateReceiver(List<Long> receiverIds) {
        if (receiverIds == null || receiverIds.isEmpty()) {
            throw new ServiceException("接受者列表为空!");
        }
    }

    //校验发送者的合法性
    private void validateSender(SendMsgRequest sendMsgRequest) {
        User sendUser = userService.getById(sendMsgRequest.getSendUserId());
        log.info("发送者状态：{}", sendUser);
        if (sendUser == null || sendUser.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("发送者状态异常");
        }

    }


    private List<Long> getReceiverUserIds(SendMsgRequest sendMsgRequest) {
        List<Long> receiverIds = new ArrayList<>();
        int sessionType = sendMsgRequest.getSessionType();
        //单聊逻辑
        if (sessionType == SessionType.SINGLE.getValue()) {
            Long receiverId = sendMsgRequest.getReceiverUserId();
            receiverIds.add(receiverId);
            //校验好友关系
            validateSingleSession(sendMsgRequest.getSendUserId(), receiverId);
        }
        //群聊逻辑
        else {
            //通过sessionId(群聊id)找到群内所有的userId
            receiverIds.addAll(userSessionService.getUserIdsBySessionId(sendMsgRequest.getSessionId()));
            log.info("群聊接受者列表:{}", receiverIds);
            //将发送者从receiverIds中移除 不用给自己发消息
            boolean removed = receiverIds.remove(sendMsgRequest.getSendUserId());
            if (removed) {
                log.info("移除发送者后的消息列表：{}", receiverIds);
            } else {
                throw new ServiceException("发送者不在群聊内");
            }
        }

        return receiverIds;
    }

    //验证好友关系
    private void validateSingleSession(Long sendUserId, Long receiverId) {
        User receiverUser = userService.getById(receiverId);
        if (receiverUser == null || receiverUser.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("接收者" + receiverId + "状态异常");
        }

        Friend friend = friendMapper.selectFriendship(sendUserId, receiverId);

        log.info("发送者id:{},接收者id:{}", sendUserId, receiverId);

        if (friend == null || friend.getStatus() != STATUS_ACTIVE) {
            throw new ServiceException("发送者" + sendUserId + "接收者" + receiverId + "不是好友关系");
        }
    }


    //雪花算法生成消息唯一id
    private Long generateMessageId() {
        Snowflake snowflake = IdUtil.getSnowflake(
                Integer.parseInt(ConfigEnum.WORKED_ID.getValue()),
                Integer.parseInt(ConfigEnum.DATACENTER_ID.getValue())
        );
        return snowflake.nextId();
    }
}
