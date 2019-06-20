Service-Oriented Projects
&middot;
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
=====
A collection of service-orinted homework projects. There are 5 different projects (each in a separate folder), which are detailed below separately. The assignment and API description of each project can be found in [docs](https://github.com/ricsinaruto/simple-soa-projects/tree/master/docs).

## [Microservices](https://github.com/ricsinaruto/simple-soa-projects/blob/master/docs/Microservices.pdf)
<a><img src="https://github.com/ricsinaruto/simple-soa-projects/blob/master/img/microservices.png" align="top" height="200" ></a>

A tutorial ticket booking service for movies containing 3 microservices (in separate subfolders):
* Banking: Simulates charging a card.
* Ticketing: Handles movie tickets.
* Movies: Movies can be added and removed with a small amount of metadata.

### Prerequisites
Installation guide for all requirements to run this project can be found [here](https://github.com/ricsinaruto/simple-soa-projects/blob/master/docs/Install_EclipseMavenWildFly.pdf).

### Running
You can use the following ways to run the services locally. In order to try them out you will need a client to send API requests detailed [here](https://github.com/ricsinaruto/simple-soa-projects/blob/master/docs/Microservices.pdf). I recommend [Postman](https://www.getpostman.com/).

1. You can simply open the project in Eclipse and run it from there using a wildfly server.

2. Or navigate to each subfolder and run the following commands to start the services:
```
mvn clean
mvn -P=generate-sources generate-sources
mvn package
mvn thorntail:run
```
