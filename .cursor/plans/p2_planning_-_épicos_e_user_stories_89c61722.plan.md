---
name: P2 Planning - Épicos e User Stories
overview: Criar estrutura completa de planejamento para P2_Planning, incluindo 6 épicos baseados nos requisitos funcionais do PRD, 31 user stories detalhadas, organização em sprints e definição de prioridades.
todos:
  - id: create-epic-01
    content: "Criar Epic 1: Gestão de Cursos com 5 user stories (RF-001 a RF-005)"
    status: pending
  - id: create-epic-02
    content: "Criar Epic 2: Gestão de Professores com 5 user stories (RF-016 a RF-020)"
    status: pending
  - id: create-epic-03
    content: "Criar Epic 3: Gestão de Alunos com 5 user stories (RF-011 a RF-015)"
    status: pending
  - id: create-epic-04
    content: "Criar Epic 4: Gestão de Disciplinas com 5 user stories (RF-006 a RF-010)"
    status: pending
    dependencies:
      - create-epic-01
      - create-epic-02
  - id: create-epic-05
    content: "Criar Epic 5: Gestão de Matrículas com 3 user stories (RF-021 a RF-023)"
    status: pending
    dependencies:
      - create-epic-03
      - create-epic-04
  - id: create-epic-06
    content: "Criar Epic 6: Gestão de Notas e Cálculo de Médias com 8 user stories (RF-024 a RF-031)"
    status: pending
    dependencies:
      - create-epic-05
  - id: create-sprint-planning
    content: Criar arquivo sprint-planning.md com organização dos sprints e prioridades
    status: pending
    dependencies:
      - create-epic-01
      - create-epic-02
      - create-epic-03
      - create-epic-04
      - create-epic-05
      - create-epic-06
  - id: update-sprint-status
    content: Atualizar sprint-status.yaml marcando P2_Planning como IN_PROGRESS
    status: pending
---

# P2 Planning - Sistema de Cálculo de Médias

## Objetivo
Criar estrutura completa de planejamento com épicos, user stories detalhadas e organização em sprints para o Sistema de Cálculo de Médias, baseado nos 31 requisitos funcionais definidos no PRD.

## Estrutura a Criar

### 1. Épicos (6 épicos principais)

Baseados nos grupos de requisitos funcionais do PRD:

1. **Epic 1: Gestão de Cursos** (RF-001 a RF-005)
   - CRUD completo de cursos
   - Validações de unicidade de nome

2. **Epic 2: Gestão de Professores** (RF-016 a RF-020)
   - CRUD completo de professores
   - Validações de unicidade de registro

3. **Epic 3: Gestão de Alunos** (RF-011 a RF-015)
   - CRUD completo de alunos
   - Validações de unicidade de matrícula

4. **Epic 4: Gestão de Disciplinas** (RF-006 a RF-010)
   - CRUD completo de disciplinas
   - Vinculação com cursos e professores
   - Depende de: Epic 1 e Epic 2

5. **Epic 5: Gestão de Matrículas** (RF-021 a RF-023)
   - Matricular/desmatricular alunos em disciplinas
   - Listar matrículas com filtros
   - Depende de: Epic 3 e Epic 4

6. **Epic 6: Gestão de Notas e Cálculo de Médias** (RF-024 a RF-031)
   - Registrar e atualizar notas
   - Cálculo automático de médias
   - Classificação automática (Aprovado/Exame/Reprovado)
   - Consultas de notas
   - Depende de: Epic 5

### 2. User Stories

Criar 31 user stories detalhadas, uma para cada RF, no formato:
- **Como** [persona]
- **Eu quero** [ação]
- **Para** [benefício]
- **Critérios de Aceitação**: Baseados nas validações e regras do PRD
- **RF Relacionado**: Link para o requisito funcional

### 3. Organização em Sprints

**Sprint 1 - Fundação** (Prioridade Alta)
- Epic 1: Gestão de Cursos
- Epic 2: Gestão de Professores
- Epic 3: Gestão de Alunos
- Objetivo: Criar entidades base sem dependências

**Sprint 2 - Relacionamentos** (Prioridade Alta)
- Epic 4: Gestão de Disciplinas
- Objetivo: Vincular disciplinas a cursos e professores

**Sprint 3 - Matrículas** (Prioridade Alta)
- Epic 5: Gestão de Matrículas
- Objetivo: Permitir matrícula de alunos em disciplinas

**Sprint 4 - Notas e Médias** (Prioridade Crítica)
- Epic 6: Gestão de Notas e Cálculo de Médias
- Objetivo: Funcionalidade core do sistema

### 4. Arquivos a Criar

1. **Épicos** (`_bmad-output/epics/`)
   - `epic-01-gestao-cursos.md`
   - `epic-02-gestao-professores.md`
   - `epic-03-gestao-alunos.md`
   - `epic-04-gestao-disciplinas.md`
   - `epic-05-gestao-matriculas.md`
   - `epic-06-gestao-notas-medias.md`

2. **User Stories** (dentro de cada épico ou arquivo separado)
   - Uma user story por RF, organizadas por épico

3. **Planejamento de Sprints** (`_bmad-output/`)
   - `sprint-planning.md` - Visão geral dos sprints
   - Atualizar `sprint-status.yaml` com informações de planejamento

## Detalhamento dos Épicos

### Epic 1: Gestão de Cursos
- 5 user stories (RF-001 a RF-005)
- Endpoints: POST, GET (lista), GET (por ID), PUT, DELETE
- Validações: Nome obrigatório, único, máximo 100 caracteres

### Epic 2: Gestão de Professores
- 5 user stories (RF-016 a RF-020)
- Endpoints: POST, GET (lista), GET (por ID), PUT, DELETE
- Validações: Nome obrigatório, registro único, máximo 100/20 caracteres

### Epic 3: Gestão de Alunos
- 5 user stories (RF-011 a RF-015)
- Endpoints: POST, GET (lista), GET (por ID), PUT, DELETE
- Validações: Nome obrigatório, matrícula única, máximo 100/20 caracteres

### Epic 4: Gestão de Disciplinas
- 5 user stories (RF-006 a RF-010)
- Endpoints: POST, GET (lista com filtros), GET (por ID), PUT, DELETE
- Validações: Nome obrigatório, curso válido, professor válido, nome único no curso

### Epic 5: Gestão de Matrículas
- 3 user stories (RF-021 a RF-023)
- Endpoints: POST (matricular), GET (lista com filtros), DELETE (desmatricular)
- Validações: Aluno válido, disciplina válida, evitar duplicatas

### Epic 6: Gestão de Notas e Cálculo de Médias
- 8 user stories (RF-024 a RF-031)
- Endpoints: POST, PUT, GET (por aluno/disciplina), GET (lista por disciplina), GET (lista por aluno)
- Funcionalidades especiais: Cálculo automático de média, classificação automática

## Priorização

1. **Crítica**: Epic 6 (funcionalidade core)
2. **Alta**: Epic 1, 2, 3 (fundação do sistema)
3. **Alta**: Epic 4, 5 (necessários para Epic 6)

## Dependências entre Épicos

```
Epic 1 (Cursos) ──┐
                   ├──> Epic 4 (Disciplinas) ──┐
Epic 2 (Professores) ──┘                        │
                                                 ├──> Epic 5 (Matrículas) ──> Epic 6 (Notas)
Epic 3 (Alunos) ────────────────────────────────┘
```

## Critérios de Sucesso da P2

- [ ] 6 épicos criados e documentados
- [ ] 31 user stories detalhadas (uma por RF)
- [ ] User stories com critérios de aceitação claros
- [ ] Sprints organizados considerando dependências
- [ ] Prioridades definidas
- [ ] Arquivo de planejamento de sprints criado
- [ ] sprint-status.yaml atualizado com status P2

## Próximos Passos Após P2

- P3 (Solutioning): Architect criará Architecture Doc baseado nos épicos
- P3 (Solutioning): TEA criará Test Design baseado nas user stories
- P4 (Implementation): Developer implementará seguindo os épicos e user stories