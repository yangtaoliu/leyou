package cn.study.service.service;

import cn.study.service.mapper.Usermapper;
import cn.study.service.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private Usermapper usermapper;

    public User queryUserById(Long id){
        return this.usermapper.selectByPrimaryKey(id);
    }
}
