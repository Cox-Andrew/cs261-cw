Installation Documentation

System Requirements:
In order to host our system on your local machine you must have:

+ The capacity to run 4 docker style containers
+ The capability to then create a docker network to interconnect the containers
+ An exposure of the web port to an external IP/Port for access by users

Installation steps:

In our repository run buildimages.sh from docker/install

After creating the 4 images supplied with our code:

+ web
+ backend
+ database
+ pythonapp

Then create a docker network called mood-net. Start the docker images and connect them to a public IP/port. Then connect these containers to your mood-net network. Finally connect your browser to the port configured for the web, e.g. http://localhost:8000. An example file for creating the docker environment is provided with our code as a shell/batch file. In order to run it, the ports 8432, 8002, 8001 and 8000 must not be in use by your machine.
