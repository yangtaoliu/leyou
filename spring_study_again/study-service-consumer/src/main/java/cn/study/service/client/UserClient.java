package cn.study.service.client;

import cn.study.service.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-provider",fallback = UserClientFallbck.class)
public interface UserClient {
    @GetMapping("user/{id}")    //全局mapping拼在这里
    public User queryUserById(@PathVariable("id") Long id);
}

