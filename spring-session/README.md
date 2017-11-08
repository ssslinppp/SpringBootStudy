# 概述
![分布式session之spring-session](http://images2017.cnblogs.com/blog/731047/201711/731047-20171108155323575-436390719.jpg)      
Session用于保存用户信息，通常一个Session保存一个用户信息，在以Tomcat为Servlet Container的web应用中，用户信息都保存在HttpSession中；  
当用户发起请求时，都会将SessionId传递过来，服务器根据传递的SessionId就可以找到对应的Session；   

HttpSession通常都会保存如下信息：
1. SessionId：唯一标示；
2. Session创建时间；
3. Session过期时间；
4. Session最近修改时间；
5. 可以设置Session的一些属性：用于保存User信息；

## 传统的Session
### Session共享的问题
HttpSession通常都会保存在服务器内存中（JVM的堆中），当Session数量变多时，会导致需要大量的JVM堆空间；   
因为保存在服务器内存中，当对应用进行横向扩展时，就会导致Session无法共享的问题；   
解决方案：将Session存储在外部存储中，比如：redis,mysql中等；

## Spring Session
### 主要特点
1. 将Session状态保存到外部Session存储中，如 redis,jdbc,mongodb等；

### 如何创建集群环境下高可用的session
因为使用外部存储方案，可以使用目前成熟的各种数据存储方案，如：redis集群, mysql集群；   

Spring Session只需要提供相关接口，能访问底层的数据存储即可，目前Spring Session提供的标准接口如下(针对不同的存储，有不同的实现)：
- `org.springframework.session.Session`接口:      
定义了session的基本功能，如`设置和移除属性`。这个接口并不关心底层技术，因此能够比servlet HttpSession适用于更为广泛的场景中。   
- `org.springframework.session.ExpiringSession`接口：    
扩展了Session接口，它提供了判断session`是否过期`的属性。RedisSession是这个接口的一个样例实现。    
- `org.springframework.session.SessionRepository`接口：     
定义了创建、保存、删除以及检索session的方法。    
将Session实例真正保存到数据存储的逻辑是在这个接口的实现中编码完成的。     
例如，RedisOperationsSessionRepository就是这个接口的一个实现，它会在Redis中创建、存储和删除session。       

### Session Id如何传输
确定一个用户请求对应的Session，需要使用`Session ID`来确定，因此客户端和服务器之间需要`协商一种传递session id的方式`;     
示例：   
1. HTTP请求：session可以通过HTTP cookie或HTTP Header信息与请求进行关联；

### Spring-Session实现思路
设计一个Filter，利用HttpServletRequestWrapper，实现自己的 getSession()方法，接管创建和管理Session数据的工作。   

---

## 环境搭建
### Step1: 搭建用于Spring Session的数据存储
Spring Session支持多种不同的后端存储类型（Store-Type），主要包括：
- JDBC
- MONGO
- REDIS
- HAZELCAST
- HASH_MAP :Simple in-memory map of sessions.

本示例中，我们使用`Redis集群`作为Spring Session的后端存储，关于如何搭建Redis集群，这里不进行详述；    

### Step2: 添加Spring Session依赖
因为使用的是Redis集群作为后端存储，所以此处也添加了 redis相关依赖；   
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Step3： 将Spring Session filter添加到web应用的配置中 
通过Spring boot的自动配置来实现，只需要在Spring boot的配置类上使用`@EnableRedisHttpSession`注解就可以。
开启，设置失效时间
```
// 设置了Session的失效时长：
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 300)

// 官方文档的解释：
Add this annotation to an {@code @Configuration} class to expose the
SessionRepositoryFilter as a bean named "springSessionRepositoryFilter" and backed by Redis. 
In this instance Spring Session is backed by Redis.
```

### Step4： 配置Spring Session如何选择session数据存储的连接
主要有两种方式选择Store-Type：
1. 使用`spring.session.store-type=xxx`配置，示例：`spring.session.store-type=redis`；
2. 使用注解方式，示例：`@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30)`；  

当使用redis集群作为Spring Session后端存储时，需要配置Redis连接信息（application.properties）：
```
server.session.timeout=10
spring.redis.host=10.254.9.21
spring.redis.port=30379
```

---

## 参考链接
[SpringBoot应用之分布式会话](https://segmentfault.com/a/1190000004358410)   
[Spring Session - Spring Boot 官网](https://docs.spring.io/spring-session/docs/current/reference/html5/guides/boot.html)
[Spring Session](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-session.html)        
    
    
    