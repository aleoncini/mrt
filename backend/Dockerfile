## Stage 1 : build with maven builder image with native capabilities
FROM quay.io/quarkus/centos-quarkus-maven:19.3.1-java11 AS build
ARG MAVEN_EXTRA_ARGS=
ARG NATIVE_BUILD_MEMORY=4g
RUN echo "$MAVEN_EXTRA_ARGS"
WORKDIR /backend
COPY ./ ./
USER root
RUN chown -R quarkus ./
USER quarkus
RUN mvn clean package -Pnative -Dnative-image.xmx=$NATIVE_BUILD_MEMORY $MAVEN_EXTRA_ARGS
RUN mkdir -p /tmp/ssl-libs/lib \
  && cp /opt/graalvm/lib/security/cacerts /tmp/ssl-libs \
  && cp /opt/graalvm/lib/libsunec.so /tmp/ssl-libs/lib/

## Stage 2 : create the docker final image
FROM registry.access.redhat.com/ubi8/ubi-minimal
WORKDIR /work/
COPY --from=build /backend/target/*-runner /work/application
COPY --from=build /tmp/ssl-libs/ /work/
RUN chmod 775 /work
EXPOSE 8080
CMD ["./application", "-Dquarkus.http.host=0.0.0.0", "-Djava.library.path=/work/lib", "-Djavax.net.ssl.trustStore=/work/cacerts"]
