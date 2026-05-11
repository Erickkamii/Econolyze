# Econolyze

Econolyze is a personal finance management platform with a web front-end, API Gateway, authentication service, financial domain service, and AI assistant. The project brings together transactions, accounts, goals, recurring expenses, payments, analytics, and investment features in a microservice architecture.

Portuguese version: [README.md](README.md)

## Overview

The system is split into two main areas:

- `econolyze-front-end`: the Next.js web application used by end users.
- `services`: the Quarkus services responsible for the API, authentication, financial rules, AI features, and local database setup.

## Main Features

- Sign up, login, token refresh, and logout with JWT.
- Wallet view with balances, accounts, and financial activity.
- Transaction history with creation, editing, filters, and categories.
- Recurring expenses and payment tracking.
- Financial goals with progress tracking.
- Analytics, dashboard, and financial reports.
- Investment comparisons and projections based on CDI.
- AI financial chat with RAG and internal tools to query user data.
- API Gateway centralizing communication between the front-end and microservices.

## Architecture

```text
econolyze-front-end (Next.js)
        |
        v
gateway-service (Quarkus, port 8080)
        |
        +--> auth-service (Quarkus, port 8081)
        +--> financial-service (Quarkus, port 8082)
        +--> ai-service (Quarkus, port 8083)
        |
        v
PostgreSQL + pgvector (port 5432)
```

## Stack

### Front-end

- Next.js 16
- React 19
- TypeScript
- Tailwind CSS 4
- Radix UI
- React Hook Form
- Zod
- Recharts
- Lucide React

### Back-end

- Java with Quarkus
- REST APIs with Jakarta REST
- PostgreSQL
- pgvector for embeddings
- JWT with RSA keys
- REST Client between services
- Scheduler in the financial service
- LangChain4j
- Groq through an OpenAI-compatible API for chat
- Gemini Embeddings for RAG

## Project Structure

```text
.
├── econolyze-front-end/
│   ├── app/
│   ├── components/
│   ├── lib/
│   └── package.json
└── services/
    ├── auth-service/
    ├── financial-service/
    ├── gateway-service/
    ├── ai-service/
    ├── init/
    └── docker-compose.yml
```

## Services

| Service | Port | Responsibility |
| --- | ---: | --- |
| Front-end | 3000 | Econolyze web interface |
| Gateway | 8080 | Main API entry point and routing to internal services |
| Auth | 8081 | Registration, login, refresh, logout, and token issuing |
| Financial | 8082 | Accounts, balances, transactions, goals, recurring expenses, payments, CDI, and reports |
| AI | 8083 | Financial chat, RAG, embeddings, and support tools |
| PostgreSQL/pgvector | 5432 | Relational persistence and vector storage |

## Requirements

- Node.js 20 or newer
- npm
- Java 21 or a version compatible with the Quarkus services
- Maven Wrapper included in each service (`./mvnw`)
- Docker or Podman
- RSA JWT keys in the services that sign or validate tokens
- API keys for AI features when running `ai-service`

## Environment Variables

The Quarkus services read configuration from environment variables. For local development, use values equivalent to these:

```env
DB_USERNAME=erick
DB_PASSWORD=029051
DB_URL=postgresql://localhost:5432/econolyze
DB_HOST=localhost
DB_PORT=5432
DB_NAME=econolyze
INTERNAL_SECRET=change-me
BANCO_CENTRAL_API_URL=https://api.bcb.gov.br
GROQ_API_KEY=your-groq-api-key
GEMINI_API_KEY=your-gemini-api-key
```

For the front-end:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

Note: `next.config.ts` may contain a hardcoded local value for `NEXT_PUBLIC_API_BASE_URL`. Adjust that value or use an environment variable according to your development network.

## Running Locally

### 1. Start the database

```bash
cd services
docker compose up -d
```

The compose file starts PostgreSQL with pgvector using the `econolyze` database.

### 2. Start the Quarkus services

Open one terminal for each service:

```bash
cd services/auth-service
./mvnw quarkus:dev
```

```bash
cd services/financial-service
./mvnw quarkus:dev
```

```bash
cd services/ai-service
./mvnw quarkus:dev
```

```bash
cd services/gateway-service
./mvnw quarkus:dev
```

The Gateway should be available at `http://localhost:8080`.

### 3. Start the front-end

```bash
cd econolyze-front-end
npm install
npm run dev
```

The web application is available at `http://localhost:3000`.

## Front-end Scripts

```bash
npm run dev      # starts the development server
npm run build    # creates a production build
npm run start    # runs the production build
npm run lint     # runs ESLint
```

## Building Services

Each Quarkus service can be packaged individually:

```bash
./mvnw package
```

After the build, run:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Main Endpoints

The web application should consume the Gateway at `http://localhost:8080/api`. The main route groups include:

- `/auth`: authentication, registration, login, refresh, and logout.
- `/api/accounts`: user accounts.
- `/api/account/balance`: consolidated balance.
- `/api/transaction`: transactions and history.
- `/api/financial/payment`: payments.
- `/api/recurring`: recurring entries.
- `/api/goal`: financial goals.
- `/api/dashboard`: summaries and indicators.
- `/api/investment`: investment data and calculations.
- `/api/chat`: AI financial assistant.

## Development Notes

- JWTs are issued by `auth-service` and validated by the other services using the public key in `src/main/resources/keys/publicKey.pem`.
- `financial-service` uses `INTERNAL_SECRET` to protect internal routes consumed by `ai-service`.
- `ai-service` depends on the pgvector database, the Groq key for chat, and the Gemini key for embeddings.
- In development mode, the Quarkus UI is available at `/q/dev/` for each service.
- The Gateway Swagger UI is configured at `/swagger-ui`.

## Status

This project is under active development. This README documents the current local workflow and can be updated as the services are prepared for deployment.
