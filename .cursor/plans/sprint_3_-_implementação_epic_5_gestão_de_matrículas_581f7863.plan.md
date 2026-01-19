---
name: Sprint 3 - Implementação Epic 5 Gestão de Matrículas
overview: Implementar o Epic 5 (Gestão de Matrículas) seguindo a arquitetura em camadas, criando Repository, DTOs, Service, Controller e todos os testes conforme o Test Design. A entidade Matricula já existe, então começamos pela camada Repository.
todos:
  - id: create-matricula-repository
    content: Criar MatriculaRepository com métodos save, findById, findAll, findByAlunoId, findByDisciplinaId, findByAlunoIdAndDisciplinaId, existsByAlunoIdAndDisciplinaId, delete
    status: pending
  - id: create-matricula-dtos
    content: Criar MatriculaDTO (response) e MatriculaInputDTO (request) com validações Jakarta Validation
    status: pending
  - id: create-matricula-service
    content: Criar MatriculaService com métodos matricular, listarTodos, listarPorAluno, listarPorDisciplina, desmatricular e todas as validações de negócio
    status: pending
    dependencies:
      - create-matricula-repository
      - create-matricula-dtos
  - id: create-matricula-controller
    content: Criar MatriculaController com endpoints POST /api/matriculas, POST /api/disciplinas/{id}/alunos/{id}, GET /api/matriculas (com filtros), DELETE /api/disciplinas/{id}/alunos/{id} e documentação Swagger/OpenAPI completa
    status: pending
    dependencies:
      - create-matricula-service
  - id: create-unit-tests-matricula-service
    content: Criar MatriculaServiceTest com todos os casos de teste unitários (matricular, listar, desmatricular, validações)
    status: pending
    dependencies:
      - create-matricula-service
  - id: create-integration-tests-matricula-repository
    content: Criar MatriculaRepositoryIT com testes de integração DB (persistência, relacionamentos, filtros, constraints)
    status: pending
    dependencies:
      - create-matricula-repository
  - id: create-functional-tests-matricula-controller
    content: Criar MatriculaControllerTest com testes funcionais para todos os endpoints REST e filtros
    status: pending
    dependencies:
      - create-matricula-controller
  - id: update-test-fixtures-matricula
    content: Adicionar método createMatricula() em TestFixtures.java
    status: pending
  - id: verify-persistence-config-matricula
    content: Verificar se Matricula está registrada nos arquivos persistence.xml (main e test)
    status: pending
  - id: validate-coverage-sprint3
    content: Executar mvn test jacoco:report e validar cobertura mínima de 80% para todas as camadas de Matricula
    status: pending
    dependencies:
      - create-unit-tests-matricula-service
      - create-integration-tests-matricula-repository
      - create-functional-tests-matricula-controller
---

# Sprint 3 - Implementação Epic 5: Gestão de Matrículas

## Objetivo

Implementar CRUD completo de Matrículas com relacionamentos com Alunos e Disciplinas, incluindo filtros de busca, validações de integridade referencial e testes completos conforme Test Design.

## Escopo

- **Epic 5**: Gestão de Matrículas
- **User Stories**: US-021 a US-023 (3 stories)
- **Duração Estimada**: 1 semana
- **Dependências**: Epic 3 (Alunos) e Epic 4 (Disciplinas) - COMPLETED

## Estado Atual

- ✅ Entidade `Matricula` já existe em `src/main/java/com/faculdade/media/domain/Matricula.java`
- ❌ `MatriculaRepository` - não existe
- ❌ `MatriculaDTO` e `MatriculaInputDTO` - não existem
- ❌ `MatriculaService` - não existe
- ❌ `MatriculaController` - não existe
- ❌ Testes - não existem

## Arquitetura e Relacionamentos

A entidade `Matricula` possui:

- `@ManyToOne` com `Aluno` (obrigatório)
- `@ManyToOne` com `Disciplina` (obrigatório)
- Constraint única: `(aluno_id, disciplina_id)` - evita matrícula duplicada

## Implementação por Camada

### 1. Repository Layer

**Arquivo**: `src/main/java/com/faculdade/media/repository/MatriculaRepository.java`

**Métodos necessários**:

- `save(Matricula matricula)` - salvar/atualizar
- `findById(Long id)` - buscar por ID
- `findAll()` - listar todas
- `findByAlunoId(Long alunoId)` - filtrar por aluno (US-022)
- `findByDisciplinaId(Long disciplinaId)` - filtrar por disciplina (US-022)
- `findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId)` - buscar matrícula específica (US-021, US-023)
- `existsByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId)` - validar matrícula duplicada (US-021)
- `delete(Matricula matricula)` - remover

**Padrão**: Seguir estrutura de `DisciplinaRepository.java`

### 2. DTO Layer

**Arquivo 1**: `src/main/java/com/faculdade/media/dto/MatriculaDTO.java` (Response)

**Campos**:

- `id` (Long)
- `aluno` (AlunoResumoDTO ou apenas id/nome)
- `disciplina` (DisciplinaResumoDTO ou apenas id/nome)

**Arquivo 2**: `src/main/java/com/faculdade/media/dto/MatriculaInputDTO.java` (Request)

**Campos com validações**:

- `alunoId` (Long, @NotNull)
- `disciplinaId` (Long, @NotNull)

**Padrão**: Seguir estrutura de `DisciplinaDTO.java` e `DisciplinaInputDTO.java`

### 3. Service Layer

**Arquivo**: `src/main/java/com/faculdade/media/service/MatriculaService.java`

**Métodos necessários**:

- `matricular(Long alunoId, Long disciplinaId)` - US-021
- Validar aluno existe (buscar via AlunoRepository)
- Validar disciplina existe (buscar via DisciplinaRepository)
- Validar matrícula não existe (existsByAlunoIdAndDisciplinaId)
- Criar e persistir matrícula
- `listarTodos()` - US-022
- Retornar todas as matrículas
- `listarPorAluno(Long alunoId)` - US-022 (filtro)
- Validar aluno existe
- Retornar matrículas do aluno
- `listarPorDisciplina(Long disciplinaId)` - US-022 (filtro)
- Validar disciplina existe
- Retornar matrículas da disciplina
- `desmatricular(Long alunoId, Long disciplinaId)` - US-023
- Validar matrícula existe (findByAlunoIdAndDisciplinaId)
- Remover matrícula
- **Política para notas**: Por enquanto, apenas remover matrícula. Notas serão tratadas na Sprint 4.

**Validações de Negócio**:

- Aluno deve existir
- Disciplina deve existir
- Matrícula duplicada não permitida
- Matrícula deve existir para desmatricular

**Padrão**: Seguir estrutura de `DisciplinaService.java`

### 4. Controller Layer

**Arquivo**: `src/main/java/com/faculdade/media/controller/MatriculaController.java`

**Endpoints REST**:

- `POST /api/matriculas` - US-021
- Recebe `MatriculaInputDTO`
- Retorna 201 (Created) com `MatriculaDTO`
- Retorna 400 (Bad Request) para validações ou matrícula duplicada
- Retorna 404 (Not Found) se aluno/disciplina não existe
- `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}` - US-021 (alternativa)
- Endpoint aninhado para matricular
- Mesma lógica do POST /api/matriculas
- `GET /api/matriculas` - US-022
- Query params opcionais: `alunoId`, `disciplinaId`
- Retorna 200 (OK) com List<MatriculaDTO>
- Suporta filtros: `?alunoId={id}`, `?disciplinaId={id}`
- `DELETE /api/disciplinas/{disciplinaId}/alunos/{alunoId}` - US-023
- Retorna 204 (No Content) se desmatriculado
- Retorna 404 (Not Found) se matrícula não existe

**Documentação Swagger/OpenAPI**:

- `@Tag(name = "Matrículas")`
- `@Operation` em cada método
- `@APIResponse` para cada status code
- `@RequestBody` para POST

**Padrão**: Seguir estrutura de `DisciplinaController.java`

### 5. Test Layer

#### 5.1 Testes Unitários (Services)

**Arquivo**: `src/test/java/com/faculdade/media/unit/service/MatriculaServiceTest.java`

**Casos de teste (US-021 a US-023)**:

- Matricular aluno com dados válidos
- Lançar exceção quando aluno não existe
- Lançar exceção quando disciplina não existe
- Lançar exceção quando aluno já está matriculado
- Listar todas as matrículas
- Listar matrículas por aluno
- Listar matrículas por disciplina
- Desmatricular aluno com sucesso
- Lançar exceção ao desmatricular quando matrícula não existe

**Framework**: JUnit 5 + Mockito
**Padrão**: Seguir `DisciplinaServiceTest.java`

#### 5.2 Testes de Integração DB (Repositories)

**Arquivo**: `src/test/java/com/faculdade/media/integration/db/repository/MatriculaRepositoryIT.java`

**Casos de teste**:

- Salvar e recuperar matrícula do banco
- Salvar matrícula com relacionamentos (Aluno e Disciplina)
- Listar todas as matrículas
- Buscar matrícula por ID
- Filtrar por aluno (findByAlunoId)
- Filtrar por disciplina (findByDisciplinaId)
- Buscar por aluno e disciplina (findByAlunoIdAndDisciplinaId)
- Verificar existsByAlunoIdAndDisciplinaId
- Falhar ao criar matrícula duplicada (constraint única)
- Falhar ao criar matrícula com aluno inexistente
- Falhar ao criar matrícula com disciplina inexistente
- Remover matrícula

**Framework**: JUnit 5 + SQLite in-memory
**Padrão**: Seguir `DisciplinaRepositoryIT.java`

#### 5.3 Testes Funcionais (Controllers)

**Arquivo**: `src/test/java/com/faculdade/media/functional/MatriculaControllerTest.java`

**Casos de teste**:

- POST /api/matriculas - retornar 201 com matrícula criada
- POST /api/matriculas - retornar 400 quando matrícula duplicada
- POST /api/matriculas - retornar 404 quando aluno/disciplina não existe
- POST /api/disciplinas/{id}/alunos/{id} - retornar 201 (endpoint alternativo)
- GET /api/matriculas - retornar 200 com lista de matrículas
- GET /api/matriculas?alunoId={id} - filtrar por aluno
- GET /api/matriculas?disciplinaId={id} - filtrar por disciplina
- DELETE /api/disciplinas/{id}/alunos/{id} - retornar 204 quando desmatriculado
- DELETE /api/disciplinas/{id}/alunos/{id} - retornar 404 quando matrícula não existe

**Framework**: JUnit 5 (testando via Service, similar a Sprint 1 e 2)
**Padrão**: Seguir `DisciplinaControllerTest.java`

### 6. Atualizações Necessárias

#### 6.1 Persistence.xml

- Verificar se `Matricula` está registrada em `src/main/resources/META-INF/persistence.xml`
- Verificar se `Matricula` está registrada em `src/test/resources/META-INF/persistence.xml`

#### 6.2 TestFixtures

- Adicionar `createMatricula()` em `src/test/java/com/faculdade/media/util/TestFixtures.java`

## Ordem de Implementação

1. **Repository Layer** - Base para persistência
2. **DTO Layer** - Objetos de transferência
3. **Service Layer** - Lógica de negócio
4. **Controller Layer** - API REST
5. **Test Layer** - Testes unitários, integração e funcionais
6. **Validação** - Executar testes e verificar cobertura

## Critérios de Aceitação

- [ ] Todos os endpoints de Matrículas funcionando
- [ ] Vinculação com Alunos funcionando
- [ ] Vinculação com Disciplinas funcionando
- [ ] Filtros de busca implementados (`?alunoId={id}`, `?disciplinaId={id}`)
- [ ] Validação de matrícula duplicada
- [ ] Testes com cobertura mínima de 80%
- [ ] Documentação Swagger atualizada
- [ ] Todos os testes passando (unitários, integração, funcionais)

## Métricas Esperadas

- Cobertura de Services: >= 85%
- Cobertura de Repositories: >= 85%
- Cobertura de Controllers: >= 75%
- Total de testes: ~15-20 testes

## Observações Técnicas

1. **Relacionamentos**: Matricula tem relacionamentos obrigatórios com Aluno e Disciplina. Validar existência antes de criar.

2. **Unicidade**: Constraint única `(aluno_id, disciplina_id)` garante que um aluno não pode estar matriculado duas vezes na mesma disciplina.

3. **Integridade Referencial**: Na desmatrícula, por enquanto apenas remover a matrícula. A política para notas existentes será definida na Sprint 4 (Notas e Médias).

4. **Filtros**: Implementar filtros opcionais no Controller usando query parameters. Service deve ter métodos específicos para cada filtro.

5. **Endpoints Duplicados**: Implementar tanto `POST /api/matriculas` quanto `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}` para flexibilidade, mas ambos devem usar o mesmo service method.

6. **DTOs**: Considerar criar DTOs resumidos para Aluno e Disciplina nas respostas de Matrícula, ou usar apenas ID e nome conforme padrão estabelecido.