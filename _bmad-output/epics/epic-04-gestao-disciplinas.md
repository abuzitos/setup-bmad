# Epic 4: Gestão de Disciplinas

## Descrição
Este épico abrange todas as funcionalidades relacionadas à gestão de disciplinas no sistema. Permite criar, listar, buscar, atualizar e excluir disciplinas, garantindo a vinculação correta com cursos e professores.

## Objetivo
Fornecer uma API REST completa para gerenciamento de disciplinas, permitindo que administradores acadêmicos cadastrem e mantenham as disciplinas oferecidas, vinculadas a cursos e professores.

## Prioridade
**Alta** - Épico necessário para permitir matrículas e registro de notas.

## Dependências
- **Epic 1: Gestão de Cursos** - Disciplinas devem estar vinculadas a cursos
- **Epic 2: Gestão de Professores** - Disciplinas devem ter professores responsáveis

## User Stories

### US-006: Criar Disciplina
**Como** Administrador Acadêmico  
**Eu quero** criar uma nova disciplina no sistema  
**Para** cadastrar as disciplinas oferecidas pela faculdade

**Critérios de Aceitação:**
- Deve aceitar nome da disciplina (obrigatório, string, máximo 100 caracteres)
- Deve aceitar ID do curso (obrigatório, referência)
- Deve aceitar ID do professor (obrigatório, referência)
- Deve gerar ID automaticamente para a disciplina criada
- Deve validar que o nome não está vazio
- Deve validar que o curso existe
- Deve validar que o professor existe
- Deve validar que o nome é único dentro do mesmo curso
- Deve retornar status 201 (Created) quando criado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos

**RF Relacionado:** [RF-006](../prd.md#rf-006-criar-disciplina)  
**Endpoint:** `POST /api/disciplinas`

---

### US-007: Listar Disciplinas
**Como** Administrador Acadêmico ou Professor  
**Eu quero** listar todas as disciplinas cadastradas  
**Para** visualizar quais disciplinas estão disponíveis no sistema

**Critérios de Aceitação:**
- Deve retornar lista de todas as disciplinas cadastradas
- Cada disciplina deve conter ID, nome, curso e professor
- Deve suportar filtro por curso (`?cursoId={id}`)
- Deve suportar filtro por professor (`?professorId={id}`)
- Deve retornar lista vazia se não houver disciplinas cadastradas
- Deve retornar status 200 (OK)

**RF Relacionado:** [RF-007](../prd.md#rf-007-listar-disciplinas)  
**Endpoint:** `GET /api/disciplinas`

---

### US-008: Buscar Disciplina por ID
**Como** Administrador Acadêmico ou Professor  
**Eu quero** buscar uma disciplina específica pelo ID  
**Para** visualizar detalhes da disciplina e alunos matriculados

**Critérios de Aceitação:**
- Deve retornar disciplina com ID, nome, curso, professor e lista de alunos matriculados
- Deve validar que o ID existe no sistema
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando disciplina não existe

**RF Relacionado:** [RF-008](../prd.md#rf-008-buscar-disciplina-por-id)  
**Endpoint:** `GET /api/disciplinas/{id}`

---

### US-009: Atualizar Disciplina
**Como** Administrador Acadêmico  
**Eu quero** atualizar informações de uma disciplina existente  
**Para** corrigir ou atualizar dados da disciplina

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o curso existe (se informado)
- Deve validar que o professor existe (se informado)
- Deve validar que o nome é único dentro do curso (se alterado)
- Deve atualizar os dados da disciplina
- Deve retornar disciplina atualizada
- Deve retornar status 200 (OK) quando atualizado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos
- Deve retornar status 404 (Not Found) quando disciplina não existe

**RF Relacionado:** [RF-009](../prd.md#rf-009-atualizar-disciplina)  
**Endpoint:** `PUT /api/disciplinas/{id}`

---

### US-010: Excluir Disciplina
**Como** Administrador Acadêmico  
**Eu quero** excluir uma disciplina do sistema  
**Para** remover disciplinas que não são mais oferecidas

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que a disciplina não possui alunos matriculados
- Deve validar que a disciplina não possui notas registradas
- Deve excluir a disciplina quando não houver dependências
- Deve retornar status 204 (No Content) quando excluído com sucesso
- Deve retornar status 400 (Bad Request) quando disciplina possui dependências
- Deve retornar status 404 (Not Found) quando disciplina não existe

**RF Relacionado:** [RF-010](../prd.md#rf-010-excluir-disciplina)  
**Endpoint:** `DELETE /api/disciplinas/{id}`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/disciplinas` | Criar disciplina |
| GET | `/api/disciplinas` | Listar disciplinas (com filtros opcionais) |
| GET | `/api/disciplinas/{id}` | Buscar disciplina por ID |
| PUT | `/api/disciplinas/{id}` | Atualizar disciplina |
| DELETE | `/api/disciplinas/{id}` | Excluir disciplina |

## Validações

- Nome da disciplina: obrigatório, não vazio, máximo 100 caracteres, único dentro do mesmo curso
- ID do curso: obrigatório, deve existir no sistema
- ID do professor: obrigatório, deve existir no sistema
- ID da disciplina: deve existir para operações de atualização, busca e exclusão
- Integridade referencial: disciplina não pode ser excluída se possuir alunos matriculados ou notas registradas

## Sprint
**Sprint 2 - Relacionamentos** (Prioridade Alta)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
