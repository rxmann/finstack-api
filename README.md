# Budget Service API

A production-style backend service for personal budget tracking, recurring expenses, payment reminders, and future shared savings workflows.

Built as a real-world backend system with strong transactional consistency, background job processing, observability, and secure authentication.

---

## About the Service

The Budget Service API is a backend-first financial planning platform designed to help users:

- Track daily and monthly budgets
- Categorize expenses
- Manage recurring budget entries
- Receive payment reminders
- Monitor long-term savings goals
- Prepare for future shared fundraising and shared savings workflows

This project is being developed as a **personal-use product** with **production-grade backend architecture, scalability, and feature evolution in mind**.

---

## Core Capabilities

- Budget creation and category management
- Recurring budget scheduling and automated budget generation
- Payment reminder workflows
- OAuth2 Google + email/password authentication
- JWT + HttpOnly cookie-based secure session flow
- Optimistic locking for concurrent updates
- Distributed background jobs and schedulers
- OpenAPI / Swagger integration
- Observability and request tracing
- Dockerized local development
- Flyway-based schema migrations

---

## Domain Model (ERD)

> ![Database Schema](/docs/er-diagram.svg)

The domain model is centered around **budget lifecycle automation**, including recurring budget generation, reminder workflows, and future collaborative savings support.

---

## Architecture

The service follows a **layered architecture**:

- **Controller Layer** → API boundary and request handling
- **Service Layer** → business workflows and transaction orchestration
- **Repository Layer** → persistence abstraction
- **Scheduler / Jobs Layer** → recurring workflow execution
- **Security Layer** → JWT, OAuth2, cookie authentication
- **Configuration Layer** → observability, OpenAPI, and infrastructure setup

### Engineering Decisions

- Strong transactional boundaries for budget creation and updates
- Optimistic locking using version columns
- Flyway-driven schema evolution
- Docker-first local development
- Background jobs for recurring budget generation
- Extensible event-ready design for future WebSocket and SSE notifications
- Domain model prepared for collaborative budgeting workflows

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Docker + Docker Compose
- OpenAPI / Swagger
- OAuth2 Google
- JWT + HttpOnly Cookies
- Observability tooling
- WebSockets (planned v2)
- AI integrations (planned v3)

---

## Security

- OAuth2 Google authentication
- Email/password login
- JWT token issuance
- HttpOnly cookie-based auth headers
- Spring Security request authorization
- Role-based access support
- Transaction-safe update boundaries
- Secure OAuth client secret configuration via environment variables

---

## API Documentation

The project ships with built-in OpenAPI documentation.

### Available Docs

- OpenAPI JSON → `http://localhost:8080/api/v1/api-docs`
- Swagger UI → `http://localhost:8080/swagger-ui/index.html`

This makes API exploration, frontend integration, and testing easier with minimal setup effort.

---

## Run Locally

### Using Docker Compose

```bash
docker compose up --build
```

### Services

- API → `http://localhost:8080`
- Swagger → `http://localhost:8080/swagger-ui/index.html`
- OpenAPI Docs → `http://localhost:8080/api/v1/api-docs`
- PostgreSQL → `localhost:5430`

### Required Environment Variables

```env
GOOGLE_CLIENT_ID=your_client_id
GOOGLE_SECRET_KEY=your_secret
```

---

## Roadmap

### v1 — Shared Savings & Fundraising

- Shared savings goals
- Collaborative pooled budgets
- Fundraising / cause-based savings
- Multi-user contribution workflows

### v2 — Realtime Notifications

- WebSocket / SSE notifications
- Payment due alerts
- Budget threshold alerts
- Email reminders

### v3 — AI-Powered Insights

- AI spending insights
- Budget anomaly detection
- Smart category suggestions
- Predictive expense forecasting

---

## Why This Project

This project represents my backend engineering approach:

- Domain-first design
- Strong transactional consistency
- Scalable feature evolution
- Production-like security
- Roadmap-driven architecture
- Real-world product ownership mindset

The goal is to evolve this beyond a personal finance API into a **collaborative financial planning platform with realtime and intelligent capabilities**.
