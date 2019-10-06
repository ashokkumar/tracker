# Email service architecture

__Table of Contents__

- [High level components](#high_level_components)

- [High level architectural considerations](#high_level_architectural_considerations)
    - [Avoid data loss - Reliability](#avoid_data_loss_-_reliability)
    - [Scalability](#scalability)
    - [Operational excellence](#operational_excellence)
    - [Security](#security)
    - [Cost effective](#cost_effective)

- [Limitations of the architecture](#limitations_of_the_architecture)

- [Other options considered](#other_options_considered)
    - [Email Service API - SQS - Lambda](#email_service_api_sqs_lambda)
    - [API Gateway - Lambda](#api_gateway_-_lambda)
    
- [Diagrams](#diagrams)
    - [Current architecture](#current_architecture)
    - [System overview](#system_overview)
    - [Email service components](#email_service_components)
    - [Email service worker components](#email_service_worker_components)
    
    
# High level components

- Email Service API
- SQS Email Queue
- Email Service Worker

# High level architectural considerations

## Avoid data loss - Reliability
In the above architecture the data loss is avoided by storing the message in SQS queue before processing it by the email providers. The durability of the messages are same as the durability of SQS, which persists the data in Multi AZ and highly durable (I am not able to find the exact 9's for durability in AWS doc). On other scenarios of data loss during network transfer like while communicating from the email-service-api to SQS the end user won't get the 202 response which can be used to re-send the message to the API by the consumer.  
      
## Scalability
The API servers are behind the load-balancer and auto-scaling, which makes sure the traffic is spread across multiple instances of API server. The FIFO SQS can handle 300 write request per second, if we hit the limit then we can raise a limit increase request. We can also have more than one listener application consuming the messages from FIFO SQS queue which provides this can be auto scaled based on the cloud watch alarm of 'ApproximateAgeOfOldestMessage' in the queue.       

## Operational excellence
As the architecture is decoupled as consumer(API) and processor(worker) they can be scaled individually based on cloud watch metrics, which will enable the systems to scale independently. Also as they are decoupled the deployed/changes to the systems can be happen without affecting one another.    

## Security
As these messages might contain PII data, we need to make sure that these messages are highly secured. We can use the SSE (server side encryption) in the SQS queue (for this exercise they are not encrypted) to protect the messages. Also from the development team perspective we need to make sure that none of the PII data/messages are being logged in the logs. For tracking purpose the unique ID of the messages can be used.
To prevent message getting into logs.
- At code review stage, we need to make sure that the PII data in the messages are not being logged.
- At the testing/qa stage need to ensure that the PII data is not being logged in the logs in both API and worker.  

## Cost effective
As the systems both API and worker can be configured as auto scalable, the cost is directly linked to the amount of traffic that the system is going to handle.

# Limitations of the architecture
- Limit of the SQS message size is 256KB
- FIFO queues default limit is 300 req/sec (Write, Read and Delete), need to raise limit request or consider other alternative approach like Queue pooling [architecture](/images/scaled-version.png)

# Other options considered

## Email Service API - SQS - Lambda
- Email Service API to standard SQS and then configure lambda trigger from SQS which will act as worker
    - Pros
        - Highly scalable (Higher limit to SQS)
        - Lambdas are triggered and automatically and process the messages from the queue.
        - Cost effective       
    - Cons
        - Standard queue can produce duplicate messages, which might be an issue (Can discuss with business as this is a rare case scenario)
        - Log aggregation from lambdas            
         
## API Gateway - Lambda
- API gateway as (email-service) to SQS and then lambda (worker)
    - Pros
        - Serverless
        - Ease of scalability
    - Cons
        - Data loss
        - Feedback to consumers form the API


# Diagrams

## Current architecture 

![Current Architecure](/images/current-version.png)

## System overview

![System overview](/images/c2.png)

## Email service components

![Email Service](/images/email-service-api.png)

## Email service worker components

![Email Service Worker](/images/email-service-worker.png)
 
