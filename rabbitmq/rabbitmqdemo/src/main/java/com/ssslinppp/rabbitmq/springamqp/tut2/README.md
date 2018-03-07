# work queue模式：单生产者-多消费者
1. 类似java中的线程池模型；
2. 将task封装为Message，发送到workQueue中；后端消费者从workQueue中取出Message并转化为task，执行；

## Exchage
Direct exchange

## 消费端--消息应答
### Spring-amqp 默认策略
1. 消费者消费完消息后，listenser会调用`channel.basicAck()`完成应答；
2. 消费者在消费message完成前挂掉了，监听器会抛出异常，此时该Message会重新放入Queue，等待下一个消费者消费；
```
Messages will be redelivered when your client quits (which may look like random redelivery), 
but RabbitMQ will eat more and more memory as it won't be able to release any unacked messages.
```

## 消息持久化
###  spring-amqp默认配置
|Property|	default|	Description|
|---|---|---|
| durable|	true	|When declareExchange is true the durable flag is set to this value|
|deliveryMode|	PERSISTENT|	PERSISTENT or NON_PERSISTENT to determine whether or not RabbitMQ should persist the messages|

1. 设置`消息持久化`并不能完全保证消息不丢失: rabbitmq接收到消息，但还未来得及保存到disk的消息会丢失；
2. 持久化保证并不strong：如果需要强有力的保证，可参考：[Consumer Acknowledgements and Publisher Confirms](https://www.rabbitmq.com/confirms.html)

---

## 消息分发策略（rabbitmq-->消费端)
主要有两种策略：通过设置`prefetch`来使用不同的策略
1. Fair dispatch；
2. Round-robin dispatching；

### round-robin 策略（RabbitMQ 默认策略）
默认时，RabbitMQ会将每个message依次发送到下一个消费者，平均下来，每个消费者将得到相同数量的消息；  
这种策略不会考虑消费者的应答，rabbitmq仅仅将第N个Message发送到第N个消费者；  
弊端：  
- 假如有两个消费者;
- 任务情况：偶数任务都很重，奇数任务都很轻量；
- 结果（弊端）：可能结果是，一个消费者一直执行繁忙的任务，另一个消费者，几乎不怎么执行工作；

### Fair分发策略（spring-amqp默认策略）
- `DEFAULT_PREFETCH_COUNT=1`: `SimpleMessageListenerContainer`默认设置；    
  表示：同一时间，不要给同一个消费者超过1个以上的任务，即：如果消费者没有消费完Message，就不要再给他派发新消息；   
  实现方式： 消费者消费完成后，会发送`应答`，监听器没接收到应答，就不会给该消费者派发新消息；
- `DEFAULT_PREFECTH_COUNT=0`：就变成了`round robin`策略；

---

## 消息队列大小
TODO
## spring-amqp默认配置-小结
1. 消息默认开启了持久化；
2. 使用Fair分发策略；
3. 若消费者挂掉，则Message会重新放入Queue，给下一个消费者消费；







