# Ticketservice

=====================================
Assignement: Ticket service
Author : Balachandar M
Date : 05/04/2016
=====================================
#Requirement:

Implement a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.
Instructions:

Your homework assignment is to design and write a Ticket Service that provides the following functions: Find the number of seats available within the venue, optionally by seating level
Note: available seats are seats that are neither held nor reserved.
Find and hold the best available seats on behalf of a customer, potentially limited to specific levels
Note: each ticket hold should expire within a set number of seconds.
Reserve and commit a specific group of held seats for a customer
Requirements:

The ticket service implementation should be written in Java
The solution and tests should build and execute entirely via the command line using either Maven or Gradle as the build tool
A README file should be included in your submission that includes instructions for building the solution and executing the tests Implementation mechanisms such as disk-based storage, a REST API, and a front-end GUI are not strictly required

#Tech Stack:
1. Java 1.7
2. Jersey
3. Spring
4. Derby
5. Java rx
6. Junit/Mockito
7. Swagger for documentation

#Build tool :

Maven:
apache-maven-3.3.3
Java version: 1.7.0_79, vendor: Oracle Corporation

#Instruction to Run:

To run application

mvn jetty:run

To Run Unit tests:

mvn test

Ticket expiration time and customer threshold to hold ticket can be managed to through common.properties in resource folder

Defaults:
ticket.expiration.period.secs=60
allowed.seats.percustomer=10

#Sample Requests to test:

Note: Create Customer before hold/reserve seats
#Create Customer
POST: http://localhost:8080/ticketservice/rest/register/customers/customer
{
  "firstName":"FirstName",
  "lastName":"LastName",
  "emailId":"example@gmail.com"
}

#Venue Detail:
http://localhost:8080/ticketservice/rest/ticket/venuedetail
Response:
[
    {
        "seatsInRow": 50,
        "numberOfRow": 25,
        "levelName": "Orchestra",
        "levelId": 1,
        "price": 100
    },
   ......
]

#Get Seats Availability:

spec: http://localhost:8080/ticketservice/rest/ticket/venue/{levelId}/availableseats

Sample:

GET: http://localhost:8080/ticketservice/rest/ticket/venue/1/availableseats

Response:
{
    "seatsAvailable": 1250,
    "venue": "Orchestra"
}
#Hold by levelId:

http://localhost:8080/spring-mvc-db/rest/ticket/hold
Request:
{
  "levelId":1,
  "emailId":"example@gmail.com",
  "numberOfSeats":10
}

#Find And Hold by specified level ids:
Note: Available check for seats in level is not performed across levels. Checked only within the level

POST:
http://localhost:8080/ticketservice/rest/ticket/findandhold
Request
{
  
  "emailId":"example@gmail.com",
  "numberOfSeats":10,
  "minLevel":1,
  "maxLevel":2
}

#Get Seats on hold by customer:

GET:
http://localhost:8080/ticketservice/rest/ticket/onhold/example@gmail.com
Response:

[
    {
        "cusomerId": -2147483648,
        "emailId": null,
        "numberOfSeats": 10,
        "timeReserved": 1462333490934,
        "holdFlag": null,
        "levelId": 1,
        "reservationId": -2147483648
    }
]

#Reserve Seats by holdId:

Spec http://localhost:8080/ticketservice/rest/ticket/reserve/{holdId}
PUT:
http://localhost:8080/ticketservice/rest/ticket/reserve/-2147483648

#Assumption

1 Reserveration is not done by Data
2 Expect seats to be held before reserving
3 Versions are not covered

#Notes:

1. Spring boot ,Java 8 and JPA could have been little more precise, But tech stack I chose based on my current project experience 
2. Unit tests are not  covered 100%, just to showcase the skillset, I have implemented for fews methods
3. Only in TicketService class has documentation in code, just to show best practice.
4. Could not complete swagger integration.




