# Build stage - Use Eclipse Temurin JDK for Maven
FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage - Use Eclipse Temurin JRE Alpine
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
RUN mkdir -p /app/questions /app/quizzes
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
