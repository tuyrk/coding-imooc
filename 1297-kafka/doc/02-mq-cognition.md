# 第2章 Day01 消息中间件（MQ）认知提升

## 2-1 本章概述（Kafka）

> RabbitMQ、Apache Kafka

课程目录

> 分布式消息队列（MQ）认知提升
> RabbitMQ实战
> RabbitMQ可靠性投递基础组件封装
> Kafka应用实战
> Kafka高吞吐量日志收集实战
> 架构思考：分布式日志、跟踪、告警、分析平台

本章目录

> 业界主流的分布式消息队列（MQ）与技术选型
>
> ActiveMQ特性原理与集群
>
> RabbitMQ特性原理与集群架构解析
>
> RocketMQ特性原理与集群架构
>
> Kafka特性原理与集群架构解析

## 2-2 消息中间件（MQ）的应用场景与性能衡量指标是什么？

分布式消息队列（MQ）应用**场景**

> 服务解耦、削峰填谷、异步化缓冲

分布式消息队列（MQ）应用**思考点**

> **生产端可靠性投递**、**消费端幂等**、高可用性、低延迟、可靠性、堆积能力、扩展性

## 2-3 消息中间件（MQ）的技术选型关注点有哪些？

业界主流的分布式消息队列（MQ）

> ActiveMQ、RabbitMQ、RocketMQ、Kafka

如何进行技术选型？

> - 各个MQ的性能、优缺点、相应的业务场景
> - 集群架构模式，分布式、可扩展、高可用、可维护性
> - 综合成本问题，人员成本
> - 未来的方向、规划、思考

## 2-4 [ActiveMQ消息中间件集群架构与原理解析](02-4-active-mq.md)

## 2-5 RabbitMQ消息中间件集群架构模型与原理解析

RabbitMQ四种集群架构

> 主备模式、远程模式、镜像模式、多活模式

### 主备模式

> Warren（兔子窝），一个主/备方案（主节点如果挂了，从节点提供服务，使用Haproxy。和ActiveMQ利用Zookeeper做主备一样）

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd6hy31xptj30ik0j4q41.jpg" alt="image-20200325213115561" style="zoom:40%;" />

主备模式-HaProxy配置

```shell
listen rabbitmq_master # 主备模式集群的名字
bind 0.0.0.0:5672 #配置TCP模式
mode tcp #简单的轮询
balance roundrobin #主节点
server bhz76 192.168.11.76:5672 check inter 5000 rise 2 fall 2
server bhz77 192.168.11.77:5672 backup check inter 5000 rise 2 fall 2 #备用节点
```

### 远程模式（不常用）

> - 远距离通信和复制，可以实现双活的一种模式，简称Shovel模式
> - Shovel就是可以把消息进行不同数据中心的复制工作，可以跨地域的让两个MQ集群互联
>
> **使用不多**，可靠性有待提高，配置也非常麻烦。

Shovel架构模型

> 在使用了Shovel插件后，模型变成了近端同步确认，远端异步确认的方式，大大提高了订单确认速度，并且还能保证可靠性

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd72j4e246j30gt0aa74i.jpg" alt="Shovel架构模型" style="zoom:80%;" />

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd72jxp99lj30ko0azjrz.jpg" alt="Shovel集群的拓扑图" style="zoom:80%;" />

Shovel集群配置步骤

> 1. Step1：启动RabbitMQ插件
>
>    ```shell
>    rabbitmq-plugins enable amqp_client
>    rabbitmq-plugins enable rabbitmq_shovel
>    ```
>
> 2. Step2：创建rabbitmq.config
>
>    ```shell
>    touch /etc/rabbitmq/rabbitmq.config
>    ```
>
> 3. Step3：添加配置见rabbitmq.config
>
> 4. Step4：源与目的地服务器使用相同的配置文件(rabbitmq.config)
>    <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd6m5i4uekj30lk0f0mza.jpg" alt="rabbitmq.config" style="zoom:80%;" />

### 镜像模式（常用）

> - 集群模式非常经典的就是Mirror镜像模式，保证100%数据不丢失。
> - 在实际工作中用的最多，并且实现集群非常简单，一般互联网大厂都会构建这种镜像集群

Mirror镜像队列

> 高可靠、数据同步、3节点
>
> 目的是为了保证rabbitmq数据的高可靠性解决方案，主要就是实现数据的同步，一般来讲是2-3个实现数据同步（对于100%数据可靠性解决方案一般是3个节点）

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd72txmzvxj30wr0kidiy.jpg" alt="RabbitMQ集群架构图" style="zoom:60%;" />

### 多活模式

> - 这种模式也是实现异地数据复制的主流模式，因为Shovel模式配置比较复杂，所以一般来说实现异地集群都是使用这种双活或者多活模型来实现的
> - 这种模型需要依赖RabbitMQ的**federation插件**，可以实现持续的,可靠的AMQP数据通信，多活模式实际配置与应用非常简单
> - RabbitMQ部署架构采用双中心模式（多中心），那么在两套（或多套）数据中心中各部署一套RabbitMQ集群，各中心的RabbitMQ服务除了需要为业务提供正常的消息服务外，中心之间还需要实现部分队列消息共享

多活集群架构图

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7mi68wfvj315a0iljtz.jpg" alt="多活集群架构图" style="zoom:40%;" />

多活模式-Federation插件

> - Federation插件是一个不需要构件Cluster，而在Brokers之间传输消息的高性能插件，Federation插件可以在Brokers或者Cluster之间传输消息，连接的双方可以使用不同的users和virtual hosts，双方也可以使用版本不同的RabbitMQ和Erlang。Federation插件使用AMQP协议通信，可以接受不连续的传输
>
> - Federation Exchange，可以看成Downstream从Upstream主动拉取消息，但并不是拉取所有消息，必须是在Downstream上已经明确定义Bindings关系的Exchange，也就是有实际的物理Queue来接受消息，才会从Upstream拉取消息到Downstream。使用AMQP协议实施代理间通信，Downstream会将绑定关系组合在一起，绑定/接触绑定命令将发送到Upstream交换机。因此，Federation Exchange只接收具有订阅的消息
>
>   <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7mrb9hm6j30zb0cfq3y.jpg" alt="Federation插件" style="zoom:50%;" />



## 2-6 [RocketMQ消息中间件集群架构与原理解析](02-6-rocket-mq.md)

## 2-7 Kafka消息中间件高性能原因分析

Kafka介绍

> - Kafka是LinkedIn开源的分布式消息系统，目前归属于Apache顶级项目
> - Kafka主要特点是基于Pull的模式来处理消息消费，追求高吞吐量，一开始的目的就是用于日志收集和传输
> - 0.8版本开始支持复制，不支持事务，对消息的重复、丢失、错误没有严格要求，适合产生大量数据的互联网服务的数据收集业务

Kafka有哪些特点

> 分布式、跨平台、实时性、伸缩性

Kafka高性能的原因是什么？

> - 顺序写，Page Cache空中接力，高效读写
> - 高性能、高吞吐
> - 后台异步、主动Flush
> - 预读策略IO调度

## 2-8 Kafka高性能核心pageCache与zeroCopy原理解析

Page Cache页面缓存：

> Page Cache是操作系统实现的主要的磁盘缓存的机制，以此来减少对磁盘IO的操作，对磁盘IO访问特别频繁会影响操作系统的性能。**把磁盘中的数据缓存到内存中，把对磁盘的访问变成对内存的访问。**

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7nlcb8vxj30t90ld0uk.jpg" alt="Page Cache的过程" style="zoom:50%;" />



<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7o5dbyirj30tw0lawg0.jpg" alt="ZeroCopy" style="zoom:50%;" />

思考：将一个消息发送给10个消费者，两种模式分别需要copy多少次数据？

> 传统模式`4*10`次，零拷贝`1+1*10`次

## 2-9 Kafka消息中间件集群模型讲解

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7ob5wsqlj30y30d3my4.jpg" alt="Kafka集群模式" style="zoom:35%;" />


## 2-10 本章总结

<img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd7oc8xymsj31260nqwij.jpg" alt="图片描述" style="zoom:100%;" />
