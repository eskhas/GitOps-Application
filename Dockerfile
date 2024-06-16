FROM eclipse-temurin:17-jdk-focal
ADD build/libs/my-app-0.0.1-SNAPSHOT.jar my-app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "my-app.jar"]