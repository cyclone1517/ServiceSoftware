## ServiceSoftware

A communicative service software to collect watermeter data with a big quantity.

## 架构图
![image](https://github.com/cyclone1517/ServiceSoftware/raw/trunk/img/framework.png)

## 模块
| 模块名 | 模块描述 |
|:----------:|-------------|
| `DeviceSimulationModule` | 客户设备模拟模块。 |
| `ServiceServerModule` | 服务器接受信息模块。接收客户端发上来的数据，存入Rocketmq消息队列中，另外从Rocketmq消息队列消费要发送给客户端的消息并发送。 |
| `ServiceProtocolModule` | 协议栈模块。从Rocketmq消息队列中消费消息，判断消息是否异常，并将消息分配到相应的插件进行解析。 |
| `ServiceSynchronizerModule` | 同步模块。从Redis中获取缓存数据并存入到Mysql数据库中。 |
| `AutoUploadDemo` | 插件模块。处理自动上报的数据。将消息进行解析，解析后的数据存入Redis数据库中缓存。 |
| `ServicePluginModule` | 编写插件是需要引用的模块，所有插件都需要实现该模块team.hnuwt.servicesoftware.plugin.service.PluginService这一接口。 |

## 运行

### 运行服务器接受信息模块

先修改配置文件ServiceServerModule/src/main/resources/application.properties中RocketMq的ip地址rocketmq.produce.address和rocketmq.consumer.address，然后编译运行服务器接受信息模块。

    cd ServiceServerModule
    make clean package -Dmaven.test.skip=true
    cd target
    java -jar ServiceServerModule-1.0.0.jar

### 运行协议栈模块

先编译需要用到的插件模块ServiceMeterPluginDemo。

    cd PluginDemo/AutoUploadDemo
    make clean package -Dmaven.test.skip=true

然后将target目录下生成的jar包放到特定的位置，此位置应与ServiceProtocolModule/plugin.xml中相应的jar的位置对应。再修改配置文件ServiceProtocolModule/src/main/resources/application.properties中RocketMq的ip地址rocketmq.consumer.address，修改plugin.xmlPath为plugin.xml存放的目录，最后编译运行协议栈模块。

    cd ServiceProtocolModule
    make clean package -Dmaven.test.skip=true
    cd target
    java -jar ServiceProtocolModule-1.0.0.jar

### 运行同步模块

先修改配置文件ServiceSynchronizerModule/src/main/resources/mybatis/config.xml中mysql数据库的相应信息，然后编译运行同步模块。

    cd ServiceSynchronizerModule
    make clean package -Dmaven.test.skip=true
    cd target
    java -jar ServiceSynchronizerModule-1.0.0.jar