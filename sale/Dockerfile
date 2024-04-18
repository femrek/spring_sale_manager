# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY sale/src /home/app/sale/src
COPY sale/pom.xml /home/app/sale
RUN mvn -f /home/app/sale/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/sale/target/sale-0.0.1-SNAPSHOT.jar  /usr/local/lib/sale-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/sale-0.0.1.jar"]
