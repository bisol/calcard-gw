# calcard

This is the first part of an implementation of the credit analysis service using microservices. 
The second part is [here](https://github.com/bisol/calcard-analysis). 
This project contains the front end, database and docker scripts.

## Architeture

The full solution uses service discovery, messaging and websockets. 
It is composed of a React web app, a credit proposal service and a credit analysis service (both are Spring boot apps).

The workflow for creating credit proposal is:

The web app sends a REST request to the credit proposal service. This service persists the credit proposal, 
triggers the credit analysis service via messaging and returns the new entity to the web app. 
The credit analysis service will proccess the proposal and notify the credit proposal service via messaging. 
On receiving the analysis result, the credit proposal service will update the CreditProposal entity and notify the web app via web sockets.

The analysis service introduces a 10s delay to simulate load and demonstrate this async nature.

## Building the docker image:

To optimize the calcard application for production, run:

    ./mvnw package -Pprod jib:dockerBuild

## Running the app:

First, download and build the analysis service [here](https://github.com/bisol/calcard-analysis).
Then run

    docker-compose -f src/main/docker/full/ up

The docker-compose configuration provided will launch an instance of Jhipster registry, Zookeper, Kafka, two databases, this app and the analysis service.

##Improvements

The web app could be prettier, and for some reason the interface is not updating correctly when receiving a websocket update (only happens when run under docker, it works in developement mode).

## About

This application was generated using JHipster 5.7.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v5.7.0](https://www.jhipster.tech/documentation-archive/v5.7.0).
There is a LOT of generated code. If you want to check the code, these are the most heavily edited files:

```
src/main/webapp/app/config/websocket-update-middleware.ts
src/main/webapp/app/modules/home/**
src/main/java/com/bisol/calcard/service/CreditProposalService.java
src/main/java/com/bisol/calcard/domain/CreditProposal.java
```

[JHipster Homepage and latest documentation]: https://www.jhipster.tech
