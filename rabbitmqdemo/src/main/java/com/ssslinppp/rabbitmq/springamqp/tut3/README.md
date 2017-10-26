# 发布/订阅模式
实现：FanoutExchange + Bingings + Queue;
![示例](https://www.rabbitmq.com/img/tutorials/python-three-overall.png)

## 消息传输流程
1. Producer只发送消息到Exchange，从不发送消息到Queue；
2. Exchange决定消息如何路由到Queue；
3. Consumer从Queue消费消息；

## Exchange类型
1. direct：
2. topic：
3. headers：
4. fanout：本示例程序使用该类型的Exchange；

### Direct Exchange 
The direct exchange type works as follows:
1. A message queue binds to the exchange using a `routing key`, K.
2. A publisher sends the exchange a message with the `routing key` R.
3. The message is passed to the message queue if `K = R`.
The server MUST implement the direct exchange type and MUST pre-declare within each virtual host at
least two direct exchanges: one named amq.direct, and one with no public name that serves as the default
exchange for Publish methods.
Note that message queues can bind using any valid routing key value, but `most often message queues will
bind using their own name as routing key`.
In particular, all message queues MUST BE automatically bound to the nameless exchange using the
message queue's name as routing key.

### Fanout exchange
The fanout exchange type works as follows:
1. A message queue binds to the exchange with no arguments.
2. A publisher sends the exchange a message.
3. The message is passed to the message queue unconditionally.
The fanout exchange is trivial to design and implement. This exchange type, and a pre-declared exchange
called **amq.fanout**, are mandatory.

### Topic Exchange
The topic exchange type works as follows:
1. A message queue binds to the exchange using a routing pattern, P.
2. A publisher sends the exchange a message with the routing key R.
3. The message is passed to the message queue if R matches P.
The routing key used for a topic exchange MUST consist of zero or more words delimited by dots. Each
word may contain the letters A-Z and a-z and digits 0-9.
The routing pattern follows the same rules as the routing key with the addition that * matches a single
word, and # matches zero or more words. Thus the routing pattern *.stock.# matches the routing keys
usd.stock and eur.stock.db but not stock.nasdaq.
One suggested design for the topic exchange is to hold the set of all known routing keys, and update this
when publishers use new routing keys. It is possible to determine all bindings for a given routing key, and
so to rapidly find the message queues for a message. This exchange type is optional.
The server SHOULD implement the topic exchange type and in that case, the server MUST pre-declare
within each virtual host at least one topic exchange, named amq.top

###  Headers Exchange 
The headers exchange type works as follows:
1. A message queue is bound to the exchange with a table of arguments containing the headers to be
matched for that binding and optionally the values they should hold. The routing key is not used.
2. A publisher sends a message to the exchange where the 'headers' property contains a table of
names and values.
3. The message is passed to the queue if the headers property matches the arguments with which the
queue was bound.
The matching algorithm is controlled by a special bind argument passed as a name value pair in the
arguments table. The name of this argument is 'x-match'. It can take one of two values, dictating how the
rest of the name value pairs in the table are treated during matching:
 'all' implies that all the other pairs must match the headers property of a message for that message to be
routed (i.e. and AND match)
 'any' implies that the message should be routed if any of the fields in the headers property match one of
the fields in the arguments table (i.e. an OR match)
A field in the bind arguments matches a field in the message if either the field in the bind arguments has
no value and a field of the same name is present in the message headers or if the field in the bind
arguments has a value and a field of the same name exists in the message headers and has that same value.
Any field starting with 'x-' other than 'x-match' is reserved for future use and will be ignored.
The server SHOULD implement the headers exchange type and in that case, the server MUST pre-declare
within each virtual host at least one headers exchange, named amq.match.

## Bindings
That relationship between exchange and a queue is called a binding
![bingings](https://www.rabbitmq.com/img/tutorials/bindings.png)  









