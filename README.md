# Sistema de Controle de EPI's — Backend

API REST para controle de estoque de Equipamentos de Proteção Individual (EPIs), com um fluxo completo de reposição de estoque: sugestão automática de itens abaixo do mínimo, geração de lista de compras exportável em PDF, e atualização do estoque a partir do que foi efetivamente recebido.

## O problema que resolve

Controle de estoque de EPI costuma ser feito em planilha, sem nenhum fluxo de trabalho: alguém precisa lembrar de olhar o estoque, decidir manualmente o que comprar, e depois atualizar à mão o que chegou. Este sistema automatiza esse ciclo — do alerta de estoque baixo até a atualização do estoque após a compra — mantendo histórico de cada lista de compras gerada, com prazo de validade e status.

## Funcionalidades

- CRUD completo de EPIs, com busca por nome ignorando acento e caixa
- Edição parcial (só os campos alterados são atualizados no banco)
- Validação de entrada (Bean Validation) e tratamento de erro centralizado (404 / 400 / 409)
- Autenticação HTTP Basic (Spring Security)
- **Lista de Compras:**
  - Sugestão automática dos EPIs com estoque abaixo do mínimo
  - Adição manual de qualquer EPI à lista (para aproveitar frete/orçamento)
  - Exportação da lista em PDF
  - Expiração automática de listas não aplicadas, via tarefa agendada diária
  - Aplicação da lista: atualiza o estoque a partir da quantidade realmente recebida por item (editável, pode divergir do que foi orçado)

## Stack

- Java 21
- Spring Boot 4.1.0 (Web, Data JPA, Security, Validation)
- PostgreSQL 16
- OpenPDF (geração de PDF)
- Maven

## Como rodar localmente

Pré-requisitos: Java 21, Maven, PostgreSQL rodando localmente.

1. Crie um banco chamado `sistema_controle_epis` no PostgreSQL.
2. Confira as credenciais em `src/main/resources/application.properties` — são credenciais de ambiente de desenvolvimento, mantidas propositalmente para fins de demonstração (ver comentário no próprio arquivo). Ajuste se o seu banco local usar outro usuário/senha.
3. Rode:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API sobe em `http://localhost:8080`.

Usuário de demonstração: `Admin` / `senha123` (credencial de portfólio, não representa produção — ver `SecurityConfig.java`).

## Principais endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/epis` | Lista todos os EPIs |
| GET | `/api/epis/buscar?nome=` | Busca por nome, ignorando acento e caixa |
| POST | `/api/epis` | Cria um EPI |
| PUT | `/api/epis/{id}` | Atualização parcial |
| DELETE | `/api/epis/{id}` | Remove |
| GET | `/api/listas-compra/sugestao` | Sugestão de itens abaixo do estoque mínimo |
| POST | `/api/listas-compra` | Cria uma lista de compras |
| GET | `/api/listas-compra` | Lista todas as listas de compras |
| GET | `/api/listas-compra/{id}` | Detalhe de uma lista |
| GET | `/api/listas-compra/{id}/pdf` | Exporta a lista em PDF |
| POST | `/api/listas-compra/{id}/aplicar` | Aplica a lista, atualizando o estoque |

## Frontend

O frontend Angular desse sistema está em [repositório separado](https://github.com/dev-gustavo-costa/controle-epis-frontend).

## Próximos passos conhecidos

- Testes automatizados (cobertura ainda inicial)
- Múltiplos usuários reais vindos do banco (hoje um único usuário fixo em memória)
- JWT no lugar de HTTP Basic
- Cálculo automático de consumo médio, para sugerir estoque mínimo automaticamente
- Ficha de EPI por funcionário (entrega, assinatura, validade do CA) e organização por cargo/setor

## Licença

Projeto pessoal de portfólio. Sem licença de código aberto — todos os direitos reservados.
