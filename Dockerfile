FROM openjdk:23-ea-17-slim
MAINTAINER qingxiaoyuan
WORKDIR /app
COPY ./yaoguang-api/target/yaoguang-api.jar /app
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone
EXPOSE 8080
CMD ["java", "-jar", "yaoguang-api.jar"]