FROM node:20-alpine

# Define o diretório de trabalho
WORKDIR /app

# Instala o 'serve' globalmente como root, antes de mudar de usuário
RUN npm install -g serve

# Ajusta as permissões da pasta /app para o usuário node
RUN chown node:node /app

# Muda para o usuário não-root 'node'
USER node

# Copia arquivos de dependências primeiro (otimização de cache)
# Definindo o dono como 'node' durante a cópia
COPY --chown=node:node package*.json ./

# Instala dependências do projeto
RUN npm install

# Copia o restante dos arquivos do código fonte
COPY --chown=node:node . .

# Gera o build da aplicação
RUN npm run build

# Exposição da porta
EXPOSE 4200

# Comando para rodar a aplicação
CMD ["serve", "-s", "dist/inventory-frontend/browser", "-l", "4200"]
