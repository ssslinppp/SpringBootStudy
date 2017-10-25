# 概述
本示例程序全部来自rabbitmq官方示例程序，[rabbitmq-demo](https://www.rabbitmq.com/getstarted.html)；          
官方共有6个demo，针对不同的语言（如 C#，Java，Spring-AMQP等），都有不同的示例程序；     
本示例程序主要是Spring-AMQP的参考示例，如果需要其他语言的参考示例，可以参考官网；
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
- Java版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-java.html)     
程序位置：java.demo1包下面

- spring-amqp版本：[单生产者单消费者](https://www.rabbitmq.com/tutorials/tutorial-one-spring-amqp.html)  
application.properties配置
```properties
spring.profiles.active=hello-world, sender, receiver
```

# demo2: 单生产者-多消费者 
[Work queues](https://www.rabbitmq.com/tutorials/tutorial-two-java.html)
application.properties配置
```properties
spring.profiles.active=work-queues, sender, receiver
#spring.profiles.active=work-queues, sender
#spring.profiles.active=work-queues, receiver
```





