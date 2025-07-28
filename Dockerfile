# 第一阶段：使用 Maven + JDK 17 构建 jar
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/healthflowbackend-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

