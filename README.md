# Multi-Tenant Task Management System

A scalable and efficient task management solution designed for multi-tenant environments. This application allows multiple tenants (organizations or user groups) to manage tasks, assign roless, projects, and streamline workflows with a secure and intuitive interface.

## Features

- **Multi-Tenant Architecture**: Each tenant gets an isolated workspace.
- **Task Management**: Create, assign, and track projects with ease.
- **Role-Based Access Control**: Manage user roles and permissions for security.
- **Caching**: Enhanced performance with Ehcache integration.
- **Dynamic Views**: Built-in support for Thymeleaf templating for customized UI.
- **Secure Data**: Implements robust authentication and authorization using Spring Security.

## Technology Stack

- **Backend**: 
  - Spring Boot Framework
  - Java 17
  - Spring Data JPA
  - Spring Security
  - Ehcache
- **Frontend**: Thymeleaf
- **Database**: PostgreSQL
- **Build Tool**: Maven
- **Testing**: Spring Boot Starter Test

## Installation

### Prerequisites

- Java 17
- PostgreSQL
- Maven (build tool)

### Steps to Run the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Shahil3/Multi-Tenant-Task-Management-System-.git
   cd Multi-Tenant-Task-Management-System-
   ```

2. **Set up the database**:
   - Create a PostgreSQL database for the application.
   - Update the `application.properties` file located in `src/main/resources` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/your_database_name
     spring.datasource.username=your_database_username
     spring.datasource.password=your_database_password
     ```

3. **Build the application**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**:
   Open your web browser and navigate to `http://localhost:8080`.

## Project Structure

```plaintext
Multi-Tenant-Task-Management-System-
├── src/
│   ├── main/
│   │   ├── java/                     # Java source files
│   │   ├── resources/                # Configuration and templates
│   │       ├── templates/            # Thymeleaf templates
│   │       ├── application.properties # Configuration properties
│   └── test/                         # Test cases
├── pom.xml                           # Maven configuration file
├── README.md                         # Project documentation
```

