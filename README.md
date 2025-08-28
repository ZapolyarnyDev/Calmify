<h1 align="center"> 📺 Calmify</h1>

## 📑 Table of Contents
1. [About the project](#-about-the-project)
2. [Tech-stack](#-tech-stack)
3. [Being integrated](#-being-integrated)
4. [Project architecture](#-project-architecture)
5. [Discovery server](#-discovery-server)
6. [API gateway](#-api-gateway)
7. [Auth service](#-auth-service)
8. [User service](#-user-service)
9. [License](#license)


## 🔎 About the project
**Calmify** is an open-source, microservice-based video platform developed using the Spring Framework.  
It is designed as a long-term personal project, serving as a continuous learning environment for experimenting with modern backend development, microservices, and software architecture.  

The platform features user authentication, user management, video uploading, and interactive video functionalities.  
Calmify is built with scalability and modularity in mind, allowing easy addition of new services, features, and technologies over time.  
This project demonstrates practical application of microservice patterns, API design, and service orchestration in a hands-on context.

## 💻 Tech stack
- Java 21
- Spring (Boot, Web, Security, Cloud, Data)
- Docker
- Kafka
- PostgreSQL
- Testcontainers
- Hibernate
- JUnit 5
- Mockito
- Mapstruct
- Hibernate validator
- Github
- jjwt
- jnanoid

## ⚡ Being integrated
- Kubernetes
- Prometheus
- Grafana
- Swagger / OpenAPI

## 📃 Project architecture

![Architecture](docs/calmify-architecture.png)

### 💾 Discovery server
A service responsible for service discovery and registration in the system.  
Built using **Eureka**, it allows other microservices to find and communicate with each other efficiently.

### 🌐 API gateway
Responsible for routing incoming requests to the appropriate microservices.

It supports:
- JWT token filtering, adds received headers to request
- Retrieves service information from **Eureka**


### 🔐 Auth service
Handles all authentication-related operations for the platform, including user registration, login, JWT token issuance and refresh.  
Implemented using **Spring Security** and configured to retrieve private and public JWT keys from environment variables for secure token management.  

Key features:
- User registration and login with JWT authentication
- Token refresh and validation for secure access control
- Integration with **Kafka**: publishes `users.created` events to notify other microservices about new users
- Role-based access control using Spring Security
- Stateless authentication to ensure scalability across microservices

This service acts as the central authentication point in the Calmify microservice ecosystem, ensuring secure access and seamless communication between services.

### 🙍‍♂ User service
Responsible for managing user profiles and handling user-related operations.  
It listens to `users.created` events from **Kafka** and creates user records accordingly.  

Key features:
- Processes `users.created` events to create new users
- Validates incoming requests for data consistency and integrity
- Allows updating user details: name, description, and handle
- Automatically generates a unique handle upon user creation
- Ensures smooth integration with other microservices in the system

This service acts as the central user management component in the Calmify ecosystem, keeping user data consistent and accessible across the platform.

## License

&copy; 2025 ZapolyarnyDev

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.
