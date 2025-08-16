package com.shanyangcode.infinitechat.authenticationservice.utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
/**
 * @ClassName VerifyCodeUtil
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/26 18:18
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
public class VerifyCodeUtil {
    public static void sendSMS(String code, String phone) throws Exception {
        String encodedCode = java.net.URLEncoder.encode(code, String.valueOf(StandardCharsets.UTF_8));
        String encodedPhone = java.net.URLEncoder.encode(phone, String.valueOf(StandardCharsets.UTF_8));
        String url = String.format("https://push.spug.cc/send/nbONk8gylaj34gXG?code=%s&targets=%s",
                encodedCode, encodedPhone);


        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        try {
            // 设置请求头和超时
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000); // 连接超时5秒
            con.setReadTimeout(5000);    // 读取超时5秒

            // 执行请求并获取响应码
            int responseCode = con.getResponseCode();

            // 处理成功响应
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {

                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    log.info("短信发送成功，响应: {}", response);
                }
            }
            // 处理错误响应
            else {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {

                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = errorReader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    throw new IOException("短信发送失败，状态码: " + responseCode + ", 详情: " + errorResponse);
                }
            }
        } finally {
            // 断开连接
            con.disconnect();
        }
    }
}