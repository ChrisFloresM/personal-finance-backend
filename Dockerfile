#1 Download dependencies
FROM maven:3.9.9-eclipse-temurin-21 as builder

WORKDIR /app

#Copy and run dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

#Copy code
COPY src src
RUN mvn clean package -DskipTests

#2 Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=builder app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
