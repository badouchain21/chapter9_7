## 使用 JDK 8 环境为基础环境，如果镜像不是本地的将会从 DockerHub 进行下载
#FROM badousoft/tomcat8.5.24
## 在宿主机的 /var/lib/docker 目录下创建一个临时文件并把它链接到 tomcat 容器的工作目录 /tmp目录
#VOLUME /tmp
## 复制文件并重命名系统包表示打包后的war包名称
#ADD ./target/initial-project-1.0.0.SNAPSHOT.war /usr/local/tomcat/webapps/zydb_platform.war
#ENV TZ=Asia/Shanghai
#ENV LANG C.UTF-8
## 为了缩短Tomcat启动时间，添加 java.security.egd 的系统属性指向 /dev/urandom 作为ENTRYPOINT
#ENTRYPOINT ["/usr/local/tomcat/bin/catalina.sh","run"]


FROM java:8
ADD ./target/initial-project-1.0.0.SNAPSHOT.jar zydb_platform.jar
EXPOSE 9003
ENTRYPOINT ["java","-Dserver.servlet.context-path=/zydb_platform","-jar","zydb_platform.jar"]