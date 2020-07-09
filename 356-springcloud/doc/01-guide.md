# 第1章 课程导学【先来了解下课程】

## 1-1 课程导学

SpringCloud：分布式、服务治理框架

> 本身不提供具体功能性的操作，更多关注服务之间的通讯、熔断、监控。因此需要很多的组件支撑这样一套功能

- Eureka：（核心组件）服务注册、发现
- Zuul：（服务网关）服务网关，提供统一的访问入口，分发客户端请求
- Feign：服务之间调用，HTTP请求
- Hystrix：容错机制、降级

微服务开发：多模块项目、编码设计、服务拆分

课程目标：

> 用户信息：用户基本信息（用户服务）、用户课程信息（课程服务）
>
> 用户服务调用课程服务