package com.shanyangcode.infinitechat.authenticationservice.factory;

import com.shanyangcode.infinitechat.authenticationservice.constants.verifyCode.VerifyCodeKeyStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName VerifyCodeKeyStrategyFactory
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/27 10:03
 */
public class VerifyCodeKeyStrategyFactory {
    private static final Map<String, String> strategies = new HashMap<>();

    static {
        strategies.put("register", VerifyCodeKeyStrategy.REGISTER.getVerifyCode());
        strategies.put("login", VerifyCodeKeyStrategy.LOGIN.getVerifyCode());
        strategies.put("reset_password", VerifyCodeKeyStrategy.RESET_PASSWORD.getVerifyCode()); // 其他场景
    }

    /**
     * 根据类型获取策略
     */
    public static String getStrategy(String type) {
        return strategies.get(type);
    }
}