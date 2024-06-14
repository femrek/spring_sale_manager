# Build stage
FROM maven AS build

## copy the cached maven dependencies
COPY --from=dependencies /root/.m2 /root/.m2

## copy the log configuration file
COPY log4j2.xml /home/app/log4j2.xml
ARG LOG4J2_CONFIG_FILE=/home/app/log4j2.xml

## compile the project
COPY service-registry/src /home/app/service-registry/src
COPY service-registry/pom.xml /home/app/service-registry
RUN mvn -f /home/app/service-registry/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/service-registry/target/service-registry-0.0.1-SNAPSHOT.jar  /usr/local/lib/service-registry-0.0.1.jar
COPY --from=build /home/app/log4j2.xml /usr/local/lib/log4j2.xml
ENV LOG4J2_CONFIG_FILE=/usr/local/lib/log4j2.xml

ENTRYPOINT ["java", "-jar", "/usr/local/lib/service-registry-0.0.1.jar"]
