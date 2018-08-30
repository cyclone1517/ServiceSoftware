版本记录：
V1.0 可以监听高并发客户端
V1.1 可以注册设备RegisteredDevices<"设备名">，和保存心跳包数据UpdatedDevices<"设备名","系统时间秒数">
V1.2 部署RocketMQ，完成生产者+消息队列+消费者的业务消息处理模式
     DeviceSimulationModule 客户设备模拟模块
     ServiceProducerModule 服务生产者模块，对应的是架构：网络接收层 + 数据层（设备独占的Redis数据，无需消息队列）
     ServiceConsumerModule 服务消费者模块，对应的是架构：业务层 + 数据层（Redis+Mysql）#
问题：
1. 现在有人断开连接就会退出，服务端没有做处理
2. 网络设备故障报告流到消息队列处理和线程池的时候再完成
