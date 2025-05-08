FROM openjdk:17-jdk-slim
WORKDIR /app
COPY /target/TestTask.jar service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]