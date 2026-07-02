# Implementation Plan: Cadastro de Cursos Profissionalizantes Virtuais

**Branch**: `001-cursos-profissionalizantes-virtuais` | **Date**: 2026-06-26 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-cursos-profissionalizantes-virtuais/spec.md`

## Summary

Implementar API REST para cadastro, consulta, atualização e desativação de **cursos
profissionalizantes virtuais** (CPV), com catálogo de **áreas profissionalizantes**
pré-populado (seed, somente leitura). Estratégia física **A**: tabela dedicada
`curso_profissionalizante_virtual`, isolada do `Curso` legado. Stack existente:
Java 21, Jersey, JPA/Hibernate, SQLite, Basic Auth (admin v1).

## Technical Context

**Language/Version**: Java 21  
**Primary Dependencies**: Jersey (Jakarta REST 3.x), Grizzly, Hibernate 6, Jakarta Validation, MicroProfile OpenAPI, Jackson  
**Storage**: SQLite via JPA (`faculdade.db` prod; `:memory:` testes)  
**Testing**: JUnit 5, Mockito, AssertJ, REST Assured; profiles Maven (`unit-tests`, `integration-tests-mock`, `integration-tests-no-mock`, `functional-tests`, `integration-tests-db`)  
**Target Platform**: JAR executável Linux (`java -jar`)  
**Project Type**: API REST monolítica em camadas (`com.faculdade.media`)  
**Performance Goals**: Operações CRUD síncronas; sem meta de throughput (catálogo institucional pequeno)  
**Constraints**: Basic Auth obrigatório; sem LMS/matrículas; modalidade fixa VIRTUAL  
**Scale/Scope**: 2 novas entidades, 2 controllers, ~15 endpoints/operações; seed 4 áreas

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

Reference: `.specify/memory/constitution.md` (cursor-projeto-dojo2 v1.2.0)

| Gate | Principle | Pass Criteria | Status |
|------|-----------|---------------|--------|
| G1 | I. Arquitetura em Camadas | Controller → Service → Repository → Domain; DTOs na fronteira HTTP | ✅ PASS |
| G2 | II. API REST Contratada | `/api`, JSON, OpenAPI annotations + openapi.yaml sync | ✅ PASS (contrato em `contracts/`; sync YAML na implementação) |
| G3 | III. Test-First | Testes antes de implementação; Red-Green-Refactor; alto risco primeiro | ✅ PASS |
| G4 | IV. Testes Orientados a Risco | Matriz de riscos abaixo | ✅ PASS |
| G5 | V. Cobertura Proporcional | Metas por nível de risco na matriz | ✅ PASS |
| G6 | VI. Simplicidade | Sem novos frameworks; seed idempotente simples | ✅ PASS |
| G7 | Restrições Tecnológicas | Stack Java 21 + Jersey + Hibernate + SQLite | ✅ PASS |
| G8 | Fluxo de Desenvolvimento | MVP incremental por user story | ✅ PASS |

### Risk Assessment (required)

| Story / Scenario | Risk Level | Rationale | Unit | Integration | Functional |
|------------------|------------|-----------|------|-------------|------------|
| US1 — cadastro CPV com FKs e unicidade nome | **Alto** | Integridade de dados, regras IL-01/04/05, contrato POST | MUST | MUST | MUST |
| US1 — nome igual a `Curso` legado permitido | **Alto** | Distinção FR-009/IL-07; falha silenciosa se unificar catálogos | MUST | MUST | MUST |
| US1 — campos obrigatórios / carga inválida | **Médio** | Validação visível 400 | MUST | SHOULD | SHOULD |
| US2 — listagem só ATIVO vs GET por id | **Alto** | Filtro FR-005; regressão afeta catálogo operacional | MUST | MUST | MUST |
| US2 — GET por id inexistente (404) | **Médio** | Edge case spec; contrato HTTP | — | SHOULD (mock IT) | SHOULD |
| US2 — lista vazia sem erro | **Baixo** | Comportamento trivial | SHOULD | MAY omitir | MAY omitir |
| US3 — atualização campos editáveis | **Médio** | CRUD com validação de área ativa | MUST | SHOULD | SHOULD |
| US3 — desativar / reativar lifecycle | **Alto** | Soft delete novo no projeto; SC-005 | MUST | MUST | MUST |
| US3 — rejeitar alteração modalidade | **Médio** | FR-008, invariante de domínio | MUST | SHOULD | — |
| Auth — 401 sem credenciais (GET e POST CPV) | **Alto** | Segurança FR-011 | — | MUST (mock IT) | — |
| Áreas — seed idempotente | **Médio** | Pré-requisito de cadastro FR-012 | MUST | SHOULD (nomock/db IT) | SHOULD |
| Áreas — listagem read-only | **Médio** | Pré-requisito de cadastro FR-012 | MUST | SHOULD (mock IT) | SHOULD |
| SC-003 — mensagens específicas de validação | **Médio** | Proxy: C2, C9, campos ausentes, instituicao inexistente | MUST | SHOULD | SHOULD |
| DTO mapping / getters domain | **Baixo** | Boilerplate | SHOULD | — | — |

**Pre-Phase 0**: All gates PASS — sem exceções.  
**Post-Phase 1**: G1, G2, G3, G4 re-verificados após `data-model.md` e `contracts/` — PASS.

## Project Structure

### Documentation (this feature)

```text
specs/001-cursos-profissionalizantes-virtuais/
├── plan.md              # Este arquivo
├── research.md          # Decisões de design (Phase 0)
├── data-model.md        # Modelo físico JPA (Phase 1)
├── quickstart.md        # Validação E2E (Phase 1)
├── contracts/
│   └── cursos-profissionalizantes-virtuais.openapi.yaml
├── checklists/
│   ├── requirements.md
│   └── api.md
└── tasks.md             # Phase 2 (/speckit-tasks — gerado)
```

### Source Code (repository root)

```text
src/main/java/com/faculdade/media/
├── domain/
│   ├── AreaProfissionalizante.java
│   ├── CursoProfissionalizanteVirtual.java
│   ├── ModalidadeEnsino.java
│   └── StatusOferta.java
├── repository/
│   ├── AreaProfissionalizanteRepository.java
│   └── CursoProfissionalizanteVirtualRepository.java
├── service/
│   ├── AreaProfissionalizanteService.java
│   ├── CursoProfissionalizanteVirtualService.java
│   └── seed/
│       └── AreaProfissionalizanteSeed.java
├── controller/
│   ├── AreaProfissionalizanteController.java
│   └── CursoProfissionalizanteVirtualController.java
├── dto/
│   ├── AreaProfissionalizanteDTO.java
│   ├── CursoProfissionalizanteVirtualDTO.java
│   ├── CursoProfissionalizanteVirtualInputDTO.java
│   └── CursoProfissionalizanteVirtualAtualizacaoInputDTO.java
└── config/
    ├── JerseyConfig.java          # bind novos services
    └── OpenAPIConfig.java         # novas tags

src/test/java/com/faculdade/media/
├── unit/domain/
│   ├── CursoProfissionalizanteVirtualTest.java
│   └── AreaProfissionalizanteTest.java
├── unit/service/
│   └── CursoProfissionalizanteVirtualServiceTest.java
├── integration/mock/controller/
│   ├── AreaProfissionalizanteControllerMockIT.java
│   └── CursoProfissionalizanteVirtualControllerMockIT.java
├── integration/nomock/controller/
│   └── CursoProfissionalizanteVirtualControllerIT.java
├── integration/nomock/seed/
│   └── AreaProfissionalizanteSeedIT.java
├── integration/db/repository/
│   └── CursoProfissionalizanteVirtualRepositoryIT.java
└── functional/
    ├── AreaProfissionalizanteControllerTest.java
    └── CursoProfissionalizanteVirtualControllerTest.java
```

**Structure Decision**: Monólito Java existente em `src/main/java/com/faculdade/media`,
seguindo o padrão de `Instituicao` e `Disciplina` (FKs). Sem novos módulos Maven.

## Complexity Tracking

> Nenhuma violação da constituição — seção vazia.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| — | — | — |

## Design Artifacts (Phase 0–1)

| Artefato | Caminho | Conteúdo |
|----------|---------|----------|
| Research | [research.md](./research.md) | Estratégia A, seed, auth, imutabilidade, erros |
| Data model | [data-model.md](./data-model.md) | Tabelas, enums, IL-01–07, queries |
| API contract | [contracts/cursos-profissionalizantes-virtuais.openapi.yaml](./contracts/cursos-profissionalizantes-virtuais.openapi.yaml) | Paths, schemas, respostas HTTP |
| Quickstart | [quickstart.md](./quickstart.md) | Cenários curl de validação |

## Implementation Notes (for `/speckit-tasks`)

1. **Ordem MVP**: US1 (cadastro + áreas seed) → US2 (listagem/consulta) → US3 (edição/desativar/reativar).
2. **Test-first**: Para cada cenário **Alto**, escrever teste falhando antes do código de produção.
3. **Registros obrigatórios**: `persistence.xml` (main + test), `JerseyConfig` binds, `OpenAPIConfig` tags, `openapi.yaml` sync.
4. **Sem DELETE físico** em CPV — usar PATCH desativar (diferente de `Instituicao`).
5. **Referência de código**: `InstituicaoController`/`Service` (CRUD), `DisciplinaService` (validação FK).

## Next Step

Executar `/speckit-implement` seguindo `tasks.md` (testes antes de implementação, por risco).
