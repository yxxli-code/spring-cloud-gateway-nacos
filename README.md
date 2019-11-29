本项目基于Spring Cloud Greenwich.RELEASE, Spring Cloud Alibaba 2.1.0, Nacos 1.1.3.

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
```# spring-cloud-gateway-nacos
