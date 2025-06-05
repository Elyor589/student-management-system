# Student Management System - Backend

This is the backend service for the Student Management System, built with Java and Spring Boot. It provides RESTful APIs for managing students, courses, and user authentication.

## Features

- Student CRUD operations
- Course management
- User login/registration (JWT or session-based)
- PostgreSQL database integration
- REST API with proper validation and error handling

## Tech Stack

- Java 21
- Spring Boot
- PostgreSQL
- Maven
- Heroku (for deployment)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Elyor589/student-management-system.git
   cd student-management-system

## config
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: your_username
    password: your_password
