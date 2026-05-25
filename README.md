# Warehouse Management System

Backend warehouse management system built with Spring Boot.

The project focuses on realistic business logic for warehouse operations, inventory tracking, stock reservations, and order processing. It includes a REST API, Swagger documentation, and architecture prepared for JWT authentication and future event-driven extensions.

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
* Order status flow
* Preparation for shipment/dispatch logic

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

## Planned Features

* JWT Authentication
* Role-based authorization
* User management
* Event-driven architecture
* Async processing
* Audit logging
* Docker support
* Unit and integration testing

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

## Release Reservation

* Release reserved quantities when an order is canceled or fails

## Ship Order

* Confirm the order
* Reduce actual inventory quantity
* Finalize reservation

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

---

# API Examples

## Create Product

```http
POST /api/products
```

```json
{
  "name": "Keyboard",
  "sku": "KB-001",
  "quantity": 50
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
      "productId": 1,
      "quantity": 2
    }
  ]
}
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
* Order management
* Reservation flow
* Swagger integration
* Validation and exception handling

## In Progress

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
