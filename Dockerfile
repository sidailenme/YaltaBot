FROM adoptopenjdk/openjdk16:centos
ARG JAR_FILE=target/*spring-boot.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]