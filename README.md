# 📚 Library 2026: Modern Library Management System

**Library 2026** is a robust, full-featured backend application designed to modernize library operations. Built with a layered architecture using **Spring Boot 3.3.5**, it handles complex tasks like JWT-based security, role-based access control, automated fine calculation, and dynamic book filtering.

## 🚀 Tech Stack
* **Backend:** Java 17, Spring Boot 3.3.5
* **Security:** Spring Security, JSON Web Token (JWT)
* **Database:** MySQL, Spring Data JPA (Hibernate)
* **Mapping & Validation:** ModelMapper, Jakarta Validation
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Build Tool:** Maven

---

## ✨ Key Features

### 📖 Book Management
* **Full CRUD Operations:** Support for adding, updating, and deleting books.
* **Partial Updates:** Implemented `PATCH` mapping using Map-based logic for scalable partial updates.
* **Advanced Searching:** Filter books dynamically by **Author**, **Category**, or **Genre**.
* **Pagination & Sorting:** Efficient data retrieval for large catalogs to optimize performance.

### 🔐 Security & Authentication
* **JWT Authentication:** Secure stateless communication using hashed passwords and token-based login.
* **Role-Based Access Control (RBAC):** Distinct permissions for `USER` and `ADMIN` roles.
* **Secure Registration:** Automated password encoding using `BCrypt`.

### 💸 Borrowing & Fine System
* **Borrowing Logic:** Users can borrow available books; records are linked to the authenticated user.
* **Admin Dashboard:** Specialized endpoints to view all borrowed books and track overdue items.
* **Revenue Tracking:** Automated fine collection and total revenue calculation for the library.

---

## 🏗️ Architecture & Design
The project follows the **Layered Architecture** pattern to ensure separation of concerns and maintainability.

* **Controller Layer:** Handles REST API endpoints and request validation.
* **Service Layer:** Contains business logic (e.g., fine calculation, borrow limits).
* **Repository Layer:** Interacts with MySQL using JPA/Hibernate.
* **DTO Pattern:** Uses `BookDto` and `UserDto` with **ModelMapper** to prevent exposing internal entities.

---

## 🛠️ Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/ce24-razjj/library-management-system.git](https://github.com/ce24-razjj/library-management-system.git)
