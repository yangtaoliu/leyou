package com.leyou.user.service;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

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
}
