# Use a lightweight OpenJDK image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy everything from your project into the container
COPY . .

# Run Maven build to package the app
RUN ./mvnw clean install

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Start the app
CMD ["java", "-jar", "target/medconnect-0.0.1-SNAPSHOT.jar"]
