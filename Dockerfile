FROM openjdk:17
WORKDIR /app
COPY /target/TaskManagementSystem-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080