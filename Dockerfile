FROM openjdk:17-jdk-alpine

VOLUME /tmp

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=UTC", "-jar","/app.jar"]
