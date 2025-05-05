# 使用 CentOS 7 作为基础镜像
FROM centos:7

# 设置环境变量
ENV JAVA_HOME=/root/jdk/jdk-21.0.1
ENV PATH=$JAVA_HOME/bin:$PATH

#统一编码
ENV LANG=en_US.UTF-8
ENV LANGUAGE=en_US:en
ENV LC_ALL=en_US.UTF-8

# 创建应用目录
RUN mkdir -p /app
WORKDIR /app

# 复制应用 JAR 文件到容器
COPY  AIHub-Backend-0.0.1-SNAPSHOT.jar  AIHub-Backend-0.0.1-SNAPSHOT.jar
COPY  /root/mcp/mcp.jar  mcp.jar

# 暴露端口
EXPOSE 8080

# 运行命令
ENTRYPOINT ["java","-jar","/tlias/tlias.jar"]

$ docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:tag

docker run -d --name neo4j -p 7474:7474 -p 7687:7687 -e NEO4J_dbms_memory_heap_initial__size=256m -e NEO4J_dbms_memory_heap_max__size=256m --memory="256m" neo4j:4.4.2