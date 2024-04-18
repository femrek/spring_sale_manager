# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY auth/src /home/app/auth/src
COPY auth/pom.xml /home/app/auth
RUN mvn -f /home/app/auth/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/auth/target/auth-0.0.1-SNAPSHOT.jar  /usr/local/lib/auth-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/auth-0.0.1.jar"]
