
# 消息接收端

---

# 配置并发消费
默认时，使用`@RabbitListener`注解消费时，是单线程处理的，为了提高消息消费的并发性，通常需要配置 `SimpleRabbitListenerContainerFactory`的并发数；        

### 方式1：直接声明一个bean定义，SimpleRabbitListenerContainerFactory
这种方式的好处是，可以为每个` @RabbitListener`都配置不同的`RabbitmqListenerContainerFactory` 
```
@Bean
public SimpleRabbitListenerContainerFactory xxxRabbitListenerContainerFactory() {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory());
    factory.setConcurrentConsumers(MIN_RABBIT_CONCURRENT_CONSUMERS);  //设置min并发数
    factory.setMaxConcurrentConsumers(MAX_RABBIT_CONCURRENT_CONSUMERS); //设置max并发数
    factory.setConsecutiveActiveTrigger(1);
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    return factory;
}
```

可以为每个 RabbitListener 都指定不同的 RabbitListenerContainerFactory
```
@RabbitListener(containerFactory = "xxxRabbitListenerContainerFactory", queues = "#{queueOfErrWarn.name}")
```

### 方式2：通过配置文件，配置默认RabbitListenerContainerFactory的并发属性（或其他属性）
配置默认 rabbitListenerContainerFactory属性
```
spring.rabbitmq.listener.simple.acknowledge-mode=manual
spring.rabbitmq.listener.simple.concurrency=5  # 默认并发数
spring.rabbitmq.listener.simple.max-concurrency=10  # 最大并发数
```
#### 源码分析上述属性的赋值过程
`RabbitAnnotationDrivenConfiguration.java`中的代码(package org.springframework.boot.autoconfigure.amqp)
```
	@Bean
	@ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			SimpleRabbitListenerContainerFactoryConfigurer configurer,
			ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}
```
跟踪上述代码中的`configurer.configure(factory, connectionFactory)`:
```
/**
 * Configure the specified rabbit listener container factory. The factory can be
 * further tuned and default settings can be overridden.
 * @param factory the {@link SimpleRabbitListenerContainerFactory} instance to
 * configure
 * @param connectionFactory the {@link ConnectionFactory} to use
 */
public void configure(SimpleRabbitListenerContainerFactory factory,
		ConnectionFactory connectionFactory) {
	Assert.notNull(factory, "Factory must not be null");
	Assert.notNull(connectionFactory, "ConnectionFactory must not be null");
	factory.setConnectionFactory(connectionFactory);
	if (this.messageConverter != null) {
		factory.setMessageConverter(this.messageConverter);
	}
	RabbitProperties.AmqpContainer config = this.rabbitProperties.getListener()
			.getSimple();
	factory.setAutoStartup(config.isAutoStartup());
	if (config.getAcknowledgeMode() != null) {
		factory.setAcknowledgeMode(config.getAcknowledgeMode());
	}
	
	// 配置min并发数
	if (config.getConcurrency() != null) {
		factory.setConcurrentConsumers(config.getConcurrency());
	}
	// 配置并发数
	if (config.getMaxConcurrency() != null) {
		factory.setMaxConcurrentConsumers(config.getMaxConcurrency());
	}
	if (config.getPrefetch() != null) {
		factory.setPrefetchCount(config.getPrefetch());
	}
	if (config.getTransactionSize() != null) {
		factory.setTxSize(config.getTransactionSize());
	}
	if (config.getDefaultRequeueRejected() != null) {
		factory.setDefaultRequeueRejected(config.getDefaultRequeueRejected());
	}
	if (config.getIdleEventInterval() != null) {
		factory.setIdleEventInterval(config.getIdleEventInterval());
	}
	ListenerRetry retryConfig = config.getRetry();
	if (retryConfig.isEnabled()) {
		RetryInterceptorBuilder<?> builder = (retryConfig.isStateless()
				? RetryInterceptorBuilder.stateless()
				: RetryInterceptorBuilder.stateful());
		builder.maxAttempts(retryConfig.getMaxAttempts());
		builder.backOffOptions(retryConfig.getInitialInterval(),
				retryConfig.getMultiplier(), retryConfig.getMaxInterval());
		MessageRecoverer recoverer = (this.messageRecoverer != null
				? this.messageRecoverer : new RejectAndDontRequeueRecoverer());
		builder.recoverer(recoverer);
		factory.setAdviceChain(builder.build());
	}

}
```

#### 配置并发后的输出结果示例：
```
# 5个Thread并发处理消息（每个Message都会处理15s）
######### CurrentThread id:【18】
######### CurrentThread id:【19】
######### CurrentThread id:【20】
######### CurrentThread id:【17】
######### CurrentThread id:【16】
DeliveryTag [1], Deal [Err/Warn] log: ReceivedMessage(id=22, logContext=log-warn-warn-warn, time=Mon Mar 19 16:01:09 CST 2018)
DeliveryTag [1], Deal [Err/Warn] log: ReceivedMessage(id=1, logContext=log-warn-warn-warn, time=Mon Mar 19 16:01:07 CST 2018)
DeliveryTag [1], Deal [Err/Warn] log: ReceivedMessage(id=27, logContext=log-warn-warn-warn, time=Mon Mar 19 16:01:05 CST 2018)
DeliveryTag [1], Deal [Err/Warn] log: ReceivedMessage(id=40, logContext=log-warn-warn-warn, time=Mon Mar 19 16:01:03 CST 2018)
DeliveryTag [1], Deal [Err/Warn] log: ReceivedMessage(id=40, logContext=log-err-err-err, time=Mon Mar 19 16:01:08 CST 2018)

# 上面的5个消息处理完成后，这5个Thread又开始并发处理5个消息
######### CurrentThread id:【20】
######### CurrentThread id:【16】
DeliveryTag [2], Deal [Err/Warn] log: ReceivedMessage(id=8, logContext=log-err-err-err, time=Mon Mar 19 16:01:10 CST 2018)
DeliveryTag [2], Deal [Err/Warn] log: ReceivedMessage(id=4, logContext=log-err-err-err, time=Mon Mar 19 16:12:24 CST 2018)
######### CurrentThread id:【17】
DeliveryTag [2], Deal [Err/Warn] log: ReceivedMessage(id=32, logContext=log-err-err-err, time=Mon Mar 19 16:12:27 CST 2018)
######### CurrentThread id:【19】
######### CurrentThread id:【18】
DeliveryTag [2], Deal [Err/Warn] log: ReceivedMessage(id=27, logContext=log-warn-warn-warn, time=Mon Mar 19 16:12:28 CST 2018)
DeliveryTag [2], Deal [Err/Warn] log: ReceivedMessage(id=23, logContext=log-err-err-err, time=Mon Mar 19 16:12:29 CST 2018)
```

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