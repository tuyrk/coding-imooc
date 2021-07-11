package com.tuyrk.activiti7.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 静态资源路径配置
 */
@Configuration
public class PathMapping implements WebMvcConfigurer {
    @Value("${activiti7.bpmn-path-mapping}")
    private String bpmnPathMapping;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // String[] resourceLocation = new String[]{"f=-le:D:\\WangJianIDEA_Test\\activiti-imooc\\src\\main\\resources\\resources\\bpmn\\", "classpath:/resources/"};
        // 默认也有这个路径映射
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/");
        registry.addResourceHandler("/bpmn/**").addResourceLocations(bpmnPathMapping);
    }
}
