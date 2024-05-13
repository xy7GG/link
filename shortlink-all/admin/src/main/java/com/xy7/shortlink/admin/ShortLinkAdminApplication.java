package com.xy7.shortlink.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xy7.shortlink.admin.dao.mapper")
@EnableFeignClients("com.xy7.shortlink.admin.remote")
public class ShortLinkAdminApplication {

    public static void main(String []args){
        SpringApplication.run(ShortLinkAdminApplication.class,args);
    }
}
