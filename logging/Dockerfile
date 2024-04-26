# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY logging/src /home/app/logging/src
COPY logging/pom.xml /home/app/logging
RUN mvn -f /home/app/logging/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/logging/target/logging-0.0.1-SNAPSHOT.jar  /usr/local/lib/logging-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/logging-0.0.1.jar"]
