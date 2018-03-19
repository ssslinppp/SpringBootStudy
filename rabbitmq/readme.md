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

### Queue
Queue的一些特性：
- Queue可以存储消息（可以对比Exchange不存储消息），等待消费；   
- 对负载均衡来说，队列是绝佳方案：1个Queue对应多个消费者，使用round-robin来分配消息；
- queue是Message的终点(对于rabbitmq broker)来讲；

创建队列时的一些参数设置：
1. exclusive：队列变为private，只有自己的应用程序才能使用该队列，应用场景示例：限制Queue的消费者个数（如：该队列只有1个消费者）；
2. auto-delete: 顾名思义，不在使用时，自动删除；
3. durable: 队列持久化的参数；

### Queue该由谁来创建？
从前面分析可知，Exchange不存储Message，只做路由，Queue真正存储消息；    
Producter只负责把消息发送到Exchange中，当没有任何Queue绑定Exchange时，Message将会被丢弃；     

**建议：**             
如果应用不能承担消息丢失的风险，应该让**producer和customer都尝试去创建Queue**；        
如果可以承担消息丢失的风险，或者有方式来重新发布未处理的消息，则可以只让消费端来声明队列；


---

# 需要解决的问题
1. 消息持久化；
2. 消费端应答（Consumer acknowledgements ）：消费端对broker的确认；
3. 发布端确认(publisher confirms)：broker对publisher发送消息的确认；
4. 消息可靠性保证：顺序保证等；
5. 如何控制消费端负载；

---

## 持久化
为了保证在RabbitMQ退出或者crash了数据仍没有丢失，持久化必须至少包括3部分：
1. 消息持久化（delivery mode设置为2）；
2. 消息发送到持久化的Exchange;
3. 到达持久化的消息队列；

上述持久化只是部分持久化，并不能完全保证消息的不丢失；

### 消息队列Queue持久化
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

### 消息持久化
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


**Spring-amqp中的源代码：默认消息是`持久化消息`**    
在`org.springframework.amqp.core.MessageProperties.java`中定义了消息的属性：
```
// 消息默认是持久化的
public static final MessageDeliveryMode DEFAULT_DELIVERY_MODE = MessageDeliveryMode.PERSISTENT;
```
看一看交付模式源代码：

```
// 消息的持久化属性定义
public enum MessageDeliveryMode {
	NON_PERSISTENT, PERSISTENT;

	public static int toInt(MessageDeliveryMode mode) {
		switch (mode) {
		case NON_PERSISTENT:
			return 1;
		case PERSISTENT:
			return 2;
		default:
			return -1;
		}
	}

	public static MessageDeliveryMode fromInt(int modeAsNumber) {
		switch (modeAsNumber) {
		case 1:
			return NON_PERSISTENT;
		case 2:
			return PERSISTENT;
		default:
			return null;
		}
	}
}
```

### 消息发送：如何保证Exchange中的消息进入Queue中保证
AMQP中，Exchange是不保存消息的，发送端将消息发送到Exchange后，必须保证消息路由到Queue中，才能保证发送端的消息不丢失。     
当没有任何Queue能够接收Exchange中的消息，此时该如何处理？就是下面要介绍的。
 
#### amqp-0-9-1协议中：basic.publish()中 mandatory 
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

#### `mandatory` vs `Publisher confirm` 
- Publisher confirm机制：是用来确认 message 的可靠投递；
- mandatory：用来确保在Exchang无法路由消息到queue时，message不会被丢弃。

---

## 消费端应答
[Consumer Acknowledgements and Publisher Confirms](https://www.rabbitmq.com/confirms.html)     
[RabbitMQ ACK 机制的意义是什么？](https://www.zhihu.com/question/41976893)    

#### 场景分析：消费端处理消息失败
- Consumer 需要验证消息内容，不合法的消息会导致 consumer 出错：将消息发送给其他消费端处理毫无意义；
- Consumer 需要将消息保存到数据库,数据库服务器挂掉会导致consumer出错：重试几次可能会解决问题；
- Consumer 要根据消息内容发送HTTP请求，HTTP服务挂了会导致consumer出错：重试几次可能会解决问题；
- Consumer 接收到消息，没来得及消费，自己就挂掉了：将消息发送给其他消费端，可以解决问题；

从上面分析可以知道，当消费端处理消息失败，是否需要将Message重新Requeue或者发送给其他消费者，应根据具体业务决定。    
针对上面的各种情况，AMQP中使用`消息应答机制ACK`（acknowledge）来进行处理。  

#### 消费端移交确认
broker将Message发送给customer，如何保证消息成功发送？以及如何保证消息被处理成功？     

##### 消息交付的唯一标识（broker-->customer     
broker将Message发送给customer时，为了区分每次交付的消息，需要为每次的交付添加一个唯一标识，这个唯一标识就是：`Delivery Tags`；
这个唯一的标识，是一个自增的整数；   
当消费端消费完成，并对broker确认交付时，会将这个`delivery tag`作为参数发送给broker；    

##### 消费端应答确认
是否启用消费端应答确认，由消费端决定，在消费端订阅消息时就已经确定。               

1. basic.ack: 肯定的确认(positive acknowledgements)；
2. basic.nack: 否定的确认(RabbitMQ对AMQP 0-9-1的扩展),支持消息`批量确认`;(negative acknowledgements)
3. basic.reject：否定的确认(negative acknowledgements)，消息消费失败后，直接从broker中将消息`delete`，`不支持批量确认`；

`肯定确认`和`否定确认`都表示Message已经成功交付，并处理完成，此时broker端都应该将message删除（针对 basic.ack和basic.reject）；     
两者的区别在于：
1. 肯定确认：交付成功，并且消息也处理成功；
2. 否定确认：交付成功，但消息并没有处理成功，broker端也应该把这个没有处理成功的消息delete；
3. `basic.reject` vs `basic.nack`： nack是AMQP-0-9-1的扩展，支持消息批量确认，reject不支持；


**消息requeue和死信队列（dead letter）**        
当消息处理失败后，通过`basic.reject` or `basic.nack`来告诉broker消息处理失败，broker对于处理失败的Message该如何处理呢？？？     
`basic.reject` or `basic.nack`方法中有一个参数为`requeue`：
1. 当requeue=true：broker将会把Message重新入队列，然后发送给下一个消费者；
2. 当requeue=false：broker不会将Message入队列（即：不会发送给下一个消费者），而是将这些Message存放在`dead letter`死信队列中；

**死信队列(dead letter)**     
专门用来存放那些**被拒绝**而**不重入队列**的消息。死信队列让你通过检测拒绝、未送达的消息来发现问题；    
如果应用程序希望从死信队列中收益，则应该将`basic.reject` or `basic.nack`中的`requeue`参数设置为false（这一特性在高版本的rabbitmq支持）

#### 消费端确认模式
1. 自动确认模式：消息一旦从broker发送出去，就认为消息交付成功；
2. 手动确认模式：

**`自动确认模式` vs `手动确认模式`**
- 自动确认模式: Message从broker发送出去，就表示交付成功（fire-and-forget），不需要Customer返回任何确认；
- 自动确认模式: 以牺牲**安全性**（如：Message发送出去后，消费端还没来得及处理就挂掉，则消息将会丢失）为代价，来提高系统的**吞吐量**；
- 自动确认模式: 可能导致Customer端**负载过重**（因为不需要确认，broker会不停的发消息，导致customer处理不过来）
- 手动确认模式：可以有效控制消费端负载（通过`frefetch`参数），当消费端没有及时应答确认，broker将会停止或放缓消息的发送；

##### 消息批量确认(delivery tags)
当开启手动确认模式，消息交付若是一个一个的进行确认，将会导致网络带宽浪费，因此可以考虑将交付确认批量进行，这样可以节约网络带宽。     

- 开启方式：`multiple`设置为true；     
- basic.reject：不具备该功能；
- basic.nack: 具备该功能；

**示例**      
假设：在Channel（ch）上有5,6,7,8这4个delivery tags未确认；      
- 情况1，`delivery_tag=8 & multiple=true`： 则5,6,7,8这4个tags都将被确认；      
- 情况2，`delivery_tag=8 & multiple=false`：则只有8被确认，而5,6,7将不会被被确认；

##### 设置Channel的`Prefetch Count`：来限制Message的交付速度（保证消费端不会过载）
消息都是通过channel发送的，每个channel都可以设置自己的`prefetch count`，那么什么是`prefetch count`呢？         
我们知道，交付确认（delivery tag）是一个异步的过程，消费端可能有大量的消息累计起来，都没有进行交付确认，我们可以设置一个`未交付确认的max值(prefetch count)`，即：   
当未确认的消息达到这个最大值时，broker将不在发送消息到消费端，这样就可以给消费端一个喘息的机会，不至于负载过重；     
当有消息被确认时，未确认的消息小于这个最大值时，又可以继续通过channel向消费端继续发送消息了；   
通过这个`prefetch`，可以有效的控制交付速度，保证消费端不会过载；

**示例**            
假设：在Channel(Ch)上有5,6,7,8共4个`未被确认`的消息，且ch的`prefetch count=4`；   
结果：rabbitmq将不会再交付任何消息到该Channel上，除非有消息被确认；


关于spring中prefetch的默认值：
- 最初的默认值为：1，即每个消费端最多处理一条消息（导致消费端无法并发消息处理）；
- 新版本的默认值修改为：250（不是很确定，反正是增大了），因为消息到消费端后，可能需要并发的消息处理，如果还是默认为1，将导致无法充分利用并发处理特性；

##### 消费确认选择，prefetch设置以及吞吐量
- 情况1：增大`prefetch`：提高向消费者传递消息的速度;
- 情况2：自动确认模式可以产生最佳的传送速率;

**应用场景建议**   
- 自动确认模式：消费端能有效处理消息，且消息的处理速度比较稳定；

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

#### NACK
Acknowledgment是消费端告诉broker当前消息是否成功消费，至于broker如何处理NACK，取决于消费端是否设置了`requeue`：
1. 消费端设置`requeue=false`: 那么`NACK`后broker会删除消息的;
2. 消费端设置`requeue=true`: `TODO`

#### ACK
Consumer做一个ACK，是为了告诉Broker这条消息已经被成功处理了（transaction committed）。    
只要broker没收到消费端的ACK，broker就会一直保存着这条消息（**但不会 requeue，更不会分配给其他 consumer，直到当前 consumer 发生断开连接之类的异常**）。              
RabbitMQ之所以是`保证移交(guaranteed delivery)`，这是一个关键。    

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



