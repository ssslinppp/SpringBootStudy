# rabbitmq安装
rabbitmq的安装依赖erlang，首先应该先安装erlang，然后安装rabbitmq；
## Step1：安装erlang
[erlang-rpm安装教程](https://github.com/rabbitmq/erlang-rpm)   
选择在Centos7 上安装：  
To use Erlang 20.x on CentOS 7:
```
# In /etc/yum.repos.d/rabbitmq-erlang.repo
[rabbitmq-erlang]
name=rabbitmq-erlang
baseurl=https://dl.bintray.com/rabbitmq/rpm/erlang/20/el/7
gpgcheck=1
gpgkey=https://www.rabbitmq.com/rabbitmq-release-signing-key.asc
repo_gpgcheck=0
enabled=1
```
然后执行：
```
yum install erlang
```

## Step2: 安装rabbitmq
[参考链接](https://www.rabbitmq.com/install-rpm.html)  
### 下载rpm
```
wget https://github.com/rabbitmq/rabbitmq-server/releases/download/rabbitmq_v3_6_12/rabbitmq-server-3.6.12-1.el7.noarch.rpm
```
### 安装rpm
```
rpm --import https://www.rabbitmq.com/rabbitmq-release-signing-key.asc
yum install rabbitmq-server-3.6.12-1.el7.noarch.rpm
```

## Step3: 设置开机自启动+开启服务
```
chkconfig rabbitmq-server on
/sbin/service rabbitmq-server stop/start/
```

## Step4： Rabbit管理（非必须）
开启Web管理插件
```
rabbitmq-plugins enable rabbitmq_management

output:
The following plugins have been enabled:
  mochiweb
  webmachine
  rabbitmq_web_dispatch
  amqp_client
  rabbitmq_management_agent
  rabbitmq_management
Applying plugin configuration to rabbit@PC-201602152056... started 6 plugins.
```
浏览器访问：
`http://localhost:15672`，   
默认用户名和密码： guest/guest;  
需要注意的是：guest用户仅仅提供localhost作为ip登录；    
如果远程登录，如：`http://192.168.35.129:15672/`, 则会提示错误，登录不了：  
```
# 如下是日志输出
=WARNING REPORT==== 21-Oct-2017::23:31:33 ===
 HTTP access denied: user 'guest' - User can only log in via localhost
```
访问控制可参考：
[Access Control (Authentication, Authorisation) in RabbitMQ](https://www.rabbitmq.com/access-control.html)   
为了让guest可远程访问，需要修改`rabbitmq.config`中的`loopback_users`参数，设置为
```
[{rabbit, [{loopback_users, []}]}].
```
官网文档描述如下,可参考官方文档：[RabbitMQ Configuration](https://www.rabbitmq.com/configure.html#configuration-file)：
```
loopback_users参数：
List of users which are only permitted to connect to the broker via a loopback interface (i.e. localhost).
If you wish to allow the default guest user to connect remotely, you need to change this to [].

Default:  [<<"guest">>] 
```
默认安装时，`rabbitmq.config`配置文件可能不存在，有两种方式可以设置配置文件；  
 - 方式1： 配置文件默认路径： /etc/rabbitmq/rabbitmq.config
 - 方式2： 使用环境变量`RABBITMQ_CONFIG_FILE`指定 `rabbitmq.config`文件位置；   
 说明如下：
```
If rabbitmq.config doesn't exist, it can be created manually. Set the RABBITMQ_CONFIG_FILE environment variable if you change the location. The Erlang runtime automatically appends the .config extension to the value of this variable.
```
修改完配置文件后，重启，就可以使用guest用户远程访问了；



