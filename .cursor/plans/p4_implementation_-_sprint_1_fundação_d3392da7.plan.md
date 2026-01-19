---
name: P4 Implementation - Sprint 1 Fundação
overview: "Implementar Sprint 1 (Fundação) da P4, criando CRUD completo para Epic 1 (Cursos), Epic 2 (Professores) e Epic 3 (Alunos), seguindo Architecture Doc e Test Design aprovados. Implementar em ordem: Domain → Repository → Service → DTO → Controller → Exceptions → Testes."
todos:
  - id: create-domain-classificacao
    content: Criar Enum Classificacao (APROVADO, EXAME, REPROVADO) com método classificar
    status: pending
  - id: create-domain-curso
    content: Criar entidade JPA Curso com validações e relacionamento OneToMany com Disciplina
    status: pending
  - id: create-domain-professor
    content: Criar entidade JPA Professor com validações e relacionamento OneToMany com Disciplina
    status: pending
  - id: create-domain-aluno
    content: Criar entidade JPA Aluno com validações e relacionamentos OneToMany com Matricula e Nota
    status: pending
  - id: create-exceptions
    content: Criar exceções customizadas (EntidadeNaoEncontradaException, ValidacaoException, IntegridadeReferencialException) e ExceptionHandler
    status: pending
  - id: create-repository-curso
    content: Criar CursoRepository com métodos save, findById, findAll, delete, existsByNome
    status: pending
    dependencies:
      - create-domain-curso
  - id: create-repository-professor
    content: Criar ProfessorRepository com métodos save, findById, findAll, delete, existsByRegistro
    status: pending
    dependencies:
      - create-domain-professor
  - id: create-repository-aluno
    content: Criar AlunoRepository com métodos save, findById, findAll, delete, existsByMatricula
    status: pending
    dependencies:
      - create-domain-aluno
  - id: create-dtos
    content: Criar todos os DTOs (CursoDTO, CursoInputDTO, ProfessorDTO, ProfessorInputDTO, AlunoDTO, AlunoInputDTO, ErroDTO) com validações Jakarta Validation
    status: pending
  - id: create-service-curso
    content: Criar CursoService com lógica de negócio, validações de unicidade e métodos CRUD
    status: pending
    dependencies:
      - create-repository-curso
      - create-dtos
  - id: create-service-professor
    content: Criar ProfessorService com lógica de negócio, validações de unicidade e métodos CRUD
    status: pending
    dependencies:
      - create-repository-professor
      - create-dtos
  - id: create-service-aluno
    content: Criar AlunoService com lógica de negócio, validações de unicidade e métodos CRUD
    status: pending
    dependencies:
      - create-repository-aluno
      - create-dtos
  - id: create-controller-curso
    content: Criar CursoController com endpoints REST documentados (Swagger) e tratamento de erros
    status: pending
    dependencies:
      - create-service-curso
      - create-exceptions
  - id: create-controller-professor
    content: Criar ProfessorController com endpoints REST documentados (Swagger) e tratamento de erros
    status: pending
    dependencies:
      - create-service-professor
      - create-exceptions
  - id: create-controller-aluno
    content: Criar AlunoController com endpoints REST documentados (Swagger) e tratamento de erros
    status: pending
    dependencies:
      - create-service-aluno
      - create-exceptions
  - id: create-tests-unit
    content: Criar testes unitários (TDD) para Services (CursoService, ProfessorService, AlunoService)
    status: pending
    dependencies:
      - create-service-curso
      - create-service-professor
      - create-service-aluno
  - id: create-tests-integration
    content: Criar testes de integração com DB para Repositories (CursoRepository, ProfessorRepository, AlunoRepository)
    status: pending
    dependencies:
      - create-repository-curso
      - create-repository-professor
      - create-repository-aluno
  - id: create-tests-functional
    content: Criar testes funcionais para Controllers (CursoController, ProfessorController, AlunoController)
    status: pending
    dependencies:
      - create-controller-curso
      - create-controller-professor
      - create-controller-aluno
  - id: validate-coverage
    content: Validar cobertura de testes (mínimo 85% Services, 75% Controllers, 85% Repositories) usando JaCoCo
    status: pending
    dependencies:
      - create-tests-unit
      - create-tests-integration
      - create-tests-functional
  - id: update-sprint-status-p4
    content: Atualizar sprint-status.yaml marcando P4_Implementation como IN_PROGRESS e Sprint 1 como em andamento
    status: pending
---

# P4 Implementation - Sprint 1: Fundação

## Objetivo
Implementar os 3 épicos base do sistema (Cursos, Professores, Alunos) seguindo o Architecture Doc e Test Design aprovados, criando todas as camadas necessárias e testes correspondentes.

## Épicos do Sprint 1
- Epic 1: Gestão de Cursos (5 user stories)
- Epic 2: Gestão de Professores (5 user stories)
- Epic 3: Gestão de Alunos (5 user stories)

## Ordem de Implementação

### Fase 1: Domain Layer (Entidades JPA)
1. Enum Classificacao (será usado em Epic 6, mas pode ser criado agora)
2. Entidade Curso
3. Entidade Professor
4. Entidade Aluno

### Fase 2: Exception Layer
1. EntidadeNaoEncontradaException
2. ValidacaoException
3. IntegridadeReferencialException
4. ExceptionHandler (Provider REST)

### Fase 3: Repository Layer
1. CursoRepository
2. ProfessorRepository
3. AlunoRepository

### Fase 4: DTO Layer
1. CursoDTO e CursoInputDTO
2. ProfessorDTO e ProfessorInputDTO
3. AlunoDTO e AlunoInputDTO
4. ErroDTO (para tratamento de erros)

### Fase 5: Service Layer
1. CursoService (com validações de negócio)
2. ProfessorService (com validações de negócio)
3. AlunoService (com validações de negócio)

### Fase 6: Controller Layer
1. CursoController (com documentação Swagger)
2. ProfessorController (com documentação Swagger)
3. AlunoController (com documentação Swagger)

### Fase 7: Testes (seguindo Test Design)
1. Testes unitários (TDD) - Services
2. Testes de integração com DB - Repositories
3. Testes funcionais - Controllers

## Arquivos a Criar

### Domain (3 entidades + 1 enum)
- `src/main/java/com/faculdade/media/domain/Classificacao.java`
- `src/main/java/com/faculdade/media/domain/Curso.java`
- `src/main/java/com/faculdade/media/domain/Professor.java`
- `src/main/java/com/faculdade/media/domain/Aluno.java`

### Exception (4 classes)
- `src/main/java/com/faculdade/media/exception/EntidadeNaoEncontradaException.java`
- `src/main/java/com/faculdade/media/exception/ValidacaoException.java`
- `src/main/java/com/faculdade/media/exception/IntegridadeReferencialException.java`
- `src/main/java/com/faculdade/media/exception/ExceptionHandler.java`

### Repository (3 classes)
- `src/main/java/com/faculdade/media/repository/CursoRepository.java`
- `src/main/java/com/faculdade/media/repository/ProfessorRepository.java`
- `src/main/java/com/faculdade/media/repository/AlunoRepository.java`

### DTO (7 classes)
- `src/main/java/com/faculdade/media/dto/CursoDTO.java`
- `src/main/java/com/faculdade/media/dto/CursoInputDTO.java`
- `src/main/java/com/faculdade/media/dto/ProfessorDTO.java`
- `src/main/java/com/faculdade/media/dto/ProfessorInputDTO.java`
- `src/main/java/com/faculdade/media/dto/AlunoDTO.java`
- `src/main/java/com/faculdade/media/dto/AlunoInputDTO.java`
- `src/main/java/com/faculdade/media/dto/ErroDTO.java`

### Service (3 classes)
- `src/main/java/com/faculdade/media/service/CursoService.java`
- `src/main/java/com/faculdade/media/service/ProfessorService.java`
- `src/main/java/com/faculdade/media/service/AlunoService.java`

### Controller (3 classes)
- `src/main/java/com/faculdade/media/controller/CursoController.java`
- `src/main/java/com/faculdade/media/controller/ProfessorController.java`
- `src/main/java/com/faculdade/media/controller/AlunoController.java`

### Testes (múltiplos arquivos)
- Testes unitários em `src/test/java/com/faculdade/media/unit/`
- Testes de integração em `src/test/java/com/faculdade/media/integration/db/`
- Testes funcionais em `src/test/java/com/faculdade/media/functional/`

## Especificações Técnicas

### Entidades JPA
- Usar annotations JPA conforme Architecture Doc
- Validações Jakarta Validation (@NotNull, @Size, etc.)
- Relacionamentos com FetchType.LAZY
- Constraints de unicidade via @UniqueConstraint

### Repositories
- Usar EntityManager para operações JPA
- Métodos: save, findById, findAll, delete, existsBy*
- Queries JPQL quando necessário

### Services
- Validações de negócio (unicidade, integridade referencial)
- Conversão DTO ↔ Entity
- Tratamento de exceções

### Controllers
- Annotations Jakarta REST (@Path, @GET, @POST, etc.)
- Annotations OpenAPI (@Operation, @APIResponse, @Tag)
- Validação de entrada (@Valid)
- Códigos HTTP corretos (200, 201, 400, 404, 204)

### Testes
- TDD para Services (especialmente validações)
- Testes de integração com SQLite em memória
- Testes funcionais com REST Assured ou Jersey Test
- Cobertura mínima: Services 85%, Controllers 75%, Repositories 85%

## Validações por Entidade

### Curso
- Nome: obrigatório, não vazio, máximo 100 caracteres, único

### Professor
- Nome: obrigatório, não vazio, máximo 100 caracteres
- Registro: obrigatório, não vazio, máximo 20 caracteres, único

### Aluno
- Nome: obrigatório, não vazio, máximo 100 caracteres
- Matrícula: obrigatório, não vazio, máximo 20 caracteres, único

## Critérios de Sucesso

- [ ] Todas as entidades JPA criadas e mapeadas corretamente
- [ ] Todos os repositories implementados
- [ ] Todos os services implementados com validações
- [ ] Todos os DTOs criados
- [ ] Todos os controllers implementados com documentação Swagger
- [ ] Exception handling implementado
- [ ] Testes unitários criados (TDD)
- [ ] Testes de integração criados
- [ ] Testes funcionais criados
- [ ] Cobertura de testes atingida (85% Services, 75% Controllers, 85% Repositories)
- [ ] Documentação Swagger funcionando
- [ ] Endpoints testados manualmente ou via testes funcionais

## Próximos Passos Após Sprint 1

- Sprint 2: Implementar Epic 4 (Disciplinas) com relacionamentos
- Sprint 3: Implementar Epic 5 (Matrículas)
- Sprint 4: Implementar Epic 6 (Notas e Médias) - funcionalidade core