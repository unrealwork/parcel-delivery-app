# parcel-delivery-app

[![CI](https://github.com/unrealwork/parcel-delivery-app/actions/workflows/gradle.yml/badge.svg)](https://github.com/unrealwork/parcel-delivery-app/actions/workflows/gradle.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=unrealwork_parcel-delivery-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=unrealwork_parcel-delivery-app) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=unrealwork_parcel-delivery-app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=unrealwork_parcel-delivery-app)

This project is a solution for the [test assignment](assets/task.pdf). Implement API for simple parcel delivery service.

## Getting started

* [API docs](https://unrealwork.github.io/parcel-delivery-app/)
* Start services locally
  ```bash
  ./gradlew :composeUp
  ```
* Navigate to [playground](#playground) at http://localhost:18080
* Use `docker-compose` to interact with services

## Architecture

![](assets/images/arch.svg)

## Implementation

The app is a distributed system that provides simple [REST API](https://unrealwork.github.io/parcel-delivery-app/) for
parcel delivery service. The main purpose of this project is to demonstrate a modern approach to the development
process. The app is developed using a full CI cycle using the following components:

* Github Actions for CI
* Sonarcloud for Code Quality
* Spring infrastructure for microservice development
* PostgresSQL for data persistence
* Testcontainers for near-production test environment
* Docker for easy deployment of the solution

### Playground

The main tool to explore the solution is the main page of the project which is available at https://localhost:18080
after
deployment. It's a Swagger-like page that provides the ability to authorize and interact with an API.

For demo purposes, `special_key` is set as default and gives ability to access any API. Also, there are three users are
created by default:

* User: `john@doe.com`
* Admin: `jane@doe.com`
* Courier: `jack@doe.com`

The default password is `password123`. Use it
to [sign in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) and then use retrieved
JWT `accessToken` for access to other resources
of the app.

![img_1.png](assets/images/playgroud.png)

## Containerization

The Solution is deployable via `docker-compose`. Each microservice has its own Dockerfile which is used in the main
docker-compose
file. Health checks are implemented for each service. By default, only one service exposes a port for an interaction,
dependent services are hidden behind the docker network.

For development purposes solution provides additional compose files that override default behavior:

* `docker-compose.dev.yml` - exposes service ports for local development
* `docker-compose.ci.yml` - contains CI-specific properties which are used during deploy test in CI

### Used technologies

* Java 17
* Spring Boot 3
* Spring Cloud Gateway
* Spring Cloud Streams
* Docker
* Springdoc
* Testcontainers
* PostgreSQL
* Liquibase
* Spring Data JPA
* Kafka
* Sonar
* Github actions

### User stories

Each user story has a corresponding API endpoint which is specified in the table:

| User                                                                                                                                                                                        | Admin                                                                                                                                                                                                 | Courier                                                                                                |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| Can [create an user account](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signup) and [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) | Can [change the status](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/status) of a parcel delivery  order                                                                                               | Can [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin)                  |
| Can [create a parcel delivery order](https://unrealwork.github.io/parcel-delivery-app/#post-/api/orders)                                                                                    | Can [view all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders)                                                                                                                         | Can [view all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders) that assigned to him     |
| Can [change the destination](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/destination) of a parcel delivery order**                                               | Can [assign parcel delivery order to courier](https://unrealwork.github.io/parcel-delivery-app/#put-/api/deliveries/-orderId-/assign)                                                                 | Can [change the status](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/status) of a parcel delivery order |
| Can [cancel a parcel delivery order](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/cancel)                                                                         | Can [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) and [create a courier account](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signup/courier) | Can see [the details of a delivery](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-) order                                                            |
| Can [see the details of a delivery](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-)                                                                                                   | Can [track the delivery order](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-/track) by coordinates                                                                  |
Can [see all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders) that he/she created                                                                                            | Can [see list of couriers](https://unrealwork.github.io/parcel-delivery-app/#get-/api/couriers) with their statuses                                                                                   |
