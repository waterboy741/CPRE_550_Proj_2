# CPRE 5500 Project 2 CORBA
[Github Repository](https://github.com/waterboy741/CPRE_550_Proj_2)  
[Docker Image](https://hub.docker.com/repository/docker/willgalles/corba_container/general)  

This project set out to learn the CORBA communication protocol. This project consists of a client program and a server program that communicate via CORBA to create a restaurant management program.
## Four Methods for running code
### Docker Compose 
The latest build of the code has been compiled into a minimal docker image that contains all of the needed dependencies to run the client and the server
```
download the compose.yaml file in this repo
modify it to map to your current X11 display (instructions in yaml file)
run docker compose up
verify the containers are running and grab their names with
  docker ps
in a separate terminal start the server with
  docker exec -it <server container name> /bin/bash
  example: docker exec -it cpre_550_proj_2-server-1 /bin/bash
  java -jar /home/RestaurantServer.jar -ORBInitialPort 1050 -ORBInitialHost localhost
in a separate terminal start the first client with
  docker exec -it <client 1 container name> /bin/bash
  example: docker exec -it cpre_550_proj_2-client1-1 /bin/bash
  java -jar /home/RestaurantClient.jar -ORBInitialPort 1050 -ORBInitialHost <server container name>
  example java -jar /home/RestaurantClient.jar -ORBInitialPort 1050 -ORBInitialHost cpre_550_proj_2-server-1
in a separate terminal start the second client with
  docker exec -it <client 1 container name> /bin/bash
  example: docker exec -it cpre_550_proj_2-client2-1 /bin/bash
  java -jar /home/RestaurantClient.jar -ORBInitialPort 1050 -ORBInitialHost <server container name>
  example java -jar /home/RestaurantClient.jar -ORBInitialPort 1050 -ORBInitialHost cpre_550_proj_2-server-1
```

### Build and run using the included vscode dev container
```
git clone https://github.com/waterboy741/CPRE_550_Proj_2.git
ensure you have docker installed
ensure you have the devcontainers extension installed for vscode
open folder up in vscode
click on prompt that asks to open up folder in container
open a terminal in vscode and navigate to the src folder
run idlj -fall Restaurant.idl
run javac *.java RestaurantApp/*.java
run orbd -ORBInitialPort 1050
in a new terminal run java RestaurantServer -ORBInitialPort 1050 -ORBInitialHost localhost
in a new terminal run java RestaurantClient -ORBInitialPort 1050 -ORBInitialHost localhost
```

### Build and Run locally
```
git clone https://github.com/waterboy741/CPRE_550_Proj_1.git
Ensure you have Java 8 installed as it is the last version that supports CORBA natively
navigate to the src folder
run idlj -fall Restaurant.idl
run javac *.java RestaurantApp/*.java
run orbd -ORBInitialPort 1050
in a new terminal run java RestaurantServer -ORBInitialPort 1050 -ORBInitialHost localhost
in a new terminal run java RestaurantClient -ORBInitialPort 1050 -ORBInitialHost localhost
```
### Run the prebuilt versions locally
```
git clone https://github.com/waterboy741/CPRE_550_Proj_1.git
navigate to the src folder
run orbd -ORBInitialPort 1050
in a new terminal run java -jar RestaurantServer.jar -ORBInitialPort 1050 -ORBInitialHost localhost
in a new terminal run java -jar RestaurantClient.jar -ORBInitialPort 1050 -ORBInitialHost localhost
```
