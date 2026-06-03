# Spring Cloud Alibaba Samples
Spring Cloud 本身并不是一个开箱即用的框架，而是一套微服务开发的规范标准，Alibaba的实现很好。

### 模块介绍
模块 | 简称                | 端口    | 说明
------ |-------------------|-------| ------
cloud-gateway-sample | gateway           | 8764  | spring cloud gateway
cloud-consumer-sample | consumer          | 8766  | web consumer
cloud-provider-sample | provider          | 8765  | web provider
cloud-consumer-reactive-sample | consumer-reactive | 8763 | webflux consumer
cloud-provider-reactive-sample | provider-reactive | 8762 | webflux provider
cloud-provider-dubbo-sample | provider-dubbo    | 50051 | dubbo provider
cloud-sample-api | api               | -     | interface api
cloud-nacos-config-sample | config            | 8761  | nacos config
cloud-sentinel-sample | sentinel          | 8767  | sentinel
cloud-sentinel-gateway-sample | sentinel-gateway  | 8768  | sentinel gateway
cloud-stream-sample | stream            | 8769  | spring cloud stream

### 服务注册与发现演示
首先安装部署nacos，请参考 nacos.io

依次启动provider-reactive,consumer-reactive,gateway
```shell
curl 'http://localhost:8763/hi?name=hongxi'
curl 'http://localhost:8764/consumer-reactive-sample/hi?name=hongxi'
```

接着启动provider,consumer
```shell
curl 'http://localhost:8766/hi?name=hongxi'
curl 'http://localhost:8764/consumer-sample/hi?name=hongxi'
```

接着启动provider-dubbo
```shell
curl 'http://localhost:8766/dubbo?name=hongxi'
curl 'http://localhost:8764/consumer-sample/dubbo?name=hongxi'
curl 'http://localhost:8763/dubbo?name=hongxi'
curl 'http://localhost:8764/consumer-reactive-sample/dubbo?name=hongxi'
```

### 脚本演示
启动所有服务（脚本最后会执行curl并输出响应结果）
```shell
sh start-all.sh
```
停止所有服务
```shell
sh start-all.sh stop
```

### 网关访问dubbo演示
启动provider-dubbo,gateway<br>
直接访问 dubbo rest 接口
```shell
curl http://localhost:50051/api/hello/lily
curl 'http://localhost:50051/api/add?a=1&b=2'
curl -X POST http://localhost:50051/api/echo -H "Content-Type: application/json" -d '{"message":"hi"}'
curl 'http://localhost:50051/api/greet/lily?lang=zh'
```
通过网关访问 dubbo rest 接口
```shell
curl http://localhost:8764/provider-dubbo-sample/api/hello/lily
curl 'http://localhost:8764/provider-dubbo-sample/api/add?a=1&b=2'
curl -X POST http://localhost:8764/provider-dubbo-sample/api/echo -H "Content-Type: application/json" -d '{"message":"hi"}'
curl 'http://localhost:8764/provider-dubbo-sample/api/greet/lily?lang=zh'
```

### 带哨兵的网关演示
执行`start-all.sh`后手动启动 sentinel-gateway
```shell
curl 'http://localhost:8768/consumer-sample/hi?name=hongxi'
curl 'http://localhost:8768/consumer-reactive-sample/hi?name=hongxi'
curl http://localhost:8768/gateway
```
第一个url快速访问几次会触发限流

&copy; [hongxi.org](http://hongxi.org)
