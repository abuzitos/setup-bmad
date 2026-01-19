# Epic 2: Gestão de Professores

## Descrição
Este épico abrange todas as funcionalidades relacionadas à gestão de professores no sistema. Permite criar, listar, buscar, atualizar e excluir professores, garantindo a integridade dos dados e validações necessárias.

## Objetivo
Fornecer uma API REST completa para gerenciamento de professores, permitindo que administradores acadêmicos cadastrem e mantenham os professores que lecionam na faculdade.

## Prioridade
**Alta** - Épico fundamental que serve como base para outros épicos (Disciplinas dependem de Professores).

## Dependências
Nenhuma - Este é um épico base, sem dependências de outros épicos.

## User Stories

### US-016: Criar Professor
**Como** Administrador Acadêmico  
**Eu quero** criar um novo professor no sistema  
**Para** cadastrar os professores que lecionam na faculdade

**Critérios de Aceitação:**
- Deve aceitar nome do professor (obrigatório, string, máximo 100 caracteres)
- Deve aceitar registro do professor (obrigatório, string, único, máximo 20 caracteres)
- Deve gerar ID automaticamente para o professor criado
- Deve validar que o nome não está vazio
- Deve validar que o registro não está vazio e é único
- Deve retornar status 201 (Created) quando criado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos

**RF Relacionado:** [RF-016](../prd.md#rf-016-criar-professor)  
**Endpoint:** `POST /api/professores`

---

### US-017: Listar Professores
**Como** Administrador Acadêmico ou Professor  
**Eu quero** listar todos os professores cadastrados  
**Para** visualizar quais professores estão disponíveis no sistema

**Critérios de Aceitação:**
- Deve retornar lista de todos os professores cadastrados
- Cada professor deve conter ID, nome e registro
- Deve retornar lista vazia se não houver professores cadastrados
- Deve suportar parâmetros de paginação (opcional)
- Deve retornar status 200 (OK)

**RF Relacionado:** [RF-017](../prd.md#rf-017-listar-professores)  
**Endpoint:** `GET /api/professores`

---

### US-018: Buscar Professor por ID
**Como** Administrador Acadêmico ou Professor  
**Eu quero** buscar um professor específico pelo ID  
**Para** visualizar detalhes do professor e suas disciplinas lecionadas

**Critérios de Aceitação:**
- Deve retornar professor com ID, nome, registro e lista de disciplinas que leciona
- Deve validar que o ID existe no sistema
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando professor não existe

**RF Relacionado:** [RF-018](../prd.md#rf-018-buscar-professor-por-id)  
**Endpoint:** `GET /api/professores/{id}`

---

### US-019: Atualizar Professor
**Como** Administrador Acadêmico  
**Eu quero** atualizar informações de um professor existente  
**Para** corrigir ou atualizar dados do professor

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o nome não está vazio
- Deve validar que o registro é único (exceto para o próprio professor)
- Deve atualizar os dados do professor
- Deve retornar professor atualizado
- Deve retornar status 200 (OK) quando atualizado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos
- Deve retornar status 404 (Not Found) quando professor não existe

**RF Relacionado:** [RF-019](../prd.md#rf-019-atualizar-professor)  
**Endpoint:** `PUT /api/professores/{id}`

---

### US-020: Excluir Professor
**Como** Administrador Acadêmico  
**Eu quero** excluir um professor do sistema  
**Para** remover professores que não lecionam mais

**Critérios de Aceitação:**
- Deve validar que o ID existe no sistema
- Deve validar que o professor não está vinculado a disciplinas (restrição de integridade)
- Deve excluir o professor quando não houver dependências
- Deve retornar status 204 (No Content) quando excluído com sucesso
- Deve retornar status 400 (Bad Request) quando professor possui disciplinas vinculadas
- Deve retornar status 404 (Not Found) quando professor não existe

**RF Relacionado:** [RF-020](../prd.md#rf-020-excluir-professor)  
**Endpoint:** `DELETE /api/professores/{id}`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/professores` | Criar professor |
| GET | `/api/professores` | Listar professores |
| GET | `/api/professores/{id}` | Buscar professor por ID |
| PUT | `/api/professores/{id}` | Atualizar professor |
| DELETE | `/api/professores/{id}` | Excluir professor |

## Validações

- Nome do professor: obrigatório, não vazio, máximo 100 caracteres
- Registro do professor: obrigatório, não vazio, único no sistema, máximo 20 caracteres
- ID do professor: deve existir para operações de atualização, busca e exclusão
- Integridade referencial: professor não pode ser excluído se possuir disciplinas vinculadas

## Sprint
**Sprint 1 - Fundação** (Prioridade Alta)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
