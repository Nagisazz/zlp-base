*ZLP—BASE*
# 介绍
**包含前后端整套解决方案的企业级基础通用平台及组件**
- 前端
    - 基于 [qiankun](https://qiankun.umijs.org/zh) 实现的微前端基础方案
    - 主应用(main-project)基于Vue3.0+Element-plus
    - 子应用1(zp-project)基于Vue2+Element
    - 子应用2(price-project)基于React18+Antd4+React-router6
    - 子应用3(file-project)基于React18+Antd4+React-router6
    - 子应用4(fund-project)基于Angular12+NG-ZORRO12，*注意组件库一定要和框架版本匹配

- 后端
    - 基于Spring boot2.7.5，划分多module
    - 基础组件包含多种常用组件配置及工具类，所有组件均可插拔
        - 组件类
            - 登录校验：采用JWT，适配多端登录
            - 日志：提供多种配置项，日志异步发送平台端入库
            - 初始化：提供基础初始化实现，业务系统继承基础类即可完成初始化操作
            - WebSocket：封装WebSocket配置及实现，提供发送消息和接收消息工具类
            - 定时任务：集成[xxl-job](https://www.xuxueli.com/xxl-job/)，提供配置项初始化
            - LogBack：添加*traceId*和*requestUrl*，便于日志跟踪
            - 异常封装拦截
            - ...
        - 工具类
            - RestHelper：封装RestTemplate，提供多种配置项
            - MinioHelper：封装minio实现
            - MsgPushHelper：集成[PushPlus](https://www.pushplus.plus/)，封装消息推送逻辑
            - ...
    - 通用平台提供基础功能，业务系统只需关注业务逻辑，提高生产力
        - 登录注册、用户信息更新
        - Token刷新
        - 文件上传、下载、缩略图预览、删除
        - 日志记录
        - 消息推送回调
        - ...
# 演示地址

# 项目模块结构
- backend：后端源码文件夹
    - zlp-base-component：基础组件包，封装常用组件，集成多种工具类
        - autoconfig：自动配置相关类
        - config：按照内部包名存放配置信息
        - property：配置项包
        - util：工具类包
    - zlp-base-entity：基础实体模块
    - zlp-platform：通用平台，需单独部署
- frontend：前端源码文件夹

# 如何使用
## 后端
### 引入基于github的个人maven仓库
```xml
<repositories>
    <repository>
        <id>mvn-repo</id>
        <!-- https://raw.github.com/用户名/仓库名/分支名 -->
        <url>https://raw.github.com/Nagisazz/mvn-repo/main</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>
</repositories>
```
### 启动通用平台
直接打包`zlp-platform`，运行即可
### 业务项目集成组件
- 定义parent
```xml
<parent>
    <groupId>com.nagisazz</groupId>
    <artifactId>zlp-base</artifactId>
    <version>1.0-SNAPSHOT</version>
</parent>
```
- 引入基础组件
```xml
<dependency>
    <groupId>com.nagisazz</groupId>
    <artifactId>zlp-base-component</artifactId>
    <version>${project.version}</version>
</dependency>
```
