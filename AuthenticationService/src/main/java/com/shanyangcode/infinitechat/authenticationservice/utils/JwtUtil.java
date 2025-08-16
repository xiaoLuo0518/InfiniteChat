package com.shanyangcode.infinitechat.authenticationservice.utils;

import com.shanyangcode.infinitechat.authenticationservice.constants.config.ConfigEnum;
import com.shanyangcode.infinitechat.authenticationservice.constants.config.TimeOutEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;


import java.time.Duration;
import java.util.Date;

/**
 * @ClassName JwtUtil
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 21:10
 */
public class JwtUtil {
    private final static Duration expiration = Duration.ofHours(TimeOutEnum.JWT_TIME_OUT.getTimeOut());
    //生成jwt
    public static String generate(Long userId) {
        Date expireDate = new Date(System.currentTimeMillis() + expiration.toMillis());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, ConfigEnum.TOKEN_SECRET_KEY.getText())
                .compact();
    }

    // 解析jwt
    public static Claims parse(String token) throws JwtException {
        if (StringUtils.isEmpty(token)){
            throw new JwtException("token 为空");
        }

        // 这个Claims对象包含了许多属性，比如签发时间、过期时间以及存放的数据等
        Claims claims = null;
        // 解析失败了会抛出异常，所以我们要捕捉一下。token过期、token非法都会导致解析失败
        claims = Jwts.parser()
                .setSigningKey(ConfigEnum.TOKEN_SECRET_KEY.getText()) // 设置秘钥
                .parseClaimsJws(token)
                .getBody();

        return claims;
    }

}
