# parcel-delivery-app

[![CI](https://github.com/unrealwork/parcel-delivery-app/actions/workflows/gradle.yml/badge.svg)](https://github.com/unrealwork/parcel-delivery-app/actions/workflows/gradle.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=unrealwork_parcel-delivery-app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=unrealwork_parcel-delivery-app)

## Task

You’ll be expected to build the back- end of the web app, while the front end will be built by another
developer in the team.

To do this, you are expected to implement a solution that enables the user stories provided. This solution
must be implemented as a **micro-service** architecture that is deployable using **container technology**.

- [ ] Design a solution architecture diagram for Parcel Delivery APP based on micro-service technologies;
- [ ] Implement the micro-services required for Parcel Delivery APP to bring the user stories provided to reality;
- [ ] Containerize your micro-service solution to allow for it to be easily tested on other developer computers;

### User stories

| User                                              | Admin                                             | Courier                                                  |
|---------------------------------------------------|---------------------------------------------------|----------------------------------------------------------|
| Can create an user account and log in*            | Can change the status of a parcel delivery  order | Can log in                                               |
| Can create a parcel delivery order**              | Can view all parcel delivery orders               | Can view all parcel delivery orders that assigned to him |
| Can change the destination of a parcel delivery order** | Can assign parcel delivery order to courier       | Can change the status of a parcel delivery order         |
| Can cancel a parcel delivery order****            | Can log in and create a courier account*          |  Can see the details of a delivery order                 |
| Can see the details of a delivery                 | Can track the delivery order by coordinates       |
Can see all parcel delivery orders that he/she created |  Can see list of couriers with their statuses |

### Requirements

- [x] Use JWT for authentication/authorization
