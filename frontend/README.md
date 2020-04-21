# Frontend Module

This is the web front end (HTML & Javascript) of the application

## Deploying on OpenShift 4

The module is based on a containerised instance of [nginx.com]](https://www.nginx.com/) web server. In this project the instance is deployed in the OCP 4 Lab of Red Hat Italy pre-sales organization.

Following you'll find instructions to deploy this module on an OpenShift 4 environment.

After having pulled the cose to your local filesystem You can locally run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```