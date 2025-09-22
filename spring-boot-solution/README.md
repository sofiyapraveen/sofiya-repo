# Spring Boot Solution - Younger Employees Count & Webhook Flow

This is a minimal Spring Boot application that:

1. On startup, sends a POST request to a configured webhook-generation endpoint to obtain a webhook URL.
2. Runs a SQL query against an in-memory H2 database prepopulated with the provided `DEPARTMENT`, `EMPLOYEE`, and `PAYMENTS` data to compute, for each employee, the number of younger employees in the same department.
3. Stores the results in a `SOLUTION_RESULTS` table.
4. Sends the final SQL query text to the webhook URL using a JWT Bearer token in the `Authorization` header.

## How to build & run

Requirements:
- Java 17+
- Maven 3.6+

Build:
```
mvn clean package
```

Run:
```
java -jar target/spring-boot-solution-0.0.1-SNAPSHOT.jar
```

Configuration:
- `src/main/resources/application.properties` contains:
  - `app.webhook.generate-url` - the endpoint to POST to in order to "generate" a webhook. It's a placeholder by default.
  - `app.jwt.secret` - secret used to sign JWT.

Note: The project uses an H2 in-memory database. The SQL query that solves the problem is in `Application.java` as a String, and also stored in the `SOLUTION_RESULTS` table after execution.
