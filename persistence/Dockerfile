# Build stage using Red Hat UBI 8 base image to compile and package application
FROM registry.redhat.io/ubi8/openjdk-11 as build
COPY --chown=jboss:jboss ./pom.xml ./
RUN mvn dependency:go-offline -B
COPY --chown=jboss:jboss ./ ./
RUN mvn clean package -DskipTests -Dquarkus.package.type=uber-jar

# Create final image
FROM registry.redhat.io/ubi8/openjdk-11
COPY --from=build /home/jboss/target/persistence-1.0.0-SNAPSHOT-runner.jar ./
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dquarkus.mongodb.mrt.connection-string=${MONGOCONNSTRING}","-jar","./persistence-1.0.0-SNAPSHOT-runner.jar"]