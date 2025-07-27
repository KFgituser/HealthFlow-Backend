# 使用官方 Java 17 镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建后的 jar 文件到容器中
COPY target/*.jar app.jar

# 暴露端口（Render 要求使用 $PORT 环境变量）
EXPOSE 10000

# 运行 jar 包
CMD ["java", "-jar", "app.jar"]
