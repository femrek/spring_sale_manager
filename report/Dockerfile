# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY report/src /home/app/report/src
COPY report/pom.xml /home/app/report
RUN mvn -f /home/app/report/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/report/target/report-0.0.1-SNAPSHOT.jar  /usr/local/lib/report-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/report-0.0.1.jar"]
