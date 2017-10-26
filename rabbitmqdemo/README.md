# 概述
本示例程序全部来自rabbitmq官方示例程序，[rabbitmq-demo](https://www.rabbitmq.com/getstarted.html)；          
官方共有6个demo，针对不同的语言（如 C#，Java，Spring-AMQP等），都有不同的示例程序；     
本示例程序主要是Spring-AMQP的参考示例，如果需要其他语言的参考示例，可以参考官网；
---
# rabbitmq简介
## 核心架构图
![核心架构图](https://lh3.googleusercontent.com/TmA6flkGzB1yc1xK6lGbJZ0YYqO__39trLIPxM62VUjsr09wClmbv9mT3WX4F0cuDssmkiHkekWR6AvXY0iVScuksmLxyM27FaJGYbgPezCIjRs-l8Ct3MfuUU3bRbpfWT6dhVBO)   
## [AMQP 0-9-1 Model Explained](https://www.rabbitmq.com/tutorials/amqp-concepts.html) 

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
使用Profile配置各个demo的运行选择，当
使用如下Jar包：
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

# demo1： 单生产者-单消费者 
![单生产者-单消费者](https://www.rabbitmq.com/img/tutorials/python-one.png) 

- Java版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)     
程序位置：java.demo1包下面

- spring-amqp版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html)  
application.properties配置
```properties
spring.profiles.active=hello-world, sender, receiver
```

---

# demo2: 单生产者-多消费者 
[Work queues](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)  
![workQueue](https://www.rabbitmq.com/img/tutorials/python-two.png)   
application.properties配置
```properties
spring.profiles.active=work-queues, sender, receiver
#spring.profiles.active=work-queues, sender
#spring.profiles.active=work-queues, receiver
```

详细描述参见：[单生产者-多消费者详细](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut2)

---

# demo3: 发布/订阅 
[Publish/Subscribe](https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html)    
![发布/订阅](https://www.rabbitmq.com/img/tutorials/exchanges.png)  
- 消费广播到多个消费者进行消费；
- 使用fanout pattern；

application.properties配置
```properties
spring.profiles.active=pub-sub, receiver , sender 
```

详细描述参见：[发布/订阅详细](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmqdemo/src/main/java/com/ssslinppp/rabbitmq/springamqp/tut3)







