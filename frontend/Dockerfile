FROM registry.redhat.io/rhel8/nginx-116
COPY ./ /tmp/src/
RUN $STI_SCRIPTS_PATH/assemble
CMD $STI_SCRIPTS_PATH/run
