# mrt
Mileage Report Tool

## Deploy a local development environment using podman

If you are developing one of the modules of the project (suppose the API module or the front-end web application) and you want to setup a lab on your workstation to test the new implementations you can use podman to run images of the other modules you need to run the entire application.

what is the MRT architecture?

The application is used to build the monthly reports of the trips that a Sales Team member makes every month to meet customers. The use consists in a quick setup of the Associate information (mainly related to the car allowance policy) and a simple data entry to record all the trips for each associates.
The architecture of the application is quite simple and typical: there is a DB to persist above specified data, basically a couple of collections: *trips* and *associates*, the first one contains all the movements (the trips) and the second one the configuration for each single associate. There is a module developed in Java based on *Quarkus* runtime that can be containerized to be run in any environment. Finally there is a module that contains a web interface to interact with application, it's an *Nginx* server with the specific html pages. Last but not least the application is protected by an external IDP, it's a Red Hat SSO server running on the Red Hat Italy internet lab at the address https://access-sso.apps.ocp4.rhocplab.com/.

So to have a local lab on the developer workstation you need to run the following components:

1. The MongoDB (see the instructions below to run it);
2. The persistence module (see the instructions in the module readme);
3. The front-end module (see the instructions in the module readme);

## Deploy Mongo DB module on OCP Lab
steps to deploy DB

0. Access your OCP 4 installation
1. from developer view access or create the project for **MRT** application and choose "add" from catalog
2. choose Mongo DB and click instantiate
3. in the properties page specify "connection username" & "connection password"
4. specify the database name: **mrt**

Wait for the instance creation then access the pod clicking on mongodb icon in the topology view, click on pod instance and click on terminal tab.
This will open a shell in the container, enter
    > mongo -u <dbuser> mrt
command, at the prompt asking for password enter the password (the username and relative password are those that you have specified at the point 3 of the above sequence) and enter "show collections;" command. Actually the first time you start the container this command will return nothing. You can try to enter an element in one of the collections the tool will use, to do it follow these steps:

1. enter the following command to create an entry in the *locations* collaction:
        > db.locations.insertOne({"destination":"test","distance":100});
2. repeat the command:
        > show collections;
3. enter the command:
        > db.locations.find();

the output should be the same object you specified at point 1.
To clean the DB after the test enter the command:

    > db.locations.drop();

## Deploy Mongo DB module locally using podman
If you are using **podman** as local container manager and you want to setup a local environment with the same containers used in OCP 4 you just need to start the container with the following command.
[NOTE] This example assumes you are using podman in rootless mode.

    > podman run --name mongodb -d -p 27017:27017 mongo:3.6

If you want to access the container to execute the same test of OCP Lab, use the following command:

    > podman exec -it mongodb /bin/bash

## DB Persistence
When you start a Mongo DB instance using the template from the OCP catalog as specified above in this document you can choice between two templates, one is *ephemeral*. The ephemeral can be used in a test environment, but if you want to persist data you have to choose the other template. That template provisions the MongoDB container with a Persistent Volume Claim so that data enterd into the DB will be persisted.

In a local environment on the other hand if you want to persist data you have to manually mount a local volume to the running container. In this case use the following command instead the one specified in the previous paragraph:

    > podman run -v /my/own/datadir:/data/db --name mongodb -d -p 27017:27017 mongo:3.6

When using a persistent volume you may incur a problem related to file system privileges, something like:

    > find: '/data/db': Permission denied
    > chown: changing ownership of '/data/db': Permission denied

In this case, if you are running the lab in local secured environment you may try to use the following command (note the *privileged* parameter):

    > podman run -v /my/own/datadir:/data/db --privileged --name mongodb -d -p 27017:27017 mongo:3.6
