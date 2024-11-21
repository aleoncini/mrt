FROM registry.access.redhat.com/ubi9/nginx-124
USER 0
COPY docs /tmp/src/

RUN chown -R 1001:0 /tmp/src
USER 1001

RUN $STI_SCRIPTS_PATH/assemble
CMD $STI_SCRIPTS_PATH/run