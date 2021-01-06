package com;

import net.unicon.cas.client.configuration.EnableCasClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCasClient
@MapperScan(basePackages="com.augurit.sys.mapper,com.augurit.tb.mapper")
@SpringBootApplication
public class RbacEntrance  {
    public static void main(String[] args) {
        SpringApplication.run(RbacEntrance.class);
        System.out.println("项目已启动...");
    }

}
