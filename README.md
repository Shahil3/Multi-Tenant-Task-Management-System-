# Multi-Tenant Task Management System

A scalable and efficient task management solution designed for multi-tenant environments. This application allows multiple tenants (organizations or user groups) to manage tasks, assign roless, projects, and streamline workflows with a secure and intuitive interface.

##  Features

- **Transactional Integrity (ACID Compliance)**:
  - Ensures Atomicity, Consistency, Isolation, and Durability across all database operations.
- **Role-Based Access Control (RBAC)**:
  - Provides user-specific permissions for secure data access.
- **Tenant-Specific Isolation**:
  - Each tenant's data is securely separated to prevent unauthorized cross-tenant access.
- **Performance Optimization**:
  - Implements tenant-specific indexing and caching to optimize query response times.
- **Scalability and Resource Management**:
  - Dynamically allocates resources to support high-volume concurrent access.
- **Audit and Compliance**:
  - Captures tenant-specific logs for monitoring, governance, and security compliance.

---

## 📝 Problem Statement

Modern shared infrastructure systems require efficient solutions for data integrity, secure isolation, and resource optimization. This project addresses these challenges in a multi-tenant environment by using **transaction management**, **data security practices**, and **optimized resource utilization** to provide a robust solution.

---

## Methods Adopted

1. **Data Isolation**:
   - Introduced a `tenant_id` column in all database tables to enforce tenant-specific data separation.
2. **Transaction Management**:
   - Ensured ACID compliance for all CRUD operations.
3. **Concurrency Control**:
   - Used optimistic locking to handle simultaneous tenant requests efficiently.
4. **Indexing**:
   - Applied tenant-based and selective indexing for frequently queried columns to enhance performance.
5. **Scalability**:
   - Leveraged dynamic resource partitioning for seamless scaling.
6. **Logging and Auditing**:
   - Implemented tenant-specific logs to monitor user actions and system events for enhanced security.

---

## Key Issues Addressed

1. **Data Isolation**:
   - Prevent unauthorized cross-tenant data access.
2. **Transaction Management**:
   - Maintain ACID properties for consistent and reliable transactions.
3. **Concurrency Control**:
   - Efficiently handle high-volume concurrent access.
4. **Indexing and Optimization**:
   - Tenant-specific indexing for faster queries and reduced response times.
5. **Backup and Recovery**:
   - Implement tenant-level backup and recovery mechanisms.
6. **Scalability**:
   - Dynamically allocate resources to handle growing demands.
7. **Audit and Logging**:
   - Enable traceability through tenant-specific logs.

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

