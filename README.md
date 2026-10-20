# authentication-api

API REST para autenticação, gerenciamento de contas e controle de sessões.

## Sobre o projeto

O projeto implementa os principais fluxos de identidade de uma aplicação: cadastro e ativação de contas, autenticação por credenciais ou Google, emissão e renovação de tokens, gerenciamento de sessões e recuperação de senha.

A aplicação é executada como um serviço Spring Boot stateless. O estado necessário para sessões, refresh tokens, ativações e recuperações de senha é persistido no PostgreSQL. O Redis é utilizado pelo rate limiting, e o RabbitMQ desacopla o envio de e-mails do processamento das requisições HTTP.

## Funcionalidades

- Cadastro de contas com papel padrão `ROLE_USER`.
- Ativação de conta por token e OTP enviado por e-mail.
- Login por e-mail e senha.
- Login com credencial Google para contas existentes.
- Emissão de access token JWT, refresh token e token de sessão.
- Renovação de tokens com rotação do refresh token quando o access token expira.
- Logout da sessão atual.
- Listagem, consulta e revogação de sessões.
- Revogação de uma ou todas as sessões da conta.
- Consulta da conta autenticada.
- Alteração de senha e desativação da conta.
- Recuperação de senha por token e OTP.
- Reenvio de e-mails de ativação e recuperação de senha.
- Rate limiting global e por grupos de endpoints.
- Limpeza agendada de sessões, ativações e recuperações expiradas.
- Registro de localização aproximada da sessão a partir do endereço IP.

## Tecnologias

- Java 26.
- Spring Boot 4.1.1.
- Spring MVC, Spring Security, Spring Data JPA e Hibernate.
- PostgreSQL 18.4.
- Flyway para migrations.
- Redis 8.2.1.
- Bucket4j 8.19.0 com Lettuce para rate limiting distribuído.
- RabbitMQ 4.3.4 para mensageria.
- Spring Mail, Thymeleaf e Mailpit para envio e inspeção de e-mails.
- Nimbus JOSE JWT 10.9.1 para access tokens JWT.
- Google API Client 2.8.1 para validação de credenciais Google.
- MapStruct e Lombok.
- Springdoc OpenAPI 3.1.0.
- Maven Wrapper.
- JUnit 5, Mockito e AssertJ nos testes.
- Docker Compose para os serviços auxiliares locais.

## Arquitetura

O código segue uma abordagem de arquitetura hexagonal:

- `domain` contém os modelos, enums e exceções de negócio, sem depender dos adapters.
- `application` contém os use cases, commands, results e ports. Os services implementam os fluxos da aplicação.
- `adapter.in` contém os pontos de entrada, como controllers REST, filtros de segurança, validações, consumers RabbitMQ e schedulers.
- `adapter.out` contém as implementações das ports, incluindo persistência JPA, JWT, criptografia, Google, geolocalização, SMTP, publicação de mensagens e logs.
- `infrastructure` concentra configurações do Spring, propriedades, CORS, OpenAPI, filas, rate limiting e transações.

Os use cases são expostos por interfaces em `application/port/in` e dependem de interfaces em `application/port/out`. As implementações concretas são conectadas pela configuração de infraestrutura.

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/br/com/guisebastiao/authenticationapi/
│   │   ├── domain/
│   │   ├── application/
│   │   │   ├── command/
│   │   │   ├── port/
│   │   │   ├── result/
│   │   │   └── service/
│   │   ├── adapter/
│   │   │   ├── in/
│   │   │   └── out/
│   │   └── infrastructure/
│   │       ├── config/
│   │       ├── properties/
│   │       └── transaction/
│   └── resources/
│       ├── db/migration/
│       ├── templates/
│       ├── application.yaml
│       ├── application-dev.yml
│       └── application-prod.yml
└── test/
    └── java/br/com/guisebastiao/authenticationapi/
```

## Requisitos

- Java 26.
- Docker e Docker Compose.
- Acesso à internet para baixar dependências Maven e, quando utilizado, validar credenciais Google e consultar a geolocalização de IP.

O repositório inclui `mvnw` e `mvnw.cmd`, portanto não é necessário instalar o Maven separadamente.

## Configuração

O profile padrão é `dev`. Nesse profile, o Spring importa automaticamente o arquivo `.env` localizado na raiz do projeto.

Crie o arquivo local a partir do exemplo:

```bash
cp .env.example .env
```

No PowerShell:

```powershell
Copy-Item .env.example .env
```

Preencha o `.env` com os valores do ambiente local. Os hosts devem apontar para os serviços publicados pelo Docker Compose quando a aplicação for executada fora de containers, normalmente usando `localhost` e as respectivas portas publicadas.

### Variáveis de ambiente

| Variável | Descrição |
| --- | --- |
| `DB_URL` | URL JDBC do PostgreSQL. |
| `DB_NAME` | Nome do banco utilizado pelo PostgreSQL. |
| `DB_USER` | Usuário do PostgreSQL. |
| `DB_PASS` | Senha do PostgreSQL. |
| `DB_PORT` | Porta local publicada para o PostgreSQL. |
| `MQ_HOST` | Host do RabbitMQ. |
| `MQ_PORT` | Porta AMQP do RabbitMQ. |
| `MQ_MANAGEMENT_PORT` | Porta da interface de management do RabbitMQ. |
| `MQ_USER` | Usuário do RabbitMQ. |
| `MQ_PASS` | Senha do RabbitMQ. |
| `REDIS_HOST` | Host do Redis. |
| `REDIS_PORT` | Porta local publicada para o Redis. |
| `MAIL_HOST` | Host do servidor SMTP. |
| `MAIL_PORT` | Porta SMTP publicada. Também é usada pelo Mailpit no ambiente local. |
| `MAIL_UI_PORT` | Porta da interface web do Mailpit. |
| `MAIL_USER` | Usuário do servidor SMTP. Essa variável é referenciada por `application.yaml`, embora não esteja declarada no `.env.example`. |
| `MAIL_FROM` | Endereço usado como remetente dos e-mails. |
| `MAIL_PASS` | Senha do servidor SMTP. |
| `GOOGLE_CLIENT_ID` | Client ID usado para validar credenciais Google. |
| `FRONTEND_URL` | Origem permitida pela configuração de CORS. |
| `ACCESS_TOKEN_SECRET` | Chave usada para assinar e validar access tokens JWT. |
| `HMAC_SECRET` | Chave usada para gerar hashes determinísticos de tokens persistidos. |
| `ACCOUNT_ACTIVATION_CLEANUP_RATE` | Intervalo do scheduler de limpeza de ativações expiradas. |
| `RECOVER_PASSWORD_CLEANUP_RATE` | Intervalo do scheduler de limpeza de recuperações expiradas. |
| `SESSION_CLEANUP_RATE` | Intervalo do scheduler de limpeza de sessões expiradas ou revogadas. |
| `RATE_LIMIT_GLOBAL_CAPACITY` | Capacidade do rate limit global. |
| `RATE_LIMIT_GLOBAL_REFILL_TOKENS` | Quantidade de tokens reposta pelo rate limit global. |
| `RATE_LIMIT_GLOBAL_DURATION` | Duração da reposição do rate limit global. |
| `RATE_LIMIT_AUTH_CAPACITY` | Capacidade do rate limit aplicado a `/auth/**`. |
| `RATE_LIMIT_AUTH_REFILL_TOKENS` | Quantidade de tokens reposta para `/auth/**`. |
| `RATE_LIMIT_AUTH_DURATION` | Duração da reposição para `/auth/**`. |
| `RATE_LIMIT_RECOVER_PASSWORD_CAPACITY` | Capacidade do rate limit aplicado a `/password-recovery/**`. |
| `RATE_LIMIT_RECOVER_PASSWORD_REFILL_TOKENS` | Quantidade de tokens reposta para `/password-recovery/**`. |
| `RATE_LIMIT_RECOVER_PASSWORD_DURATION` | Duração da reposição para `/password-recovery/**`. |
| `RATE_LIMIT_ACTIVATION_ACCOUNT_CAPACITY` | Capacidade do rate limit aplicado a `/activation-account/**`. |
| `RATE_LIMIT_ACTIVATION_ACCOUNT_REFILL_TOKENS` | Quantidade de tokens reposta para `/activation-account/**`. |
| `RATE_LIMIT_ACTIVATION_ACCOUNT_DURATION` | Duração da reposição para `/activation-account/**`. |

Os valores das chaves, senhas, tokens e demais credenciais devem ser definidos somente no ambiente local ou no mecanismo de secrets utilizado pelo ambiente de execução.

## Executando o projeto

1. Crie e preencha o arquivo `.env` conforme a seção de configuração.
2. Para desenvolvimento no Windows, inicie a API e os serviços auxiliares:

```powershell
.\dev.bat
```

Esse comando usa `compose.yaml` com `compose.dev.yaml`, inicia o Mailpit e
habilita o modo de desenvolvimento com atualização automática do código.

Em Linux/macOS, use:

```bash
docker compose -f compose.yaml -f compose.dev.yaml up -d --build --watch
```

3. Como alternativa, para executar a aplicação fora do container, inicie
somente os serviços auxiliares:

```bash
docker compose -f compose.yaml -f compose.dev.yaml up -d
```

Depois inicie a aplicação com o profile padrão `dev`:

```bash
./mvnw spring-boot:run
```

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação escuta em `0.0.0.0:8080`.

Para produção no Windows, configure o `.env` com um servidor SMTP acessível
pela rede e execute:

```powershell
.\prod.bat
```

O ambiente de produção não inicia o Mailpit; `MAIL_HOST`, `MAIL_PORT`,
`MAIL_USER` e `MAIL_PASS` devem apontar para o SMTP de produção.

Para interromper os serviços auxiliares:

```bash
docker compose -f compose.yaml -f compose.dev.yaml down
```

## Testes

Os testes são executados pelo Maven e usam principalmente JUnit 5, Mockito e AssertJ. A suíte contém testes unitários para services da aplicação e adapters de segurança, persistência, e-mail e mensageria.

Execute todos os testes com:

```bash
./mvnw test
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

## API

Os controllers estão agrupados nos seguintes recursos:

| Método | Endpoint | Descrição |
| --- | --- | --- |
| POST | `/auth/sign-in` | Autenticação por e-mail e senha. |
| POST | `/auth/google/sign-in` | Autenticação com credencial Google. |
| POST | `/auth/sign-up` | Criação de conta e início da ativação. |
| POST | `/auth/sign-out` | Logout da sessão atual. |
| POST | `/auth/refresh` | Renovação dos tokens de autenticação. |
| GET | `/accounts/me` | Consulta da conta autenticada. |
| PATCH | `/accounts/me/password` | Alteração da senha. |
| DELETE | `/accounts/me` | Desativação da conta. |
| GET | `/sessions` | Listagem das sessões ativas. |
| GET | `/sessions/current` | Consulta da sessão atual. |
| DELETE | `/sessions` | Revogação de sessões selecionadas. |
| DELETE | `/sessions/all` | Revogação de todas as sessões. |
| POST | `/password-recovery` | Início da recuperação de senha. |
| POST | `/password-recovery/verify` | Validação do OTP da recuperação. |
| POST | `/password-recovery/resend` | Reenvio do e-mail de recuperação. |
| PATCH | `/password-recovery/reset` | Redefinição da senha. |
| POST | `/account-activation` | Ativação da conta. |
| POST | `/account-activation/resend` | Reenvio do e-mail de ativação. |

O Swagger/OpenAPI é a referência principal para contratos HTTP, parâmetros, requests, responses, schemas e códigos HTTP.

## Autenticação e segurança

- As rotas protegidas exigem o header `Authorization` com um access token no formato Bearer e o header `X-Session` com o token da sessão.
- O access token é um JWT assinado com HS256 e tem validade de 15 minutos.
- As sessões têm validade inicial de 7 dias e podem ser revogadas individualmente ou em conjunto.
- O refresh token é associado a uma sessão e é rotacionado durante a renovação após a expiração do access token.
- As senhas são protegidas com BCrypt.
- Tokens de sessão, refresh, ativação e recuperação são persistidos na forma de hash.
- A aplicação usa `SessionCreationPolicy.STATELESS` e não mantém sessão HTTP no servidor.
- As rotas públicas incluem autenticação, cadastro, recuperação de senha, ativação de conta e documentação OpenAPI.
- O rate limiting possui uma política global por endereço IP e políticas específicas para autenticação, recuperação de senha e ativação de conta.

A configuração atual declara a política de rate limiting de ativação para `/activation-account/**`, enquanto os controllers de ativação usam `/account-activation`. Os nomes e caminhos acima refletem o estado atual do código.

## Serviços e integrações

### PostgreSQL

Armazena contas, papéis, sessões, refresh tokens, ativações de conta e solicitações de recuperação de senha.

### Redis

Armazena os buckets distribuídos do rate limiting implementado com Bucket4j e Lettuce.

### RabbitMQ

O envio de e-mails é assíncrono. A aplicação publica mensagens no exchange `email.exchange` usando a routing key `email.send`; o consumer processa a fila durável `email.queue`.

### SMTP e Mailpit

O consumer renderiza os templates Thymeleaf e envia as mensagens por SMTP. No ambiente local, o Docker Compose fornece o Mailpit, cuja interface web fica disponível na porta definida por `MAIL_UI_PORT`.

### Google

O login Google valida a credencial recebida usando um `GoogleIdTokenVerifier` configurado com `GOOGLE_CLIENT_ID`.

### Geolocalização de IP

Durante a criação de uma sessão, a aplicação consulta `https://ipwho.is` para obter cidade e país. Se a consulta falhar, a sessão recebe a localização `Localização desconhecida`.

## Banco de dados e migrations

O Flyway é executado automaticamente a partir de `classpath:db/migration`. A migration inicial está em:

```text
src/main/resources/db/migration/V1__initial_schema.sql
```

Ela cria as tabelas de contas, papéis, sessões, refresh tokens, ativações e recuperações de senha, além dos relacionamentos e índices correspondentes. Também cria os papéis iniciais `ROLE_ADMIN` e `ROLE_USER`.

No profile `dev`, o Hibernate não cria o schema (`ddl-auto: none`) e o Flyway permite limpeza. No profile `prod`, o schema é validado pelo Hibernate, o Flyway usa `baseline-on-migrate` e a limpeza fica desabilitada.

## Swagger / OpenAPI

No profile `dev`, com a aplicação em execução:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Especificação OpenAPI: `http://localhost:8080/v3/api-docs`

O Swagger UI utiliza os esquemas `bearerAuth` para o access token JWT e `sessionAuth` para o header `X-Session`.

No profile `prod`, a documentação OpenAPI e o Swagger UI são desabilitados pela configuração do projeto.

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo `LICENSE` para o texto completo.
