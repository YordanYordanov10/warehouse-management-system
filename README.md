# Warehouse Management System

Backend warehouse management system built with Spring Boot.

The project focuses on realistic business logic for warehouse operations, inventory tracking, stock reservations, and order processing. It includes a REST API, Swagger documentation, unit-tested domain logic, and an architecture prepared for JWT authentication and future event-driven extensions.

---

# Features

## Inventory Management

* Create and manage products
* Track inventory quantities
* Update stock levels
* Handle insufficient stock validation
* Reservation logic for stock allocation
* Release reserved quantities

## Order Management

* Create orders
* Add order items
* Validate stock availability during order creation
* Order status flow (`RESERVED` → `SHIPPED` / `CANCELLED`)
* Cancel and ship reserved orders, with stock automatically released or consumed

## Reservation Flow

* Reserve stock
* Release reservations
* Confirm and ship orders
* Prevent over-selling

## API & Documentation

* RESTful API
* Swagger/OpenAPI documentation
* JSON request/response models
* Request validation
* Global exception handling

## Testing

* Unit tests for domain logic (`Inventory`, `Order`)
* Happy-path and edge-case coverage (invalid quantities, boundary values, invalid state transitions)
* JaCoCo code coverage reporting
* Service-level and integration tests (planned)

## Planned Features

* JWT Authentication
* Role-based authorization
* User management
* Event-driven architecture
* Async processing
* Audit logging
* Docker support

---

# Tech Stack

## Backend

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Spring Validation
* Lombok

## Database

* MySQL

## Documentation

* Swagger / OpenAPI

## Testing

* JUnit 5
* JaCoCo

## Build Tools

* Maven

---

# Architecture

The project follows a layered architecture:

```text
Controller
   ↓
Service
   ↓
Repository
   ↓
Database
```

## Main Modules

```text
Product
Inventory
Order
Reservation
Event
User (planned)
Security (planned)
```

---

# Order Flow

## Create Order

1. Create order
2. Validate stock availability
3. Reserve inventory
4. Save order items
5. Update order status

## Cancel Order

* Only orders in `RESERVED` status can be cancelled
* Releases the reserved quantities back to available stock
* Records a cancellation reason

## Ship Order

* Only orders in `RESERVED` status can be shipped
* Reduces actual inventory quantity and clears the reservation
* Updates order status to `SHIPPED`

---

# Swagger Documentation

After starting the application:

```text
http://localhost:8080/swagger-ui/index.html
```

---

# Installation

## Clone Repository

```bash
git clone <your-repository-url>
```

## Configure Database

Create a MySQL database:

```sql
CREATE DATABASE warehouse_management;
```

## application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/warehouse
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Run Project

```bash
mvn spring-boot:run
```

## Run Tests

```bash
mvn test
```

Coverage report is generated at:

```text
target/site/jacoco/index.html
```

---

# API Examples

## Create Product

```http
POST /api/products
```

```json
{
  "name": "Laptop",
  "sku": "LAP12345",
  "description": "A high-performance laptop for gaming and work",
  "price": 999.99
}
```

## Create Order

```http
POST /api/orders
```

```json
{
  "items": [
    {
      "productId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
      "quantity": 2
    }
  ]
}
```

## Cancel Order

```http
POST /api/orders/{id}/cancel
```

```json
{
  "reason": "Customer requested cancellation"
}
```

## Ship Order

```http
POST /api/orders/{id}/ship
```

---

# Project Goals

* Build realistic backend architecture
* Maintain clear separation of concerns
* Practice transactional business logic
* Prepare for microservice/event-driven patterns
* Improve knowledge of the Spring ecosystem

---

# Current Status

## Completed

* Core inventory logic
* Product management
* Order management (create, cancel, ship)
* Reservation flow
* Swagger integration
* Validation and exception handling
* Unit test coverage for domain layer (`Inventory`, `Order`)

## In Progress

* Service-level and integration testing
* Event system refactor
* Security layer
* JWT authentication
* User module

---

# Future Improvements

* Kafka/RabbitMQ integration
* Redis caching
* Docker Compose setup
* CI/CD pipeline
* Monitoring & logging
* Pagination & filtering
* Advanced reporting

---

# Author

Yordan Yordanov

LinkedIn: https://www.linkedin.com/in/yordan-yordanov-dev
GitHub: https://github.com/YordanYordanov10
