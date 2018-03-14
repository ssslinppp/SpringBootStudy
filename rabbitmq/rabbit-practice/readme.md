## 关于RabbitMq的总结
[rabbitmq相关概念介绍](https://github.com/ssslinppp/SpringBootStudy/tree/master/rabbitmq/rabbitmqdemo)          

---

## 需求说明
### 功能  
假设需要做一个日志处理的功能，共有4种日志级别的消息，分别为：Error/Warn/Info/Debug;    

### 发送端    
负责发送4种日志级别的日志到RabbitMq中；

### 接收端  
负责从Rabbitmq中接收日志消息并处理，并能对不同级别的日志做不同的处理工作；   
如：    
对Error、Warn级别的日志，需要短信通知或邮件通知；    
对Info、Debug级别的日志，简单的记录下即可；

### 程序设计思路
采用RabbitMq的`TopicExchange`进行消息路由；         

发送端-接收端-采用Json格式消息
发送端和接收端的Message对象（POJO）不同：
- 消息发送端： 发送Json格式的消息(SendedMessage);
- 消息接收端： 接收Json消息，并转化为 ReceivedMessage；

消息发送端：将routingKey设计为4种不同的值，分别为：
```
log.err.msg
log.warn.msg
log.info.msg
log.debug.msg
```

消息接收端：使用不同的Queue和RoutingKey进行绑定，来接收不同级别的消息，并进行相应的处理工作；       
routingKey设计：
```
Queue1绑定如下2个routingKey
LOG_ROUTING_KEY_ERR = "*.err.#";
LOG_ROUTING_KEY_WARN = "*.warn.#";

Queue2绑定如下2个routingKey
LOG_ROUTING_KEY_INFO = "*.info.#";
LOG_ROUTING_KEY_DEBUG = "*.debug.#";
```

---

## 发送端
### 使用TopicExchange
### 消息发送格式为Json
需要配置MessageConverter
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

### 发送端的RoutingKey
```
LOG_ROUTING_KEY_LIST.add("log.err.msg");
LOG_ROUTING_KEY_LIST.add("log.warn.msg");
LOG_ROUTING_KEY_LIST.add("log.info.msg");
LOG_ROUTING_KEY_LIST.add("log.debug.msg");
```

### 创建Queue，并绑定到Exchange
目的不是为了消费，而是为了消息不丢失

---

## 接收端
### 接收Queue
使用2个Queue进行消息的接收；   

Queue1： queueOfErrWarn
职责：只负责处理 Error 和 Warn 级别的消息
```
private static final String LOG_ROUTING_KEY_ERR = "*.err.#";
private static final String LOG_ROUTING_KEY_WARN = "*.warn.#";
    
@Bean
    public Queue queueOfErrWarn() {
        return new Queue("practice.log.err.warn.queue");
    }

    @Bean
    public Binding bindingErr(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_ERR);
    }

    @Bean
    public Binding bindingWarn(TopicExchange topic, Queue queueOfErrWarn) {
        return BindingBuilder.bind(queueOfErrWarn).to(topic).with(LOG_ROUTING_KEY_WARN);
    }
```

Queue2： queueOfInfoDebug
职责：只负责处理 Info 和 Debug 级别的消息
```
private static final String LOG_ROUTING_KEY_INFO = "*.info.#";
private static final String LOG_ROUTING_KEY_DEBUG = "*.debug.#";

 @Bean
    public Queue queueOfInfoDebug() {
        return new Queue("practice.log.Info.debug.queue");
    }

    @Bean
    public Binding bindingInfo(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_INFO);
    }

    @Bean
    public Binding bindingDebug(TopicExchange topic, Queue queueOfInfoDebug) {
        return BindingBuilder.bind(queueOfInfoDebug).to(topic).with(LOG_ROUTING_KEY_DEBUG);
    }
```

### 设置Json格式转换
有两种不同的处理方式
#### 方式1：使用MessageConverter直接转换Json为POJO对象
```
@Bean
public MappingJackson2MessageConverter consumerJsonMessageConverter() {
    return new MappingJackson2MessageConverter();
}

@Bean
public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
    DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
    factory.setMessageConverter(consumerJsonMessageConverter());
    return factory;
}

@Override
public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
}
```

使用MessageConverter直接转换Json为POJO对象
```
 @RabbitListener(queues = "#{queueOfErrWarn.name}")
    public void receive(ReceivedMessage msg) throws InterruptedException, IOException {
        handleMsg(msg);
    }
```

#### 方式2：获取到Json字符串，使用第三方Lib进行Json转换
使用第三方Lib进行Json转换
```
@RabbitListener(queues = "#{queueOfInfoDebug.name}")
public void receive(Message msg) throws InterruptedException, IOException {
    handleMsg(msg);
}

private void handleMsg(Message msg) throws InterruptedException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    ReceivedMessage message = objectMapper.readValue(msg.getBody(), ReceivedMessage.class);
    System.out.println("Deal [info/debug] log: " + message);
}
```

---

# 注意点
## 发送端消息持久化
Exchange只用于转发消息，但是**不会做存储**，如果没有Queue绑定到Exchange、或者routingKey和任何bingingKey都无法匹配，    
即：Exchange无法将Message路由到Queue，它会直接**弃掉**Producer发送过来的消息（可参考 mandatory=false 的含义）。         
消费端最初使用**匿名Queue**接收，当消费端断掉后，Queue也自动删除了，导致发送到Exchange的消息全部被丢弃；   
为了使发送端消息能够持久化，接收端的Queue也要设置为持久化的Queue；
```
new AnonymousQueue();   //匿名Queue
new Queue("practice.log.Info.debug.queue");  //持久化队列
```
## Producer和Consumer都应该去创建同一个queue，尽管只有一个地方的创建是真正起作用的
从上一个注意点可知，如果发送端不去创建Queue，而消费端也没有启动，则Exchange中的消息都将会被丢弃！！！    
为了避免这种情况发生，Producer也应该去创建同样的Queue，目的不是为了消费，而是为了让消息不被丢弃；     
    
当然，也有另外的方式，通过设置basic.publish()中的参数`mandatory=true`，在发送端重新发送，这种方式执行起来比较麻烦，而且容易导致消息的堆积。








