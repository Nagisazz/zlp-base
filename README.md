*ZLP—BASE*
# 介绍
**包含前后端整套解决方案的企业级基础通用平台及组件**
- 前端
    - 基于 [qiankun](https://qiankun.umijs.org/zh) 实现的微前端基础方案
    - 主应用(main-project)基于Vue3.0+Element-plus
    - 子应用1(zp-project)基于Vue2+Element
    - 子应用2(pricefile-project)基于React18+Antd4+React-router6
        - 子应用2中包含两个项目：通过路由切换
        - 猜价攻略（/price）默认进这个路由
        - 享到（/file）
- 后端
    - 基于Spring boot2.3.6，划分多module
    - 登录验证采用JWT，适配多端登录
    - 通用平台提供用户相关、系统注册、日志等基础功能，上层项目只需关注业务逻辑，提高生产力
# 演示地址

# 项目模块结构
- backend：后端源码文件夹
    - zlp-base-component：基础组件包，封装常用组件，集成多种工具类
    - zlp-base-entity：基础实体模块
    - zlp-platform：通用平台，提供登录、注册、token获取等一系列基础功能，需单独部署
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
