# Epic 5: Gestão de Matrículas

## Descrição
Este épico abrange todas as funcionalidades relacionadas à gestão de matrículas de alunos em disciplinas. Permite matricular e desmatricular alunos, além de listar matrículas com filtros.

## Objetivo
Fornecer uma API REST completa para gerenciamento de matrículas, permitindo que administradores acadêmicos vinculem alunos a disciplinas, habilitando o registro de notas posteriormente.

## Prioridade
**Alta** - Épico necessário para permitir registro de notas (aluno deve estar matriculado para receber notas).

## Dependências
- **Epic 3: Gestão de Alunos** - Matrículas dependem de alunos existentes
- **Epic 4: Gestão de Disciplinas** - Matrículas dependem de disciplinas existentes

## User Stories

### US-021: Matricular Aluno em Disciplina
**Como** Administrador Acadêmico  
**Eu quero** matricular um aluno em uma disciplina  
**Para** permitir que o aluno receba notas nessa disciplina

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (obrigatório)
- Deve aceitar ID da disciplina (obrigatório)
- Deve validar que o aluno existe
- Deve validar que a disciplina existe
- Deve validar que o aluno não está já matriculado na disciplina (evitar duplicatas)
- Deve criar a matrícula
- Deve retornar status 201 (Created) quando matriculado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos ou matrícula duplicada
- Deve retornar status 404 (Not Found) quando aluno ou disciplina não existem

**RF Relacionado:** [RF-021](../prd.md#rf-021-matricular-aluno-em-disciplina)  
**Endpoint:** `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}` ou `POST /api/matriculas`

---

### US-022: Listar Matrículas
**Como** Administrador Acadêmico ou Professor  
**Eu quero** listar todas as matrículas  
**Para** visualizar quais alunos estão matriculados em quais disciplinas

**Critérios de Aceitação:**
- Deve retornar lista de todas as matrículas
- Cada matrícula deve conter aluno e disciplina
- Deve suportar filtro por aluno (`?alunoId={id}`)
- Deve suportar filtro por disciplina (`?disciplinaId={id}`)
- Deve retornar lista vazia se não houver matrículas
- Deve retornar status 200 (OK)

**RF Relacionado:** [RF-022](../prd.md#rf-022-listar-matrículas)  
**Endpoint:** `GET /api/matriculas`

---

### US-023: Desmatricular Aluno
**Como** Administrador Acadêmico  
**Eu quero** desmatricular um aluno de uma disciplina  
**Para** remover a matrícula quando necessário

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (path parameter)
- Deve aceitar ID da disciplina (path parameter)
- Deve validar que a matrícula existe
- Deve remover a matrícula
- Deve definir política para notas existentes (excluir notas ou manter histórico)
- Deve retornar status 204 (No Content) quando desmatriculado com sucesso
- Deve retornar status 404 (Not Found) quando matrícula não existe

**RF Relacionado:** [RF-023](../prd.md#rf-023-desmatricular-aluno)  
**Endpoint:** `DELETE /api/disciplinas/{disciplinaId}/alunos/{alunoId}`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}` | Matricular aluno |
| POST | `/api/matriculas` | Matricular aluno (alternativa) |
| GET | `/api/matriculas` | Listar matrículas (com filtros opcionais) |
| DELETE | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}` | Desmatricular aluno |

## Validações

- ID do aluno: obrigatório, deve existir no sistema
- ID da disciplina: obrigatório, deve existir no sistema
- Matrícula duplicada: aluno não pode estar matriculado duas vezes na mesma disciplina
- Integridade referencial: ao desmatricular, definir política para notas existentes

## Sprint
**Sprint 3 - Matrículas** (Prioridade Alta)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
