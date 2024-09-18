FROM openjdk:24-jdk-slim

WORKDIR /app

COPY target/dittoWodtThingMock-1.0.0-jar-with-dependencies.jar /app/dittoWodtThingMock-1.0.0-jar-with-dependencies.jar
COPY src/main/resources/ /app/

CMD ["java", "-jar", "/app/dittoWodtThingMock-1.0.0-jar-with-dependencies.jar"]