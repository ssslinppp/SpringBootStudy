# rabbitmq核心概念讲解

---

## rabbitmq模拟器
[模拟器](http://tryrabbitmq.com/)

---

# rabbitmq简介
## 核心架构图
![核心架构图](https://lh3.googleusercontent.com/TmA6flkGzB1yc1xK6lGbJZ0YYqO__39trLIPxM62VUjsr09wClmbv9mT3WX4F0cuDssmkiHkekWR6AvXY0iVScuksmLxyM27FaJGYbgPezCIjRs-l8Ct3MfuUU3bRbpfWT6dhVBO)   
![数据流转图](http://img.it610.com/image/product/e4ea0bc1d9a54f9396d3078202782532.jpg)    
![架构图](http://upload-images.jianshu.io/upload_images/2799767-05b3dc7216205c41.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## [AMQP 0-9-1 Model Explained](https://www.rabbitmq.com/tutorials/amqp-concepts.html)
## [amqp-0-9-1-reference：讲述了所有理论概念](http://www.rabbitmq.com/amqp-0-9-1-reference.html) 

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

### Exchange
Exchange用于转发消息，但是它**不会做存储** ，如果没有 Queue bind到Exchange的话，它会直接丢弃掉Producer发送过来的消息

---

### 持久化
为了保证在RabbitMQ退出或者crash了数据仍没有丢失，需要将queue和Message都要持久化。   
即持久化包括：
1. 消息队列持久化；
2. 消息持久化；

上述持久化只是部分持久化，并不能完全保证消息的不丢失；

#### 消息队列Queue持久化
声明Queue时，将持久化标志`durable`设置为true，代表是一个持久的队列，那么在服务重启后，也会存在。   
`durable`官方解释：
```
durable: 
If set when creating a new queue, the queue will be marked as durable. 
Durable queues remain active when a server restarts. 
Non-durable queues (transient queues) are purged if/when a server restarts.

Note that durable queues do not necessarily hold persistent messages, 
(即：若消息不是持久性的，即使队列是持久性的，重启后，队列不丢失，消息也会丢失)

although it does not make sense to send persistent messages to a transient queue.
(发送持久消息  到  暂时队列是没有意义的)

The server MUST recreate the durable queue after a restart.
The server MUST support both durable and transient queues.
```

如下是一个类库的示例:
```
/**
 * Construct a new queue, given a name, durability, exclusive and auto-delete flags.
 * @param name the name of the queue.
 * @param durable true if we are declaring a durable queue (the queue will survive a server restart)
 * @param exclusive true if we are declaring an exclusive queue (the queue will only be used by the declarer's
 * connection)
 * @param autoDelete true if the server should delete the queue when it is no longer in use
 */
public Queue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
	this(name, durable, exclusive, autoDelete, null);
}
```

#### 消息持久化
队列是可以被持久化，但是里面的消息是否为持久化那还要看消息的持久化设置。   
也就是说，如果重启之前那个queue里面还有没有发出去的消息的话，重启之后那队列里面是不是还存在原来的消息，这个就要取决于发送者在发送消息时对消息的设置了。

在发送消息时，通过设置属性值（`delivery_mode=1 or 2`）：
`|octet delivery-mode|	Non-persistent (1) or persistent (2).|`

完整的属性值可参考(搜索：`delivery-mode`)：[amqp-0-9-1-reference](http://www.rabbitmq.com/amqp-0-9-1-reference.html)  

|属性名|属性值|
|---|---|
|shortstr content-type|	MIME content type.|
|shortstr content-encoding|	MIME content encoding.|
|table headers|	Message header field table.|
|octet delivery-mode|	Non-persistent (1) or persistent (2).|
|octet priority|	Message priority, 0 to 9.|
|shortstr correlation-id|	Application correlation identifier.|
|shortstr reply-to|	Address to reply to.|
|shortstr expiration|	Message expiration specification.|
|shortstr message-id|	Application message identifier.|
|timestamp timestamp|	Message timestamp.|
|shortstr type	|Message type name.|
|shortstr user-id|	Creating user id.|
|shortstr app-id|	Creating application id.|
|shortstr reserved|	Reserved, must be empty.|


代码实现示例（spring boot中的代码设置 TODO）：
```
channel.basic_publish(exchange='',  
                      routing_key="task_queue",  
                      body=message,  
                      properties=pika.BasicProperties(  
                         delivery_mode = 2, # make message persistent  
                      ))  
```

---

### amqp-0-9-1协议中：basic.publish()中 mandatory 
官方解释：
```
 This flag tells the server how to react if the message cannot be routed to a queue. 
 If this flag is set, the server will return an unroutable message with a Return method. 
 If this flag is zero, the server silently drops the message.
```

当Exchange中的Message**无法route到任何一个Queue**时，对Message的处理对策；    
出现上述情况的原因主要有2种：
1. 声明 exchange后**未绑定**任何queue；
2. 声明 exchange后绑定了queue，但发送到该exchange上的message所使用的routingKey不匹配任何bindingKey;

概括来说，mandatory标志(true标志)告诉服务器**至少**将该消息route到一个队列中，否则将消息返还给生产者;
如果没有设置该标志，则消息默认静默丢弃；

### `mandatory` vs `Publisher confirm` 
- Publisher confirm机制：是用来确认 message 的可靠投递；
- mandatory：用来确保在Exchang无法路由消息到queue时，message不会被丢弃。

---

# 消费端确认
## Delivery Identifiers: Delivery Tags
消费者注册后，rabbitmq将消息交付给消费者时，都会带有一个“Delivery Tags”，这个是唯一的ID标识，id以整数的递增的方式实现。

## Acknowledgement Modes（消费端）
### 自动确认模式
- 发送之后，就认为是发送成功(fire-and-forget)
- 消息不停的发送到消费端消费，无需等待消费端任何确认；

缺点：
- 可能造成消费端不堪重负;

### 手动模式
1. basic.ack: 肯定的确认；
2. basic.nack: 否定的确认(RabbitMQ对AMQP 0-9-1的扩展),支持消息`批量确认`;
3. basic.reject：否定的确认，消息消费失败后，直接从broker中将消息`delete`，`不支持批量确认`；

## Acknowledging Multiple Deliveries at Once(消息批量确认)
- 一次确认多个消息发送，而不是每一个消息单独确认；
- basic.reject：不具备该功能；
- basic.nack: 具备该功能；

### 实现方式
- multiple field： 设置为true；

### 示例
假设：在Channel（ch）上有5,6,7,8这4个delivery tags未确认；      
- 情况1，`delivery_tag=8 & multiple=true`： 则5,6,7,8这4个tags都将被确认；      
- 情况2，`delivery_tag=8 & multiple=false`：则只有8被确认，而5,6,7将不会被被确认；

## Channel Prefetch Count (QoS)[可以设置消费端消费的速率] 
- 消息消费是`异步`完成的，手动确认也是`异步`的；
- 有一部分消息是被消费了，但是还未来得及确认：`希望控制未被确认消息的size，防止无界的缓存`；
- `prefetch count`：使用`basic.qos`方法设置该值可以控制未被确认消息的max size；
- 当达到该最大值时，rabbitmq将停止交付消息进行消费；
- 仅对`basic.qos`方法有效，对`basic.get`方法无效；

### 示例
假设：在Channel(Ch)上有5,6,7,8共4个`未被确认`的消息，且ch的`prefetch count=4`；   
结果：rabbitmq将不会再交付任何消息到该Channel上，除非有消息被确认；

## 消费确认选择，prefetch设置以及吞吐量
- 情况1：增大`prefetch`：提高向消费者传递消息的速度;
- 情况2：自动确认模式可以产生最佳的传送速率;

应避免：
1. `自动确认模式`；
2. `手动确认模式` + `无限制的prefetch`；

结论：  
- `情况1`和`情况2`都可能导致`交付但未来得及处理`的Message增加，增大RAM的消耗；

推荐值：
- `prefetch`: 100~300，可以有效提高吞吐量，并避免RAM消耗过多的风险；

## 消费失败或连接中断： 自动重新reQueue
当消息发送给消费端后，如果出现如下情况，则消息会`重新reQueue`，会被再次发送；
1. TCP连接中断；
2. 消费端挂掉：无法进行消息确认；

## Client Errors: Double Acking and Unknown Tags
消费端无法对同一个消息确认超过一次，当超过一次之后，将抛出Channel error: `PRECONDITION_FAILED - unknown delivery tag XXXX`


## 总结
- 每个交付给消费端的消息，都有一个唯一的标识`delivery tag`；
- 自动消息确认；
- 手动消息确认：`每个消息单独确认`和`批量消息确认`;
- `prefetchCount`：可以控制消息端的吞吐量，避免消费端消费过慢，产生RAM大量消耗；
- 失败重传：`TCP连接中断`或`消费端挂掉`，都会引起消息重新入队列，重新消费(手动消息确认时)；
- 无法对同一个消息进行2次或2次以上的`确认`，否则会抛出异常；


---

# 发送端确认

## Channel事务
- 不推荐使用： 会严重降低吞吐量；

在 AMQP 0-9-1中，保证消息不丢失的唯一方法，就是使用事务；
1. 开启Channel事务；
2. 发送消息，提交事务；

## 类似消费端的应答确认机制
- `confirm.select`: 应用于Channel时，表示使用`确认模式`；
- `事务`和`确认模式`无法共存：二者只能选择其一；

## 确认模式 (confirm.select)
- 发送端使用`confirm.select`;
- `broker`发送`basic.ack`来确认Message已被处理；
- `delivery-tag`： 消息序列，具有唯一性；
- `multiple=true`： 用于设置`批量消息确认`；
- 无法保证消息何时被确认；
- 确认模式：消息要么被`confirmed（OK）`，要么被`nack(fail)`，且only once；

Java示例：(发送端发送大量messages，使用确认模式)
[程序-确认模式](https://raw.githubusercontent.com/rabbitmq/rabbitmq-perf-test/master/src/main/java/com/rabbitmq/examples/ConfirmDontLoseMessages.java)    

## 否定确认
异常情况时，服务端无法处理消息，则`broker`发送`basic.nack`来进行`否定确认`；  

## 应答延时和持久化消息
- 仅当消息被持久化到disk之后，才会发送`basic.ack`应答；
- 吞吐量提高建议：`异步处理应答`、`批量发送消息`;

## 应答顺序
当使用异步发送和持久化消息时，broker对消息的`确认顺序`可能和发送者的`消息发送顺序`不一致；

## 发送确认 + 保证交付
- 消息持久化： 并不能保证消息不丢失（在写入disk前broker就挂掉）；

---

# 限制
Delivery tag is a 64 bit long value, and thus its maximum value is 9223372036854775807.Since delivery tags are scoped per channel, it is very unlikely that a publisher or consumer will run over this value in practice.

# 参考
[Consumer Acknowledgements and Publisher Confirms](https://www.rabbitmq.com/confirms.html)



