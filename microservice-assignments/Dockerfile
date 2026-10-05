FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY microservice-assignments/pom.xml /app/microservice-assignments/pom.xml
COPY microservice-assignments/src /app/microservice-assignments/src
RUN apk add --no-cache maven && \
    mvn -f /app/microservice-assignments/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && \
    mkdir -p /app/uploads && chown -R appuser:appgroup /app/uploads
COPY --from=builder /app/microservice-assignments/target/*.jar app.jar
USER appuser
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]