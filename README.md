# mrt
Mileage Report Tool

This application is available at [aleoncini.github.io/mrt](https://aleoncini.github.io/mrt/)
  
Previous versions have been developed as server-side, you can reade about the old architecture and deployment options by going [here](https://aleoncini.github.io/mrt/)

### Build a containerized application using podman

If you want to build a container that you can then run locally which serves the pages with the application you can:

1. go to the directory where you cloned this project:
   > git clone https://github.com/aleoncini/mrt.git
   > cd mrt
2. build the container using Podman:
   > podman build -t mrt .
3. run the container locally:
   > podman run --name mrtapp -d --rm -p 8080:8080 mrt
4. access the web page containing the application using your browser:
   > open http://localhost:8080/

### What is the MRT architecture?

As already said, previously this application was designed to be a three levels web application, with a persistence module,
a Front-End module and the browser as client.

In this latest version it is a stand alone application served as single web page. 
The persistent data used by the application are stored locally in the browser, that means that:
1. if you change browser you won't find any saved data
2. if you change location of the serving server you won't find any saved data too.
3. there are no data saved server side, information inserted by the users remain in the local space of the browser.

So the suggestion is to use the github public pages until you need specific requirements to run the server internally or in the user's workstation workplace.