
# 消息接收端


# 配置接收Json格式的Message
## 方式1：
- 不配置任何消息转换器；
- 从Queue中取出Json格式的消息后，使用第三方库进行Json的转换

代码示例：
```java
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