package com.shanyangcode.infinitechat.messagingservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyangcode.infinitechat.messagingservice.model.Friend;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @ClassName FriendMapper
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/15 21:10
 */
public interface FriendMapper extends BaseMapper<Friend> {
    @Select("select * from friend where user_id=#{userId} and frien_id ==#{friendId} and status = 1")
    Friend selectFriendship(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
