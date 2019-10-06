# Email service

Create a service that accepts the necessary information of an email.


__Table of Contents__

- [Overview](#overview)

- [Documentation](#documentation)
  - [Install](#install)
    - [Steps](#steps)
    - [Without docker](#without-docker)
    - [With docker](#without-docker)
  - [API Details](#api-details)
    - [Endpoint](#endpoint)
    - [Example](#example)
      - [Params](#params)
      - [CURL command](#curl-command)
      - [Response](#response)

- [Development](#development)
  - [Notes](#notes)
  - [TODO](#todo)
    - [Operational](#operational)
    - [Features](#features)

# Overview

This app accepts the minimal necessary data required to send an email in JSON format and put the data in the SQS queue for further processing. The `email-service-worker` picks the message from the queue and sends the email using the email providers.

# Documentation
## Install
- Requires Java 1.8 
- Requires mvn 
- Require aws-cli (Instructions - https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)
- Requires ASW SQS - (Script provided create_sqs.sh)
- Docker (Optional) - (https://docs.docker.com/install/)
- Docker-compose (Optional) - (https://docs.docker.com/compose/install/)

### Steps

- Unzip the code repo `email-service.zip` (Which is a git repo)
- Update the `/src/main/resources/application.properties` with `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
- Update the `create_sqs.sh` with `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` then run the script which will create FIFO SQS 

#### Without docker
- `mvn package`
- `java -jar email-service-0.0.1-SNAPSHOT.jar`

#### With docker
- `docker build`
- `docker-compose up`

## API details

This app accepts the minimal necessary data required to send an email in JSON format.

### Endpoint

- POST ```/api/v1/email```
    - Content-Type ```application/json```
    - Params 
    
| Name     | Type | Required | Description | 
| :-----------: | :----:|:-------: |:-------:|
| from  | String ("valid email format")  | YES | email-id of the sender 
| to  | Array[String] ("valid email format")  | YES | email-ids of the to (receiver)
| cc  | Array[String] ("valid email format")  | No| email-id of the cc
| bcc  | Array[String] ("valid email format")  | No| email-id of the bcc
| subject  | String  | No| subject of the email
| body     | String  | No| body of the email
| sendAt   | Int (> 0) | Yes | A valid unix timestamp
        
## Example 
### Params
```json
{ 
   "from":"asdtest123@gmail.com",
   "to":[ 
      "asdtest123@mailinator.com",
      "asdtest456@mailinator.com"
   ],
   "cc":[ 
      "asdtest789@mailinator.com"
   ],
   "bcc":[ 
      "asdtest234@mailinator.com"
   ],
   "subject":"Subject of the email",
   "body":"Body of the email",
   "sendAt":1570339545
}    
```
     
### CURL command

```sh
curl -i --request POST \
--URL http://localhost:8080/api/v1/email
--header 'content-type: application/json'
--data '<payload json>'
```

### Response
- Success: 202 (Accepted)


- Error Response:

  - Missing required field: 400 (Bad Request)
    Response:
  ```
  {
       "status": "BAD_REQUEST",
       "message": {
           "short": "Invalid data",
           "long": " <detailed error messsage> "
       },
       "errors": {
           "from": "must not be empty",
           "sendAt": "must provide valid sendAt"
       }
   }
  ```

  - Invalid params: 400 (Bad Request)
    Response:
  ```
  {
      "status": "BAD_REQUEST",
      "message": {
          "short": "Invalid data",
          "long": "<detailed message>"
      }
  }
  ```

  - Not able to connect to SQS: 500 (Internal Server Error)
    Response:
    ```
    {
        "status": "INTERNAL_SERVER_ERROR",
        "message": {
            "short": "Not able to add email in queue to process, Try again",
            "long": "<detailed message>"
        }
    }

    ```

  - Invalid content-type: 415 (Unsupported Media Type) 
    Response:
    ```
    {
        "status": "UNSUPPORTED_MEDIA_TYPE",
        "message": {
            "short": "Not supported",
            "long": "Content type 'text/plain;charset=UTF-8' not supported"
        }
    }
    ```

# Development

## Notes
- To run test `mvn test`
- To build `mvn build`
- To run checkstyle `mvn verify`
- To package `mvn package`


## TODO
### Operational
- Add health-check endpoint
- ~~Versioning of the endpoint~~
- Cloudformation stack with load balancer and auto scaling 
- Add monitoring of the application 
    - Cloud watch alarm (For API and SQS)
    - On-call Support for agreed SLA (pagerduty or others)
    - Configure App insights (new relic)
    - Log forwarder/aggregator (Splunk)

### Feature
- Support name of the person in from, to, cc,bcc
- Support email headers
- Go through all other possible error scenario and provide custom error message if required
- Discuss with product owner about supporting other features like HTML/Attachment and others.
