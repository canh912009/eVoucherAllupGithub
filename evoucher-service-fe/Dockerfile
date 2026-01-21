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
COPY --from=builder src/main/resources/application.properties config/application.properties 
COPY --from=builder src/main/resources/message_template_en.json message_template_en.json
COPY --from=builder src/main/resources/transfer_message_template_en.json transfer_message_template_en.json
# Expose the port on which your Java application listens
EXPOSE 19932

# Execute the Java application
CMD ["java", "-Dspring.config.location=/app/config/application.properties", "-jar", "app.jar"]
