package cn.study.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//@EnableEurekaClient   可以使用这个注解，但是常使用下面这个
@EnableDiscoveryClient  //启用eureka客户端

@MapperScan("cn.study.service.mapper")  //mapper接口的包扫描，不需要再在接口上面写mapper注解了
public class StudyServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyServiceProviderApplication.class, args);
    }

}
