.PHONY: help up down build rebuild logs clean test test-backend test-frontend status ps

GREEN  := \033[0;32m
YELLOW := \033[0;33m
CYAN   := \033[0;36m
RESET  := \033[0m

help: ## Mostra esta ajuda
	@echo ""
	@echo "$(CYAN)Inventory System - Comandos disponíveis$(RESET)"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(GREEN)%-15s$(RESET) %s\n", $$1, $$2}'
	@echo ""

up: ## Sobe todos os serviços
	@echo "$(CYAN)Subindo serviços...$(RESET)"
	@docker compose up -d
	@echo "$(GREEN)Serviços rodando!$(RESET)"
	@$(MAKE) -s status

down: ## Para todos os serviços
	@echo "$(YELLOW)Parando serviços...$(RESET)"
	@docker compose down

build: ## Builda todos os serviços
	@echo "$(CYAN)Buildando serviços...$(RESET)"
	@docker compose build

rebuild: ## Rebuilda e sobe todos os serviços
	@echo "$(CYAN)Rebuildando serviços...$(RESET)"
	@docker compose up -d --build
	@echo "$(GREEN)Serviços rodando!$(RESET)"
	@$(MAKE) -s status

logs: ## Mostra logs de todos os serviços
	@docker compose logs -f

logs-product: ## Mostra logs do product-service
	@docker compose logs -f product-service

logs-stock: ## Mostra logs do stock-service
	@docker compose logs -f stock-service

logs-frontend: ## Mostra logs do frontend
	@docker compose logs -f frontend

status: ## Mostra status dos serviços
	@echo ""
	@echo "$(CYAN)Status dos serviços:$(RESET)"
	@docker compose ps --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"
	@echo ""

ps: status ## Alias para status

clean: ## Remove containers, volumes e imagens
	@echo "$(YELLOW)Limpando tudo...$(RESET)"
	@docker compose down -v --rmi local
	@echo "$(GREEN)Limpo!$(RESET)"

test: test-backend test-frontend ## Roda todos os testes

test-backend: test-product test-stock ## Roda testes do backend
	@echo "$(GREEN)Testes do backend concluídos!$(RESET)"

test-product: ## Roda testes do product-service
	@echo "$(CYAN)Testando Product Service...$(RESET)"
	@docker run --rm -v $(PWD)/backend/product-service:/app -w /app maven:3.9-eclipse-temurin-21 mvn test -q

test-stock: ## Roda testes do stock-service
	@echo "$(CYAN)Testando Stock Service...$(RESET)"
	@docker run --rm -v $(PWD)/backend/stock-service:/app -w /app maven:3.9-eclipse-temurin-21 mvn test -q

test-frontend: ## Roda lint e build do frontend
	@echo "$(CYAN)Verificando Frontend...$(RESET)"
	@cd frontend && npm run lint
	@cd frontend && npm run build
	@echo "$(GREEN)Frontend OK!$(RESET)"

install: ## Instala dependências do frontend
	@echo "$(CYAN)Instalando dependências do frontend...$(RESET)"
	@cd frontend && npm install

health: ## Verifica health dos serviços
	@echo "$(CYAN)Health Check:$(RESET)"
	@echo "Product Service: $$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8080/actuator/health)"
	@echo "Stock Service:   $$(curl -s -o /dev/null -w '%{http_code}' http://localhost:8081/actuator/health)"
	@echo "Frontend:        $$(curl -s -o /dev/null -w '%{http_code}' http://localhost:4200)"

urls: ## Mostra URLs dos serviços
	@echo ""
	@echo "$(CYAN)URLs dos serviços:$(RESET)"
	@echo "  $(GREEN)Frontend:$(RESET)        http://localhost:4200"
	@echo "  $(GREEN)Product API:$(RESET)     http://localhost:8080/api/v1/products"
	@echo "  $(GREEN)Stock API:$(RESET)       http://localhost:8081/api/v1/stock/{id}"
	@echo "  $(GREEN)Swagger Product:$(RESET) http://localhost:8080/swagger-ui.html"
	@echo "  $(GREEN)Swagger Stock:$(RESET)   http://localhost:8081/swagger-ui.html"
	@echo ""
