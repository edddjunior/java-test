# Sistema de Estoque - Microserviços

Sistema de gerenciamento de estoque construído com arquitetura de microserviços, seguindo princípios de **Domain-Driven Design (DDD)** e **Arquitetura Hexagonal**.

## Arquitetura

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    Frontend     │     │ Product Service │     │  Stock Service  │
│   (Angular)     │────▶│  (Spring Boot)  │◀────│  (Spring Boot)  │
│   Port: 4200    │     │   Port: 8080    │     │   Port: 8081    │
└─────────────────┘     └────────┬────────┘     └─────────────────┘
                                 │
                        ┌────────▼────────┐
                        │   PostgreSQL    │
                        │   Port: 5432    │
                        └─────────────────┘
```

### Estrutura do Projeto

```
├── backend/
│   ├── product-service/    # CRUD de produtos (Hexagonal + DDD)
│   └── stock-service/      # Consulta de estoque (Circuit Breaker)
├── frontend/               # Frontend Angular para demonstração
├── docker-compose.yml
├── Makefile
└── postman-collection.json
```

## Features

| Feature | Descrição |
|---------|-----------|
| **Hexagonal Architecture** | Separação clara entre domain, application e infrastructure |
| **Cache (Caffeine)** | Cache em memória no Product Service (TTL: 5min) |
| **Circuit Breaker** | Resilience4j no Stock Service para falhas do Product Service |
| **Rate Limiter** | 100 req/s por serviço |
| **Optimistic Locking** | Controle de concorrência com `@Version` |
| **Soft Delete** | Produtos não são removidos fisicamente |
| **Paginação** | Listagem paginada de produtos |
| **Structured Logging** | Logs JSON com requestId para rastreamento |
| **OpenAPI/Swagger** | Documentação automática das APIs |

## Quick Start

```bash
# Clonar e rodar
git clone <repo>
cd <repo>
make rebuild

# Verificar status
make status
make health
```

## Comandos Make

```bash
make help           # Lista todos os comandos
make up             # Sobe os serviços
make down           # Para os serviços
make rebuild        # Rebuilda e sobe tudo
make status         # Status dos containers
make health         # Health check das APIs
make logs           # Logs de todos os serviços
make test           # Roda todos os testes
make clean          # Remove containers e volumes
```

## APIs

| Serviço | Endpoint | Método | Descrição |
|---------|----------|--------|-----------|
| Product | `/api/v1/products` | GET | Lista produtos (paginado) |
| Product | `/api/v1/products` | POST | Cria produto |
| Product | `/api/v1/products/{id}` | GET | Busca por ID |
| Product | `/api/v1/products/{id}` | PUT | Atualiza produto |
| Product | `/api/v1/products/{id}` | DELETE | Remove produto (soft) |
| Stock | `/api/v1/stock/{productId}` | GET | Consulta estoque |

**Swagger UI:**
- Product: http://localhost:8080/swagger-ui.html
- Stock: http://localhost:8081/swagger-ui.html

---

## Demonstração de Features

### 1. Cache (Caffeine)

```bash
# Primeira requisição (busca no banco)
time curl -s http://localhost:8080/api/v1/products | head -c 100

# Segunda requisição (cache - mais rápida)
time curl -s http://localhost:8080/api/v1/products | head -c 100

# Verificar estatísticas do cache
curl -s http://localhost:8080/actuator/caches | jq
```

### 2. Rate Limiter (100 req/s)

```bash
# Enviar 150 requisições rapidamente
for i in {1..150}; do
  curl -s -o /dev/null -w "%{http_code} " http://localhost:8080/api/v1/products
done
echo ""
# Algumas retornarão 429 (Too Many Requests)
```

### 3. Circuit Breaker

```bash
# Parar o Product Service
docker compose stop product-service

# Consultar estoque (Circuit Breaker ativa)
curl -s http://localhost:8081/api/v1/stock/867de4e9-db92-44e6-ba29-ec58b60ef9ab | jq
# Retorna: SERVICE_UNAVAILABLE

# Reiniciar e aguardar recuperação
docker compose start product-service
sleep 20
curl -s http://localhost:8081/api/v1/stock/867de4e9-db92-44e6-ba29-ec58b60ef9ab | jq
# Retorna: dados do produto
```

### 4. Optimistic Locking

```bash
# Buscar produto e obter versão
PRODUCT=$(curl -s http://localhost:8080/api/v1/products | jq -r '.data.content[0]')
ID=$(echo $PRODUCT | jq -r '.id')
echo "Produto: $ID"

# Terminal 1: Atualizar produto
curl -s -X PUT "http://localhost:8080/api/v1/products/$ID" \
  -H "Content-Type: application/json" \
  -d '{"name":"Update 1","description":"Desc","price":100,"stockQuantity":10}' | jq

# Terminal 2: Tentar atualizar simultaneamente (conflito)
# Se a versão mudou, retorna 409 CONFLICT
```

### 5. Validação de Erros

```bash
# Produto não encontrado (404)
curl -s http://localhost:8080/api/v1/products/00000000-0000-0000-0000-000000000000 | jq

# UUID inválido (400)
curl -s http://localhost:8080/api/v1/products/invalid | jq

# Validação de campos (400)
curl -s -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"name":"","price":-1}' | jq
```

### 6. Paginação

```bash
# Página 0, 5 itens
curl -s "http://localhost:8080/api/v1/products?page=0&size=5" | jq '.data | {page, size, totalElements, totalPages}'

# Página 1
curl -s "http://localhost:8080/api/v1/products?page=1&size=5" | jq '.data | {page, first, last}'
```

### 7. Stock Service (Comunicação entre Microserviços)

```bash
# Listar produtos
curl -s http://localhost:8080/api/v1/products | jq '.data.content[0] | {id, name, stockQuantity}'

# Consultar estoque via Stock Service (chama Product Service internamente)
curl -s http://localhost:8081/api/v1/stock/<ID_DO_PRODUTO> | jq
```

---

## Tecnologias

**Backend:**
- Java 21
- Spring Boot 3.3
- Spring Data JPA
- PostgreSQL 16
- Resilience4j (Circuit Breaker, Rate Limiter)
- Caffeine Cache
- OpenFeign
- Flyway
- OpenAPI/Swagger

**Frontend:**
- Angular
- TypeScript
- RxJS

**Infraestrutura:**
- Docker & Docker Compose

## Testes

```bash
# Todos os testes
make test

# Apenas backend
make test-backend

# Apenas frontend (lint + build)
make test-frontend
```

## Postman

Importe o arquivo `postman-collection.json` no Postman para testar todas as APIs.
