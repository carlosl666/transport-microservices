FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY microservice-api-gateway/pom.xml /app/microservice-api-gateway/pom.xml
COPY microservice-api-gateway/src /app/microservice-api-gateway/src
RUN apk add --no-cache maven && \
    mvn -f /app/microservice-api-gateway/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=builder /app/microservice-api-gateway/target/*.jar app.jar
USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]