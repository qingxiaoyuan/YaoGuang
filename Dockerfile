FROM openjdk:23-ea-17-slim
MAINTAINER qingxiaoyuan
WORKDIR /app
COPY ./yaoguang-api/target/yaoguang-api.jar /app
EXPOSE 8080
CMD ["java", "-jar", "yaoguang-api.jar"]