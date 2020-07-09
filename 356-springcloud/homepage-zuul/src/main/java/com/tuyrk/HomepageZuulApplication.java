package com.tuyrk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关应用程序
 * EnableProxy：标识当前应用是 Zuul Server
 * SpringCloudApplication：用于简化配置的组合注解
 *
 * @author tuyrk
 */
@EnableZuulProxy
@SpringCloudApplication
public class HomepageZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(HomepageZuulApplication.class, args);
    }
}
