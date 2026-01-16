# Student Course Registration

[![Java](https://img.shields.io/badge/Java-17-brightgreen)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.10-blue)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Description
Spring Boot REST API за регистрация на студенти за курсове.  
Позволява:
- CRUD операции със студенти и курсове
- Регистрация на студенти за курсове
- JWT базирана автентикация и авторизация

---

## Technologies
- **Java 17**
- **Spring Boot 3**
- **PostgreSQL**
- **Spring Data JPA**
- **Spring Security (JWT)**
- **Maven**
- **Swagger/OpenAPI**## API Endpoints

---

Swagger UI за тестване на API е достъпен на:

http://localhost:8080/swagger-ui/index.html

---

## Setup / Installation

1. **Клонирай репото:**
git clone https://github.com/yourusername/student-course-register.git
cd student-course-register

2. **Създай локална база данни PostgreSQL**

3. В **src/main/resources/application-dev.properties** (gitignored) сложи реалните данни за локална разработка

---

### Authentication / Автентикация

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| POST | /auth/register/teacher | Регистрация на нов преподавател | ADMIN |
| POST | /auth/activate/teacher | Активиране на профил на преподавател с токен | Everyone with token |
| POST | /auth/login/teacher | Вход на преподавател (JWT token) | TEACHER, ADMIN |
| POST | /auth/register/student | Регистрация на нов студент | ADMIN, TEACHER |
| POST | /auth/activate/student | Активиране на студентски профил с токен | Everyone with token |
| POST | /auth/login/student | Вход на студент (JWT token) | STUDENT, ADMIN, TEACHER |
| POST | /auth/change-password/teacher | Смяна на парола за преподавател | TEACHER, ADMIN |
| POST | /auth/change-password/student | Смяна на парола за студент | STUDENT, ADMIN |

---

### Teachers / Преподаватели

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| GET | /api/teachers | Връща всички преподаватели | ADMIN |
| GET | /api/teachers/{id} | Връща конкретен преподавател | ADMIN или собственик |
| PUT | /api/teachers/{id} | Актуализира преподавател | ADMIN |
| DELETE | /api/teachers/{id} | Изтрива преподавател | ADMIN |
| PATCH | /api/teachers/{id}/role | Променя ролята на преподавател | ADMIN |

---

### Students / Студенти

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| GET | /api/students | Връща всички студенти | ADMIN |
| GET | /api/students/{id} | Връща конкретен студент | ADMIN или собственик |
| PUT | /api/students/{id} | Актуализира студент | ADMIN |
| DELETE | /api/students/{id} | Изтрива студент | ADMIN |

---

### Courses / Курсове

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| GET | /api/courses | Всички курсове | ADMIN |
| GET | /api/courses/{id} | Конкретен курс | ADMIN или преподавателът му |
| GET | /api/courses/teacher/{teacherId} | Всички курсове на преподавател | ADMIN или преподавател |
| POST | /api/courses/save | Създаване на нов курс | ADMIN |
| PUT | /api/courses/{id} | Актуализация на курс | ADMIN |
| DELETE | /api/courses/{id} | Изтриване на курс | ADMIN |

---

### Rooms / Зали

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| GET | /api/rooms | Всички зали | ADMIN |
| GET | /api/rooms/{id} | Конкретна зала | ADMIN |
| POST | /api/rooms/save | Създаване на нова зала | ADMIN |
| PUT | /api/rooms/{id} | Актуализация на зала | ADMIN |
| DELETE | /api/rooms/{id} | Изтриване на зала | ADMIN |

---

### Enrollments / Записвания

| Method | URL | Description | Roles |
|--------|-----|------------|-------|
| POST | /api/enrollments/save | Създаване на ново записване (студент за курс) | ADMIN |
| GET | /api/enrollments | Всички записвания | ADMIN |
| GET | /api/enrollments/{id} | Конкретно записване | ADMIN или собственик |
| PUT | /api/enrollments/{id} | Актуализиране на оценка за записване | ADMIN или собственик |
| DELETE | /api/enrollments/{id} | Изтриване на записване | ADMIN |

---

### Забележки

- **JWT Token**: За всички операции, освен регистрация/активация, е нужен токен в Authorization header:  
`Authorization: Bearer <token>`  
- **Роли**:  
  - `ADMIN` – администратор, пълен достъп  
  - `TEACHER` – достъп до своите курсове и ученици  
  - `STUDENT` – достъп до собствените си записи и профил  
- **Validation**: Всички POST/PUT/PATCH заявки очакват JSON тела със съответния DTO формат.  
 за документация

---

