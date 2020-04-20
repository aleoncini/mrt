# Gatekeeper Module

THis is a secure web proxy that is used to protect all the web components of the MRT Project.
It is Based on [Keycloak Gatekeeper](https://github.com/keycloak/keycloak-gatekeeper) project.
## Deploying on OpenShift 4

After having pulled the cose to your local filesystem You can locally run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## appunti

       oc login -u aleoncin https://api.ocp4.rhocplab.com:6443
 4698  oc get configmaps
 4699  cd /home/andrea/worklab/projects/prove/gatekeeper/
 4700  oc create configmap secure-proxy-config --from-file=config.yaml 
 4701  oc create deployment --help
 4702  oc create --help
 4703  oc create deployment -h
 4704  oc create -n spid -f deploymentconfig-keycloak-gatekeeper.yaml 
 4705  oc rollout latest dc/keycloak-gatekeeper 
 4706  oc rollout history dc/keycloak-gatekeeper 
 4707  oc get deployments
 4708  oc rollout history keycloak-gatekeeper 
 4709  oc get services
 4710  oc get svc
