---
name: P3 Solutioning - Architecture Doc e Test Design
overview: Criar Architecture Doc (Software Architect) e Test Design (TEA) na fase P3_Solutioning, definindo arquitetura completa do sistema, entidades JPA, padrões de design, estratégia de testes e casos de teste detalhados para todos os 6 épicos.
todos:
  - id: create-architecture-doc
    content: Criar Architecture Doc completo com arquitetura, entidades JPA, relacionamentos, padrões de design e configurações
    status: pending
  - id: create-test-design
    content: Criar Test Design completo com estratégia de testes, casos de teste por épico/user story e plano de cobertura
    status: pending
  - id: update-sprint-status-p3
    content: Atualizar sprint-status.yaml marcando P3_Solutioning como IN_PROGRESS
    status: pending
---

# P3 Solutioning - Architecture Doc e Test Design

## Objetivo

Criar os documentos técnicos fundamentais para a implementação: Architecture Doc (definindo arquitetura, entidades, padrões) e Test Design (definindo estratégia de testes e casos de teste detalhados).

## Documentos a Criar

### 1. Architecture Doc (Software Architect)

#### 1.1 Visão Geral da Arquitetura

- Arquitetura em camadas (Controller, Service, Repository, Domain, DTO, Exception)
- Diagrama de camadas e fluxo de dados
- Princípios arquiteturais (SOLID, separação de responsabilidades)

#### 1.2 Estrutura de Pacotes

```
com.faculdade.media/
├── controller/     # Endpoints REST (Jakarta REST)
├── service/        # Lógica de negócio
├── repository/     # Acesso a dados (JPA)
├── domain/         # Entidades JPA
├── dto/            # Objetos de transferência
├── exception/      # Exceções customizadas
└── config/         # Configurações (JPA, OpenAPI, etc.)
```

#### 1.3 Entidades JPA e Relacionamentos

- **Curso**: id, nome (único)
- **Professor**: id, nome, registro (único)
- **Aluno**: id, nome, matricula (única)
- **Disciplina**: id, nome, curso (ManyToOne), professor (ManyToOne)
- **Matricula**: id, aluno (ManyToOne), disciplina (ManyToOne), constraint única (aluno, disciplina)
- **Nota**: id, aluno (ManyToOne), disciplina (ManyToOne), nota1, nota2, media (calculado), classificacao (calculado), constraint única (aluno, disciplina)

#### 1.4 Relacionamentos JPA

- Curso 1:N Disciplina
- Professor 1:N Disciplina
- Aluno N:M Disciplina (via Matricula)
- Aluno N:M Disciplina (via Nota)
- Disciplina N:1 Curso
- Disciplina N:1 Professor

#### 1.5 Padrões de Design

- Repository Pattern para acesso a dados
- Service Layer para lógica de negócio
- DTO Pattern para transferência de dados
- Exception Handling customizado
- Builder Pattern (opcional para DTOs complexos)

#### 1.6 Configuração JPA

- persistence.xml com unidade de persistência
- Configuração Hibernate para SQLite
- Estratégia de geração de schema (create/update)
- Configuração de transações

#### 1.7 Documentação Swagger/OpenAPI

- Estrutura de annotations por camada
- Exemplos de controllers documentados
- Schemas de DTOs
- Tags e agrupamento de endpoints

#### 1.8 Regras de Negócio

- Cálculo de média: (nota1 + nota2) / 2
- Classificação: Aprovado (>=7), Exame (>=5 e <7), Reprovado (<5)
- Validações de integridade referencial
- Validações de unicidade

### 2. Test Design (Test Engineering Agent)

#### 2.1 Estratégia de Testes por Épico

**Epic 1-3 (Cursos, Professores, Alunos):**

- Testes unitários (Services) - TDD
- Testes de integração com DB (Repositories)
- Testes funcionais (Controllers/REST)

**Epic 4 (Disciplinas):**

- Testes unitários (Services com mocks)
- Testes de integração com DB (validação de relacionamentos)
- Testes funcionais (endpoints com filtros)

**Epic 5 (Matrículas):**

- Testes unitários (Services)
- Testes de integração com DB (validação de constraint única)
- Testes funcionais (endpoints de matrícula)

**Epic 6 (Notas e Médias):**

- Testes unitários (cálculo de média e classificação) - TDD obrigatório
- Testes de integração com DB (persistência de notas)
- Testes funcionais (todos os endpoints de notas)
- Testes de regras de negócio (validação de cálculos)

#### 2.2 Casos de Teste por User Story

- Para cada uma das 31 user stories, definir:
  - Casos de teste de sucesso
  - Casos de teste de erro/validação
  - Casos de teste de borda
  - Dados de teste necessários

#### 2.3 Plano de Cobertura

- Metas por camada:
  - Services: 80% mínimo
  - Controllers: 70% mínimo
  - Repositories: 80% mínimo
  - Domain: 90% mínimo (validações)

#### 2.4 Configurações de Ambiente de Teste

- SQLite em memória para testes de integração
- Mocks para testes unitários
- Configuração de profiles Maven
- Setup e teardown de dados de teste

#### 2.5 Estrutura de Testes

- Organização por tipo (unit, integration, functional)
- Fixtures e builders para dados de teste
- Helpers e utilitários de teste
- Configuração de assertions (AssertJ)

## Arquivos a Criar

1. **Architecture Doc** (`_bmad-output/architecture-doc.md`)

   - Visão geral
   - Estrutura de pacotes
   - Entidades JPA detalhadas
   - Relacionamentos
   - Padrões de design
   - Configurações
   - Exemplos de código

2. **Test Design** (`_bmad-output/test-design.md`)

   - Estratégia de testes
   - Casos de teste por épico/user story
   - Plano de cobertura
   - Configurações
   - Estrutura de testes

## Critérios de Sucesso

- [ ] Architecture Doc completo com todas as seções
- [ ] Entidades JPA definidas com relacionamentos
- [ ] Padrões de design documentados
- [ ] Test Design completo com estratégia clara
- [ ] Casos de teste definidos para todas as 31 user stories
- [ ] Plano de cobertura definido
- [ ] Documentos prontos para aprovação

## Próximos Passos Após P3

Após aprovação dos documentos:

- P4 (Implementation): Developer implementará seguindo Architecture Doc
- P4 (Implementation): TEA criará testes seguindo Test Design
- P4 (Implementation): TEA validará cobertura e realizará Code Review