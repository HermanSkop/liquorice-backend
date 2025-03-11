# Use an OpenJDK 17 base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

COPY ./target/liquorice-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app is running on (8080)
EXPOSE 8080

# Set the entrypoint to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
