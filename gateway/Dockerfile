# Build stage
FROM maven AS build

## copy the log configuration file
COPY log4j2.xml /home/app/log4j2.xml
ARG LOG4J2_CONFIG_FILE=/home/app/log4j2.xml

## compile the project
COPY gateway/src /home/app/gateway/src
COPY gateway/pom.xml /home/app/gateway
RUN mvn -f /home/app/gateway/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/gateway/target/gateway-0.0.1-SNAPSHOT.jar  /usr/local/lib/gateway-0.0.1.jar
COPY --from=build /home/app/log4j2.xml /usr/local/lib/log4j2.xml
ENV LOG4J2_CONFIG_FILE=/usr/local/lib/log4j2.xml

ENTRYPOINT ["java", "-jar", "/usr/local/lib/gateway-0.0.1.jar"]
