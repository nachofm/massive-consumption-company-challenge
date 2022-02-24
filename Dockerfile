FROM openjdk:11-jre-slim
COPY target/massive-consumption-company-challenge-docker.jar /massive-consumption-company-challenge-docker.jar
ENTRYPOINT ["java", "-jar", "/massive-consumption-company-challenge-docker.jar"]
