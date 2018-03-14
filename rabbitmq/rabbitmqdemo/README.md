# 概述

本示例程序全部来自rabbitmq官方示例程序，[rabbitmq-demo](https://www.rabbitmq.com/getstarted.html)；          
官方共有6个demo，针对不同的语言（如 C#，Java，Spring-AMQP等），都有不同的示例程序；     
本示例程序主要是Spring-AMQP的参考示例，如果需要其他语言的参考示例，可以参考官网；

---

# jar包说明
## Java版本  
Java版本使用如下jar（说明：若是使用）：
```
<dependency>
    <groupId>com.rabbitmq</groupId>
    <artifactId>amqp-client</artifactId>
    <version>4.0.2</version>
</dependency>
```
## Spring-AMQP版本  
[Spring AMQP 官方详细文章](https://docs.spring.io/spring-amqp/reference/htmlsingle/)   
使用Profile配置各个demo的运行选择，当
使用如下Jar包：
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

---

# 代码编写
如何只是讲述了常规 生产端 和 消费端 的代码编写，没有涉及消息确认、持久化、Json等知识点；

## 消息生产者
常规情况下，只需要关心如下：
1. Exchange类型：除非使用默认的DirectExchange，否则需要显示的定义Exchange；
2. 指定Exchange的name：发送到哪个Exchange，使用exchange.name来进行区分；
3. routingKey：消息发送到Exchange时使用的routingKey，不同的Exchange类型对routingKey有不同的解释；

## 消息消费者
主要分两步：     
首先创建Queue到Exchange的绑定关系：   
1. 创建Queue和Exchange：用于接收Exchange分发的消息； 
2. 创建Binding： 绑定Queue和Exchange，指定routingKey（Queue绑定到Exchange使用的routingKey）；
3. 一个Queue可以【绑定多个routingKey】到同一个Exchange上；

然后，监听Queue，进行消费：
1. 每个Queue都有有个唯一的name；
2. 通过监听每个Queue（需要指定queue.name），来进行Message消费;

---

# demo1： 单生产者-单消费者 
- Exchange类型：Direct exchange


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
- Exchange类型：Direct exchange

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
该demo的readme文档详细讲解了4种不同Exchange的区别；

[Publish/Subscribe官方示例](https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html)    
![发布/订阅](https://www.rabbitmq.com/img/tutorials/exchanges.png)  

- 使用：Fanout Exchange；
- 消费广播到多个消费者进行消费；
- 若没有消费者监听，则Message将会被丢弃；

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

