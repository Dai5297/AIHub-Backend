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
  - JWT认证体系（支持Token自动续期）
  - 完整用户生命周期管理（注册/登录/密码重置/注销）
  - 基于角色的权限控制（RBAC基础实现）
- **技术实现**:
  - Spring Security + JWT工具库（使用HMAC-SHA512算法）
  - MySQL数据库存储用户凭证（密码使用BCrypt加密）
  - Redis用于token黑名单管理（支持集群部署）
  - 邮件服务集成（JavaMailSender实现验证码发送）
- **安全增强**:
  - 登录失败次数限制（基于Redis的滑动窗口限流）
  - 密码复杂度校验（正则表达式验证）

### 2. 通用聊天模块 (chatAI)
- **核心功能**:
  - 基于LangChain4j的对话交互（支持流式响应）
  - 实时联网搜索功能（集成SearchAPI多引擎支持）
  - 对话历史持久化（支持跨设备同步）
- **技术实现**:
  - LangChain4j集成Qwen大模型（配置温度系数0.7）
  - WebSearchEngine实现搜索增强（支持Google/Bing自定义搜索）
  - ChatMemoryStore持久化对话历史（MySQL存储结构优化）
  - 使用chat_sessions表管理会话状态（包含最后访问时间TTL机制）
  - 支持Markdown格式输出（前端自动渲染）
- **特色能力**:
  - 多级缓存机制（本地Caffeine+Redis二级缓存）
  - 敏感词过滤（基于DFA算法实现）

### 3. 医疗助手专家模块 (chatMedical)
- **核心功能**:
  - 医疗知识图谱查询（7类实体+10种关系）
  - 症状自检辅助（基于决策树算法）
  - 药物相互作用查询
- **技术实现**:
  - LangChain4j集成Qwen大模型（医疗垂直领域微调）
  - Neo4j存储和查询医疗知识图谱（Cypher查询优化）
  - 使用APOC库实现复杂路径查询
  - 医疗数据更新脚本（Python定时任务每日同步）
  - 构建医疗知识图谱的实体关系：