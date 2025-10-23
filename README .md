# **Secure Task Management API**

## **📝 Project Overview**

This project is a RESTful API built with **Spring Boot** for managing user tasks. It adheres to modern backend best practices, featuring a layered architecture, secure user authentication using **JWT (JSON Web Tokens)**, and strong password handling via **BCrypt**.

The application uses an in-memory **H2 database** for persistence, making setup and testing exceptionally fast.

## **✨ Features**

* **User Registration (/auth/register):** Securely create a new user with BCrypt-hashed password storage.  
* **User Login (/auth/login):** Authenticates a user and issues a time-bound JWT access token.  
* **Token-Based Security:** All task management endpoints are protected and require a valid JWT in the Authorization header.  
* **Task CRUD Operations:**  
  * Create, Fetch, Update (Status), and Delete tasks.  
  * Users can **only** access their own tasks, enforcing security and data separation (403 Forbidden for unauthorized access attempts).  
* **Global Error Handling:** Custom exception handling with @ControllerAdvice ensures clear, structured error responses for validation failures (400) and unauthorized access (401/403).  
* **Layered Architecture:** Clear separation of concerns (Controller, Service, Repository).

## **🛠️ Technologies Used**

* **Framework:** Spring Boot 3.2+  
* **Build Tool:** Maven  
* **Language:** Java 17+  
* **Security:** Spring Security, BCrypt, JJWT (JSON Web Tokens)  
* **Data:** Spring Data JPA, H2 Database (In-Memory)  
* **Utilities:** Lombok (for boilerplate reduction)

## **🚀 Getting Started**

### **Prerequisites**

You will need the following installed on your machine:

* **Java Development Kit (JDK) 17 or higher**  
* **Maven 3.6.3 or higher**  
* A REST client for testing (e.g., Postman, Insomnia, or cURL).

### **Setup and Running**

1. **Clone the repository:**  
   git clone \[your-repository-url\]  
   cd secure-task-management

2. **Build the project using Maven:**  
   mvn clean install

3. **Run the application:**  
   mvn spring-boot:run

The application will start on http://localhost:8080.

**💡 H2 Console:** While running, you can access the H2 database console at http://localhost:8080/h2-console. Use the JDBC URL configured in application.properties (e.g., jdbc:h2:mem:taskdb).

## **🔑 API Endpoints**

### **Authentication**

| Method | Endpoint | Description | Status Codes |
| :---: | :---: | :---: | :---: |
| POST | /auth/register | Create a new user account. | 200 (Success), 400 (Validation/Duplicate Email) |
| POST | /auth/login | Authenticate and receive a JWT. | 200 (Success), 400 (Invalid Credentials) |
| POST | /auth/logout | Client-side logout (discard token). | 200 |

#### **Example: Register Request**

POST /auth/register  
{  
  "email": "testuser@example.com",  
  "password": "strongpassword123",  
  "name": "Test User"  
}

#### **Example: Login Response**

{  
  "accessToken": "eyJhbGciOiJIUzI1NiI...",  
  "name": "Test User",  
  "email": "testuser@example.com"  
}

### **Task Management**

**Note:** All /tasks endpoints require the JWT in the Authorization header: Authorization: Bearer \<accessToken\>

| Method | Endpoint | Description | Status Codes |
| :---: | :---: | :---: | :---: |
| POST | /tasks | Create a new task for the logged-in user. | 201 (Created), 401 (Unauthorized) |
| GET | /tasks | Retrieve all tasks owned by the current user. | 200 (OK), 401 (Unauthorized) |
| PUT | /tasks/{id}?status={NEW\_STATUS} | Update the status of a specific task. | 200, 401, 403 (Not owner) |
| DELETE | /tasks/{id} | Delete a specific task. | 204 (No Content), 401, 403 (Not owner) |

#### **Example: Create Task Request**

POST /tasks  
Authorization: Bearer \<token\>  
{  
  "title": "Finish Spring Boot Project",  
  "description": "Ensure all services and security layers are implemented.",  
  "status": "IN\_PROGRESS"  
}

#### **Example: Update Task Status**

PUT /tasks/1?status=DONE  
Authorization: Bearer \<token\>

## **📄 Configuration Details**

The application configuration is managed in src/main/resources/application.properties. Key properties include:

| Property | Description |
| :---: | :---: |
| spring.datasource.url | H2 JDBC URL (jdbc:h2:mem:taskdb) |
| jwt.secret.key | The secret key used to sign the JWTs. **Must be long and secure in production.** |
| jwt.expiration.ms | JWT expiry time in milliseconds (default: 1 hour) |

