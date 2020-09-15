package cn.study.springboot.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration//声明是一个java配置类，相当于一个xml文件
//@PropertySource("classpath:application.properties")
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcConfiguration {

    //@Autowired
    //private JdbcProperties jdbcProperties;

/*    public JdbcConfiguration(JdbcProperties jdbcProperties){
        this.jdbcProperties = jdbcProperties;
    }*/

/*    @Bean   //把方法的返回值注入到spring容器
    public DataSource dataSource(JdbcProperties jdbcProperties){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(this.jdbcProperties.getUrl());
        dataSource.setDriverClassName(this.jdbcProperties.getDriverClassName());
        dataSource.setUsername(this.jdbcProperties.getUsername());
        dataSource.setPassword(this.jdbcProperties.getPassword());

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(jdbcProperties.getUrl());
        dataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());

        return dataSource;
    }*/
   @Bean   //把方法的返回值注入到spring容器
   @ConfigurationProperties(prefix = "jdbc")
   public DataSource dataSource(JdbcProperties jdbcProperties){
       DruidDataSource dataSource = new DruidDataSource();
       return dataSource;
   }

}
