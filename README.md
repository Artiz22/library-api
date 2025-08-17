#  Library Management API

A simple **library management API** built with **Spring Boot**.  
It provides endpoints to manage **books**, **authors**, and **genres**, with authentication and authorization using **JWT**.

---

##  Features

### Books
- Retrieve a paginated list of books
- Search books by **title**
- Filter books by **genre** or **author**
- CRUD operations (create, update, delete) – **restricted to ADMIN role**

###  Genres
- Retrieve all genres
- CRUD operations – **restricted to ADMIN role**

###  Authors
- Retrieve all authors
- Get author by **ID**
- CRUD operations – **restricted to ADMIN role**

---

##  Security

- **GET endpoints** → Public (no authentication required)
- **POST, PUT, DELETE endpoints** → Require **authentication** with **ADMIN role**
- Authentication method: **JWT Bearer Token**

---

##  Tech Stack

- **Java 24**
- **Spring Boot**
    - Spring Web
    - Spring Data JPA
    - Spring Validation
    - Spring Security (JWT)
- **PostgreSQL** (via Docker)
- **Swagger / OpenAPI 3** → interactive API documentation

---

## API Documentation

After running the project, the Swagger documentation will be available here:  
 `http://localhost:8080/swagger-ui.html`

---

##  Future Improvements

- Add **comments** and **ratings** for books 
- Introduce a **USER role** (read-only access + ability to comment and rate)
- Advanced search (title + author + genre filters combined)
- Improved authentication system (refresh tokens, user registration)

---

## How to Run the Project

### 1. Start PostgreSQL with Docker
Make sure you have **Docker** and **Docker Compose** installed.  
In the project, the Docker Compose file is located at: `src/docker/postgresql.yml`

Run PostgreSQL with:
```bash
docker compose -f postgresql.yml up -d

```

### 2. Run the Spring Boot application

Once PostgreSQL is up, you can start the application with Maven:

```bash
./mvnw spring-boot:run
