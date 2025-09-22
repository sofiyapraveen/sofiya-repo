# Webhook

A Spring Boot application that calculates the number of employees who are younger than each employee, grouped by their respective departments. The application stores the results in an H2 in-memory database and can send the final SQL query to a webhook URL using a JWT token.



## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Database Schema](#database-schema)
- [Setup & Run](#setup--run)
- [Configuration](#configuration)
- [SQL Query Used](#sql-query-used)
- [H2 Console](#h2-console)
- [Webhook Integration](#webhook-integration)
- [License](#license)



## Features

- Calculates the number of employees younger than each employee within the same department.
- Stores results in an H2 in-memory database (`SOLUTION_RESULTS` table).
- Sends the SQL query to a configurable webhook URL using a JWT token.
- Uses Spring Boot, JDBC, and JJWT for JWT signing.
- H2 in-memory database preloaded with sample data.



## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Maven**
- **H2 Database**
- **JJWT (JWT signing)**
- **REST API for webhook integration**



## Database Schema

### DEPARTMENT
| Column | Type | Notes |
|--------|------|------|
| DEPARTMENT_ID | INT | Primary Key |
| DEPARTMENT_NAME | VARCHAR | Name of the department |

### EMPLOYEE
| Column | Type | Notes |
|--------|------|------|
| EMP_ID | INT | Primary Key |
| FIRST_NAME | VARCHAR | Employee first name |
| LAST_NAME | VARCHAR | Employee last name |
| DOB | DATE | Date of birth |
| GENDER | VARCHAR | Male/Female |
| DEPARTMENT | INT | Foreign Key → DEPARTMENT(DEPARTMENT_ID) |

### PAYMENTS
| Column | Type | Notes |
|--------|------|------|
| PAYMENT_ID | INT | Primary Key |
| EMP_ID | INT | Foreign Key → EMPLOYEE(EMP_ID) |
| AMOUNT | DECIMAL | Salary credited |
| PAYMENT_TIME | TIMESTAMP | Date and time of payment |


###Download:
https://github.com/sofiyapraveen/sofiya-repo.git

###RAW downloadable link - https://raw.githubusercontent.com/sofiyapraveen/sofiya-repo/refs/heads/main/spring-boot-solution/src/main/java/com/example/springbootsolution/Application.java
