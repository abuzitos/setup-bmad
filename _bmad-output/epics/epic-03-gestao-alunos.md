# Epic 3: Gestão de Alunos

## Descrição
Este épico abrange todas as funcionalidades relacionadas à gestão de alunos no sistema. Permite criar, listar, buscar, atualizar e excluir alunos, garantindo a integridade dos dados e validações necessárias.

## Objetivo
Fornecer uma API REST completa para gerenciamento de alunos, permitindo que administradores acadêmicos cadastrem e mantenham os alunos matriculados na faculdade.

## Prioridade
**Alta** - Épico fundamental que serve como base para outros épicos (Matrículas e Notas dependem de Alunos).

## Dependências
Nenhuma - Este é um épico base, sem dependências de outros épicos.

## User Stories

### US-011: Criar Aluno
**Como** Administrador Acadêmico  
**Eu quero** criar um novo aluno no sistema  
**Para** cadastrar os alunos matriculados na faculdade

**Critérios de Aceitação:**
- Deve aceitar nome do aluno (obrigatório, string, máximo 100 caracteres)
- Deve aceitar matrícula do aluno (obrigatório, string, único, máximo 20 caracteres)
- Deve gerar ID automaticamente para o aluno criado
- Deve validar que o nome não está vazio
- Deve validar que a matrícula não está vazia e é única
- Deve retornar status 201 (Created) quando criado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos

**RF Relacionado:** [RF-011](../prd.md#rf-011-criar-aluno)  
**Endpoint:** `POST /api/alunos`

---

### US-012: Listar Alunos
**Como** Administrador Acadêmico ou Professor  
**Eu quero** listar todos os alunos cadastrados  
**Para** visualizar quais alunos estão matriculados no sistema

**Critérios de Aceitação:**
- Deve retornar lista de todos os alunos cadastrados
- Cada aluno deve conter ID, nome e matrícula
- Deve retornar lista vazia se não houver alunos cadastrados
- Deve suportar parâmetros de paginação (opcional)
- Deve retornar status 200 (OK)

**RF Relacionado:** [RF-012](../prd.md#rf-012-listar-alunos)  
**Endpoint:** `GET /api/alunos`

---

### US-013: Buscar Aluno por ID
**Como** Administrador Acadêmico ou Professor  
**Eu quero** buscar um aluno específico pelo ID  
**Para** visualizar detalhes do aluno e suas disciplinas matriculadas

**Critérios de Aceitação:**
- Deve retornar aluno com ID, nome, matrícula e lista de disciplinas matriculadas
- Deve validar que o ID existe no sistema
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando aluno não existe

**RF Relacionado:** [RF-013](../prd.md#rf-013-buscar-aluno-por-id)  
**Endpoint:** `GET /api/alunos/{id}`

---

### US-014: Atualizar Aluno
**Como** Administrador Acadêmico  
**Eu quero** atualizar informações de um aluno existente  
**Para** corrigir ou atualizar dados do aluno

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o nome não está vazio
- Deve validar que a matrícula é única (exceto para o próprio aluno)
- Deve atualizar os dados do aluno
- Deve retornar aluno atualizado
- Deve retornar status 200 (OK) quando atualizado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos
- Deve retornar status 404 (Not Found) quando aluno não existe

**RF Relacionado:** [RF-014](../prd.md#rf-014-atualizar-aluno)  
**Endpoint:** `PUT /api/alunos/{id}`

---

### US-015: Excluir Aluno
**Como** Administrador Acadêmico  
**Eu quero** excluir um aluno do sistema  
**Para** remover alunos que não estão mais matriculados

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o aluno não possui notas registradas (ou definir política de exclusão em cascata)
- Deve excluir o aluno quando não houver dependências
- Deve retornar status 204 (No Content) quando excluído com sucesso
- Deve retornar status 400 (Bad Request) quando aluno possui notas registradas
- Deve retornar status 404 (Not Found) quando aluno não existe

**RF Relacionado:** [RF-015](../prd.md#rf-015-excluir-aluno)  
**Endpoint:** `DELETE /api/alunos/{id}`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/alunos` | Criar aluno |
| GET | `/api/alunos` | Listar alunos |
| GET | `/api/alunos/{id}` | Buscar aluno por ID |
| PUT | `/api/alunos/{id}` | Atualizar aluno |
| DELETE | `/api/alunos/{id}` | Excluir aluno |

## Validações

- Nome do aluno: obrigatório, não vazio, máximo 100 caracteres
- Matrícula do aluno: obrigatório, não vazio, único no sistema, máximo 20 caracteres
- ID do aluno: deve existir para operações de atualização, busca e exclusão
- Integridade referencial: aluno não pode ser excluído se possuir notas registradas (política a definir)

## Sprint
**Sprint 1 - Fundação** (Prioridade Alta)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
