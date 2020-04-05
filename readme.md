![poseidon](https://github.com/muggle0/poseidon-cloud/blob/master/project-document/png/factory.jpg?raw=true) 


# poseidon-boot-starter 使用说明
为 `poseidon-cloud-starter` 衍生项目， `poseidon-cloud-starter` 用于分布式项目 spring cloud 开发，该项目用于单体项目 springboot的开发

框架集成功能：

- 异常报警
- 权限动态配置
- 幂等锁

## 开发日志

- 2020.3.9 1.0.0.Beta 发布。多方测试bug

- 2020.4.5 项目从cloud-starter 分裂出来，补充部分不成熟地方。

## 快速开始

具体使用案例可参考 [sofia](https://github.com/fighting-v/sofia)

第一步拉取项目 并且使用 maven 安装到本地。
拉取项目：
```
git clone https://github.com/muggle0/poseidon-boot-starter.git
```
安装到本地仓库：
```
cd poseidon-boot-starter

mvn install

```

第二步
创建 spring boot工程 并引入依赖：

```xml

 <dependency>
    <groupId>com.muggle</groupId>
    <artifactId>poseidon-boot-starter</artifactId>
    <version>0.0.1.Alpha</version>
 </dependency>
```
第三步开启自动化配置并注册 `tokenService` 和 `securityStore`

```java

```
## 备忘录
