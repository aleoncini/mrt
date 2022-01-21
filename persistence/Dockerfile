# Build stage using Red Hat UBI 8 base image to compile and package application
FROM registry.access.redhat.com/ubi8/openjdk-11 as build

ENV S2I_SOURCE_DEPLOYMENTS_FILTER="*-runner.jar lib"
ENV MAVEN_ARGS_APPEND="-Dquarkus.package.type=uber-jar"

COPY --chown=jboss:0 ./ /tmp/src

RUN /usr/local/s2i/assemble

# Create final image
FROM registry.access.redhat.com/ubi8/openjdk-11
COPY --chown=jboss:0 --from=build /deployments /deployments