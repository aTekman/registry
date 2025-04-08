FROM gradle:8.5.0-jdk21 as build
COPY . /app
WORKDIR /app
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]