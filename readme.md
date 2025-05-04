# Gasto Comum

## Visão Geral
Gasto Comum é um projeto baseado em **Java** desenvolvido para gerenciar despesas compartilhadas. Ele utiliza o framework **Spring Boot** e integra-se a um banco de dados utilizando **SQL**. O projeto inclui funcionalidades como autenticação de usuários, gerenciamento de despesas e tratamento de exceções.

## Funcionalidades
- **Autenticação de Usuários**: Autenticação segura utilizando tokens.
- **Gerenciamento de Despesas**: Controle de despesas compartilhadas entre usuários.
- **Tratamento de Exceções Personalizado**: Tratamento de erros com mensagens significativas.
- **Segurança**: Implementação de filtro de segurança para validação de requisições e proteção de endpoints.

## Tecnologias
- **Java**
- **Spring Boot**
- **Maven**
- **SQL**
- **Spring Security**

## Estrutura do Projeto
- `src/main/java/com/br/gasto_comum/infra`: Contém classes relacionadas à infraestrutura, como manipuladores de exceções e filtros de segurança.
- `src/main/java/com/br/gasto_comum/infra/security`: Contém configurações e filtros relacionados à segurança.
- `src/main/java/com/br/gasto_comum/exceptions`: Classes de exceções personalizadas para tratamento de erros específicos.
- `src/main/java/com/br/gasto_comum/users`: Classes e repositórios relacionados a usuários.
- `src/main/java/com/br/gasto_comum/expenses`: Classes e repositórios relacionados a despesas.

## Entidades
### User
Representa um usuário no sistema. Cada usuário possui:
- **id**: Identificador único.
- **name**: Nome completo do usuário.
- **email**: Endereço de e-mail (utilizado para login).
- **password**: Senha criptografada para autenticação.

### Spending
Representa uma despesa compartilhada. Cada despesa possui:
- **id**: Identificador único.
- **description**: Descrição breve da despesa.
- **amount**: Valor total da despesa.
- **date**: Data de criação da despesa.
- **participants**: Lista de usuários que compartilham a despesa.

### ExpensesDividedAccounts
Representa o detalhamento de como as despesas foram divididas entre os participantes. Cada registro possui:
- **id**: Identificador único.
- **user**: Usuário associado à divisão da despesa.
- **spending**: Despesa relacionada.
- **amountOwed**: Valor que o usuário deve ou pagou.

## Componentes Principais
### Tratamento de Exceções
As exceções personalizadas são tratadas na classe `RestExceptionHandler`, que mapeia exceções para códigos de status HTTP e mensagens de erro apropriadas.

### Segurança
A classe `SecurityFilter` garante que apenas usuários autenticados possam acessar endpoints protegidos. Ela valida tokens e configura o contexto de segurança para a requisição.

## Como Executar
1. Clone o repositório:
   ```bash
   git clone <repository-url>
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd gasto_comum
   ```
3. Compile o projeto utilizando o Maven:
   ```bash
   mvn clean install
   ```
4. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints
### Endpoints Públicos
- Endpoints de autenticação e registro (não requerem autenticação).

### Endpoints Protegidos
- Endpoints de gerenciamento de despesas e usuários (requerem autenticação).

## Configuração
- Atualize as configurações de conexão com o banco de dados no arquivo `application.properties` para corresponder ao seu ambiente.
- Defina os endpoints públicos em `SecurityConfigurations.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED`.

## Tratamento de Erros
A aplicação retorna mensagens de erro estruturadas no formato JSON para exceções, como:
```json
{
  "status": "NOT_FOUND",
  "message": "Recurso não encontrado"
}
```