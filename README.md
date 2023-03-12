# spring-boot-project
This is a test project using Spring boot. Techstack using in this project:
- Spring boot
- JPA
- Spring security with JWT
- Spring validation

## Features
- Get access token: Generate user access token.
- Add batteries: Add new battery to the system.
- Get batteries: Get list of batteries name based on input postcode range.

## Installation

This project requires maven to build and java to run.

Run the follow command to build

```sh
mvn package
```
Get the target jar file and run
```sh
java -jar jarfilename.jar
```

## API
###  Get access token
Currently have one 1 dummy user

POST /api/v1/user/access-token

Request Headers
> Content-Type: application/json

Request Body
> {
>     "username": "user",
>     "password": "user"
> }

###  Add batteries

POST /api/v1/batteries

Request Headers
> Content-Type: application/json
> Authorization: Bearer xxx

Request Body
> {
>     "batteries" : [
>         {
>             "name": "battery4",
>             "postcode": "6010",
>             "capacity": 170
>         },
>         {
>             "name": "battery5",
>             "postcode": "6010",
>             "capacity": 170
>         }
>     ]
> }

### Get batteries
GET /api/v1/batteries?postcode_start=60001&postcode_end=70000

Request Headers
> Content-Type: application/json
> Authorization: Bearer xxx
