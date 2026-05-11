# Econolyze

Econolyze e uma plataforma de gestao financeira pessoal com front-end web, API Gateway, servicos de autenticacao, dominio financeiro e assistente de IA. O projeto combina controle de transacoes, contas, metas, recorrencias, pagamentos, analises e recursos de investimento em uma arquitetura de microsservicos.

English version: [README_en.md](README_en.md)

## Visao Geral

O sistema e organizado em duas partes principais:

- `econolyze-front-end`: aplicacao web em Next.js usada pelo usuario final.
- `services`: conjunto de servicos Quarkus responsaveis pela API, autenticacao, regras financeiras, IA e banco de dados local.

## Principais Recursos

- Cadastro, login, refresh de token e logout com JWT.
- Carteira com saldo, contas e movimentacoes financeiras.
- Historico de transacoes com criacao, edicao, filtros e categorias.
- Despesas recorrentes e controle de pagamentos.
- Metas financeiras com acompanhamento de progresso.
- Analises, dashboard e relatorios financeiros.
- Comparacoes e projecoes de investimento baseadas em CDI.
- Chat financeiro com IA, RAG e ferramentas internas para consultar dados do usuario.
- API Gateway centralizando a comunicacao entre front-end e microsservicos.

## Arquitetura

```text
econolyze-front-end (Next.js)
        |
        v
gateway-service (Quarkus, porta 8080)
        |
        +--> auth-service (Quarkus, porta 8081)
        +--> financial-service (Quarkus, porta 8082)
        +--> ai-service (Quarkus, porta 8083)
        |
        v
PostgreSQL + pgvector (porta 5432)
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

- Java com Quarkus
- REST APIs com Jakarta REST
- PostgreSQL
- pgvector para embeddings
- JWT com chaves RSA
- REST Client entre servicos
- Scheduler no servico financeiro
- LangChain4j
- Groq via API compativel com OpenAI para chat
- Gemini Embeddings para RAG

## Estrutura do Projeto

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

## Servicos

| Servico | Porta | Responsabilidade |
| --- | ---: | --- |
| Front-end | 3000 | Interface web do Econolyze |
| Gateway | 8080 | Entrada principal da API e roteamento para os servicos internos |
| Auth | 8081 | Registro, login, refresh, logout e emissao de tokens |
| Financial | 8082 | Contas, saldo, transacoes, metas, recorrencias, pagamentos, CDI e relatorios |
| AI | 8083 | Chat financeiro, RAG, embeddings e ferramentas de apoio |
| PostgreSQL/pgvector | 5432 | Persistencia relacional e armazenamento vetorial |

## Pre-requisitos

- Node.js 20 ou superior
- npm
- Java 21 ou versao compativel com o projeto Quarkus
- Maven Wrapper incluido em cada servico (`./mvnw`)
- Docker ou Podman
- Chaves JWT RSA nos servicos que validam ou assinam tokens
- Chaves de API para os recursos de IA, quando o `ai-service` for usado

## Variaveis de Ambiente

Os servicos Quarkus leem configuracoes por variaveis de ambiente. Para desenvolvimento local, use valores equivalentes aos abaixo:

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

No front-end:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api
```

Observacao: o `next.config.ts` pode conter um valor local fixo para `NEXT_PUBLIC_API_BASE_URL`. Ajuste esse valor ou use variavel de ambiente conforme sua rede de desenvolvimento.

## Executando Localmente

### 1. Subir o banco

```bash
cd services
docker compose up -d
```

O compose sobe um PostgreSQL com pgvector usando o banco `econolyze`.

### 2. Subir os servicos Quarkus

Abra um terminal para cada servico:

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

O Gateway deve ficar disponivel em `http://localhost:8080`.

### 3. Subir o front-end

```bash
cd econolyze-front-end
npm install
npm run dev
```

A aplicacao web fica disponivel em `http://localhost:3000`.

## Scripts do Front-end

```bash
npm run dev      # inicia o servidor de desenvolvimento
npm run build    # gera build de producao
npm run start    # executa a build de producao
npm run lint     # executa ESLint
```

## Build dos Servicos

Cada servico Quarkus pode ser empacotado individualmente:

```bash
./mvnw package
```

Depois do build, execute:

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

## Endpoints Principais

A aplicacao web deve consumir o Gateway em `http://localhost:8080/api`. Entre os principais grupos de rotas estao:

- `/auth`: autenticacao, registro, login, refresh e logout.
- `/api/accounts`: contas do usuario.
- `/api/account/balance`: saldo consolidado.
- `/api/transaction`: transacoes e historico.
- `/api/financial/payment`: pagamentos.
- `/api/recurring`: recorrencias.
- `/api/goal`: metas financeiras.
- `/api/dashboard`: resumos e indicadores.
- `/api/investment`: dados e calculos de investimento.
- `/api/chat`: assistente financeiro com IA.

## Observacoes de Desenvolvimento

- Os tokens JWT sao emitidos pelo `auth-service` e validados pelos demais servicos usando a chave publica em `src/main/resources/keys/publicKey.pem`.
- O `financial-service` usa `INTERNAL_SECRET` para proteger rotas internas consumidas pelo `ai-service`.
- O `ai-service` depende do banco com pgvector, da chave do Groq para chat e da chave do Gemini para embeddings.
- Em desenvolvimento, a UI do Quarkus fica disponivel em `/q/dev/` para cada servico.
- O Swagger UI do Gateway esta configurado em `/swagger-ui`.

## Status

Projeto em desenvolvimento. O README descreve o fluxo local atual e pode ser ajustado conforme os servicos forem consolidados para deploy.
