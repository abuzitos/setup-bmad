# Planejamento de Sprints - Sistema de Cálculo de Médias

## Visão Geral

Este documento apresenta o planejamento de sprints para o desenvolvimento do Sistema de Cálculo de Médias, organizando os 6 épicos em 4 sprints sequenciais, respeitando as dependências entre épicos.

## Estrutura de Sprints

### Sprint 1 - Fundação
**Prioridade:** Alta  
**Objetivo:** Criar entidades base sem dependências

**Épicos:**
- Epic 1: Gestão de Cursos
- Epic 2: Gestão de Professores
- Epic 3: Gestão de Alunos

**User Stories:** 15 stories (US-001 a US-005, US-016 a US-020, US-011 a US-015)

**Duração Estimada:** 2-3 semanas

**Entregáveis:**
- CRUD completo de Cursos
- CRUD completo de Professores
- CRUD completo de Alunos
- Endpoints REST funcionais
- Testes unitários e de integração
- Documentação Swagger atualizada

**Dependências:** Nenhuma

---

### Sprint 2 - Relacionamentos
**Prioridade:** Alta  
**Objetivo:** Vincular disciplinas a cursos e professores

**Épicos:**
- Epic 4: Gestão de Disciplinas

**User Stories:** 5 stories (US-006 a US-010)

**Duração Estimada:** 1-2 semanas

**Entregáveis:**
- CRUD completo de Disciplinas
- Vinculação com Cursos (Epic 1)
- Vinculação com Professores (Epic 2)
- Endpoints REST com filtros
- Validações de integridade referencial
- Testes unitários e de integração
- Documentação Swagger atualizada

**Dependências:**
- Epic 1: Gestão de Cursos (Sprint 1)
- Epic 2: Gestão de Professores (Sprint 1)

---

### Sprint 3 - Matrículas
**Prioridade:** Alta  
**Objetivo:** Permitir matrícula de alunos em disciplinas

**Épicos:**
- Epic 5: Gestão de Matrículas

**User Stories:** 3 stories (US-021 a US-023)

**Duração Estimada:** 1 semana

**Entregáveis:**
- Funcionalidade de matricular aluno em disciplina
- Funcionalidade de desmatricular aluno
- Listagem de matrículas com filtros
- Validações de matrícula duplicada
- Testes unitários e de integração
- Documentação Swagger atualizada

**Dependências:**
- Epic 3: Gestão de Alunos (Sprint 1)
- Epic 4: Gestão de Disciplinas (Sprint 2)

---

### Sprint 4 - Notas e Médias
**Prioridade:** Crítica  
**Objetivo:** Funcionalidade core do sistema - registro de notas e cálculo automático

**Épicos:**
- Epic 6: Gestão de Notas e Cálculo de Médias

**User Stories:** 8 stories (US-024 a US-031)

**Duração Estimada:** 2-3 semanas

**Entregáveis:**
- Registro de notas (2 notas por aluno/disciplina)
- Atualização de notas
- Cálculo automático de médias
- Classificação automática (Aprovado/Exame/Reprovado)
- Consultas de notas (por aluno, por disciplina)
- Listagem de notas
- Testes unitários e de integração
- Testes funcionais completos
- Documentação Swagger atualizada

**Dependências:**
- Epic 5: Gestão de Matrículas (Sprint 3)

---

## Diagrama de Dependências

```
Sprint 1 (Fundação)
├── Epic 1: Cursos ──┐
├── Epic 2: Professores ──┤
└── Epic 3: Alunos         │
                           │
Sprint 2 (Relacionamentos) │
└── Epic 4: Disciplinas ←──┘
                           │
Sprint 3 (Matrículas)      │
└── Epic 5: Matrículas ←───┘
                           │
Sprint 4 (Notas e Médias)  │
└── Epic 6: Notas ←────────┘
```

## Priorização

### Prioridade Crítica
- **Epic 6: Gestão de Notas e Cálculo de Médias** - Funcionalidade core do sistema

### Prioridade Alta
- **Epic 1, 2, 3: Gestão de Cursos, Professores e Alunos** - Fundação do sistema
- **Epic 4, 5: Gestão de Disciplinas e Matrículas** - Necessários para Epic 6

## Critérios de Aceitação por Sprint

### Sprint 1
- [ ] Todos os endpoints de Cursos funcionando
- [ ] Todos os endpoints de Professores funcionando
- [ ] Todos os endpoints de Alunos funcionando
- [ ] Validações implementadas
- [ ] Testes com cobertura mínima de 80%
- [ ] Documentação Swagger completa

### Sprint 2
- [ ] Todos os endpoints de Disciplinas funcionando
- [ ] Vinculação com Cursos funcionando
- [ ] Vinculação com Professores funcionando
- [ ] Filtros de busca implementados
- [ ] Validações de integridade referencial
- [ ] Testes com cobertura mínima de 80%
- [ ] Documentação Swagger atualizada

### Sprint 3
- [ ] Funcionalidade de matricular aluno funcionando
- [ ] Funcionalidade de desmatricular aluno funcionando
- [ ] Listagem de matrículas com filtros funcionando
- [ ] Validação de matrícula duplicada
- [ ] Testes com cobertura mínima de 80%
- [ ] Documentação Swagger atualizada

### Sprint 4
- [ ] Registro de notas funcionando
- [ ] Atualização de notas funcionando
- [ ] Cálculo automático de médias funcionando
- [ ] Classificação automática funcionando
- [ ] Todas as consultas de notas funcionando
- [ ] Regras de negócio validadas
- [ ] Testes com cobertura mínima de 80%
- [ ] Testes funcionais completos
- [ ] Documentação Swagger atualizada

## Estimativas

| Sprint | Épicos | User Stories | Duração Estimada |
|--------|--------|--------------|------------------|
| Sprint 1 | 3 | 15 | 2-3 semanas |
| Sprint 2 | 1 | 5 | 1-2 semanas |
| Sprint 3 | 1 | 3 | 1 semana |
| Sprint 4 | 1 | 8 | 2-3 semanas |
| **Total** | **6** | **31** | **6-9 semanas** |

## Riscos e Mitigações

### Riscos Identificados

1. **Dependências entre sprints**
   - **Risco:** Atraso em um sprint impacta os seguintes
   - **Mitigação:** Priorizar épicos base (Sprint 1) e ter buffer de tempo

2. **Complexidade do cálculo de médias**
   - **Risco:** Lógica de cálculo e classificação pode ter bugs
   - **Mitigação:** Testes unitários extensivos e validação de regras de negócio

3. **Integridade referencial**
   - **Risco:** Exclusões podem quebrar relacionamentos
   - **Mitigação:** Validações rigorosas e testes de integração

4. **Cobertura de testes**
   - **Risco:** Não atingir meta de 80% de cobertura
   - **Mitigação:** TDD, testes desde o início de cada sprint

## Próximos Passos

Após aprovação deste planejamento:

1. **P3 (Solutioning):**
   - Software Architect criará Architecture Doc baseado nos épicos
   - Test Engineering Agent criará Test Design baseado nas user stories

2. **P4 (Implementation):**
   - Developer implementará seguindo os épicos e user stories
   - TEA validará testes e realizará Code Review

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
