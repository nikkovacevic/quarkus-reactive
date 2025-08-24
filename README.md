# Quarkus-based development of reactive backend systems

_Master's thesis_

In modern microservice environments, cloud infrastructure costs can be high. Which is why in our thesis, we explore reactive backend systems, which offer greater efficiency and lower resource consumption when processing requests. We focused on Quarkus-based reactive systems and compared them to traditional backend systems developed in Quarkus and Spring Boot frameworks.
In our thesis we have implemented a reactive backend system using Quarkus and compared it to the traditional ones, identifying all the challenges we encountered throughout the entire development cycle. During testing, we simulated high load by sending concurrent requests and found that the reactive approach results in shorter processing times and lower resource consumption. When choosing an approach, it is necessary to balance the higher development costs of a reactive system with potential savings in cloud infrastructure costs.

## Purpose

This project was developed as a practical part of my masters thesis on Quarkus based reactive systems. The goal was to build a reactive backend system using Quarkus, traditional backend system using Quarkus and traditional backend system using Spring Boot.
All of the projects have the same CRUD features, so that we can compare the development, testing and deployment process between them. The backend systems were also tested on performance by simulating concurrent requests to the server in local and cloud (MS Azure) environments.

## Findings

The thesis explores reactive backend development with Quarkus and compares it to traditional approaches (Quarkus and Spring Boot). Reactive systems offer faster request handling and more efficient resource usage but introduce higher complexity, a steeper learning curve, and more challenging debugging. Tests under high load confirmed that reactive systems are more efficient and responsive, especially in resource-constrained environments, while traditional systems remain simpler and more practical when resources are not a limitation. Quarkus enables combining both approaches within one backend, allowing developers to balance efficiency with development complexity.

## Thesis

The full thesis is available at [...](https://dk.um.si/IzpisGradiva.php?id=91139&lang=slv)
