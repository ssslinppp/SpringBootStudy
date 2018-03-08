
# 创建项目
## 添加依赖
```
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
 </dependency>
```

## rabbitmq配置
rabbitmq管理界面：`ip:15672(如： http://localhost:15672)`     

配置信息如下：
```
# config rabbitmq connect info
spring.rabbitmq.host=192.168.35.129
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

# 发送端--消息格式设置为Json
使用 `RabbitTemplate` 模板进行消息发送，需要配置 MessageConverter; 

```
   @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
```

---





