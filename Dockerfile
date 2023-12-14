#
# Build stage
#
FROM maven:3.9.5-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:22-ea-17-jdk-slim
COPY --from=build /home/app/target/attendance-system-1.0.1-BETA.jar /usr/local/lib/attendance-system.jar
EXPOSE 7000
EXPOSE 80
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/attendance-system.jar"]