
# 消息接收端

---

# 消费端-`手动确认` vs `自动确认`
手动确认模式（manual ack）
配置：
```
# 设置手动配置
spring.rabbitmq.listener.simple.acknowledge-mode=manual
```

设置prefetch，`@RabbitListener`标注的方法中添加参数：
1. Channel: 用于手动确认的发送；
2. @Header：添加deliveryTag，在消息确认时会用到；
3. prefetch：控制消费端的负载
```
 @RabbitListener(queues = "#{queueOfErrWarn.name}")
    public void receiveWithManualACK(ReceivedMessage msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws Exception {
        /// 设置prefetch：用于控制流量，避免消费端负载过高
        /// 当未确认Message个数达到prefetch count时，broker将不再向channel中发送消息
        channel.basicQos(50); // 官方建议取值100~300

        try {
            handleMsg(msg, deliveryTag);
        } catch (Exception e) {
            System.out.println("tag: 【" + deliveryTag + "】 fail, requeue,  msg: " + msg);

            ////// 当消息处理失败后，重新入队列，传给下一个消费者
            channel.basicNack(deliveryTag, false, true);
//            throw new Exception(e);
            return;
        }
        channel.basicAck(deliveryTag, false);  // 消息应答
    }
```
参考链接： https://stackoverflow.com/questions/38728668/spring-rabbitmq-using-manual-channel-acknowledgement-on-a-service-with-rabbit 


---

# 配置接收Json格式的Message
## 方式1：
- 不配置任何消息转换器；
- 从Queue中取出Json格式的消息后，使用第三方库进行Json的转换

代码示例：
```
public class RabbitCustomer {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive(Message msg) throws InterruptedException, IOException {
        handleMsg(msg);
    }

    private void handleMsg(Message msg) throws InterruptedException, IOException {
        // 使用第3方库，对Json进行转换
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("==> 接收到消息: " + objectMapper.readValue(msg.getBody(), ReceivedMessage.class));

        System.out.println(msg);
        System.out.println(new String(msg.getBody()));
        System.out.println();
    }
}
```


## 方式2：
配置MessageConverter，接收消息时，直接转换为所需的对象
```
 @Bean
 public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
     return new MappingJackson2MessageConverter();
 }

 @Bean
 public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
     DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
     factory.setMessageConverter(consumerJackson2MessageConverter());
     return factory;
 }

 @Override
 public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
     registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
 }
```

接收端代码：
```
@RabbitListener(queues = "#{autoDeleteQueue1.name}")
public void receive(ReceivedMessage msg) throws InterruptedException, IOException {
   handleMsg(msg);
}

private void handleMsg(ReceivedMessage msg) throws InterruptedException, IOException {
    System.out.println("==> 接收到消息: " + msg);
}
```