# Use official OpenJDK 21 image
FROM eclipse-temurin:21-jdk as build

# Set working directory inside the container
WORKDIR /app

# Copy project files
COPY . .

# Build the project using Maven wrapper
RUN ./mvnw clean install -DskipTests

# Use a smaller image just to run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/Medconnect-0.0.1-SNAPSHOT.jar app.jar

# Expose port (Render uses $PORT env variable)
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "app.jar"]
