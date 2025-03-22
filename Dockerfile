FROM maven:3.8.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR
COPY --from=build /app/target/*.jar app.jar

# Copy the questions folder (contains db.txt and images)
COPY questions/ questions/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
