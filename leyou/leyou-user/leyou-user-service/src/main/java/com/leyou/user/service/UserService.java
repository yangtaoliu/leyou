package com.leyou.user.service;

import com.leyou.common.utils.NumberUtils;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String KEY_PREFIX = "user:verify";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 校验数据是否可用
     * @param data 数据
     * @param type 1 用户名 2电话号码
     * @return
     */
    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if(type == 1){
            user.setUsername(data);
        }else if(type == 2){
            user.setPhone(data);
        }else{
            return null;
        }
        return this.userMapper.selectCount(user) == 0;
    }

    public void verifyCode(String phone) {
        if(StringUtils.isBlank(phone)){
            return;
        }
        //生成验证码
        String code = NumberUtils.generateCode(6);
        //发送消息到rabbitmq
        Map<String, String > msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        this.amqpTemplate.convertAndSend("leyou.sms.exchange", "verifycode.sms", msg);
        //保存验证码到redis缓存中
        this.stringRedisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }

    public Boolean register(User user, String code) {
        if(!checkUser(user.getUsername(), 1)){
            return false;
        }
        if(!checkUser(user.getPhone(), 2)){
            return false;
        }
        String redisCode = this.stringRedisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        //1 校验验证码
        if(!StringUtils.equals(code, redisCode)){
            return false;
        }
        //2 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        //加盐加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        //新增用户
        user.setId(null);
        user.setCreated(new Date());

        this.userMapper.insertSelective(user);

        //删除redis缓存，可以提升内存效率
        this.stringRedisTemplate.delete(KEY_PREFIX + user.getPhone());
        return  true;
    }

    public User queryUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        User user = this.userMapper.selectOne(record);
        if(user == null){
            return null;
        }
        //获取盐，对输入的密码加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());
        if(!StringUtils.equals(password, user.getPassword())){
            return null;
        }
        return user;
    }
}
