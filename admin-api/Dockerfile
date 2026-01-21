FROM --platform=linux/amd64 maven:3.8-openjdk-11 as builder
COPY pom.xml .
COPY src src
RUN mvn clean package -Dmaven.test.skip

## Use the official OpenJDK 11 base image
FROM --platform=linux/amd64 openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Java application JAR file to the container
COPY --from=builder target/*.jar app.jar
COPY --from=builder src/main/resources/application.yaml config/application.yaml
# Expose the port on which your Java application listens
EXPOSE 8088

# Execute the Java application
CMD ["java", "-Dspring.config.location=/app/config/application.yaml", "-jar", "app.jar"]
