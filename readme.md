# Gasto Comum

## Visão Geral
Gasto Comum é um projeto baseado em **Java** desenvolvido para gerenciar despesas compartilhadas. Ele utiliza o framework **Spring Boot** e integra-se a um banco de dados utilizando **SQL**. O projeto inclui funcionalidades como autenticação de usuários, gerenciamento de despesas, divisão de contas, tratamento de exceções e segurança avançada.

## Funcionalidades
- **Autenticação de Usuários**: Autenticação segura utilizando tokens JWT e OAuth2.
- **Gerenciamento de Despesas**: Controle de despesas compartilhadas entre usuários.
- **Divisão de Contas**: Registro detalhado de como cada despesa foi dividida entre os participantes.
- **Tratamento de Exceções Personalizado**: Tratamento de erros com mensagens significativas.
- **Segurança**: Filtro de segurança para validação de requisições, proteção de endpoints e integração com OAuth2.
- **Cadastro e Consulta de Usuários**: Endpoints para registro, consulta, atualização e remoção de usuários.
- **Cadastro e Consulta de Despesas**: Endpoints para registro, consulta, atualização e remoção de despesas.
- **Listagem de Despesas por Usuário**: Consulta de todas as despesas associadas a um usuário.
- **Refresh Token**: Endpoint para renovação de tokens de autenticação.

## Tecnologias
- **Java**
- **Spring Boot**
- **Maven**
- **SQL**
- **Spring Security**
- **OAuth2**


## Como Executar
1. Clone o repositório:
   ```bash
   git clone <repository-url>
   ```
2. Acesse o diretório:
   ```bash
   cd gasto_comum
   ```
3. Compile com Maven:
   ```bash
   mvn clean install
   ```
4. Execute a aplicação:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints

### Autenticação
- `POST /api/auth/register` — Registrar usuário
- `POST /api/auth/sign-in` — Login
- `POST /api/auth/refresh-token` — Renovar token
- `POST /api/auth/logout` — Logout

### Usuário
- `GET /api/user/me` — Dados do usuário logado
- `GET /api/user` — Listar usuários (com busca)
- `POST /api/user` (multipart/form-data) — Upload foto de perfil
- `GET /api/user/profile-picture/download/{userId}` — Download foto de perfil

### Despesas
- `POST /api/spending` (multipart/form-data) — Criar despesa (com comprovante)
- `GET /api/spending` — Listar despesas (com filtros)
- `PUT /api/spending` (multipart/form-data) — Atualizar despesa
- `GET /api/spending/{id}` — Detalhar despesa
- `DELETE /api/spending/{id}` — Remover despesa
- `GET /api/spending/voucher/download/{fileName}` — Download comprovante

### Grupos
- `POST /api/group` — Criar grupo
- `POST /api/group/add/spending` — Adicionar despesa ao grupo
- `GET /api/group` — Listar grupos
- `GET /api/group/{id}` — Detalhar grupo

### Divisão de Contas
- `POST /api/expenses-divided-accounts` — Registrar divisão de despesa
- `GET /api/expenses-divided-accounts` — Listar divisões (com filtros)
- `PUT /api/expenses-divided-accounts/pay/{id}` — Pagar divisão

### Dashboard
- `GET /api/dashboard` — Dados financeiros do usuário

## Tratamento de Erros
Respostas estruturadas em JSON, por exemplo:
```json
{
  "status": "NOT_FOUND",
  "message": "Recurso não encontrado"
}
```

---

> Atualize conforme novas funcionalidades forem implementadas.