# mrt
Mileage Report Tool

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

