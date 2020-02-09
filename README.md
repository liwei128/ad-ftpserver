## 基于AD账号验证的ftp文件共享服务

### 功能介绍

> 1.1 支持AD账号登录使用。

> 1.2用户管理以及在线情况、支持api接口授权；

[![url](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/user.png)](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/user.png)

> 1.3用户使用日志记录。

[![url](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/log.png)](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/log.png)

> 1.4全局统计信息。

[![url](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/statistics.png)](https://raw.githubusercontent.com/liwei128/ad-ftpserver/master/img/statistics.png)

### 快速部署
> 2.1 环境准备
* AD域环境
* 服务器资源
* 软件环境：mysql、JDK1.8

> 2.2 配置信息
* web后台管理配置文件application.properties
* ftp服务配置文件ftp.properties
* 配置独立，互不影响; ftp服务和web管理页面均可选择独立运行、分离部署
* 项目可按包路径com.sf.ftp.server、com.sf.ftp.web拆分为两个项目，项目间无任何依赖、耦合

> 2.3 打包
* 测试环境打包mvn clean install -DskipTests -Psit
* 生产环境打包mvn clean install -DskipTests -Pprod
* 特殊说明：测试环境ad密码统一为123

> 2.4 启动部署
* 同时启动ftp服务和web管理页面  java -jar ftpserver.jar
* 只启动ftp服务  java -jar ftpserver.jar ftp
* 只启动web管理页面  java -jar ftpserver.jar web
* 特殊说明：建议ftp和web分开启动，便于后期进行web管理页面迭代发布时不影响ftp的使用
