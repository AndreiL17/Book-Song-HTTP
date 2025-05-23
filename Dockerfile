# We Take the docker image from https://hub.docker.com/_/eclipse-temurin
FROM eclipse-temurin:23-alpine AS builder

# Set our working directory
WORKDIR /app

# Copy maven wrappers and main pom file to the container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Module pom files
COPY Application/pom.xml ./Application/pom.xml
COPY Books/pom.xml ./Books/pom.xml
COPY Reviews/pom.xml ./Reviews/pom.xml
COPY Albums/pom.xml ./Albums/pom.xml

# Copy the rest to the container (source code etc)
COPY . .

# Convert line endings from CLRF to LF. I do this because git keeps changing these back to CLRF and that breaks bash scripts such as this.
RUN apk add --no-cache dos2unix
RUN dos2unix ./mvnw

# Make sure we have perms to execute on the script
RUN chmod +x ./mvnw

# Install dependencies inside the container
# This can fail if git changes line endings to CLRF when committing. Bash scripts need to be LF
RUN ./mvnw clean package -DskipTests

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# For overriding with "docker compose run ...", mainly for tests
ENTRYPOINT ["sh", "-c"]

# Default
CMD ["java -jar ./Application/target/Application-0.0.1-SNAPSHOT.jar"]
