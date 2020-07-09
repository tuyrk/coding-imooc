# 第2章 SpringCloud框架预览【框架给我们提供了什么】

##  2-1 Eureka 和 Zuul 的介绍

### SpringCloud Eureka

> Eureka包含两个组件：Eureka Server和Eureka Client

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ge6dsp92rdj30m807djum.jpg" alt="" style="zoom:80%;" />

```java
ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>
```

第一个String：表示应用的名称

第二个String：实例的名称（每一个微服务可以部署多个实例）

Lease：保存实例的信息

### SpringCloud Zuul

> Zuul是一个API Gateway服务器，本质上是一个Web Servlet应用
>
> Zuul提供了动态路由、监控等服务，这些功能实现的核心是一系列的filters

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ge6dt7ujxfj30e207ijtd.jpg" alt="" style="zoom:80%;" />

1. pre filters：在请求被路由之前调用，利用这种过滤器实现身份验证、在集群中选择请求的微服务等
2. routing filters：构建发送给微服务的一个请求，请求对应的微服务，微服务的响应返回给routing filters
3. post filters：路由到微服务之后执行，为响应添加标准的HTTP头，把响应发送给客户端
4. custom filters：自定义过滤器，实现相应的功能
5. error filters：HTTP请求执行过程中，随时调用，发生错误时执行的过滤器

##  2-2 Feign 和 Hystrix 的介绍

> Ribbon和Feign存在的目的是什么？
>
> 答：解决微服务之间相互调用的一些问题

### SpringCloud Ribbon

Ribbon包括了两个部分：负载均衡算法+app_name转具体的ip:port

每一个微服务都会部署多个实例，在调用的时候会怎样选择实例

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ge6dto6xzlj30hs08awgz.jpg" alt="" style="zoom:80%;" />

### SpringCloud Feign

定义接口，并在接口上添加注解，消费者通过调用接口的形式进行服务消费

Feign依赖于Ribbon，还整合了Hystrix，实现熔断降级的功能

### SpringCloud Hystrix

服务雪崩是熔断器解决的最核心问题

1. 在微服务架构中，通常会有多个服务层级的调用，某个服务的故障，可能会导致级联故障，进而造成整个系统不可用的情况，这种现象被称为服务雪崩效应
2. 服务提供者的不可用，导致服务消费者的不可用，并将不可用扩大化，最终导致整个系统瘫痪

<img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ge6du60d4lj30fs09jtcd.jpg" alt="" style="zoom:80%;" />

Hystrix三个特性：断路器机制、Fallback、资源隔离

1. 断路器机制：当Hystrix Command请求后端服务器失败数量超过一个阈值比例，断路器就会切换到开路状态。所有请求会失败，不会发送到后端，对请求失败进行了控制

2. Fallback：降级回滚策略。请求失败之后，有兜底的返回，例如果请求课程服务，课程服务不可用，可以返回一个空的列表、或者返回缓存的数据。

3. 资源隔离：不同的微服务调用使用不同的线程池来管理。线程池实现资源隔离，根据服务进行划分，每个服务都有单独的线程池（维护多个线程池，带来额外开销）

   <img src="https://tva1.sinaimg.cn/large/007S8ZIlgy1ge6duimbmrj30e7047wfm.jpg" alt="" style="zoom:80%;" />
