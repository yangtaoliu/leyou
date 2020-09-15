package cn.study.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/*
    不配置zuul参数，也可以通过服务名称+路径访问


 */
@SpringBootApplication
@EnableZuulProxy    //启用zuul网关组件
@EnableDiscoveryClient//启用eureka客户端
public class StudyZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyZuulApplication.class, args);
    }

}
