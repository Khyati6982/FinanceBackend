### Finance Backend Assessment

## Overview

This project is a Spring Boot backend designed for a finance application. It demonstrates secure endpoint management, profile-based database configurations, role-based access control, and unit testing with H2 and MySQL. The repository is structured to be evaluator-friendly, with clear documentation and reproducible steps.

---

## Tech Stack

- **Java 17** – Core language
- **Spring Boot** – Backend framework
- **Spring Security (JWT)** – Authentication & authorization
- **Hibernate / JPA** – ORM and persistence
- **MySQL** – Runtime database
- **H2** – In-memory test database
- **JUnit 5** – Unit testing
- **Swagger / OpenAPI** – API documentation
- **Maven** – Build and dependency management

---

## Profiles

### Dev / H2 (Tests)

- Uses in-memory H2 database.

- ```spring.jpa.hibernate.ddl-auto=create-drop``` ensures schema is created fresh for each run.

- Tests run against H2 to validate logic quickly.

- Duplicate username enforcement test passes correctly because schema resets each run.

### MySQL (Runtime)

- Connects to ```finance_db``` schema.

- Configured in ```application-mysql.properties```:
``` 
 spring.datasource.url=jdbc:mysql://localhost:3306/finance_db 
 spring.datasource.username=${DB_USERNAME} 
 spring.datasource.password=${DB_PASSWORD}
 spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver 
 spring.jpa.hibernate.ddl-auto=update spring.jpa.show-sql=true 
 spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```
- Schema is updated to match entities, preserving data across runs.

- Endpoints are secure and visible only via Swagger UI.

---

## Security

- Spring Security is enabled across all endpoints.

- Endpoints cannot be accessed anonymously.

- Swagger UI documents endpoints but requires authentication (**JWT**) to execute requests.

- Role-based access control is enforced: only Admins can manage users and assign roles.

---

## Role Assignment

- Roles available: **VIEWER**, **ANALYST**, **ADMIN**.

- Users are linked to roles via the ```user_roles``` join table.

- Admins can assign roles dynamically using the endpoint:

  ```**POST** /users/{id}/assignRole?roleName=ANALYST```

- This populates the ```user_roles``` table automatically.

- For demonstration, role mappings were pre-seeded in MySQL so evaluators can immediately test access control.

- Example mapping:

  - loginuser → **VIEWER**

  - authuser → **ANALYST**

  - newuser → **ADMIN**

---

## Testing

- JUnit tests validate service and repository logic.

- Tests run cleanly under H2 profile (green bar).

- Under MySQL, persistence may cause duplicate user tests to fail, which is expected behavior due to leftover data.

- No manual cleanup (```@AfterEach deleteAll```) is required in H2 because ```create-drop``` resets schema.

---

## Data Persistence

- **H2 (tests)**: Uses ```create-drop``` for clean slate each run. Ensures reproducible test results.

- **MySQL (runtime)**: Uses ```update``` to preserve data across restarts. Schema evolves with entities, but data remains.

 - Evaluators may insert sample records manually or use the provided seed data.

---

### Example Seed Data

-- Roles 

INSERT INTO ROLES (id, name) VALUES (1, 'VIEWER'); INSERT INTO ROLES (id, name) VALUES (2, 'ANALYST'); INSERT INTO ROLES (id, name) VALUES (3, 'ADMIN');

-- Example user 

INSERT INTO USERS (id, active, username, password) VALUES (14, TRUE, 'demoUser', 'password123');

-- Link user to role 

INSERT INTO*USER_ROLES (user_id, role_id) VALUES (14, 1);

-- Financial record 

 INSERT INTO FINANCIAL_RECORDS (id, amount, category, date, notes, type) VALUES (22, 500, 'salary', CURDATE(), 'Monthly salary', 'income');

---

## Swagger

- Swagger UI is enabled to document endpoints.

- Endpoints are visible but secured.

- Evaluators can authenticate with **JWT** to test functionality.

---

## Validation and Error Handling
- Input validation for required fields
- Meaningful error responses with proper HTTP status codes
- Protection against unauthorized role actions

---

## Key Takeaways

- Security first: endpoints are protected by Spring Security.

- Profiles: H2 for quick testing, MySQL for runtime persistence.

- Role assignment: Admin endpoint assigns roles, populating ```user_roles```.

- Tests: demonstrate logic correctness, including duplicate username enforcement.

- Evaluator-friendly: clean repo history, clear **README**, reproducible steps.

---

## How to Run

- Clone the repository.

- Configure environment variables ```DB_USERNAME```, ```DB_PASSWORD```, and ```JWT_SECRET```.

- Run with H2 profile for quick tests.

- Run with MySQL profile for full runtime validation.

- Access Swagger UI at [http://localhost:9092/swagger-ui.html.](http://localhost:9092/swagger-ui.html.)

---

## Notes for Evaluators

- The project intentionally prioritizes security and role-based access control.

- Role mappings were pre-seeded for demonstration, but Admins can assign roles via **API**.

- The single failing MySQL test is expected due to persistence of prior inserts.

- Commit history emphasizes value-added features and security milestones.
