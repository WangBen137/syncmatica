# Syncmatica 项目结构详解

## 项目概述

Syncmatica 是一个 Minecraft Fabric 模组，旨在与 litematica 集成，实现原理图和其放置位置的轻松共享。它支持客户端和服务器端，需要配合 litematica 和 malilib 使用。

## 核心组件

### 主类: Syncmatica.java

- 定义了项目版本(0.3.11)和MOD_ID("syncmatica")
- 声明了服务器和客户端路径常量
- 包含服务器端(initServer)和客户端(initClient)初始化方法
- 上下文管理(getContext)、重启客户端(restartClient)和关闭(shutdown)等核心功能
- 通信、文件存储等组件的集成

### 上下文管理: Context.java

- 项目上下文管理核心
- 构造函数初始化文件存储、通信管理器、同步管理器等组件
- 服务启动/关闭方法(startup/shutdown)
- 配置加载逻辑(loadConfiguration)
- 获取配额服务、调试服务、特性集等核心功能的方法
- 服务器/客户端模式判断、配置文件路径处理等上下文相关操作

### 通信管理: CommunicationManager.java

- 定义了项目的通信管理机制
- 包含广播目标集合、下载状态和修改状态映射等成员变量
- 实现了数据包处理(onPacket)、元数据发送(sendMetaData)与接收(receiveMetaData)、位置数据处理、下载管理(download)等核心方法
- 涉及ServerPlacement、PlayerIdentifier等数据结构
- 以及ExchangeTarget、PacketType等通信相关组件的交互

### 同步管理: SyncmaticManager.java

- 同步管理核心
- 包含放置方案(ServerPlacement)的增删查改方法
- 通过placements.json文件进行数据持久化(saveServer/loadServer)
- 维护消费者列表处理更新通知
- 依赖Context获取配置路径和运行模式

### 文件存储接口: IFileStorage.java

- 定义了文件存储功能接口
- 包含获取本地状态(getLocalState)、创建本地litematic(createLocalLitematic)、获取本地litematic(getLocalLitematic)和设置上下文(setContext)四个抽象方法
- 依赖ServerPlacement类和Context类

### 文件存储实现: FileStorage.java

- 实现了IFileStorage接口
- 使用HashMap缓存文件修改时间
- 包含获取本地状态(getLocalState)、判断下载状态(isDownloading)、获取本地文件(getLocalLitematic)、创建本地文件(createLocalLitematic)等核心方法
- 通过hashCompare方法比较文件哈希值与placement哈希确保同步一致性
- 根据服务器/客户端模式使用不同路径规则生成.litematic文件路径

## GUI 模块

### 服务器放置方案管理界面: GuiSyncmaticaServerPlacementList.java

- 继承自GuiListBase
- 定义了服务器放置方案管理界面
- 包含构造函数（设置标题）、初始化GUI方法（添加返回主菜单按钮）、创建列表部件方法
- 依赖litematica和malilib的GUI组件
- 使用ServerPlacement数据类型和StringUtils进行文本翻译

## 服务模块

### 配额服务: QuotaService.java

- 定义了配额服务的默认配置（默认禁用，默认限制40000000字节）
- 包含进度跟踪Map、isOverQuota（检查是否超出配额）和progressQuota（更新配额进度）核心方法
- 实现配置加载（从配置获取enabled和limit参数）和默认配置保存功能
- 空实现的startup和shutdown方法

### 调试服务: DebugService.java

- 定义了调试服务
- 包含日志记录方法(logReceivePacket/logSendPacket)
- 实现配置加载（从配置获取doPackageLogging参数）
- 空实现的startup和shutdown方法

## 配置文件

### CONFIG.md

- 详细说明了项目的配置选项
- 介绍了quota（配额）和debug（调试）两个主要配置类别
- 解释了各个参数的含义和作用

### config.json

- 项目的实际配置文件
- 位于config/syncmatica/config.json
- 包含quota和debug两个主要配置部分

## 其他重要组件

### ServerPlacement.java

- 定义了服务器端的放置方案
- 包含ID、名称、哈希值、所有者等属性
- 提供了位置、维度、旋转、镜像等操作方法
- 支持JSON序列化和反序列化

### Feature.java

- 定义了项目的功能特性
- 用于控制不同版本或配置下的功能启用

### PlayerIdentifier.java

- 定义了玩家标识符
- 包含UUID和名称
- 用于标识方案的所有者和最后修改者

## 项目流程

1. 项目启动时，根据是客户端还是服务器端初始化相应的Context
2. Context初始化时加载配置文件，启动相关服务
3. 通信管理器处理网络数据包，同步管理器管理放置方案
4. 文件存储系统负责本地文件的管理
5. GUI模块提供用户界面交互
6. 服务模块提供配额和调试等辅助功能

## 开发注意事项

1. 项目使用Fabric Mod Loader构建
2. 依赖litematica和malilib模组
3. 需要注意Minecraft版本兼容性
4. 配置文件的修改需要重启项目才能生效
5. 网络通信部分需要处理好数据包的发送和接收
6. 文件存储部分需要确保文件的一致性和完整性