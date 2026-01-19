# Epic 1: Gestão de Cursos

## Descrição
Este épico abrange todas as funcionalidades relacionadas à gestão de cursos no sistema. Permite criar, listar, buscar, atualizar e excluir cursos, garantindo a integridade dos dados e validações necessárias.

## Objetivo
Fornecer uma API REST completa para gerenciamento de cursos, permitindo que administradores acadêmicos cadastrem e mantenham os cursos oferecidos pela faculdade.

## Prioridade
**Alta** - Épico fundamental que serve como base para outros épicos (Disciplinas dependem de Cursos).

## Dependências
Nenhuma - Este é um épico base, sem dependências de outros épicos.

## User Stories

### US-001: Criar Curso
**Como** Administrador Acadêmico  
**Eu quero** criar um novo curso no sistema  
**Para** cadastrar os cursos oferecidos pela faculdade

**Critérios de Aceitação:**
- Deve aceitar nome do curso (obrigatório, string, máximo 100 caracteres)
- Deve gerar ID automaticamente para o curso criado
- Deve validar que o nome não está vazio ou nulo
- Deve validar que o nome é único (não pode haver dois cursos com o mesmo nome)
- Deve retornar status 201 (Created) quando criado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos

**RF Relacionado:** [RF-001](../prd.md#rf-001-criar-curso)  
**Endpoint:** `POST /api/cursos`

---

### US-002: Listar Cursos
**Como** Administrador Acadêmico ou Professor  
**Eu quero** listar todos os cursos cadastrados  
**Para** visualizar quais cursos estão disponíveis no sistema

**Critérios de Aceitação:**
- Deve retornar lista de todos os cursos cadastrados
- Cada curso deve conter ID e nome
- Deve retornar lista vazia se não houver cursos cadastrados
- Deve suportar parâmetros de paginação (opcional)
- Deve retornar status 200 (OK)

**RF Relacionado:** [RF-002](../prd.md#rf-002-listar-cursos)  
**Endpoint:** `GET /api/cursos`

---

### US-003: Buscar Curso por ID
**Como** Administrador Acadêmico ou Professor  
**Eu quero** buscar um curso específico pelo ID  
**Para** visualizar detalhes do curso e suas disciplinas vinculadas

**Critérios de Aceitação:**
- Deve retornar curso com ID, nome e lista de disciplinas vinculadas
- Deve validar que o ID existe no sistema
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando curso não existe

**RF Relacionado:** [RF-003](../prd.md#rf-003-buscar-curso-por-id)  
**Endpoint:** `GET /api/cursos/{id}`

---

### US-004: Atualizar Curso
**Como** Administrador Acadêmico  
**Eu quero** atualizar informações de um curso existente  
**Para** corrigir ou atualizar dados do curso

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o nome não está vazio
- Deve validar que o nome é único (exceto para o próprio curso)
- Deve atualizar os dados do curso
- Deve retornar curso atualizado
- Deve retornar status 200 (OK) quando atualizado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos
- Deve retornar status 404 (Not Found) quando curso não existe

**RF Relacionado:** [RF-004](../prd.md#rf-004-atualizar-curso)  
**Endpoint:** `PUT /api/cursos/{id}`

---

### US-005: Excluir Curso
**Como** Administrador Acadêmico  
**Eu quero** excluir um curso do sistema  
**Para** remover cursos que não são mais oferecidos

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o curso não possui disciplinas vinculadas (restrição de integridade)
- Deve excluir o curso quando não houver dependências
- Deve retornar status 204 (No Content) quando excluído com sucesso
- Deve retornar status 400 (Bad Request) quando curso possui disciplinas vinculadas
- Deve retornar status 404 (Not Found) quando curso não existe

**RF Relacionado:** [RF-005](../prd.md#rf-005-excluir-curso)  
**Endpoint:** `DELETE /api/cursos/{id}`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/cursos` | Criar curso |
| GET | `/api/cursos` | Listar cursos |
| GET | `/api/cursos/{id}` | Buscar curso por ID |
| PUT | `/api/cursos/{id}` | Atualizar curso |
| DELETE | `/api/cursos/{id}` | Excluir curso |

## Validações

- Nome do curso: obrigatório, não vazio, máximo 100 caracteres, único no sistema
- ID do curso: deve existir para operações de atualização, busca e exclusão
- Integridade referencial: curso não pode ser excluído se possuir disciplinas vinculadas

## Sprint
**Sprint 1 - Fundação** (Prioridade Alta)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
