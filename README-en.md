[![pt-br](https://img.shields.io/badge/lang-pt--br-green.svg)](./README.md)

# 📜 Todo List — Full Stack Application

A full stack task management application built with **Spring Boot**, **React**, and **PostgreSQL**, containerized with **Docker Compose**. This project demonstrates RESTful API design, relational database modeling and frontend-backend integration.

---

## 📌 Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Tech Stack](#tech-stack)
- [Database Design](#database-design)
- [API Reference](#api-reference)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)

---

## 💻 Overview

This application allows users to create, read, update, and delete tasks through a clean web interface. The backend exposes a REST API consumed by a React single-page application. All services are orchestrated with Docker Compose, making the entire stack reproducible with a single command.

---

## ⚙️ System Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                     Docker Network                           │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐  │
│  │   React App  │ ->  │  Spring Boot │ ->  │PostgreSQL    │  │
│  │  (Port 3000) │     │  (Port 8080) │     │  (Port 5432) │  │
│  └──────────────┘     └──────────────┘     └──────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Technology
|---|---|
| Frontend | React 
| Backend  | Spring Boot 
| Database | PostgreSQL
| ORM | Spring Data JPA / Hibernate 
| Build Tool | Maven 
| Containerization | Docker + Docker Compose

---

## 🗄️ Database Design

### Entity: `task`

```sql
CREATE TABLE task (
    id             BIGSERIAL     PRIMARY KEY,
    description    VARCHAR(255)  NOT NULL,
    done           BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP     NOT NULL DEFAULT NOW()
);
```

| Column | Type | Description |
|---|---|---|
| `id` | `BIGSERIAL` | Auto-incremented surrogate primary key |
| `description` | `VARCHAR(255)` | The task description, required |
| `done` | `BOOLEAN` | Status flag, defaults to `false` on creation |
| `created_at` | `TIMESTAMP` | Immutable audit timestamp set at insert time |
---

## 🌐 API Reference

**Base URL:** `http://localhost:8080/api/todos`

| Method | Endpoint | Description | Request Body | Response |
|---|---|---|---|---|
| `GET` | `/tasks` | List all todos | — | `200 OK` — array of todos |
| `GET` | `/tasks/{id}` | Get a single todo | — | `200 OK` / `404 Not Found` |
| `POST` | `/tasks` | Create a new todo | `{ "description": "Read a book" }` | `201 Created` — created todo |
| `PUT` | `/tasks/{id}` | Full update of a todo | `{ "description": "Read a book", "done": boolean }` | `200 OK` — updated todo |
| `PATCH` | `/tasks/{id}` | Partial update of a todo | `{ "done": boolean }` | `200 OK` — updated todo |
| `DELETE` | `/tasks/{id}` | Delete a todo | — | `204 No Content` |

### Example Payloads

**Create — `POST /tasks`**
```json
// Request
{
    "description": "Read a book"
}

// Response 201
{
    "id": 1,
    "description": "Read a book",
    "created_at": "2026-06-04T22:22:00",
    "done": false
}
```

**Update — `PATCH /tasks/1`**
```json
// Request
{ "done": true }

// Response 200
{
    "id": 1,
    "description": "Read a book",
    "created_at": "2026-06-04T22:22:00",
    "done": true
}
```

---

## 📁 Project Structure

```
todo-app/
├── backend/                        # Spring Boot application
│   ├── src/main/java/com/todo/
│   │   ├── controllers/
│   │   │   └── TaskController.java
│   │   ├── service/
│   │   │   └── TodoService.java    # Business logic layer
│   │   ├── repositories/
│   │   │   └── TaskRepository.java # Spring Data JPA interface
│   │   ├── domain/
│   │   │   └── dto
│   │   │   └── entities
│   ├── src/main/resources/
│   │   └── application.properties  # DB config, server port
│   └── pom.xml
│
├── frontend/                       # React application
│   ├── src/
│   │   ├── components/
│   │   │   ├── ...
│   │   ├── services/
│   │   │   └── ...
│   │   └── App.jsx
│   └── package.json
│
├── docker-compose.yml              # Orchestrates all three services
└── README-en.md
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed

### Run the full stack

```bash
# Clone the repository
git clone https://github.com/elissonh/todo-list.git
cd todo-app

# Build and start all services
docker compose up --build
```

| Service | URL |
|---|---|
| React frontend | http://localhost:3000 |
| Spring Boot API | http://localhost:8080/tasks/ |
| PostgreSQL | `localhost:5432` (user: `postgres`, db: `tododb`) |

### Stop the stack

```bash
docker compose down

# To also remove the database volume
docker compose down -v
```

### Run services individually (development)

```bash
# Backend — from /backend
./mvn spring-boot:run

# Frontend — from /frontend
npm install && npm run dev
```

> Make sure a PostgreSQL instance is running locally and `application.properties` is updated with the correct connection string before running services individually.
