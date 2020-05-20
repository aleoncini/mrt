# Build stage
FROM registry.redhat.io/ubi8/openjdk-8 as build
COPY --chown=jboss:jboss ./pom.xml ./
RUN mvn dependency:go-offline -B
COPY --chown=jboss:jboss ./ ./
RUN mvn clean package

# Create final image
FROM registry.redhat.io/ubi8/openjdk-8
COPY --from=build /home/jboss/target/backend-1.0-SNAPSHOT-runner.jar ./
COPY --from=build /home/jboss/target/lib ./lib/
EXPOSE 8080
ENV MONGOCONNSTRING mongodb://localhost:27017
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dquarkus.mongodb.mrt.connection-string=${MONGOCONNSTRING}","-jar","./backend-1.0-SNAPSHOT-runner.jar"]
