package com.shanyangcode.infinitechat.realtimecommunicationservice.webSocket;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ChannelManage
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/29 18:18
 */
public class ChannelManager {
    //用户id和管道的映射
    private static final ConcurrentHashMap<String, Channel> USER_CHANNEL_MAP = new ConcurrentHashMap<String, Channel>();
    private static final ConcurrentHashMap<Channel, String> CHANNEL_USER_MAP = new ConcurrentHashMap<Channel, String>();

    //添加一个用户和管道映射
    public static void addUserChannel(String userUuId, Channel channel) {
        USER_CHANNEL_MAP.put(userUuId, channel);
    }

    //删除一个用户和管道映射
    public static void removeUserChannel(String userUuId) {
        USER_CHANNEL_MAP.remove(userUuId);
    }

    //获得一个用户和管道映射
    public static Channel getChannelByUserId(String userUuId) {
        return USER_CHANNEL_MAP.get(userUuId);
    }

    //添加一个管道和用户映射
    public static void addChannelUser(Channel channel,String userUuId) {
        CHANNEL_USER_MAP.put(channel, userUuId);
    }

    //删除一个管道和用户映射
    public static void removeChannelUser(Channel channel) {
        CHANNEL_USER_MAP.remove(channel);
    }

    //获得一个用户和管道映射
    public static String getUserByChannel(Channel channel) {
        return CHANNEL_USER_MAP.get(channel);
    }


}
