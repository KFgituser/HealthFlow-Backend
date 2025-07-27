# 第一阶段：使用 Maven 构建 jar 文件
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二阶段：运行构建好的 jar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 10000
CMD ["java", "-jar", "app.jar"]