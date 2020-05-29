# Build stage
FROM registry.redhat.io/ubi8/openjdk-8 as build
COPY --chown=jboss:jboss ./pom.xml ./
RUN mvn dependency:go-offline -B
COPY --chown=jboss:jboss ./ ./
RUN mvn clean package -DskipTests

# Create final image
FROM registry.redhat.io/ubi8/openjdk-8
COPY --from=build /home/jboss/target/pdf-1.0-SNAPSHOT-runner.jar ./
COPY --from=build /home/jboss/target/lib ./lib/
USER root
RUN mkdir /reports \
    && chown 1001 /reports \
    && chmod "g+rwX" /reports \
    && chown 1001:root /reports
COPY --chown=1001:root ./src/main/resources/RHBannerNew.png /reports

EXPOSE 8080
USER 1001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","./pdf-1.0-SNAPSHOT-runner.jar"]