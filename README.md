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

The app is a distributed system which provides simple [REST API](https://unrealwork.github.io/parcel-delivery-app/) for
parcel delivery service. The main purpose of this project to demonstrate modern approach to development process. App was
developed using full CI cycle using following components:

* Github Actions for CI
* Sonarcloud for Code Quality
* Spring infrastructure for microservice development
* PostgresSQL for data persistence
* Testcontainers for near production test environment
* Docker for easy deployment of the solution

### Playground

The main tool to explore the solution is main page of the project which available at https://localhost:18080 after
deploy. It's a swagger like page which provide ability to authorize and interact with an API. For demo purposes
three users are created.

* User: `john@doe.com`
* Admin: `jane@doe.com`
* Courier: `jack@doe.com`

Default password is `password123`. Use it
to [sign in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) and then use retrieved
JWT `accessToken` for access to other resources
of the app.

![img_1.png](assets/images/playgroud.png)

## Containerization

The Solution is deployable via docker compose. Each microservice has own Dockerfile which is used in main docker-compose
file. Health checks are implemented for each service. By default, only one service expose a
port for an interaction, dependent services are hidden behind docker network.

For development purposes solution provides additional compose files which overrides default behaviour:

* `docker-compose.dev.yml` - exposes service ports for local development
* `docker-compose.ci.yml` - contains CI specific properties which are used during deploy test in CI

### Used technologies

* Java 17
* Spring Boot 3
* Spring Cloud Gateway
* Docker
* Springdoc
* Testcontainers
* PostgresSQL
* Liquibase
* Spring Data JPA
* Kafka
* Sonar
* Github actions

### User stories

Each user story has a corresponding API endpoint which specified in the table:

| User                                                                                                                                                                                        | Admin                                                                                                                                                                                                 | Courier                                                                                                |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| Can [create an user account](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signup) and [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) | Can [change the status](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/status) of a parcel delivery  order                                                                                               | Can [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin)                  |
| Can [create a parcel delivery order](https://unrealwork.github.io/parcel-delivery-app/#post-/api/orders)                                                                                    | Can [view all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders)                                                                                                                         | Can [view all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders) that assigned to him     |
| Can [change the destination](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/destination) of a parcel delivery order**                                               | Can [assign parcel delivery order to courier](https://unrealwork.github.io/parcel-delivery-app/#put-/api/deliveries/-orderId-/assign)                                                                 | Can [change the status](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/status) of a parcel delivery order |
| Can [cancel a parcel delivery order](https://unrealwork.github.io/parcel-delivery-app/#put-/api/orders/-id-/cancel)                                                                         | Can [log in](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signin) and [create a courier account](https://unrealwork.github.io/parcel-delivery-app/#post-/api/auth/signup/courier) | Can see [the details of a delivery](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-) order                                                            |
| Can [see the details of a delivery](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-)                                                                                                   | Can [track the delivery order](https://unrealwork.github.io/parcel-delivery-app/#get-/api/deliveries/-orderId-/track) by coordinates                                                                  |
Can [see all parcel delivery orders](https://unrealwork.github.io/parcel-delivery-app/#get-/api/orders) that he/she created                                                                                            | Can [see list of couriers](https://unrealwork.github.io/parcel-delivery-app/#get-/api/couriers) with their statuses                                                                                   |
