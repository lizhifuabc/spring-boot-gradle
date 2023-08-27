# 基础镜像 使用alibabadragonwell/dragonwell:17-anolis
FROM alibabadragonwell/dragonwell:17-anolis as builder
# 在容器内创建目录。
RUN mkdir -p /spring-boot-gradle
# 切换工作目录
WORKDIR /spring-boot-gradle
# 指定应用程序的 JAR 文件路径
ARG JAR_FILE=/app/spring-boot-gradle.jar
ARG SH_FILE=/app/entrypoint.sh
# 复制本地主机的 （Dockerfile 所在目录的相对路径）到容器中的
# 将构建上下文中的应用程序 JAR 文件复制到容器内的 app.jar
COPY ${JAR_FILE} app.jar
COPY ${SH_FILE} entrypoint.sh

# 指定 Spring Boot 的 "layertools" 模式，来执行 JAR 内容的提取操作，把 JAR 文件中的不同分层提取到不同的目录中，准备用于构建 Docker 镜像
# 删除之前的 JAR 文件，提取内容后，JAR 文件不再需要  && rm app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# 使用相同的基础镜像， 作为第二个阶段
FROM alibabadragonwell/dragonwell:17-anolis
# 切换工作目录
WORKDIR /spring-boot-gradle

# 从第一个阶段中，拷贝之前提取的分层内容到当前目录（可以添加用户支持 COPY --chown=1001:1001 --from=builder /app/dependencies/ ./）
COPY --from=builder spring-boot-gradle/dependencies/ ./
COPY --from=builder spring-boot-gradle/snapshot-dependencies/ ./
COPY --from=builder spring-boot-gradle/spring-boot-loader/ ./
COPY --from=builder spring-boot-gradle/application/ ./
COPY --from=builder spring-boot-gradle/entrypoint.sh ./

# 添加执行权限
RUN chmod +x entrypoint.sh

# 声明容器将监听的端口号为
EXPOSE 9991
# 设置为 /dev/./urandom，以提高随机数生成的性能
ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

# 设置默认的命令为 exec
CMD ["exec"]
# entrypoint.sh脚本使用exec将java作为容器1号进程，避免无法接受linux 信号导致java进程无法正常退出
ENTRYPOINT ["bash","entrypoint.sh"]

# 设置默认的命令为 java -jar app.jar
# ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

