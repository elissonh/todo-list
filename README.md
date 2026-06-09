[![en](https://img.shields.io/badge/lang-en-red.svg)](./README-en.md)

# 📜 Lista de Tarefas — Aplicação Full Stack

Aplicação full-stack para gerenciamento de tarefas construída com **Spring Boot**, **React** e **PostgreSQL**, conteinerizada com **Docker Compose**. Este projeto busca aplicar conceitos de design de APIs RESTful, modelagem de banco de dados relacional e integração entre frontend e backend.

---

## 📌 Sumário

- [Visão Geral](#visão-geral)
- [Arquitetura do Sistema](#arquitetura-do-sistema)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Modelagem do Banco de Dados](#modelagem-do-banco-de-dados)
- [Referência da API](#referência-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)

---

## 💻 Visão Geral

Esta aplicação permite que usuários criem, consultem, atualizem e removam tarefas por meio de uma interface web simples e intuitiva. O backend disponibiliza uma API REST consumida por uma aplicação React do tipo SPA (Single Page Application).

Todos os serviços são orquestrados com Docker Compose, tornando todo o ambiente reproduzível com um único comando.

---

## ⚙️ Arquitetura do Sistema

```text
┌──────────────────────────────────────────────────────────────┐
│                     Rede Docker                              │
│  ┌──────────────┐     ┌──────────────┐     ┌──────────────┐  │
│  │ Aplicação    │ ->  │ Spring Boot  │ ->  │ PostgreSQL   │  │
│  │ React        │     │ (Porta 8080) │     │ (Porta 5432) │  │
│  │ (Porta 3000) │     │              │     │              │  │
│  └──────────────┘     └──────────────┘     └──────────────┘  │
└──────────────────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|---------|-----------|
| Frontend | React |
| Backend | Spring Boot |
| Banco de Dados | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Ferramenta de Build | Maven |
| Conteinerização | Docker + Docker Compose |

---

## 🗄️ Modelagem do Banco de Dados

### Entidade: `task`

```sql
CREATE TABLE task (
    id             BIGSERIAL     PRIMARY KEY,
    description    VARCHAR(255)  NOT NULL,
    done           BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP     NOT NULL DEFAULT NOW()
);
```

| Coluna | Tipo | Descrição |
|---------|------|-----------|
| `id` | `BIGSERIAL` | Chave primária auto incrementável |
| `description` | `VARCHAR(255)` | Descrição da tarefa (obrigatória) |
| `done` | `BOOLEAN` | Indicador de status; por padrão é `false` |
| `created_at` | `TIMESTAMP` | Data e hora de criação registradas automaticamente |

---

## 🌐 Referência da API

**URL Base:** `http://localhost:8080/api/todos`

| Método | Endpoint | Descrição | Corpo da Requisição | Resposta |
|----------|----------|-----------|---------------------|-----------|
| `GET` | `/tasks` | Lista todas as tarefas | — | `200 OK` — lista de tarefas |
| `GET` | `/tasks/{id}` | Obtém uma tarefa específica | — | `200 OK` / `404 Not Found` |
| `POST` | `/tasks` | Cria uma nova tarefa | `{ "description": "Ler um livro" }` | `201 Created` — tarefa criada |
| `PUT` | `/tasks/{id}` | Atualização completa da tarefa | `{ "description": "Ler um livro", "done": boolean }` | `200 OK` — tarefa atualizada |
| `PATCH` | `/tasks/{id}` | Atualização parcial da tarefa | `{ "done": boolean }` | `200 OK` — tarefa atualizada |
| `DELETE` | `/tasks/{id}` | Remove uma tarefa | — | `204 No Content` |

### Exemplos de Payloads

#### Criar Tarefa — `POST /tasks`

```json
// Requisição
{
    "description": "Ler um livro"
}

// Resposta 201
{
    "id": 1,
    "description": "Ler um livro",
    "created_at": "2026-06-04T22:22:00",
    "done": false
}
```

#### Atualizar Tarefa — `PATCH /tasks/1`

```json
// Requisição
{
    "done": true
}

// Resposta 200
{
    "id": 1,
    "description": "Ler um livro",
    "created_at": "2026-06-04T22:22:00",
    "done": true
}
```

---

## 📁 Estrutura do Projeto

```text
todo-app/
├── backend/                        # Aplicação Spring Boot
│   ├── src/main/java/com/todo/
│   │   ├── controllers/
│   │   │   └── TaskController.java
│   │   ├── service/
│   │   │   └── TodoService.java    # Camada de regras de negócio
│   │   ├── repositories/
│   │   │   └── TaskRepository.java # Interface Spring Data JPA
│   │   ├── domain/
│   │   │   └── dto
│   │   │   └── entities
│   ├── src/main/resources/
│   │   └── application.properties  # Configuração do banco e servidor
│   └── pom.xml
│
├── frontend/                       # Aplicação React
│   ├── src/
│   │   ├── components/
│   │   │   ├── ...
│   │   ├── services/
│   │   │   └── ...
│   │   └── App.jsx
│   └── package.json
│
├── docker-compose.yml              # Orquestra todos os serviços
├── README-en.md
└── README.md
```

---

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose instalados

### Executando toda a aplicação

```bash
# Clonar o repositório
git clone https://github.com/elissonh/todo-list.git
cd todo-app

# Construir e iniciar todos os serviços
docker compose up --build
```

| Serviço | URL |
|----------|-----|
| Frontend React | http://localhost:3000 |
| API Spring Boot | http://localhost:8080/tasks/ |
| PostgreSQL | `localhost:5432` (usuário: `postgres`, banco: `tododb`) |

### Parando os serviços

```bash
docker compose down

# Para remover também o volume do banco de dados
docker compose down -v
```

### Executando os serviços individualmente (desenvolvimento)

```bash
# Backend — dentro da pasta /backend
./mvn spring-boot:run

# Frontend — dentro da pasta /frontend
npm install && npm run dev
```

> Certifique-se de que uma instância do PostgreSQL esteja em execução localmente e que o arquivo `application.properties` esteja configurado com a string de conexão correta antes de executar os serviços individualmente.

---
