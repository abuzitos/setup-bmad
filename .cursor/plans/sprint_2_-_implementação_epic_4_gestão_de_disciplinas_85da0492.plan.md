---
name: Sprint 2 - Implementação Epic 4 Gestão de Disciplinas
overview: Implementar o Epic 4 (Gestão de Disciplinas) seguindo a arquitetura em camadas, criando Repository, DTOs, Service, Controller e todos os testes conforme o Test Design. A entidade Disciplina já existe, então começamos pela camada Repository.
todos:
  - id: create-disciplina-repository
    content: Criar DisciplinaRepository com métodos save, findById, findAll, findByCursoId, findByProfessorId, findByCursoIdAndProfessorId, existsByNomeAndCursoId, existsByNomeAndCursoIdExcluindoId, delete
    status: pending
  - id: create-disciplina-dtos
    content: Criar DisciplinaDTO (response) e DisciplinaInputDTO (request) com validações Jakarta Validation
    status: pending
  - id: create-disciplina-service
    content: Criar DisciplinaService com métodos criar, listarTodos, listarPorCurso, listarPorProfessor, listarPorCursoEPProfessor, buscarPorId, atualizar, remover e todas as validações de negócio
    status: pending
    dependencies:
      - create-disciplina-repository
      - create-disciplina-dtos
  - id: create-disciplina-controller
    content: Criar DisciplinaController com endpoints POST, GET (com filtros), GET/{id}, PUT/{id}, DELETE/{id} e documentação Swagger/OpenAPI completa
    status: pending
    dependencies:
      - create-disciplina-service
  - id: create-unit-tests-disciplina-service
    content: Criar DisciplinaServiceTest com todos os casos de teste unitários (criar, buscar, listar, atualizar, remover, validações)
    status: pending
    dependencies:
      - create-disciplina-service
  - id: create-integration-tests-disciplina-repository
    content: Criar DisciplinaRepositoryIT com testes de integração DB (persistência, relacionamentos, filtros, constraints)
    status: pending
    dependencies:
      - create-disciplina-repository
  - id: create-functional-tests-disciplina-controller
    content: Criar DisciplinaControllerTest com testes funcionais para todos os endpoints REST e filtros
    status: pending
    dependencies:
      - create-disciplina-controller
  - id: update-test-fixtures
    content: Adicionar método createDisciplina() em TestFixtures.java
    status: pending
  - id: verify-persistence-config
    content: Verificar se Disciplina está registrada nos arquivos persistence.xml (main e test)
    status: pending
  - id: validate-coverage-sprint2
    content: Executar mvn test jacoco:report e validar cobertura mínima de 80% para todas as camadas de Disciplina
    status: pending
    dependencies:
      - create-unit-tests-disciplina-service
      - create-integration-tests-disciplina-repository
      - create-functional-tests-disciplina-controller
---

# Sprint 2 - Implementação Epic 4: Gestão de Disciplinas

## Objetivo
Implementar CRUD completo de Disciplinas com relacionamentos com Cursos e Professores, incluindo filtros de busca, validações de integridade referencial e testes completos conforme Test Design.

## Escopo
- **Epic 4**: Gestão de Disciplinas
- **User Stories**: US-006 a US-010 (5 stories)
- **Duração Estimada**: 1-2 semanas
- **Dependências**: Epic 1 (Cursos) e Epic 2 (Professores) - COMPLETED

## Estado Atual
- ✅ Entidade `Disciplina` já existe em `src/main/java/com/faculdade/media/domain/Disciplina.java`
- ✅ `DisciplinaResumoDTO` já existe
- ❌ `DisciplinaRepository` - não existe
- ❌ `DisciplinaDTO` e `DisciplinaInputDTO` - não existem
- ❌ `DisciplinaService` - não existe
- ❌ `DisciplinaController` - não existe
- ❌ Testes - não existem

## Arquitetura e Relacionamentos

A entidade `Disciplina` possui:
- `@ManyToOne` com `Curso` (obrigatório)
- `@ManyToOne` com `Professor` (obrigatório)
- `@OneToMany` com `Matricula` (futuro - Sprint 3)
- `@OneToMany` com `Nota` (futuro - Sprint 4)
- Constraint única: `(nome, curso_id)` - nome único dentro do mesmo curso

## Implementação por Camada

### 1. Repository Layer

**Arquivo**: `src/main/java/com/faculdade/media/repository/DisciplinaRepository.java`

**Métodos necessários**:
- `save(Disciplina disciplina)` - salvar/atualizar
- `findById(Long id)` - buscar por ID
- `findAll()` - listar todas
- `findByCursoId(Long cursoId)` - filtrar por curso (US-007)
- `findByProfessorId(Long professorId)` - filtrar por professor (US-007)
- `findByCursoIdAndProfessorId(Long cursoId, Long professorId)` - filtrar por ambos (US-007)
- `existsByNomeAndCursoId(String nome, Long cursoId)` - validar unicidade (US-006, US-009)
- `existsByNomeAndCursoIdExcluindoId(String nome, Long cursoId, Long idExcluir)` - validar unicidade na atualização
- `delete(Disciplina disciplina)` - remover

**Padrão**: Seguir estrutura de `CursoRepository.java`, `ProfessorRepository.java`, `AlunoRepository.java`

### 2. DTO Layer

**Arquivo 1**: `src/main/java/com/faculdade/media/dto/DisciplinaDTO.java` (Response)

**Campos**:
- `id` (Long)
- `nome` (String)
- `curso` (CursoResumoDTO ou apenas id/nome)
- `professor` (ProfessorResumoDTO ou apenas id/nome)
- `matriculas` (List<MatriculaResumoDTO> - opcional, para US-008)

**Arquivo 2**: `src/main/java/com/faculdade/media/dto/DisciplinaInputDTO.java` (Request)

**Campos com validações**:
- `nome` (String, @NotBlank, @Size(max=100))
- `cursoId` (Long, @NotNull)
- `professorId` (Long, @NotNull)

**Padrão**: Seguir estrutura de `CursoDTO.java` e `CursoInputDTO.java`

### 3. Service Layer

**Arquivo**: `src/main/java/com/faculdade/media/service/DisciplinaService.java`

**Métodos necessários**:
- `criar(DisciplinaInputDTO inputDTO)` - US-006
  - Validar inputDTO não nulo
  - Validar nome não vazio
  - Validar curso existe (buscar via CursoRepository)
  - Validar professor existe (buscar via ProfessorRepository)
  - Validar nome único no curso (existsByNomeAndCursoId)
  - Criar e persistir disciplina
- `listarTodos()` - US-007
  - Retornar todas as disciplinas
- `listarPorCurso(Long cursoId)` - US-007 (filtro)
  - Validar curso existe
  - Retornar disciplinas do curso
- `listarPorProfessor(Long professorId)` - US-007 (filtro)
  - Validar professor existe
  - Retornar disciplinas do professor
- `listarPorCursoEPProfessor(Long cursoId, Long professorId)` - US-007 (filtro combinado)
- `buscarPorId(Long id)` - US-008
  - Validar disciplina existe
  - Retornar com relacionamentos carregados
- `atualizar(Long id, DisciplinaInputDTO inputDTO)` - US-009
  - Validar disciplina existe
  - Validar curso existe (se informado)
  - Validar professor existe (se informado)
  - Validar nome único no curso (excluindo próprio ID)
  - Atualizar e persistir
- `remover(Long id)` - US-010
  - Validar disciplina existe
  - Validar não possui matrículas (getMatriculas().isEmpty())
  - Validar não possui notas (getNotas().isEmpty())
  - Remover disciplina

**Validações de Negócio**:
- Nome obrigatório e não vazio
- Curso deve existir
- Professor deve existir
- Nome único dentro do mesmo curso
- Não pode excluir se tiver matrículas ou notas

**Padrão**: Seguir estrutura de `CursoService.java`, `ProfessorService.java`, `AlunoService.java`

### 4. Controller Layer

**Arquivo**: `src/main/java/com/faculdade/media/controller/DisciplinaController.java`

**Endpoints REST**:
- `POST /api/disciplinas` - US-006
  - Recebe `DisciplinaInputDTO`
  - Retorna 201 (Created) com `DisciplinaDTO`
  - Retorna 400 (Bad Request) para validações
  - Retorna 404 (Not Found) se curso/professor não existe
- `GET /api/disciplinas` - US-007
  - Query params opcionais: `cursoId`, `professorId`
  - Retorna 200 (OK) com List<DisciplinaDTO>
  - Suporta filtros: `?cursoId={id}`, `?professorId={id}`, `?cursoId={id}&professorId={id}`
- `GET /api/disciplinas/{id}` - US-008
  - Retorna 200 (OK) com `DisciplinaDTO` completo
  - Retorna 404 (Not Found) se não existe
- `PUT /api/disciplinas/{id}` - US-009
  - Recebe `DisciplinaInputDTO`
  - Retorna 200 (OK) com `DisciplinaDTO` atualizado
  - Retorna 400 (Bad Request) para validações
  - Retorna 404 (Not Found) se disciplina/curso/professor não existe
- `DELETE /api/disciplinas/{id}` - US-010
  - Retorna 204 (No Content) se excluído
  - Retorna 400 (Bad Request) se possui dependências
  - Retorna 404 (Not Found) se não existe

**Documentação Swagger/OpenAPI**:
- `@Tag(name = "Disciplinas")`
- `@Operation` em cada método
- `@APIResponse` para cada status code
- `@RequestBody` para POST/PUT

**Padrão**: Seguir estrutura de `CursoController.java`, `ProfessorController.java`, `AlunoController.java`

### 5. Test Layer

#### 5.1 Testes Unitários (Services)

**Arquivo**: `src/test/java/com/faculdade/media/unit/service/DisciplinaServiceTest.java`

**Casos de teste (US-006 a US-010)**:
- Criar disciplina com dados válidos
- Lançar exceção quando inputDTO é nulo
- Lançar exceção quando nome é nulo/vazio
- Lançar exceção quando curso não existe
- Lançar exceção quando professor não existe
- Lançar exceção quando nome já existe no curso
- Buscar disciplina por ID com sucesso
- Lançar exceção quando disciplina não encontrada
- Listar todas as disciplinas
- Listar disciplinas por curso
- Listar disciplinas por professor
- Atualizar disciplina com sucesso
- Lançar exceção ao atualizar com nome duplicado
- Remover disciplina com sucesso
- Lançar exceção ao remover com matrículas vinculadas
- Lançar exceção ao remover com notas vinculadas

**Framework**: JUnit 5 + Mockito
**Padrão**: Seguir `CursoServiceTest.java`, `ProfessorServiceTest.java`, `AlunoServiceTest.java`

#### 5.2 Testes de Integração DB (Repositories)

**Arquivo**: `src/test/java/com/faculdade/media/integration/db/repository/DisciplinaRepositoryIT.java`

**Casos de teste**:
- Salvar e recuperar disciplina do banco
- Salvar disciplina com relacionamentos (Curso e Professor)
- Listar todas as disciplinas
- Buscar disciplina por ID
- Filtrar por curso (findByCursoId)
- Filtrar por professor (findByProfessorId)
- Filtrar por curso e professor (findByCursoIdAndProfessorId)
- Verificar existsByNomeAndCursoId
- Verificar existsByNomeAndCursoIdExcluindoId
- Falhar ao criar disciplina com nome duplicado no mesmo curso
- Falhar ao criar disciplina com curso inexistente
- Falhar ao criar disciplina com professor inexistente
- Atualizar disciplina mantendo relacionamentos
- Remover disciplina

**Framework**: JUnit 5 + SQLite in-memory
**Padrão**: Seguir `CursoRepositoryIT.java`, `ProfessorRepositoryIT.java`, `AlunoRepositoryIT.java`

#### 5.3 Testes Funcionais (Controllers)

**Arquivo**: `src/test/java/com/faculdade/media/functional/DisciplinaControllerTest.java`

**Casos de teste**:
- POST /api/disciplinas - retornar 201 com disciplina criada
- POST /api/disciplinas - retornar 400 quando dados inválidos
- POST /api/disciplinas - retornar 404 quando curso/professor não existe
- GET /api/disciplinas - retornar 200 com lista de disciplinas
- GET /api/disciplinas?cursoId={id} - filtrar por curso
- GET /api/disciplinas?professorId={id} - filtrar por professor
- GET /api/disciplinas?cursoId={id}&professorId={id} - filtrar por ambos
- GET /api/disciplinas/{id} - retornar 200 com disciplina encontrada
- GET /api/disciplinas/{id} - retornar 404 quando não existe
- PUT /api/disciplinas/{id} - retornar 200 com disciplina atualizada
- PUT /api/disciplinas/{id} - retornar 404 quando não existe
- PUT /api/disciplinas/{id} - retornar 400 quando dados inválidos
- DELETE /api/disciplinas/{id} - retornar 204 quando excluído
- DELETE /api/disciplinas/{id} - retornar 404 quando não existe
- DELETE /api/disciplinas/{id} - retornar 400 quando possui dependências

**Framework**: JUnit 5 (testando via Service, similar a Sprint 1)
**Padrão**: Seguir `CursoControllerTest.java`, `ProfessorControllerTest.java`, `AlunoControllerTest.java`

### 6. Atualizações Necessárias

#### 6.1 Persistence.xml
- Verificar se `Disciplina` está registrada em `src/main/resources/META-INF/persistence.xml`
- Verificar se `Disciplina` está registrada em `src/test/resources/META-INF/persistence.xml`

#### 6.2 TestFixtures
- Adicionar `createDisciplina()` em `src/test/java/com/faculdade/media/util/TestFixtures.java`

## Ordem de Implementação

1. **Repository Layer** - Base para persistência
2. **DTO Layer** - Objetos de transferência
3. **Service Layer** - Lógica de negócio
4. **Controller Layer** - API REST
5. **Test Layer** - Testes unitários, integração e funcionais
6. **Validação** - Executar testes e verificar cobertura

## Critérios de Aceitação

- [ ] Todos os endpoints de Disciplinas funcionando
- [ ] Vinculação com Cursos funcionando
- [ ] Vinculação com Professores funcionando
- [ ] Filtros de busca implementados (`?cursoId={id}`, `?professorId={id}`)
- [ ] Validações de integridade referencial
- [ ] Testes com cobertura mínima de 80%
- [ ] Documentação Swagger atualizada
- [ ] Todos os testes passando (unitários, integração, funcionais)

## Métricas Esperadas

- Cobertura de Services: >= 85%
- Cobertura de Repositories: >= 85%
- Cobertura de Controllers: >= 75%
- Total de testes: ~20-25 testes

## Observações Técnicas

1. **Relacionamentos**: Disciplina tem relacionamentos obrigatórios com Curso e Professor. Validar existência antes de criar/atualizar.

2. **Unicidade**: Nome deve ser único dentro do mesmo curso, não globalmente. Usar constraint `(nome, curso_id)`.

3. **Integridade Referencial**: Na exclusão, validar que não há matrículas ou notas vinculadas. As entidades Matricula e Nota ainda não existem (Sprint 3 e 4), mas a validação deve estar preparada.

4. **Filtros**: Implementar filtros opcionais no Controller usando query parameters. Service deve ter métodos específicos para cada filtro.

5. **DTOs**: Considerar criar DTOs resumidos para Curso e Professor nas respostas de Disciplina, ou usar apenas ID e nome conforme padrão estabelecido.