package com.shanyangcode.infinitechat.messagingservice.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.messagingservice.constants.BalanceLogType;
import com.shanyangcode.infinitechat.messagingservice.constants.RedPacketConstants;
import com.shanyangcode.infinitechat.messagingservice.constants.RedPacketStatus;
import com.shanyangcode.infinitechat.messagingservice.constants.SessionType;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.RedPacketMessageBody;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketRequest;
import com.shanyangcode.infinitechat.messagingservice.data.SendRedPacket.SendRedPacketResponse;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgRequest;
import com.shanyangcode.infinitechat.messagingservice.data.sendMsg.SendMsgResponse;
import com.shanyangcode.infinitechat.messagingservice.exception.ServiceException;
import com.shanyangcode.infinitechat.messagingservice.mapper.BalanceLogMapper;
import com.shanyangcode.infinitechat.messagingservice.mapper.RedPacketMapper;
import com.shanyangcode.infinitechat.messagingservice.mapper.UserBalanceMapper;
import com.shanyangcode.infinitechat.messagingservice.mapper.UserMapper;
import com.shanyangcode.infinitechat.messagingservice.model.BalanceLog;
import com.shanyangcode.infinitechat.messagingservice.model.RedPacket;
import com.shanyangcode.infinitechat.messagingservice.model.UserBalance;
import com.shanyangcode.infinitechat.messagingservice.service.RedPacketService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * @ClassName RedPacketServiceImpl
 * @Description
 * @Author XiaoLuo
 * @Date 2025/8/19 18:38
 */
@Service
@Transactional
public class RedPacketServiceImpl extends ServiceImpl<RedPacketMapper, RedPacket> implements RedPacketService {

    private final UserServiceImpl userServiceImpl;
    private final UserBalanceMapper userBalanceMapper;
    private final BalanceLogMapper balanceLogmapper;
    private final RedPacketMapper redPacketMapper;
    private final MessageServiceImpl messageService;
    private final StringRedisTemplate stringRedisTemplate;
    private final Snowflake snowflake;

    @Autowired
    public RedPacketServiceImpl(UserMapper userMapper, UserServiceImpl userServiceImpl, UserBalanceMapper userBalanceMapper, BalanceLogMapper balanceLogmapper, RedPacketMapper redPacketMapper, MessageServiceImpl messageService, StringRedisTemplate stringRedisTemplate) {
        this.userServiceImpl = userServiceImpl;
        this.userBalanceMapper = userBalanceMapper;
        this.balanceLogmapper = balanceLogmapper;
        this.redPacketMapper = redPacketMapper;
        this.messageService = messageService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.snowflake = IdUtil.getSnowflake(
                Integer.parseInt(RedPacketConstants.WORKED_ID.getValue()),
                Integer.parseInt(RedPacketConstants.DATACENTER_ID.getDateTimeFormat())
        );
    }

    //发送红包消息
    @Override
    public SendRedPacketResponse sendRedPacket(SendRedPacketRequest sendRedPacketRequest) {
        //1.提取并验证参数
        SendRedPacketRequest.Body body = sendRedPacketRequest.getBody();
        Long senderId = sendRedPacketRequest.getSendUserId();
        BigDecimal totalAmount = body.getTotalAmount();
        int totalCount = body.getTotalCount();
        int redPacketType = body.getRedPacketType();
        //验证红包参数
        this.validateRedPacket(sendRedPacketRequest);
        //验证并扣除用户余额
        this.deductUserAmount(getUserBalance(senderId), totalAmount);

        //创建红包记录
        RedPacket redPacket = createRedPacket(senderId, sendRedPacketRequest, body, totalAmount, totalCount, redPacketType);

        //创建余额变动记录
        BalanceLog balanceLog = createBalanceLog(senderId, totalAmount, redPacket.getRedPacketId());

        //发送红包
        SendMsgResponse sendMsgResponse = sendRedPacketMessage(sendRedPacketRequest, redPacket);


        return null;
    }

    //对接消息模块
    private SendMsgResponse sendRedPacketMessage(SendRedPacketRequest sendRedPacketRequest, RedPacket redPacket) {
        SendMsgRequest sendMsgRequest = new SendMsgRequest();
        BeanUtils.copyProperties(sendRedPacketRequest, sendMsgRequest);

        //构建redPacketMessageBody
        RedPacketMessageBody redPacketMessageBody = new RedPacketMessageBody();
        redPacketMessageBody.setContent(redPacket.getRedPacketId().toString());
        redPacketMessageBody.setRedPacketWrapperText(redPacket.getRedPacketWrapperText());

        sendMsgRequest.setData(redPacketMessageBody);

    }

    //创建余额变动记录
    private BalanceLog createBalanceLog(Long senderId, BigDecimal totalAmount, Long redPacketId) {
        BalanceLog balanceLog = new BalanceLog();
        balanceLog.setBalanceLogId(generateId());
        balanceLog.setUserId(senderId);
        balanceLog.setAmount(totalAmount.negate());
        balanceLog.setType(BalanceLogType.SEND_RED_PACKET.getType());
        balanceLog.setRelatedId(redPacketId);
        balanceLog.setCreatedAt(LocalDateTime.now());

        balanceLogmapper.insert(balanceLog);

        return balanceLog;
    }

    //创建红包记录
    private RedPacket createRedPacket(Long senderId, SendRedPacketRequest sendRedPacketRequest, SendRedPacketRequest.Body body, BigDecimal totalAmount, int totalCount, int redPacketType) {
        RedPacket redPacket = new RedPacket();
        redPacket.setRedPacketId(generateId());
        redPacket.setSenderId(senderId);
        redPacket.setSessionId(sendRedPacketRequest.getSessionId());
        redPacket.setRedPacketType(redPacketType);
        // 若红包封面文案是否为空或为null则设置默认祝福语
        String text = body.getRedPacketWrapperText();
        if (text == null || text.trim().isEmpty()) {
            redPacket.setRedPacketWrapperText("恭喜发财，大吉大利");
        } else {
            redPacket.setRedPacketWrapperText(text);
        }
        redPacket.setTotalAmount(totalAmount);
        redPacket.setTotalCount(totalCount);
        redPacket.setRemainingAmount(totalAmount);
        redPacket.setRemainingCount(totalCount);
        redPacket.setStatus(RedPacketStatus.UNCLAIMED.getStatus());
        redPacket.setCreatedAt(LocalDateTime.now());

        //保存到数据库
        redPacketMapper.insert(redPacket);
        return redPacket;
    }

    private UserBalance getUserBalance(Long userId) {
        UserBalance userBalance = userBalanceMapper.selectById(userId);
        if (userBalance == null) {
            throw new ServiceException("用户余额表有误!");
        }
        return userBalance;
    }

    //验证用户余额并扣减
    private void deductUserAmount(UserBalance userBalance, BigDecimal totalAmount) {
        //获得用户余额-红包金额
        BigDecimal userNewTotalAmount = userBalance.getBalance().subtract(totalAmount);
        if (userNewTotalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("用户余额不足");
        }
        //更新用户余额表
        userBalance.setBalance(userNewTotalAmount);
        int count = userBalanceMapper.updateById(userBalance);
        if (count != 1) {
            throw new ServiceException("余额扣减失败");
        }
    }


    //验证红包参数
    private void validateRedPacket(SendRedPacketRequest request) {
        SendRedPacketRequest.Body body = request.getBody();

        if (body == null) {
            throw new ServiceException("请求体不能为空");
        }

        //提取参数
        BigDecimal totalAmount = body.getTotalAmount();
        int totalCount = body.getTotalCount();
        int redPacketType = body.getRedPacketType();
        int sessionType = request.getSessionType();


        if (sessionType == SessionType.SINGLE.getValue()) {
            // 单聊红包校验
            if (totalAmount == null || totalAmount.compareTo(RedPacketConstants.MAX_AMOUNT_PER_PACKET.getBigDecimalValue()) > 0) {
                throw new ServiceException("红包总金额不能大于200元");
            }
            if (totalAmount.compareTo(RedPacketConstants.MIN_AMOUNT.getBigDecimalValue()) < 0) {
                throw new ServiceException("红包总金额不能低于0.01元");
            }
        } else if (sessionType == SessionType.GROUP.getValue()) {
            // 群聊红包校验
            //1.红包得够分 不能出现0.02元三个人分的情况
            BigDecimal minRedPacketAmount = totalAmount.divide(
                    BigDecimal.valueOf(totalCount),
                    RedPacketConstants.DIVIDE_SCALE.getIntValue(),
                    RoundingMode.DOWN
            );
            if (minRedPacketAmount.compareTo(RedPacketConstants.MIN_AMOUNT.getBigDecimalValue()) < 0) {
                throw new ServiceException("单个红包金额不能低于0.01元");
            }
            //2.单个红包不能高于200元(红包数量*单个红包最大值200<=totalAmount)
            BigDecimal maxRedPacketAmount = RedPacketConstants.MAX_AMOUNT_PER_PACKET.getBigDecimalValue()
                    .multiply(BigDecimal.valueOf(totalCount));
            if (maxRedPacketAmount.compareTo(totalAmount) < 0) {
                throw new ServiceException("单个红包金额不能超过200元");
            }
        }
        if (!RedPacketConstants.RED_PACKET_TYPE_NORMAL.getIntValue().equals(redPacketType) &&
                !RedPacketConstants.RED_PACKET_TYPE_RANDOM.getIntValue().equals(redPacketType)) {
            throw new ServiceException("无效的红包类型");
        }

    }


    //处理过期红包
    @Override
    public void handlerExpiredRedPacked(Long redPackedId) {

    }


    //雪花算法生成唯一id
    public Long generateId() {
        return snowflake.nextId();
    }
}
