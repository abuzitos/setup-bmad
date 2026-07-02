# Tasks: Cadastro de Cursos Profissionalizantes Virtuais

**Input**: Design documents from `/specs/001-cursos-profissionalizantes-virtuais/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: Test-First is NON-NEGOTIABLE (constitution III). Test strategy MUST follow the
**risk matrix** in plan.md (constitution IV). For each user story:

1. Classify scenarios as **Alto / Médio / Baixo** risk
2. List test tasks **before** implementation, **highest risk first**
3. Include only test types required by the risk level (unit / integration / functional)
4. Omit lower-layer tests only with justification in plan.md Complexity Tracking

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions
- **Risco** (Alto/Médio/Baixo): indicado nos testes; fases Setup/Foundational/Polish omitem `[Story]`

## Path Conventions

- **Single project (this repo)**: `src/main/java/com/faculdade/media/` with packages
  `controller/`, `service/`, `repository/`, `domain/`, `dto/`, `config/`, `exception/`
- **Tests (constitution III–IV)**: `src/test/java/com/faculdade/media/`
  - `unit/` — `*Test.java` (TDD, JUnit 5)
  - `integration/mock|nomock|db/` — `*IT.java`
  - `functional/` — `*Test.java` (REST Assured / Jersey Test)
- Paths shown below assume single project — adjust based on plan.md structure

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Registro de persistência, contrato OpenAPI e tags de documentação

- [x] T001 Registrar entidades CPV em `src/main/resources/META-INF/persistence.xml` (`AreaProfissionalizante`, `CursoProfissionalizanteVirtual`)
- [x] T002 [P] Registrar entidades CPV em `src/test/resources/META-INF/persistence.xml` (`AreaProfissionalizante`, `CursoProfissionalizanteVirtual`)
- [x] T003 [P] Sincronizar paths CPV e áreas de `specs/001-cursos-profissionalizantes-virtuais/contracts/cursos-profissionalizantes-virtuais.openapi.yaml` em `src/main/resources/META-INF/openapi.yaml`
- [x] T004 [P] Adicionar tags OpenAPI para CPV e Áreas em `src/main/java/com/faculdade/media/config/OpenAPIConfig.java`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Enums de domínio, catálogo de áreas (seed read-only) e infraestrutura compartilhada — MUST completar antes de qualquer user story

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

### Tests for Foundational (Áreas — write FIRST) ⚠️

> **Risk level**: Médio — ver plan.md (seed e listagem read-only FR-012)

- [ ] T005 [P] [Alto] Teste de integração mock 401 sem credenciais em `src/test/java/com/faculdade/media/integration/mock/controller/AreaProfissionalizanteControllerMockIT.java`
- [ ] T006 [P] [Médio] Teste unitário de invariantes de área em `src/test/java/com/faculdade/media/unit/domain/AreaProfissionalizanteTest.java`
- [ ] T007 [P] [Médio] Teste funcional de listagem de áreas ativas em `src/test/java/com/faculdade/media/functional/AreaProfissionalizanteControllerTest.java`
- [ ] T008 [P] [Médio] Teste de integração nomock seed idempotente (TEC/SAU/GES/SER) em `src/test/java/com/faculdade/media/integration/nomock/seed/AreaProfissionalizanteSeedIT.java`

### Implementation for Foundational

- [ ] T009 Criar enum `ModalidadeEnsino` em `src/main/java/com/faculdade/media/domain/ModalidadeEnsino.java`
- [ ] T010 [P] Criar enum `StatusOferta` em `src/main/java/com/faculdade/media/domain/StatusOferta.java`
- [ ] T011 Criar entidade `AreaProfissionalizante` em `src/main/java/com/faculdade/media/domain/AreaProfissionalizante.java`
- [ ] T012 Criar `AreaProfissionalizanteRepository` em `src/main/java/com/faculdade/media/repository/AreaProfissionalizanteRepository.java` (`findAllAtivas`, `findAtivaById`, `existsByCodigo`)
- [ ] T013 [P] Criar `AreaProfissionalizanteDTO` em `src/main/java/com/faculdade/media/dto/AreaProfissionalizanteDTO.java`
- [ ] T014 Criar seed idempotente `AreaProfissionalizanteSeed` em `src/main/java/com/faculdade/media/service/seed/AreaProfissionalizanteSeed.java` (TEC, SAU, GES, SER)
- [ ] T015 Criar `AreaProfissionalizanteService` em `src/main/java/com/faculdade/media/service/AreaProfissionalizanteService.java`
- [ ] T016 Criar `AreaProfissionalizanteController` em `src/main/java/com/faculdade/media/controller/AreaProfissionalizanteController.java` (GET listagem e GET por id)
- [ ] T017 Registrar `AreaProfissionalizanteService` em `src/main/java/com/faculdade/media/config/JerseyConfig.java` e invocar seed na startup em `src/main/java/com/faculdade/media/Main.java`
- [ ] T018 [P] Adicionar fixtures de área em `src/test/java/com/faculdade/media/util/TestFixtures.java`

**Checkpoint**: Foundation ready — catálogo de áreas disponível; user story implementation can now begin

---

## Phase 3: User Story 1 - Cadastrar curso profissionalizante virtual (Priority: P1) 🎯 MVP

**Goal**: Administrador autenticado cadastra CPV com FKs válidas, modalidade VIRTUAL fixa e unicidade de nome no escopo CPV

**Independent Test**: POST com dados mínimos retorna 201; curso aparece com `tipoCurso=PROFISSIONALIZANTE`, `modalidade=VIRTUAL`, `status=ATIVO`; nome duplicado no CPV retorna 400; nome igual a `Curso` legado é aceito

### Tests for User Story 1 (REQUIRED — risk-driven, write FIRST) ⚠️

> **Red-Green-Refactor**: confirmar testes FAIL antes das tarefas de implementação

- [ ] T019 [P] [US1] [Alto] Teste unitário de regras de cadastro (cargaHoraria, modalidade VIRTUAL, trim nome) em `src/test/java/com/faculdade/media/unit/domain/CursoProfissionalizanteVirtualTest.java`
- [ ] T020 [P] [US1] [Alto] Teste de integração DB para unicidade nome CPV e FKs em `src/test/java/com/faculdade/media/integration/db/repository/CursoProfissionalizanteVirtualRepositoryIT.java`
- [ ] T021 [P] [US1] [Alto] Teste de integração DB nome igual a `Curso` legado permitido (FR-009/IL-07) em `src/test/java/com/faculdade/media/integration/db/repository/CursoProfissionalizanteVirtualRepositoryIT.java`
- [ ] T022 [P] [US1] [Alto] Teste funcional POST cadastro happy path em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T023 [P] [US1] [Alto] Teste funcional nome igual a `Curso` legado permitido em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T024 [P] [US1] [Alto] Teste de integração mock 401 POST cadastro sem credenciais em `src/test/java/com/faculdade/media/integration/mock/controller/CursoProfissionalizanteVirtualControllerMockIT.java`
- [ ] T025 [P] [US1] [Médio] Teste unitário de validações de service (campos obrigatórios, instituicao inexistente, área inativa) em `src/test/java/com/faculdade/media/unit/service/CursoProfissionalizanteVirtualServiceTest.java`
- [ ] T026 [P] [US1] [Médio] Teste de integração mock POST com campos inválidos e mensagens SC-003 (C2 nome duplicado, C9 carga inválida, campos ausentes) em `src/test/java/com/faculdade/media/integration/mock/controller/CursoProfissionalizanteVirtualControllerMockIT.java`
- [ ] T027 [P] [US1] [Médio] Teste unitário nome com acentuação/caracteres especiais permitido e rejeição de nome só espaços em `src/test/java/com/faculdade/media/unit/domain/CursoProfissionalizanteVirtualTest.java`

### Implementation for User Story 1

- [ ] T028 [P] [US1] Criar entidade `CursoProfissionalizanteVirtual` em `src/main/java/com/faculdade/media/domain/CursoProfissionalizanteVirtual.java`
- [ ] T029 [P] [US1] Criar `CursoProfissionalizanteVirtualRepository` em `src/main/java/com/faculdade/media/repository/CursoProfissionalizanteVirtualRepository.java` (`save`, `existsByNome`)
- [ ] T030 [P] [US1] Criar `CursoProfissionalizanteVirtualDTO` e `CursoProfissionalizanteVirtualInputDTO` em `src/main/java/com/faculdade/media/dto/`
- [ ] T031 [US1] Implementar `CursoProfissionalizanteVirtualService.criar()` em `src/main/java/com/faculdade/media/service/CursoProfissionalizanteVirtualService.java` (FKs IL-04/05, unicidade IL-01, modalidade VIRTUAL R5, trim R9)
- [ ] T032 [US1] Implementar POST em `src/main/java/com/faculdade/media/controller/CursoProfissionalizanteVirtualController.java`
- [ ] T033 [US1] Registrar `CursoProfissionalizanteVirtualService` em `src/main/java/com/faculdade/media/config/JerseyConfig.java`
- [ ] T034 [P] [US1] Adicionar fixtures CPV em `src/test/java/com/faculdade/media/util/TestFixtures.java`

**Checkpoint**: User Story 1 fully functional — cadastro CPV testável independentemente

---

## Phase 4: User Story 2 - Consultar cursos profissionalizantes virtuais (Priority: P2)

**Goal**: Administrador autenticado lista somente CPV ativos e consulta por id incluindo desativados

**Independent Test**: Após cadastrar ativo e desativado, GET listagem retorna só ATIVO; GET `/{id}` retorna ambos; GET id inexistente retorna 404; lista vazia retorna 200 com array vazio

### Tests for User Story 2 (REQUIRED — write FIRST) ⚠️

- [ ] T035 [P] [US2] [Alto] Teste unitário/service filtro listagem ATIVO vs findById em `src/test/java/com/faculdade/media/unit/service/CursoProfissionalizanteVirtualServiceTest.java`
- [ ] T036 [P] [US2] [Alto] Teste de integração DB `findAllAtivos` em `src/test/java/com/faculdade/media/integration/db/repository/CursoProfissionalizanteVirtualRepositoryIT.java`
- [ ] T037 [P] [US2] [Alto] Teste funcional GET listagem só ATIVO e GET por id com desativado em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T038 [P] [US2] [Alto] Teste de integração mock 401 GET sem credenciais em `src/test/java/com/faculdade/media/integration/mock/controller/CursoProfissionalizanteVirtualControllerMockIT.java`
- [ ] T039 [P] [US2] [Médio] Teste funcional/mock GET por id inexistente retorna 404 em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T040 [P] [US2] [Baixo] Teste funcional listagem vazia sem erro em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`

### Implementation for User Story 2

- [ ] T041 [US2] Implementar `listarAtivos()` e `buscarPorId()` em `src/main/java/com/faculdade/media/service/CursoProfissionalizanteVirtualService.java`
- [ ] T042 [US2] Implementar GET listagem e GET por id em `src/main/java/com/faculdade/media/controller/CursoProfissionalizanteVirtualController.java`
- [ ] T043 [US2] Garantir mapeamento DTO com `tipoCurso=PROFISSIONALIZANTE` e campos derivados em `src/main/java/com/faculdade/media/dto/CursoProfissionalizanteVirtualDTO.java`

**Checkpoint**: User Stories 1 AND 2 independently testable

---

## Phase 5: User Story 3 - Atualizar e desativar curso profissionalizante virtual (Priority: P3)

**Goal**: Administrador edita campos permitidos, desativa/reativa CPV sem exclusão física; modalidade permanece imutável

**Independent Test**: PUT altera descrição/carga/área; PATCH desativar remove da listagem; GET por id mantém histórico; PATCH reativar restaura listagem; tentativa de alterar modalidade retorna 400

### Tests for User Story 3 (REQUIRED — write FIRST) ⚠️

- [ ] T044 [P] [US3] [Alto] Teste funcional lifecycle desativar/reativar e ausência na listagem (SC-005) em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T045 [P] [US3] [Alto] Teste de integração nomock lifecycle em `src/test/java/com/faculdade/media/integration/nomock/controller/CursoProfissionalizanteVirtualControllerIT.java`
- [ ] T046 [P] [US3] [Médio] Teste funcional PUT atualização campos editáveis em `src/test/java/com/faculdade/media/functional/CursoProfissionalizanteVirtualControllerTest.java`
- [ ] T047 [P] [US3] [Médio] Teste unitário rejeição alteração modalidade (FR-008) em `src/test/java/com/faculdade/media/unit/domain/CursoProfissionalizanteVirtualTest.java`

### Implementation for User Story 3

- [ ] T048 [P] [US3] Criar `CursoProfissionalizanteVirtualAtualizacaoInputDTO` em `src/main/java/com/faculdade/media/dto/CursoProfissionalizanteVirtualAtualizacaoInputDTO.java`
- [ ] T049 [US3] Implementar `atualizar()`, `desativar()` e `reativar()` em `src/main/java/com/faculdade/media/service/CursoProfissionalizanteVirtualService.java`
- [ ] T050 [US3] Implementar PUT, PATCH `/desativar` e PATCH `/reativar` em `src/main/java/com/faculdade/media/controller/CursoProfissionalizanteVirtualController.java`

**Checkpoint**: All user stories independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Validação final, documentação e qualidade transversal

- [ ] T051 Executar suite completa `mvn verify` e corrigir falhas CPV
- [ ] T052 Validar cenários manuais C1–C9 e SC-001 (< 3 min cadastro) de `specs/001-cursos-profissionalizantes-virtuais/quickstart.md`
- [ ] T053 [P] Atualizar documentação de testes CPV em `src/test/java/com/faculdade/media/README.md`
- [ ] T054 [P] Revisar anotações MicroProfile OpenAPI nos controllers CPV e áreas para paridade com `openapi.yaml`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion — BLOCKS all user stories
  - **T005–T008 (testes) MUST completar antes de T009–T018 (implementação)**
- **User Stories (Phase 3–5)**: All depend on Foundational phase completion
  - Recommended sequential order: US1 → US2 → US3 (MVP incremental)
  - US2/US3 may start after US1 entity+service exist for shared fixtures
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) — No dependencies on other stories
- **User Story 2 (P2)**: Depends on US1 cadastro (needs CPV records to list/consult) — independently testable with fixtures
- **User Story 3 (P3)**: Depends on US1 cadastro — independently testable with fixtures; reuses US2 GET for verification

### Within Each User Story

- Risk classification from plan.md before writing tests
- Tests (required by risk level) MUST be written and FAIL before implementation
- High-risk scenarios MUST be tested first
- Models before services
- Services before endpoints
- Story complete before moving to next priority

### Parallel Opportunities

- T002, T003, T004 (Setup) — arquivos distintos
- T005–T008 (testes foundational) — arquivos distintos; **antes** de T009–T018
- T010 (enum StatusOferta) — paralelo a T009
- T019–T027 (testes US1) — arquivos distintos; executar antes de T028–T034
- T028, T029, T030 (entidade, repo, DTOs US1) — arquivos distintos
- T035–T040 (testes US2) — paralelizáveis entre si
- T044–T047 (testes US3) — paralelizáveis entre si
- T053, T054 (Polish) — paralelizáveis

---

## Parallel Example: User Story 1

```bash
# 1. Escrever todos os testes US1 em paralelo (devem FALHAR):
# T019 — unit/domain/CursoProfissionalizanteVirtualTest.java
# T020, T021 — integration/db/repository/CursoProfissionalizanteVirtualRepositoryIT.java
# T022, T023 — functional/CursoProfissionalizanteVirtualControllerTest.java
# T024 — integration/mock/controller/CursoProfissionalizanteVirtualControllerMockIT.java (401 POST)
# T025 — unit/service/CursoProfissionalizanteVirtualServiceTest.java
# T026 — integration/mock (SC-003 proxy: C2, C9, campos ausentes)
# T027 — unit/domain (caracteres especiais + trim)

# 2. Implementar modelos em paralelo:
# T028 — domain/CursoProfissionalizanteVirtual.java
# T029 — repository/CursoProfissionalizanteVirtualRepository.java
# T030 — dto/CursoProfissionalizanteVirtualDTO.java + InputDTO

# 3. Sequencial: service (T031) → controller (T032) → JerseyConfig (T033)
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL — blocks all stories; testes T005–T008 primeiro)
3. Complete Phase 3: User Story 1 (cadastro CPV + áreas seed)
4. **STOP and VALIDATE**: `mvn test -Punit-tests -Dtest="*CursoProfissionalizante*,*AreaProfissionalizante*"`
5. Demo cadastro via curl (quickstart C1)

### Incremental Delivery

1. Setup + Foundational → catálogo de áreas operacional
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Polish → `mvn verify` + quickstart manual (incl. SC-001)

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together (tests T005–T008 before impl)
2. Once Foundational is done:
   - Developer A: User Story 1 (tests + impl)
   - Developer B: prepara fixtures/cenários US2 aguardando US1 entity
   - Developer C: prepara cenários lifecycle US3
3. After US1 merge: B implements US2; C implements US3 in parallel

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing (Red-Green-Refactor)
- Referência de código: `InstituicaoController`/`Service` (CRUD), `DisciplinaService` (validação FK)
- Sem DELETE físico em CPV — usar PATCH desativar (diferente de `Instituicao`)
- `nome` imutável após cadastro (R4); modalidade não é campo de input (R5)
- FR-003 e FR-009 são complementares: unicidade no escopo CPV + distinção via `tipoCurso` (sem bloqueio global de nome)
