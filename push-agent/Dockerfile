FROM --platform=linux/amd64 maven:3.8-openjdk-11 as builder
COPY pom.xml .
COPY src src
RUN mvn clean package -Dmaven.test.skip

## Use the official OpenJDK 11 base image
FROM --platform=linux/amd64 openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Java application JAR file to the container
COPY --from=builder target/push-agent*.jar app.jar
COPY --from=builder src/main/resources/application.properties config/application.properties 
# Expose the port on which your Java application listens
EXPOSE 8085

# Execute the Java application
CMD ["java", "-Dspring.config.location=/app/config/application.properties", "-jar", "app.jar"]
