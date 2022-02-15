FROM registry.access.redhat.com/ubi8/nodejs-14 as build

COPY --chown=1001:0 ./ /tmp/src/

RUN /usr/libexec/s2i/assemble

CMD /usr/libexec/s2i/run

# Serve app with nginx server

FROM registry.access.redhat.com/ubi8/nginx-118

COPY --from=build --chown=1001:0 /opt/app-root/src/dist/frontend-ang /tmp/src/

# Add nginx configuration
ADD ./nginx.conf "${NGINX_CONF_PATH}"
ADD ./nginx-default-cfg/*.conf "${NGINX_DEFAULT_CONF_PATH}"

# Run script uses standard ways to run the application
CMD nginx -g "daemon off;"