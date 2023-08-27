# Spring boot + Gradle + Docker

创建项目： 

![1.png](image%2F1.png)

## 配置文件解析

**gradle-wrapper.properties:** gradle版本控制

**build.gradle：** 项目gradle文件，可以添加依赖包，或者写一些自定义的任务脚本等。

**settings.gradle：** 控制多module

## 默认打包命令

默认打包命令 bootJar：./gradlew bootJar

![2.png](image%2F2.png)

## 自定义打包命令

修改 build.gradle 文件，添加打包命令：

```shell
def jarName = String.format("%s-%s.jar",project.name,version)
// 拷贝文件 dependsOn: bootJar 依赖springboot 插件的 bootJar打包命令
tasks.register('copyConfigFile', Copy) {
    dependsOn bootJar
    // 清除app目录的历史文件
    delete "app/"
    // copy jar包 从 build/libs/ 目录到 app/ 目录
    from('build/libs/' + jarName)
    into 'app/'
    // 重命名成我们要的名字
    rename(jarName, project.name + '.jar')
}
// 依赖 clean 任务
tasks.register('buildTodoCoderJar') {
    dependsOn clean
    dependsOn copyConfigFile
}
```

执行命令： ./gradlew buildTodoCoderJar

##  基于Docker部署

```dockerfile
# 基础镜像 使用alibabadragonwell/dragonwell:17-anolis
FROM alibabadragonwell/dragonwell:17-anolis
# 在容器内创建目录。
RUN mkdir -p /spring-boot-gradle
# 切换工作目录
WORKDIR /spring-boot-gradle
# 指定应用程序的 JAR 文件路径
ARG JAR_FILE=/app/spring-boot-gradle.jar
# 将构建上下文中的应用程序 JAR 文件复制到容器内的 app.jar
COPY ${JAR_FILE} app.jar
# 声明容器将监听的端口号为
EXPOSE 9083
# 设置为 /dev/./urandom，以提高随机数生成的性能
# # jvm启动参数
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

# 容器启动后,先睡眠3秒钟
# 然后再执行java命令启动应用
CMD sleep 3; java $JAVA_OPTS -jar app.jar
```

执行命令：bash build.sh

```shell
#!/bin/bash
# 打jar包
./gradlew buildTodoCoderJar
# 构建docker镜像
docker build -t spring-boot-gradle/spring-boot-gradle:v1.0.0 .
# 运行镜像
docker run --name=spring-boot-gradle -d -p 9083:9083 spring-boot-gradle/spring-boot-gradle:v1.0.0
```

## 基于Docker部署,分层打包

```dockerfile
# layers/Dockerfile
```

