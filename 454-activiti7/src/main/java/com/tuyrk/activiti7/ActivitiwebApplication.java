package com.tuyrk.activiti7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivitiwebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivitiwebApplication.class, args);
    }
}


/// war包打包方式
/*import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ActivitiwebApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ActivitiwebApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ActivitiwebApplication.class, args);
    }
}*/
