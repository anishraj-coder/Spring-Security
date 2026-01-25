# Use the official Eclipse Temurin (Adoptium) image for Java 25
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Ensure the jar name matches your pom.xml artifactId
COPY target/atuthTest-0.0.1-SNAPSHOT.jar app.jar

# Your application.yml says port 8082
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
