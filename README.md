# AIHub-Backend

- AIHub后端项目，提供用户认证、通用聊天助手、医疗助手专家、出行助手和PDF文档助手等功能。
- AIHUB前端项目 [传送门](https://github.com/Dai5297/AIHub-Frontend)
- 项目在线地址: [传送门](https://aihub.daiai.top)  账号: admin  密码: 123456 

## 模块介绍

### 1. 用户登录模块
- **功能**: 提供用户注册、登录、密码重置等用户认证功能。
- **技术实现**:
  - 使用JWT（JSON Web Token）进行用户认证。
  - 数据存储在MySQL数据库中。

### 2. 通用聊天模块
- **功能**: 提供基于百炼大模型的通用聊天功能，支持联网搜索。
- **技术实现**:
  - 使用LangChain4j接入百炼大模型。
  - 集成WebSearchEngine进行联网搜索。

### 3. 医疗助手专家模块
- **功能**: 提供基于百炼大模型和Neo4j知识图谱的医疗专家咨询功能。
- **技术实现**:
  - 使用LangChain4j接入百炼大模型。
  - 使用Neo4j存储和查询医疗知识图谱。

### 4. 出行助手模块
- **功能**: 提供基于百炼大模型和地图API的出行规划功能。
- **技术实现**:
  - 使用LangChain4j接入百炼大模型。
  - 集成百度地图和高德地图API进行路线规划。
  - 使用自建MCP工具，通过API请求实现出行规划功能。

### 5. PDF助手模块
- **功能**: 提供基于向量化数据库的PDF文档问答功能。
- **技术实现**:
  - 使用向量化数据库存储和检索PDF文档内容。

## 技术栈
- **核心框架**: Spring Boot 3.4.5
- **数据库**:
  - MySQL: 用于存储用户信息和其他结构化数据
  - Neo4j: 用于存储医疗知识图谱
  - 向量化数据库: 用于存储PDF文档向量
- **认证机制**: JWT
- **AI工具**: LangChain4j WebSearchEngine MCP服务
- **地图API**: 百度地图和高德地图
- **编程语言**: Java 21
- **构建工具**: Maven

## 环境要求
- JDK 21
- MySQL
- Neo4j
- 向量化数据库
- Maven

## 环境变量
项目运行需要配置以下环境变量:
- `SEARCHAPI_API_KEY`: 用于网络搜索功能
- `NEO4J_URI`: Neo4j数据库连接URI
- `NEO4J_USERNAME`: Neo4j数据库用户名
- `NEO4J_PASSWORD`: Neo4j数据库密码
- `BAIDU_MAP_API_KEY`: 百度地图API密钥
- `AMAP_API_KEY`: 高德地图API密钥

## 构建运行
