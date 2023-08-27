#!/bin/bash
# 打jar包
./gradlew buildTodoCoderJar
# 构建docker镜像
docker build -t spring-boot-gradle/spring-boot-gradle:v1.0.0 .
# 运行镜像
docker run --name=spring-boot-gradle -d -p 9083:9083 spring-boot-gradle/spring-boot-gradle:v1.0.0