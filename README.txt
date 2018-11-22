版本记录：
V1.0 可以监听高并发客户端
V1.1 可以注册设备RegisteredDevices<"设备名">，和保存心跳包数据UpdatedDevices<"设备名","系统时间秒数">
V1.2 部署RocketMQ，完成生产者+消息队列+消费者的业务消息处理模式
     DeviceSimulationModule 客户设备模拟模块
     ServiceProducerModule 服务生产者模块，对应的是架构：网络接收层 + 数据层（设备独占的Redis数据，无需消息队列）
     ServiceConsumerModule 服务消费者模块，对应的是架构：业务层 + 数据层（Redis+Mysql）#
        业务层类 ConsumeTool 消费业务消息，并放入Redis队列中
        数据层 实现线程池，从业务层取出消息，并最终存入MySql
V1.3 整个数据可以流通
     数据流水：
        DeviceSimulationModule：客户端上传数据
        -> ServiceProducerModule：服务端收到数据，心跳数据存在本地，业务数据上传给RocketMQ服务器
        -> RocketMQ
        -> ServiceConsumerModule：消费者取出数据放入Redis队列，线程池的工作线程从Redis队列获取存入MySql
           (部署时可以把（Redis）和（工作线程+Mysql）部署在不同的机器上)