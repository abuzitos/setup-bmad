---
name: Implementar Testes Conforme Test Design - Sprint 1
overview: Criar e atualizar todos os testes para Epic 1 (Cursos), Epic 2 (Professores) e Epic 3 (Alunos) conforme especificado no Test Design, incluindo testes unitários completos, testes de integração DB expandidos e testes funcionais para os controllers.
todos:
  - id: update-unit-curso-service
    content: Atualizar CursoServiceTest com todos os casos de teste do Test Design (US-001 a US-005)
    status: pending
  - id: create-unit-professor-service
    content: Criar ProfessorServiceTest completo com todos os casos de teste (US-016 a US-020)
    status: pending
  - id: create-unit-aluno-service
    content: Criar AlunoServiceTest completo com todos os casos de teste (US-011 a US-015)
    status: pending
  - id: expand-integration-curso-repo
    content: Expandir CursoRepositoryIT com casos faltantes (constraint única, relacionamentos, integridade referencial)
    status: pending
  - id: expand-integration-professor-repo
    content: Expandir ProfessorRepositoryIT com casos faltantes (CRUD completo, integridade referencial)
    status: pending
  - id: expand-integration-aluno-repo
    content: Expandir AlunoRepositoryIT com casos faltantes (CRUD completo, integridade referencial)
    status: pending
  - id: create-functional-curso-controller
    content: Criar CursoControllerTest com todos os endpoints REST (GET, POST, PUT, DELETE) e validações HTTP
    status: pending
  - id: create-functional-professor-controller
    content: Criar ProfessorControllerTest com todos os endpoints REST e validações HTTP
    status: pending
  - id: create-functional-aluno-controller
    content: Criar AlunoControllerTest com todos os endpoints REST e validações HTTP
    status: pending
  - id: create-test-helpers
    content: Criar BaseTestIT e TestFixtures para reutilização de código de teste
    status: pending
  - id: validate-coverage
    content: Executar mvn test jacoco:report e validar cobertura (85% Services, 75% Controllers, 85% Repositories)
    status: pending
    dependencies:
      - update-unit-curso-service
      - create-unit-professor-service
      - create-unit-aluno-service
      - expand-integration-curso-repo
      - expand-integration-professor-repo
      - expand-integration-aluno-repo
      - create-functional-curso-controller
      - create-functional-professor-controller
      - create-functional-aluno-controller
---

# Implementar Testes Conforme Test Design - Sprint 1

## Objetivo
Criar e atualizar todos os testes para os 3 épicos do Sprint 1 (Cursos, Professores, Alunos) conforme especificado no Test Design, garantindo cobertura mínima de 85% para Services, 75% para Controllers e 85% para Repositories.

## Escopo
- **Epic 1**: Gestão de Cursos (5 user stories - US-001 a US-005)
- **Epic 2**: Gestão de Professores (5 user stories - US-016 a US-020)
- **Epic 3**: Gestão de Alunos (5 user stories - US-011 a US-015)

## Estado Atual
- ✅ Testes de integração DB básicos criados (CursoRepositoryIT, ProfessorRepositoryIT, AlunoRepositoryIT)
- ⚠️ Testes unitários parciais (apenas CursoServiceTest com validações básicas)
- ❌ Testes funcionais não criados
- ❌ Testes de integração com mock não criados

## Estrutura de Testes a Criar/Atualizar

### 1. Testes Unitários (TDD) - Services

#### 1.1 CursoServiceTest (Atualizar)
**Arquivo**: `src/test/java/com/faculdade/media/unit/service/CursoServiceTest.java`

**Casos de teste a adicionar** (conforme US-001 a US-005):
- ✅ Criar curso com nome válido (com EntityManager mockado)
- ✅ Gerar ID automaticamente
- ✅ Lançar exceção quando nome é nulo/vazio
- ✅ Lançar exceção quando nome excede 100 caracteres
- ✅ Lançar exceção quando nome já existe
- ✅ Buscar curso por ID com sucesso
- ✅ Lançar exceção quando curso não encontrado
- ✅ Listar todos os cursos
- ✅ Atualizar curso com sucesso
- ✅ Lançar exceção ao atualizar com nome duplicado
- ✅ Remover curso com sucesso
- ✅ Lançar exceção ao remover curso com disciplinas vinculadas

**Abordagem**: Usar Mockito para mockar EntityManager e CursoRepository, testando lógica de negócio isoladamente.

#### 1.2 ProfessorServiceTest (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/unit/service/ProfessorServiceTest.java`

**Casos de teste** (conforme US-016 a US-020):
- Criar professor com dados válidos
- Validações de nome e registro
- Validação de unicidade de registro
- Operações CRUD completas
- Validação de integridade referencial

#### 1.3 AlunoServiceTest (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/unit/service/AlunoServiceTest.java`

**Casos de teste** (conforme US-011 a US-015):
- Criar aluno com dados válidos
- Validações de nome e matrícula
- Validação de unicidade de matrícula
- Operações CRUD completas
- Validação de integridade referencial (matrículas e notas)

### 2. Testes de Integração DB - Repositories (Expandir)

#### 2.1 CursoRepositoryIT (Expandir)
**Arquivo**: `src/test/java/com/faculdade/media/integration/db/repository/CursoRepositoryIT.java`

**Casos adicionais**:
- ✅ Falhar ao criar curso com nome duplicado (constraint única)
- ✅ Buscar curso com disciplinas vinculadas (lazy loading)
- ✅ Atualizar curso mantendo relacionamentos
- ✅ Falhar ao excluir curso com disciplinas (integridade referencial)

#### 2.2 ProfessorRepositoryIT (Expandir)
**Arquivo**: `src/test/java/com/faculdade/media/integration/db/repository/ProfessorRepositoryIT.java`

**Casos adicionais**:
- Listar todos os professores
- Falhar ao criar professor com registro duplicado
- Atualizar professor
- Remover professor
- Falhar ao remover professor com disciplinas vinculadas

#### 2.3 AlunoRepositoryIT (Expandir)
**Arquivo**: `src/test/java/com/faculdade/media/integration/db/repository/AlunoRepositoryIT.java`

**Casos adicionais**:
- Listar todos os alunos
- Falhar ao criar aluno com matrícula duplicada
- Atualizar aluno
- Remover aluno
- Falhar ao remover aluno com matrículas/notas vinculadas

### 3. Testes Funcionais - Controllers

#### 3.1 CursoControllerTest (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/functional/CursoControllerTest.java`

**Framework**: Jersey Test Framework ou REST Assured

**Casos de teste** (conforme US-001 a US-005):
- POST /api/cursos - retornar 201 com curso criado
- POST /api/cursos - retornar 400 quando nome inválido
- POST /api/cursos - retornar 400 quando nome duplicado
- GET /api/cursos - retornar 200 com lista de cursos
- GET /api/cursos - retornar lista vazia quando não há cursos
- GET /api/cursos/{id} - retornar 200 com curso encontrado
- GET /api/cursos/{id} - retornar 404 quando curso não existe
- PUT /api/cursos/{id} - retornar 200 com curso atualizado
- PUT /api/cursos/{id} - retornar 404 quando curso não existe
- PUT /api/cursos/{id} - retornar 400 quando dados inválidos
- DELETE /api/cursos/{id} - retornar 204 quando excluído
- DELETE /api/cursos/{id} - retornar 404 quando curso não existe
- DELETE /api/cursos/{id} - retornar 400 quando curso possui dependências

#### 3.2 ProfessorControllerTest (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/functional/ProfessorControllerTest.java`

**Estrutura similar a CursoControllerTest**, adaptada para professores.

#### 3.3 AlunoControllerTest (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/functional/AlunoControllerTest.java`

**Estrutura similar a CursoControllerTest**, adaptada para alunos.

### 4. Helpers e Utilitários

#### 4.1 BaseTestIT (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/integration/db/BaseTestIT.java`

Classe base para testes de integração com setup/teardown comum:
- EntityManagerFactory e EntityManager
- Transações
- Limpeza de dados

#### 4.2 TestFixtures (Criar)
**Arquivo**: `src/test/java/com/faculdade/media/util/TestFixtures.java`

Métodos estáticos para criar entidades de teste:
- `createCurso()`, `createProfessor()`, `createAluno()`
- Builders para facilitar criação de dados de teste

## Estratégia de Implementação

### Fase 1: Testes Unitários (Services)
1. Atualizar `CursoServiceTest` com todos os casos do Test Design
2. Criar `ProfessorServiceTest` completo
3. Criar `AlunoServiceTest` completo

**Desafio**: Services usam EntityManager diretamente, não injeção de dependência. Solução: Mockar EntityManager e criar repositories dentro dos métodos de teste.

### Fase 2: Testes de Integração DB (Repositories)
1. Expandir `CursoRepositoryIT` com casos faltantes
2. Expandir `ProfessorRepositoryIT` com casos faltantes
3. Expandir `AlunoRepositoryIT` com casos faltantes

### Fase 3: Testes Funcionais (Controllers)
1. Criar `CursoControllerTest` usando Jersey Test Framework
2. Criar `ProfessorControllerTest`
3. Criar `AlunoControllerTest`

**Nota**: Testes funcionais podem requerer configuração adicional de CDI/Jersey para injeção de dependências funcionar nos testes.

### Fase 4: Validação de Cobertura
1. Executar `mvn clean test jacoco:report`
2. Verificar cobertura por camada
3. Adicionar testes adicionais se necessário para atingir metas

## Arquivos a Criar/Modificar

### Criar:
- `src/test/java/com/faculdade/media/unit/service/ProfessorServiceTest.java`
- `src/test/java/com/faculdade/media/unit/service/AlunoServiceTest.java`
- `src/test/java/com/faculdade/media/functional/CursoControllerTest.java`
- `src/test/java/com/faculdade/media/functional/ProfessorControllerTest.java`
- `src/test/java/com/faculdade/media/functional/AlunoControllerTest.java`
- `src/test/java/com/faculdade/media/integration/db/BaseTestIT.java`
- `src/test/java/com/faculdade/media/util/TestFixtures.java`

### Atualizar:
- `src/test/java/com/faculdade/media/unit/service/CursoServiceTest.java`
- `src/test/java/com/faculdade/media/integration/db/repository/CursoRepositoryIT.java`
- `src/test/java/com/faculdade/media/integration/db/repository/ProfessorRepositoryIT.java`
- `src/test/java/com/faculdade/media/integration/db/repository/AlunoRepositoryIT.java`

## Critérios de Sucesso

- [ ] Todos os casos de teste do Test Design para Epic 1, 2 e 3 implementados
- [ ] Cobertura de Services >= 85%
- [ ] Cobertura de Controllers >= 75%
- [ ] Cobertura de Repositories >= 85%
- [ ] Todos os testes passando
- [ ] Relatório JaCoCo gerado e validado

## Observações Técnicas

1. **Mocking de EntityManager**: Como os Services criam repositories internamente, será necessário mockar EntityManager e simular comportamento do repository.

2. **Testes Funcionais**: Jersey Test Framework requer configuração adequada de CDI. Pode ser necessário criar um Application de teste ou usar REST Assured com servidor embutido.

3. **Transações em Testes DB**: Usar `@BeforeEach` para iniciar transação e `@AfterEach` para rollback, garantindo isolamento entre testes.

4. **Assertions**: Usar AssertJ para assertions fluentes conforme padrão do projeto.