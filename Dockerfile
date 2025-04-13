# Use Maven image with JDK 17 for building
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (to cache this layer)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the project
COPY . .

# Build the application (skip tests for speed)
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image for runtime
FROM openjdk:17-jdk-slim

# Set workdir
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/LMS-0.0.1-SNAPSHOT.jar app.jar

# Expose the app port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
