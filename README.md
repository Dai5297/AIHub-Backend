# AIHub-Backend

AIHub后端项目，提供用户认证、通用聊天助手、医疗助手专家、出行助手和PDF文档助手等功能。项目采用模块化设计，各模块可独立运行且技术栈清晰，适合新手快速上手。

## 项目结构概览
| 模块名称       | 功能类型       | 核心技术栈                  | 数据存储方案          |
|----------------|----------------|-----------------------------|-----------------------|
| chatAI         | 通用对话       | LangChain4j+WebSearchEngine | MySQL                 |
| chatMedical    | 医疗咨询       | LangChain4j+Neo4j           | Neo4j+MySQL           |
| chatTravel     | 出行规划       | LangChain4j+MCP+地图API     | MySQL+向量化数据库    |
| chatPDF        | PDF文档问答    | LangChain4j+向量化数据库    | 向量化数据库+MySQL    |

## 模块详解

### 1. 用户登录模块
- **功能**:
  - JWT认证体系
  - 用户生命周期管理（注册/登录/密码重置）
- **技术实现**:
  - Spring Security + JWT工具库
  - MySQL数据库存储用户凭证
  - Redis用于token黑名单管理（待补充）

### 2. 通用聊天模块 (chatAI)
- **核心功能**:
  - 基于LangChain4j的对话交互
  - 实时联网搜索功能
- **技术实现**:
  - LangChain4j集成Qwen大模型
  - WebSearchEngine实现搜索增强
  - ChatMemoryStore持久化对话历史
  - MySQL存储对话记录
- **特色能力**:
  - 支持多轮对话
  - 集成搜索增强
  - 对话历史持久化

### 3. 医疗助手专家模块 (chatMedical)
- **核心功能**:
  - 基于LangChain4j的对话交互
  - 医疗知识图谱查询
- **技术实现**:
  - LangChain4j集成Qwen大模型
  - Neo4j存储和查询医疗知识图谱
  - MySQL存储用户凭证
- **特色能力**:
  - 医疗知识图谱查询
  - 对话历史持久化

### 4. 出行助手模块 (chatTravel)
- **核心功能**:
  - 基于LangChain4j的对话交互
  - 实时出行规划
- **技术实现**:
  - LangChain4j集成Qwen大模型
  - MCP服务实现出行规划
  - 百度地图和高德地图API获取路线信息
  - MySQL存储用户凭证
  - 向量化数据库存储出行历史
- **特色能力**:
  - 实时出行规划
  - 多地图API支持
  - 出行历史持久化

### 5. PDF助手模块 (chatPDF)
- **核心功能**:
  - 基于LangChain4j的对话交互
  - PDF文档问答
- **技术实现**:
  - LangChain4j集成Qwen大模型
  - 向量化数据库存储PDF文档向量
  - MySQL存储用户凭证
- **特色能力**:
  - PDF文档问答
  - 向量化数据库支持

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