# Research: Cadastro de Cursos Profissionalizantes Virtuais

**Feature**: `001-cursos-profissionalizantes-virtuais`  
**Date**: 2026-06-26

## R1 — Estratégia de modelo físico (A vs B)

**Decision**: Estratégia **A** — tabela dedicada `curso_profissionalizante_virtual` separada de `cursos`.

**Rationale**: A spec recomenda isolamento do legado acadêmico (matrículas, disciplinas fora de escopo). O projeto já segue entidades independentes por agregado (`Instituicao`, `Curso`, `Disciplina`). Tabela dedicada evita migração em `cursos` e preserva UK de nome apenas no escopo CPV (FR-003, IL-01).

**Alternatives considered**:
- **B — Generalização de `Curso`**: catálogo unificado, mas exige migração, novos atributos em entidade legada e risco de regressão em `Disciplina`/`Matricula`. Rejeitada para v1.

---

## R2 — Seed do catálogo de áreas profissionalizantes

**Decision**: Inicializador idempotente na startup (`AreaProfissionalizanteSeed`) + helper espelhado em `TestFixtures` para testes.

**Rationale**: O projeto não usa Flyway/Liquibase nem `import.sql`. O padrão existente é SQL manual (`meuSql.sql`). Um seed idempotente (INSERT somente se `codigo` ausente) garante FR-012 sem intervenção manual e reproduz o catálogo em SQLite `:memory:` nos testes.

**Conteúdo mínimo do seed** (áreas ativas):

| codigo | nome |
|--------|------|
| TEC | Tecnologia |
| SAU | Saúde |
| GES | Gestão |
| SER | Serviços |

**Alternatives considered**:
- SQL manual apenas: frágil em dev/testes automatizados.
- CRUD de áreas na v1: fora de escopo (FR-012).

---

## R3 — Autenticação e autorização v1

**Decision**: Reutilizar `AuthenticationFilter` (Basic Auth) existente; credencial única `admin`/`123456` satisfaz FR-011.

**Rationale**: Não há `@RolesAllowed` nem perfis no projeto. A spec assume Basic Auth e admin-only na v1. Endpoints CPV ficam protegidos pelo filtro global (mesmo padrão de `InstituicaoController`).

**Alternatives considered**:
- Novo filtro de role: YAGNI para v1 com credencial única.
- Endpoints públicos de leitura: viola FR-011.

---

## R4 — Imutabilidade de `nome` após cadastro

**Decision**: `nome` é **imutável** após criação; `CursoProfissionalizanteVirtualAtualizacaoInputDTO` não inclui `nome`.

**Rationale**: FR-006 lista apenas `descricao`, `area_profissionalizante_id` e `carga_horaria` como editáveis. Unicidade (IL-01) simplifica-se sem rename. Alinha com checklist CHK015.

**Alternatives considered**:
- Permitir edição de nome com revalidação de unicidade: possível, mas não exigido pela spec v1.

---

## R5 — Confirmação de modalidade virtual no cadastro

**Decision**: Modalidade **não** é campo de input; invariante `VIRTUAL` aplicada no service na criação. Input não aceita `modalidade`.

**Rationale**: FR-002 exige "confirmação de modalidade virtual" como regra de negócio, não como campo explícito. Como só existe um valor permitido, fixar no domínio evita payload redundante e simplifica validação (FR-008).

**Alternatives considered**:
- Campo booleano `modalidadeVirtual: true` obrigatório: redundante e sujeito a erro de cliente.

---

## R6 — Limite superior de `carga_horaria`

**Decision**: Intervalo **[1, 9999]** horas como regra de negócio (Bean Validation `@Min(1)` `@Max(9999)` + validação no service).

**Rationale**: Modelo lógico da spec define limite superior "sugerido"; adotar como regra fechada remove ambiguidade (CHK011) e previne valores absurdos.

**Alternatives considered**:
- Apenas `> 0`: insuficiente para edge cases de overflow de exibição.

---

## R7 — Reativação de curso desativado

**Decision**: Expor `PATCH /cursos-profissionalizantes-virtuais/{id}/reativar` além de `.../desativar`.

**Rationale**: IL-06 e diagrama DDD preveem transição `DESATIVADO → ATIVO`. User Story 3 não cobre explicitamente, mas edge case da spec e método `reativar()` no agregado exigem endpoint ou operação equivalente.

**Alternatives considered**:
- Reativação via `PUT` com `status`: mistura lifecycle com edição de conteúdo; PATCH dedicado é mais claro.

---

## R8 — Formato de contrato HTTP e erros

**Decision**: Seguir padrão existente — `ErroDTO` (`codigo`, `mensagem`, `timestamp`); códigos HTTP via `ExceptionHandler`.

| Situação | HTTP | codigo sugerido |
|----------|------|-----------------|
| Validação de entrada | 400 | `VALIDACAO` |
| Não autenticado | 401 | `NAO_AUTENTICADO` |
| Entidade não encontrada | 404 | `NAO_ENCONTRADO` |
| Nome duplicado CPV | 400 | `NOME_DUPLICADO` |
| Modalidade imutável | 400 | `MODALIDADE_IMUTAVEL` |
| Área inativa | 400 | `AREA_INATIVA` |

**Rationale**: Consistência com `InstituicaoService` e `ExceptionHandler` existentes; atende FR-010 e SC-003.

---

## R9 — Trim e normalização de `nome`

**Decision**: Aplicar `trim()` no service antes de persistir e validar; rejeitar nome vazio após trim. Caracteres especiais e acentuação UTF-8 são **permitidos** (ex.: hífen, aspas, `%`).

**Rationale**: Edge case da spec (espaços em branco). Bean Validation `@NotBlank` não cobre strings só com espaços após trim implícito — normalização explícita no service. Restringir caracteres especiais não agrega valor na v1.

---

## R10 — Sincronização OpenAPI

**Decision**: Fonte primária = anotações MicroProfile nos controllers/DTOs; atualizar `openapi.yaml` com paths CPV e áreas para alinhar à constituição (Princípio II).

**Rationale**: `openapi.yaml` está desatualizado (só `/cursos`). Anotações geram `/api/openapi` em runtime, mas a constituição exige sincronização do YAML estático.

**Alternatives considered**:
- Apenas anotações: viola gate G2 parcialmente.
