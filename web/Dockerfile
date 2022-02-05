FROM registry.access.redhat.com/ubi8/nginx-118

# Add application sources
ADD ./nginx.conf "${NGINX_CONF_PATH}"
ADD ./nginx-default-cfg/*.conf "${NGINX_DEFAULT_CONF_PATH}"
#ADD test-app/nginx-cfg/*.conf "${NGINX_CONFIGURATION_PATH}"
ADD ./ .

# Run script uses standard ways to run the application
CMD nginx -g "daemon off;"