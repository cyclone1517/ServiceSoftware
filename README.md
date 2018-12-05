## ServiceSoftware

A communicative service software to collect watermeter data with a big quantity.

## 架构图
![image](https://github.com/cyclone1517/ServiceSoftware/raw/trunk/img/framework.png)

## 模块
| 模块名 | 模块描述 |
|:----------:|-------------|
| `DeviceSimulationModule` | 客户设备模拟模块。 |
| `ServiceServerModule` | 服务器接受信息模块。接收客户端发上来的数据，如果是心跳包，则存入Redis数据库中，非心跳包则存入Rocketmq消息队列中，并从Rocketmq消息队列消费要发送给客户端的消息并发送。 |
| `ServiceProtocolModule` | 协议栈模块。从Rocketmq消息队列中消费消息，判断消息是否异常，并将消息分配到相应的插件进行解析。另外从Redis中获取缓存数据并异步存入到Mysql数据库中。 |
| `ServiceMeterPluginDemo` | 插件模块。将消息进行解析，解析后的数据提供给其他模块进行操作，或者存入Redis数据库中缓存。 |
