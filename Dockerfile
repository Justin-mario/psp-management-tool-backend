FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

#Copy the entire project, not just src
COPY . /app

# Use mvn instead of ./mvn
RUN mvn clean package

EXPOSE 8080

# Adjust this CMD to match your JAR file name
CMD ["java", "-jar", "target/psp-management-tool-0.0.1-SNAPSHOT.jar"]

## Build stage
#FROM maven:3.8.4-openjdk-21 as build
#WORKDIR /app
#COPY . .
#RUN mvn clean package -DskipTests
#
## Run stage
#FROM openjdk:21-jdk-slim
#WORKDIR /app
#COPY --from=build /app/target/psp-management-tool-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","app.jar"]