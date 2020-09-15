package cn.study.service.controller;

import cn.study.service.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/*
circuitBreaker.requestVolumeThreshold=10
circuitBreaker.sleepWindowInMilliseconds=10000
circuitBreaker.errorThresholdPercentage=50

- requestVolumeThreshold：触发熔断的最小请求次数，默认20
- errorThresholdPercentage：触发熔断的失败请求最小占比，默认50%
- sleepWindowInMilliseconds：休眠时长，默认是5000毫秒






 */
@Controller
@RequestMapping("consumer/user")
//@DefaultProperties(defaultFallback = "queryUserByIdFallback")
public class UserController {
    //使用feign组件，不需要这个了
/*    @Autowired
    private RestTemplate restTemplate;*/

    @Autowired
    private UserClient userClient;

/*    @Autowired
    private DiscoveryClient discoveryClient;    */      //包含了拉取的所有服务信息

//@HystrixCommand(fallbackMethod = "queryUserByIdFallback")   //改成全局方法，整个类中的方法都使用,如果写上，可以使用方法
                                                              // 上面的，并且参数要和原来的方法一致，优先级比全局高

    @GetMapping
    @ResponseBody
    //@HystrixCommand     //每个需要熔断的方法都要加上注解     全局熔断也要写上，否则不熔断，如果写上具体方法，则会不使用全局的而使用自己写的
    public String queryUserById(@RequestParam("id")Long id){
/*        List<ServiceInstance> instances = this.discoveryClient.getInstances("service-provider");

        ServiceInstance serviceInstance = instances.get(0);//服务实例

        return this.restTemplate.getForObject("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/user/" + id, User.class);
        */
/*        if(id == 1){
            throw new RuntimeException("太忙了");
        }*/
        //return this.restTemplate.getForObject("http://service-provider/user/" + id, String.class);

        return this.userClient.queryUserById(id).toString();

    }

/*    //全局熔断方法不带参数，局部的要和被熔断的参数保持一致
    public String queryUserByIdFallback(){
        return "服务器正忙，请稍后重试！！！！！！ ";
    }*/
}
