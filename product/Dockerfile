# Build stage
FROM maven AS build

## compile the core module
COPY common-codebase/src /home/app/common-codebase/src
COPY common-codebase/pom.xml /home/app/common-codebase
RUN mvn -f /home/app/common-codebase/pom.xml install

## compile the project
COPY product/src /home/app/product/src
COPY product/pom.xml /home/app/product
RUN mvn -f /home/app/product/pom.xml package

# Package stage
FROM eclipse-temurin AS package
COPY --from=build /home/app/product/target/product-0.0.1-SNAPSHOT.jar  /usr/local/lib/product-0.0.1.jar

ENTRYPOINT ["java", "-jar", "/usr/local/lib/product-0.0.1.jar"]
