# AIHub-Backend
AIHub后端项目

## 技术栈
- Java 21
- Spring Boot 3.4.5
- MySQL + MyBatis
- Neo4j
- JWT认证
- LangChain4j

## 功能特性
- 聊天助手
- 医疗助手
- PDF文档助手
- 用户认证

## 环境要求
- JDK 21
- MySQL
- Neo4j
- Maven

## 环境变量
项目运行需要配置以下环境变量:
- SEARCHAPI_API_KEY: 用于网络搜索功能

## 构建运行
```bash
mvn clean install
mvn spring-boot:run
```

## API接口
- `/login`: 用户登录
- `/ai/chat`: 聊天助手接口
- `/ai/chat/title`: 生成对话标题
- 更多接口详见Controller层代码

## 项目结构
```
src/main/java/com/dai/
├── config/         # 配置类
├── controller/     # 控制器
├── service/       # 服务层
├── mapper/        # MyBatis映射
├── entity/        # 实体类
└── utils/         # 工具类
```