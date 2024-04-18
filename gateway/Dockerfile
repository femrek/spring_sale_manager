# Build stage
FROM maven AS build

## compile the project
COPY gateway/src /home/app/gateway/src
COPY gateway/pom.xml /home/app/gateway
RUN mvn -f /home/app/gateway/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/gateway/target/gateway-0.0.1-SNAPSHOT.jar  /usr/local/lib/gateway-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/gateway-0.0.1.jar"]
