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

## Configure audience in Keycloak

*ERROR* unable to verify the id token	{"error": "oidc: JWT claims invalid: invalid claims, 'aud' claim and 'client_id' do not match, aud=account, client_id=kgk"}

1. Go to the realm configuration
2. if not already available create a specific client that manage the Keycloak Gatekeeper secure proxy (in my case I named it "kgk")
3. On the left menu go to "Client Scopes" menu
4. Add a new Client scope 'mrt-service'
5. Open the 'Mappers' tab in the settings page of the 'mrt-service'
6. Create Protocol Mapper 'mrt-audience'
7. Choose Mapper type: Audience, Included Client Audience: kgk, Add to access token: on
8. Now open the configuration page for client 'kgk' in the "Clients" menu
Client Scopes tab in my-app settings
Add available client scopes "good-service" to assigned default client scopes
If you have more than one client repeat the steps for the other clients as well and add the good-service scope. The intention behind this is to isolate client access. The issued access token will only be valid for the intended audience. This is thoroughly described in Keycloak's documentation [1,2].