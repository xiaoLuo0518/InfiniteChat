package com.shanyangcode.infinitechat.authenticationservice.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyangcode.infinitechat.authenticationservice.constants.config.ConfigEnum;
import com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum;
import com.shanyangcode.infinitechat.authenticationservice.constants.user.Register;
import com.shanyangcode.infinitechat.authenticationservice.constants.verifyCode.VerifyCodeKeyStrategy;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginCodeResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.login.LoginResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.register.RegisterResponse;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarRequest;
import com.shanyangcode.infinitechat.authenticationservice.data.user.updateAvatar.UpdateAvatarResponse;
import com.shanyangcode.infinitechat.authenticationservice.exception.CodeException;
import com.shanyangcode.infinitechat.authenticationservice.exception.DataBaseException;
import com.shanyangcode.infinitechat.authenticationservice.exception.LoginException;
import com.shanyangcode.infinitechat.authenticationservice.exception.UserException;
import com.shanyangcode.infinitechat.authenticationservice.mapper.UserMapper;
import com.shanyangcode.infinitechat.authenticationservice.model.User;
import com.shanyangcode.infinitechat.authenticationservice.service.UserService;
import com.shanyangcode.infinitechat.authenticationservice.utils.JwtUtil;
import com.shanyangcode.infinitechat.authenticationservice.utils.NickNameGenerateUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum.LOGIN_PASSWORD_ERROR;
import static com.shanyangcode.infinitechat.authenticationservice.constants.user.ErrorEnum.LOGIN_USER_NOT_EXIST;

/**
 * @author 雒勇涛
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-07-25 10:25:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 用户注册方法
     */
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        String phone = registerRequest.getPhone();
        String password = registerRequest.getPassword();

        //1.首先查询该手机号是否已经存在
        boolean isRegister = this.isRegister(phone);
        log.info("{}", isRegister);
        if (isRegister) {
            throw new UserException(ErrorEnum.REGISTER_ERROR);
        }


        //拼接key   register：code：135*****
        //获取redis中register：code：135*****对应的value
        String redisCode = redisTemplate.opsForValue().get(Register.REGISTER_CODE + phone);
        if (redisCode == null || !redisCode.equals(registerRequest.getCode())) {
            throw new CodeException(ErrorEnum.Code_ERROR);
        }
        //相等的话就存数据库
        //雪花算法生成唯一ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        //用户密码存储方式   1.明文存储   2.加密存储
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = new User()
                .setUserId(snowflake.nextId())
                .setPassword(encryptedPassword)
                .setPhone(phone)
                .setUserName(NickNameGenerateUtil.generateNickName());

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new DataBaseException("数据库异常:注册失败！");
        }


        return new RegisterResponse().setPhone(phone);
    }

    @Override
    public LoginResponse loginByPassword(LoginRequest loginRequest) {

        String phone = loginRequest.getPhone();


        //判斷用戶是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = this.getOne(queryWrapper, true);

        if (user == null) {
            throw new UserException("用户不存在", LOGIN_USER_NOT_EXIST.getCode());
        }

        //获取加密形式的密码  方便与数据库中的密码进行比对
        String password = DigestUtils.md5DigestAsHex(loginRequest.getPassword().getBytes());
        if (user.getPassword().equals(password)) {
            throw new UserException(LOGIN_PASSWORD_ERROR.getMessage(), LOGIN_PASSWORD_ERROR.getCode());
        }

        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(user, loginResponse);

        //token,session,jwt
        //jwt  json web token
        //header payload signature
        String token = JwtUtil.generate(user.getUserId());
        loginResponse.setToken(token);


        Long userId = loginResponse.getUserId();
        saveToken(userId, token);

        return loginResponse;

    }

    @Override
    public LoginCodeResponse loginByVerifyCode(LoginCodeRequest loginCodeRequest) {
        String phone = loginCodeRequest.getPhone();
        String redisCode = redisTemplate.opsForValue().get(VerifyCodeKeyStrategy.LOGIN.getVerifyCode() + phone);
        if (redisCode == null || !redisCode.equals(loginCodeRequest.getCode())) {
            throw new CodeException(ErrorEnum.Code_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        User user = this.getOnly(queryWrapper, true);
        if (user == null) {
            throw new LoginException(LOGIN_USER_NOT_EXIST.getMessage(), LOGIN_USER_NOT_EXIST.getCode());
        }

        LoginCodeResponse loginCodeResponse = new LoginCodeResponse();
        BeanUtils.copyProperties(user, loginCodeResponse);

        //token,session,jwt
        //jwt  json web token
        //header payload signature
        String token = JwtUtil.generate(user.getUserId());
        loginCodeResponse.setToken(token);


        Long userId = loginCodeResponse.getUserId();
        saveToken(userId, token);


        return loginCodeResponse;

    }

    //将redis保存到redis   userId:token:xxxxxxxxxxx
    private void saveToken(Long userId, String token) {
        // 解析 JWT，拿到 exp（秒级时间戳）
        Claims claims = Jwts.parser()
                .setSigningKey(ConfigEnum.TOKEN_SECRET_KEY.getText())
                .parseClaimsJws(token)
                .getBody();
        String key = "user:token:" + userId;
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expireAt(key, claims.getExpiration());
    }

    @Override
    public UpdateAvatarResponse updateAvatar(String id, UpdateAvatarRequest request) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", Long.valueOf(id));
        User user = this.getOnly(queryWrapper, true);
        if (user == null) {
            throw new UserException(ErrorEnum.SYSTEM_ERROR);
        }

        user.setAvatar(request.avatarUrl);

        boolean isUpdate = this.updateById(user);

        if (!isUpdate) {
            throw new UserException(ErrorEnum.UPDATE_AVATAR_ERROR);
        }


        UpdateAvatarResponse updateAvatarResponse = new UpdateAvatarResponse();
        BeanUtils.copyProperties(user, updateAvatarResponse);

        return updateAvatarResponse;
    }


    //手机号是否已经注册
    private boolean isRegister(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        //返回查询到的数量
        long count = this.count(queryWrapper);

        //2.查询到记录，说明该手机号已经存在
        return count > 0;
    }

}




