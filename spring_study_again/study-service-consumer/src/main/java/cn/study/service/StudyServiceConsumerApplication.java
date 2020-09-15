package cn.study.service;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication
//@EnableEurekaClient   可以使用这个注解，但是常使用下面这个
//@EnableDiscoveryClient     //客户端注册到eureka
//@EnableCircuitBreaker      //开启熔断器
@SpringCloudApplication      //组合注解，相当于上面3个的总和
@EnableFeignClients          //启用feign组件
public class StudyServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyServiceConsumerApplication.class, args);
    }

    //使用feign组件，不需要这个了
/*    @Bean
    @LoadBalanced   //开启负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }*/

}
