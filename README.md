# API Gateway For Chat Application

This microservice is part of the _[real-time chat application](https://github.com/vsayfb/real-time-chat-application)._

## Description

This microservice serves as a centralized point for distributed microservices and exposes an endpoint for communicating with them.

Each downstream service has its own endpoints, which can be viewed at the /api-docs endpoint of the respective downstream service. For example _http://api-gateway/service-name/api-docs._

## Running the application

#### Development

`docker compose up -d && docker compose logs -f`

#### Testing

`BUILD_TARGET=test docker compose up -d && docker compose logs -f`

#### Production

`docker build -t api-gateway . && kubectl apply -f deployment.yml`

