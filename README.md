# StockFlow - Backend

API REST para controle de estoque de produtos desenvolvida com Spring Boot.

## O que foi desenvolvido

- **Arquitetura em Camadas**: Organização do projeto seguindo a estrutura padrão de controladores, serviços de negócio, repositórios e entidades JPA.
- **Banco de Dados MySQL**: Configuração de banco de dados rodando em ambiente isolado via container para persistência dos dados de produtos.
- **Ambiente de Testes Isolado**: Utilização de banco de dados em memória para execução de testes automatizados sem interferir nos dados reais.
- **CRUD de Produtos**: Endpoints completos para cadastrar, listar, atualizar e deletar produtos do estoque.
- **Validações de Regras de Negócio**: Impedimento de inserção de dados inconsistentes como quantidades negativas, nomes vazios ou valores unitários inválidos.
- **Respostas Padronizadas**: Todas as saídas da API retornam em um envelope padrão indicando o status de sucesso, mensagem e dados de retorno.
- **Tratamento de Exceções**: Centralização do retorno de erros com os códigos HTTP adequados.
- **Suporte a CORS**: Liberação das rotas da API para consumo por aplicações frontend.

## Configuração do Banco de Dados MySQL

O projeto utiliza o MySQL como banco de dados de produção. A seguir estão os comandos SQL para configuração manual do banco (caso não esteja usando Docker Compose):

```sql
-- 1. Criação do Banco de Dados se não existir
CREATE DATABASE IF NOT EXISTS stockflow_db;

-- 2. Criação do Utilizador do Sistema com uma senha segura
CREATE USER IF NOT EXISTS 'nexasystems'@'localhost' IDENTIFIED BY 'senha_segura_senai';

-- 3. Concessão de permissões totais para o utilizador gerir este banco de dados
GRANT ALL PRIVILEGES ON stockflow_db.* TO 'nexasystems'@'localhost';
FLUSH PRIVILEGES;

-- 4. Seleção do banco de dados para a criação da tabela
USE stockflow_db;

-- 5. Criação da tabela do CRUD de produtos
CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL
);

-- 6. Carga Inicial de Dados (Massa de teste automatizado)
INSERT INTO produtos (nome, quantidade, preco_unitario) VALUES 
('Componente Eletrónico X', 150, 12.50),
('Placa de Circuito Impresso Y', 45, 89.90),
('Cabo de Fibra Óptica 10m', 200, 35.00);
```

### Observações:
1. **Autenticação**: Substitua `senha_segura_senai` por uma senha mais segura em ambientes de produção
2. **Docker Compose**: Se estiver usando o Docker Compose, a configuração do banco é automatizada via `docker-compose.yml`
3. **Dados Iniciais**: A carga inicial de dados é apenas para fins de demonstração e testes automatizados
4. **Segurança**: Em ambientes de produção, recomenda-se configurar regras de acesso mais restritivas

## Como Executar

1. **Com Docker Compose**:
   ```bash
   docker-compose up -d
   ```
   O comando acima levanta o container MySQL com todas as configurações necessárias

2. **Sem Docker**:
    - Execute os comandos SQL acima no seu cliente MySQL
    - Configure as credenciais no arquivo `application.properties` do projeto Spring Boot
