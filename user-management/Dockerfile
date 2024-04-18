# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY user-management/src /home/app/user-management/src
COPY user-management/pom.xml /home/app/user-management
RUN mvn -f /home/app/user-management/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/user-management/target/user-management-0.0.1-SNAPSHOT.jar  /usr/local/lib/user-management-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/user-management-0.0.1.jar"]
