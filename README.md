
# 📚 Library Management System

## 🔖 Project Overview & Features
This is a comprehensive **Library Management System** developed using Java 21 and Spring Boot 3.1.5. The system supports user registration/login, book search, borrowing/returning, overdue tracking, JWT authentication, role-based access control, Kafka logging, reactive tracking with WebFlux, and Docker deployment.

---

## 🔁 System Flow Diagram
![System Flow Diagram](assets/akıs diagram.jpg)

---

## ⚙️ Technology Stack
| Technology            | Description                             |
|-----------------------|------------------------------------------|
| Java 21               | Backend language                         |
| Spring Boot 3.1.5     | Application framework                    |
| PostgreSQL            | Relational database                      |
| Spring Security + JWT | Role-based authentication and security  |
| MapStruct             | DTO-Entity mapping                       |
| Spring WebFlux        | Reactive module                          |
| Kafka                 | Logging infrastructure (request & error) |
| Docker + Compose      | Container orchestration                  |
| Swagger/OpenAPI       | API documentation                        |
| JUnit & Mockito       | Testing frameworks                       |
| Lombok                | Boilerplate code reduction               |

---

## 🚀 Running the Application Locally
1. Navigate to the project directory:
```bash
cd Library-Management-System
```
2. Create a PostgreSQL database named `library_db` accessible via port 5432.
3. Build the application skipping tests:
```bash
./mvnw clean install -DskipTests
```
4. Run the application:
```bash
./mvnw spring-boot:run
```
> Application will be available at `http://localhost:8080`

---

## 🐳 Docker Setup
```bash
docker-compose up --build
```
> PostgreSQL, Kafka, and the application will be launched in containers.

![Docker Screenshot](assets/docker.png)

---

## 🧩 Key Features

### 👥 Authentication & Role-Based Authorization
- Login with JWT token
- Sample users:
  - `username: librarian`, `password: password123` → LIBRARIAN role
  - `username: patron`, `password: password321` → PATRON role
- Role-based endpoint restrictions
- Controller-level access control via `@PreAuthorize`
- Token authentication can be tested through Swagger UI

![JWT Token Swagger](assets/sw2.png)

---

### 📚 Book Management (LIBRARIAN)
- Add, update, delete books
- Search/filter by title, author, genre, or ISBN
- Pagination and sorting support
- Stock tracking using `inventoryCount`; availability also tracked in real-time via WebFlux

![Book Endpoints](assets/sw3.png)

---

### 🔄 Borrowing & Returning Flow
- Patrons can borrow/return books
- Overdue detection with `@Scheduled` job
- Books automatically marked as `OVERDUE`
- Max 5 books can be borrowed at a time
- If a user has at least 1 overdue book or already has 5 books borrowed → user becomes `inactive`
- Active users (`active=true`) cannot be deleted

![Borrowing Endpoints](assets/sw4.png)

---

### 🧪 Testing
- Unit tests for BookService, UserService, BorrowingService
- Integration tests for controller layer with DB & JWT
- Role-based security tests

**Integration Test Coverage:**
![Integration Coverage](assets/all uIntegrationTest.png)

**Unit Test Coverage:**
![Unit Coverage](assets/all unitTest.png)

---

### 📜 Exception Handling & Response Wrapper
- Global exception handling via `@ControllerAdvice`
- Standardized responses using `RestResponse<T>`
- Enum-based error messaging with `GeneralErrorMessage`, `ErrorMessage`

---

### 📦 Kafka Logging Integration
- Logs generated via `LogHelper` (INFO, ERROR, WARN)
- Logs sent to `request-log` and `error-log` topics when Kafka is enabled
- Fallback to console logging if Kafka is not used
- To enable Kafka via Docker, set `logger.kafka.enabled=true` in `application.properties`; set to `false` when running locally without Kafka

---

## 🔗 Swagger Documentation
http://localhost:8080/swagger-ui/index.html

---

## 📮 Postman Collection
- All endpoints grouped by function (Auth, Book, Borrowing, User)
- Sample requests with JWT tokens
- File path: `postman/LibraryManagementSystem.postman_collection.json`

---

## 🗃️ Database Schema
![Database Schema](assets/sql library db2.png)

- Relationships between `user`, `book`, and `borrowing` tables

---

## 🧠 Development Notes
- Adheres to Clean Code, SOLID, and Testable Architecture principles
- Version-controlled using Git throughout development

---

## 👤 Developer
**Merve Saruhan**  
[LinkedIn](https://www.linkedin.com/in/mervesaruhan/) • [GitHub](https://github.com/mervesaruhan)


---

## 🎥 Project Presentation Video

Watch the full project walkthrough here:  
🔗 [Loom Video Presentation](https://www.loom.com/share/1149b59ff9d64175b5d7ecd8d1bdb804?sid=6c93c791-67b6-480e-b11d-6a396d5e5b06)
