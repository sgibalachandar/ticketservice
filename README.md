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

Tech Stack:
1. Java 1.7
2. Jersey
3. Spring
4. Derby
5. Java rx
6. Junit/Mockito
7. Swagger for documentation

Build tool :

Maven:
apache-maven-3.3.3
Java version: 1.7.0_79, vendor: Oracle Corporation

Instruction to Run:

mvn 
