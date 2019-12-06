本项目基于Spring Cloud Greenwich.RELEASE, Spring Cloud Alibaba 2.1.0, Nacos 1.1.3, SEATA 0.9.0
环境准备：
  1. Nacos Server参见官网，这里10.0.0.21:8848
  2. SEATA Server参见官网，这里10.0.0.22:8091
  3. 带有InnoDB引擎的MySQL

## 一.使用Nacos实现动态路由

本部分借鉴了文章http://www.springcloud.cn/view/412

### 1.1 在Nacos新增DataID: sc-routes, Group: DEFAULT_GROUP, 示例JSON配置

```json
[
    {
    "id": "",
    "uri": "http://www.baidu.com",
    "order": 1111,
    "filters": [
        {
            "name": "StripPrefix",
            "args": {
                "_genkey_0": "1"
            }
        }
    ],
    "predicates": [
        {
            "name": "Path",
            "args": {
                "_genkey_0": "/baidu/**"
            }
        }
    ],
    "description": "测试路由新增"
}
]
```

## 二.使用Nacos实现基于HTTP Header Version的流量控制

原理：服务提供者增加元数据配置不同的版本号，服务消费者同样进行版本号配置，那么只需要判断当前的匹配规则，就可以实现有选择性的跳转到指定的服务提供者实例。
本示例实现版本流量控制，当访问端HTTP Header指定对应参数VERSION，后续经过网关，或使用远程Feign，或使用RestTemplate访问的接口都会进行流量过滤，只有当目的服务的Nacos版本元数据和请求头的版本数据匹配时，对应目标服务接口才会响应。流量控制是全链路的，也就是说当客户端访问服务A，而服务A再访问服务B，而服务B继续访问服务C直至响应回客户端，所有链路的服务都要做版本流量过滤。

### 2.1 网关启用流量控制

#### 2.1.1 新建Spring Cloud Gateway网关项目sc-gateway-server并在pom.xml引入依赖

```xml
		<dependency>
			<groupId>com.example</groupId>
			<artifactId>sc-gateway-sidecar</artifactId>
			<version>0.0.1-SNAPSHOT</version>
		</dependency>
```

#### 2.1.2 在application.yml启用sidecar

```yaml
ribbon:
  filter:
    metadata:
      enabled: true
```

#### 2.1.3 在启动类@ComponentScan新增包扫描

```text
  com.example.scgatewaysidecar, com.example.scsidecar
```

### 2.2 普通项目启用流量控制

#### 2.2.1 新建Spring Boot项目sc-consumer并在pom.xml引入依赖

```xml
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>sc-sidecar</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```

#### 2.2.2 在application.yml启用sidecar

```yaml
ribbon:
  filter:
    metadata:
      enabled: true
```

#### 2.2.3 在启动类@ComponentScan新增包扫描

```text
  com.example.scsidecar
```

### 2.3 使用示例

#### 2.3.1 分别针对每个服务启动两个实例，并在Nacos配置元数据


实例 | 端口 | 版本元数据
---- | ---- | ----
sc-consumer | 8846 | VERSION:1.0
sc-consumer | 8847 | VERSION:2.0
sc-gateway-server | 8080 | VERSION:1.0
sc-gateway-server | 8081 | VERSION:2.0
sc-product | 8844 | VERSION:1.0
sc-product | 8845 | VERSION:2.0

#### 2.3.2 测试

通过sc-consumer使用RestTemplate访问sc-gateway-server的接口：
```text
curl -X GET \
  http://localhost:8080/v1/consumer/echo/app-name \
  -H 'VERSION: 2.0' 
```

通过sc-consumer使用FeignClient访问sc-gateway-server的接口：
```text
curl -X GET \
  http://localhost:8080/v1/consumer/get/gateway \
  -H 'VERSION: 2.0' 
```

通过sc-consumer使用FeignClient访问sc-product的接口：
```text
curl -X GET \
  http://localhost:8080/v1/consumer/get/product \
  -H 'VERSION: 2.0' 
```

## 三.使用Nacos实现动态配置

### 示例：在Nacos配置DataId: sc-consumer.yml GROUP: DEFAULT_GROUP 

```yaml
custom:
  test-value: nacos
```

测试：
```text
curl -X GET \
  http://localhost:8080/v1/consumer/get/test-value \
  -H 'VERSION: 1.0' 
```

## 四.集成SEATA

示例由官网示例演变而来，未尽事宜参见官网文档。

### 4.1 创建数据库及表

连接MySQL并执行sc-docs/db下面的SQL文件。
注意：实际上，示例用例中的3个服务应该有3个数据库。但是，我们只需创建一个数据库并配置3个数据源即可。

### 4.2 启动Seata Server

准备：
  1. 复制sc-account/src/main/resources/registry.conf, 覆盖conf/registry.conf
  2. 修改 conf/nacos-config.txt配置

删除
```text
service.vgroup_mapping.my_test_tx_group=default
```  
新增
```text
service.vgroup_mapping.sc-storage-fescar-service-group=default
service.vgroup_mapping.sc-order-fescar-service-group=default
service.vgroup_mapping.sc-business-fescar-service-group=default
service.vgroup_mapping.sc-account-fescar-service-group=default
```
也可以在 Nacos 配置页面添加，data-id 为 service.vgroup_mapping.${YOUR_SERVICE_NAME}-fescar-service-group, group 为 SEATA_GROUP， 如果不添加该配置，启动后会提示no available server to connect
注意配置文件末尾有空行，需要删除。

  3. 同步配置数据到Nacos
  
```text
cd conf
sh nacos-config.sh 10.0.0.21
```

成功后命令行有提示
```text
init nacos config finished, please start seata-server
```

在Nacos管理页面应该可以看到有约47个Group为SEATA_GROUP的配置

  4. 启动SEATA
 
```text
sh seata-server.sh -p 8091 -h 10.0.0.22 -m file &
```
启动后在 Nacos 的服务列表下面可以看到一个名为serverAddr的服务。

### 4.3 运行示例

运行sc-account, sc-business, sc-gateway-server, sc-order, sc-storage.
注意：默认系统版本为1.0，测试需要时可以到Nacos动态配置实现灰度路由。

### 4.4 测试

正常执行完成的方法:
```text
curl -X GET \
  http://localhost:8080/v1/business/purchase/commit \
  -H 'VERSION: 2.0' 
```

会发生异常并正常回滚的方法:
```text
curl -X GET \
  http://localhost:8080/v1/business/purchase/rollback \
  -H 'VERSION: 2.0' 
```
