# Package stage
FROM eclipse-temurin AS package
COPY --from=app-builder /home/app/report/target/report-0.0.1-SNAPSHOT.jar  /usr/local/lib/report-0.0.1.jar
COPY --from=app-builder /home/app/log4j2.xml /usr/local/lib/log4j2.xml
ENV LOG4J2_CONFIG_FILE=/usr/local/lib/log4j2.xml

ENTRYPOINT ["java", "-jar", "/usr/local/lib/report-0.0.1.jar"]
