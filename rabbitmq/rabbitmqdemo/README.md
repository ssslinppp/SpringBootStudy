# 概述

本示例程序全部来自rabbitmq官方示例程序，[rabbitmq-demo](https://www.rabbitmq.com/getstarted.html)；          
官方共有6个demo，针对不同的语言（如 C#，Java，Spring-AMQP等），都有不同的示例程序；     
本示例程序主要是Spring-AMQP的参考示例，如果需要其他语言的参考示例，可以参考官网；

## rabbitmq模拟器
[模拟器](http://tryrabbitmq.com/)
---
# rabbitmq简介
## 核心架构图
![核心架构图](https://lh3.googleusercontent.com/TmA6flkGzB1yc1xK6lGbJZ0YYqO__39trLIPxM62VUjsr09wClmbv9mT3WX4F0cuDssmkiHkekWR6AvXY0iVScuksmLxyM27FaJGYbgPezCIjRs-l8Ct3MfuUU3bRbpfWT6dhVBO)   
![数据流转图](http://img.it610.com/image/product/e4ea0bc1d9a54f9396d3078202782532.jpg)

## [AMQP 0-9-1 Model Explained](https://www.rabbitmq.com/tutorials/amqp-concepts.html) 

## 重要语法说明
- producer或publisher: 消息生产者/发布者，即：产生消息的；
- Exchange：producer或publisher只会将message发送到Exchange，目前有4种不同的Exchange类型；
- Queue：消息队列，所有的消费者都是直接从Queue获取Message并消费；
- Binging：连接Exchange和Queue的纽带，决定Exchange如何路由消息到不同的Queue；
- routingKey：生产者-->message-->Exchange，需要指定一个key，叫做routingKey;
- routingKey：Exchange-->Binging-->Queue，Binging有一个Key值，叫routingKey或bingingKey;
- bingingKey：Exchange-->Binging-->Queue，Binging有一个Key值，bingingKey;   

## 核心理解
4种不同的Exchange，对routingKey的解释都不相同；   
对routingKey的不同解释，决定了Exchange路由Message到Queue的不同方案；  
1. direct exchange： 匹配2个routingKey（即routingKey和bingingKey）是否相等，相等时才进行消息路由；
2. fanout exchange： 忽略routingKey，会将Message路由到所有绑定的Queue；
3. topic exchange： routingKey格式形如`aaa.bbb.xxx`、`*.ccc.dd.#`，类似正则表达式匹配；
4. headers exchange：


---
# jar包说明
- Java版本：  
Java版本使用如下jar（说明：若是使用）：
```
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>4.0.2</version>
</dependency>
```
- Spring-AMQP版本：  
[Spring AMQP 官方详细文章](https://docs.spring.io/spring-amqp/reference/htmlsingle/)   
使用Profile配置各个demo的运行选择，当
使用如下Jar包：
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

# demo1： 单生产者-单消费者 
![单生产者-单消费者官方示例](https://www.rabbitmq.com/img/tutorials/python-one.png) 

- Java版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)     
程序位置：java.demo1包下面

- spring-amqp版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html)  
application.properties配置
```properties
spring.profiles.active=hello-world, sender, receiver
```

---

# demo2: 单生产者-多消费者 
[Work queues官方示例](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)  
![workQueue](https://www.rabbitmq.com/img/tutorials/python-two.png)   
application.properties配置
```properties
spring.profiles.active=work-queues, sender, receiver
#spring.profiles.active=work-queues, sender
#spring.profiles.active=work-queues, receiver
```

详细描述参见：[单生产者-多消费者详细](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut2)

---

# demo3: 发布/订阅 
[Publish/Subscribe官方示例](https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html)    
![发布/订阅](https://www.rabbitmq.com/img/tutorials/exchanges.png)  
- 消费广播到多个消费者进行消费；
- 使用fanout pattern；

application.properties配置
```properties
spring.profiles.active=pub-sub, receiver , sender 
```

详细描述参见：[发布/订阅详细](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut3)

---

# demo4: Routing
[Routing官方示例](http://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html)    
### Direct exchange 模式进行route结构图
![direct-exchange](http://www.rabbitmq.com/img/tutorials/direct-exchange.png)
a message goes to the queues whose `binding key` exactly matches the `routing key` of the message;（相等时才路由）       

### Multiple bindings
![Multiple bindings](http://www.rabbitmq.com/img/tutorials/direct-exchange-multiple.png)  
两个Queue使用相同的BingingKey(black) ==> 效果类似于：发布/订阅模式（demo3）；  


### 完整的结构图
![together](http://www.rabbitmq.com/img/tutorials/python-four.png)  

application.properties配置
```properties
pring.profiles.active=routing, receiver , sender  
```

详细描述参见：[发布/订阅详细](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut4)

---

# demo5: Topics
[Topics官方示例](http://www.rabbitmq.com/tutorials/tutorial-five-spring-amqp.html)
![结构示例图](https://www.rabbitmq.com/img/tutorials/python-five.png)  
- 使用 Topic exchange实现；
- 发送到Topic exchange的routingKey必须满足一定要求：用"."分割的words列表，如：`*.aaa.bbb.#` ； 
- BingingKey和routingKey有相同的格式要求；
- `*` : 可以匹配一个word；
- `#`: 可以匹配0个或多个words；  

application.properties配置
```properties
pring.profiles.active=topics, receiver , sender 
```

详细描述参见：[Topics](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut5)

---

# demo6： RPC over RabbitMQ 
[RPC官方示例](https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html)   
## 结构图
![架构图](https://www.rabbitmq.com/img/tutorials/python-six.png)    

application.properties配置
```properties
spring.profiles.active=rpc,server
#spring.profiles.active=rpc,client
```
详细描述参见：[RPC](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut6)
