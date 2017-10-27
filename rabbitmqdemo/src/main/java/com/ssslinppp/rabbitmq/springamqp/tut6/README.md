## [RPC官方示例](https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html)   

## 结构图
![架构图](https://www.rabbitmq.com/img/tutorials/python-six.png)    

- 使用`RabbitTemplate.convertSendAndReceive()`; 
- Callback queue: 用于接收RPC响应；  
- Correlation Id： 关联请求和响应，当RPC响应到达Callback Queue时，通过CorrelationID可以确定对应的请求；   
Spring-amqp为每个请求自动创建唯一的correlationId，并处理请求与响应关联的细节；

## 常用属性
- deliveryMode: Marks a message as persistent (with a value of 2) or transient (any other value). You may remember this property from the second tutorial.
- contentType: Used to describe the mime-type of the encoding. For example for the often used JSON encoding it is a good practice to set this property to: application/json.
- replyTo: Commonly used to name a callback queue.
- correlationId: Useful to correlate RPC responses with requests.

## 工作流程






