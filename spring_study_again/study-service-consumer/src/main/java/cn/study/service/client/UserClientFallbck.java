package cn.study.service.client;

import cn.study.service.pojo.User;
import org.springframework.stereotype.Component;


@Component
public class UserClientFallbck implements UserClient{

    @Override
    public User queryUserById(Long id) {
        User user = new User();
        user.setUserName("服务器正忙，请稍后！！！");
        return user;
    }
}
