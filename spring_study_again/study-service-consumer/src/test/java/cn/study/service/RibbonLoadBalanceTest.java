package cn.study.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = StudyServiceConsumerApplication.class)//可以指定引导类，也可以省略，默认本模块下的引导类
@RunWith(SpringRunner.class)
public class RibbonLoadBalanceTest {
    @Autowired
    private RibbonLoadBalancerClient ribbonLoadBalancerClient;

    @Test
    public void test(){
        for (int i = 0; i < 50; i++) {
            ServiceInstance instance = this.ribbonLoadBalancerClient.choose("service-provider");
            System.out.println(instance.getHost() + ":" + instance.getPort());
        }
    }
}
