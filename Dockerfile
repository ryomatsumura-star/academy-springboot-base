FROM eclipse-temurin:17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon
FROM eclipse-temurin:17-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]