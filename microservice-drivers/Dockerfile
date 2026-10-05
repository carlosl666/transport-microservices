FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY microservice-drivers/pom.xml /app/microservice-drivers/pom.xml
COPY microservice-drivers/src /app/microservice-drivers/src
RUN apk add --no-cache maven && \
    mvn -f /app/microservice-drivers/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=builder /app/microservice-drivers/target/*.jar app.jar
USER appuser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]