# Build stage
FROM maven AS build

## compile the project
COPY src /home/app/service-registry/src
COPY pom.xml /home/app/service-registry
RUN mvn -f /home/app/service-registry/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/service-registry/target/service-registry-0.0.1-SNAPSHOT.jar  /usr/local/lib/service-registry-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/service-registry-0.0.1.jar"]
