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
