package com.shanyangcode.infinitechat.authenticationservice.utils;

import lombok.val;

import java.util.Random;

/**
 * @ClassName NickNameGenerateUtil
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/25 20:47
 */
public class NickNameGenerateUtil {
    static String[] adjs = {
            "粘人的", "聪明的", "胖乎乎的", "调皮的",
            "高冷的", "毛茸茸的", "圆滚滚的", "温顺的",
            "活泼的", "懒洋洋的", "机警的", "慢吞吞的",
            "威风的", "软萌的", "凶巴巴的", "乖巧的",
            "傻乎乎的", "敏捷的", "呆呆的", "傲娇的"
    };
    static String[] animals = {
            "狗", "猫", "老鼠", "兔子", "小熊",
            "狐狸", "老虎", "狮子", "熊猫", "仓鼠",
            "松鼠", "斑马", "长颈鹿", "企鹅", "猫头鹰",
            "海豚", "鲸鱼", "刺猬", "考拉", "袋鼠",
            "蜥蜴", "鹦鹉", "金鱼", "刺猬", "绵羊"
    };

    public static String generateNickName() {
        Random random = new Random();
        String adj = adjs[random.nextInt(adjs.length)];
        String animal = animals[random.nextInt(animals.length)];
        return adj + animal;
    }
}
