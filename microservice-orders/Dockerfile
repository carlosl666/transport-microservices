FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml /app/pom.xml
COPY microservice-orders/pom.xml /app/microservice-orders/pom.xml
COPY microservice-orders/src /app/microservice-orders/src
RUN apk add --no-cache maven && \
    mvn -f /app/microservice-orders/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
COPY --from=builder /app/microservice-orders/target/*.jar app.jar
USER appuser
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]