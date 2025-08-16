package com.shanyangcode.infinitechat.authenticationservice.utils;

import java.util.Random;

/**
 * @ClassName RandomNumUtil
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 11:20
 */
public class RandomNumUtil {
    public static String getRandomCode() {
        Random random = new Random();
        int num = random.nextInt(900000) + 100000;
        return String.format("%06d", num);
    }
}
