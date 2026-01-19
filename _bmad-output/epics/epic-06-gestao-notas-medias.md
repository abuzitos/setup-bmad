# Epic 6: Gestão de Notas e Cálculo de Médias

## Descrição
Este épico abrange todas as funcionalidades relacionadas ao registro de notas, cálculo automático de médias e classificação de aprovação. É a funcionalidade core do sistema, permitindo que professores registrem notas e o sistema calcule automaticamente médias e classificações.

## Objetivo
Fornecer uma API REST completa para registro de notas, com cálculo automático de médias e classificação de aprovação (Aprovado/Exame/Reprovado), baseado nas regras de negócio definidas.

## Prioridade
**Crítica** - Esta é a funcionalidade core do sistema, o objetivo principal do projeto.

## Dependências
- **Epic 5: Gestão de Matrículas** - Aluno deve estar matriculado na disciplina para receber notas

## User Stories

### US-024: Registrar Notas
**Como** Professor  
**Eu quero** registrar as 2 notas de um aluno em uma disciplina  
**Para** avaliar o desempenho do aluno

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (obrigatório)
- Deve aceitar ID da disciplina (obrigatório)
- Deve aceitar Nota 1 (obrigatório, decimal entre 0.0 e 10.0)
- Deve aceitar Nota 2 (obrigatório, decimal entre 0.0 e 10.0)
- Deve validar que o aluno existe
- Deve validar que a disciplina existe
- Deve validar que o aluno está matriculado na disciplina
- Deve validar que as notas estão no intervalo [0.0, 10.0]
- Deve calcular média automaticamente: (Nota 1 + Nota 2) / 2
- Deve classificar automaticamente baseado na média
- Não deve permitir mais de um registro de notas para o mesmo aluno/disciplina (ou permitir atualização)
- Deve retornar status 201 (Created) quando registrado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos ou aluno não matriculado
- Deve retornar status 404 (Not Found) quando aluno ou disciplina não existem

**RF Relacionado:** [RF-024](../prd.md#rf-024-registrar-notas)  
**Endpoint:** `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`

---

### US-025: Atualizar Notas
**Como** Professor  
**Eu quero** atualizar as notas de um aluno em uma disciplina  
**Para** corrigir notas registradas incorretamente

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (path parameter)
- Deve aceitar ID da disciplina (path parameter)
- Deve aceitar Nota 1 (obrigatório, decimal entre 0.0 e 10.0)
- Deve aceitar Nota 2 (obrigatório, decimal entre 0.0 e 10.0)
- Deve validar que o registro de notas existe
- Deve validar que as notas estão no intervalo [0.0, 10.0]
- Deve recalcular média automaticamente: (Nota 1 + Nota 2) / 2
- Deve reclassificar automaticamente baseado na nova média
- Deve retornar status 200 (OK) quando atualizado com sucesso
- Deve retornar status 400 (Bad Request) quando dados inválidos
- Deve retornar status 404 (Not Found) quando registro de notas não existe

**RF Relacionado:** [RF-025](../prd.md#rf-025-atualizar-notas)  
**Endpoint:** `PUT /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`

---

### US-026: Consultar Notas
**Como** Professor ou Aluno  
**Eu quero** consultar as notas de um aluno em uma disciplina  
**Para** visualizar o desempenho e status de aprovação

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (path parameter)
- Deve aceitar ID da disciplina (path parameter)
- Deve validar que o registro de notas existe
- Deve retornar Nota 1, Nota 2, média calculada e classificação (Aprovado/Exame/Reprovado)
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando registro de notas não existe

**RF Relacionado:** [RF-026](../prd.md#rf-026-consultar-notas)  
**Endpoint:** `GET /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`

---

### US-027: Listar Notas por Disciplina
**Como** Professor  
**Eu quero** listar todas as notas de uma disciplina  
**Para** visualizar o desempenho de todos os alunos matriculados

**Critérios de Aceitação:**
- Deve aceitar ID da disciplina (path parameter)
- Deve validar que a disciplina existe
- Deve retornar lista de alunos com suas respectivas notas, médias e classificações
- Deve retornar lista vazia se não houver notas registradas
- Deve retornar status 200 (OK)
- Deve retornar status 404 (Not Found) quando disciplina não existe

**RF Relacionado:** [RF-027](../prd.md#rf-027-listar-notas-por-disciplina)  
**Endpoint:** `GET /api/disciplinas/{disciplinaId}/notas`

---

### US-028: Listar Notas por Aluno
**Como** Aluno ou Administrador Acadêmico  
**Eu quero** listar todas as notas de um aluno em todas as disciplinas  
**Para** visualizar o desempenho completo do aluno

**Critérios de Aceitação:**
- Deve aceitar ID do aluno (path parameter)
- Deve validar que o aluno existe
- Deve retornar lista de disciplinas com respectivas notas, médias e classificações
- Deve retornar lista vazia se o aluno não tiver notas registradas
- Deve retornar status 200 (OK)
- Deve retornar status 404 (Not Found) quando aluno não existe

**RF Relacionado:** [RF-028](../prd.md#rf-028-listar-notas-por-aluno)  
**Endpoint:** `GET /api/alunos/{alunoId}/notas`

---

### US-029: Calcular Média Automaticamente
**Como** Sistema  
**Eu quero** calcular automaticamente a média quando as notas são registradas ou atualizadas  
**Para** garantir precisão e consistência dos cálculos

**Critérios de Aceitação:**
- Deve calcular média automaticamente após criação de notas: Média = (Nota 1 + Nota 2) / 2
- Deve recalcular média automaticamente após atualização de notas
- Média deve ser um valor decimal entre 0.0 e 10.0
- Média deve ter precisão de 2 casas decimais
- Cálculo deve ser acionado automaticamente (trigger)

**RF Relacionado:** [RF-029](../prd.md#rf-029-calcular-média-automaticamente)  
**Endpoint:** Automático (não exposto como endpoint)

---

### US-030: Classificar Aprovação Automaticamente
**Como** Sistema  
**Eu quero** classificar automaticamente o status de aprovação baseado na média calculada  
**Para** determinar se o aluno está aprovado, em exame ou reprovado

**Critérios de Aceitação:**
- Deve classificar automaticamente após cálculo da média
- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0
- Classificação deve ser representada como Enum ou String ("APROVADO", "EXAME", "REPROVADO")
- Classificação deve ser acionada automaticamente (trigger)

**RF Relacionado:** [RF-030](../prd.md#rf-030-classificar-aprovação-automaticamente)  
**Endpoint:** Automático (não exposto como endpoint)

---

### US-031: Consultar Status de Aprovação
**Como** Aluno ou Professor  
**Eu quero** consultar o status de aprovação de um aluno em uma disciplina  
**Para** saber se o aluno está aprovado, em exame ou reprovado

**Critérios de Aceitação:**
- Deve aceitar ID do aluno e ID da disciplina
- Deve retornar média e classificação (Aprovado/Exame/Reprovado)
- Esta funcionalidade está incluída no RF-026 (Consultar Notas)
- Deve retornar status 200 (OK) quando encontrado
- Deve retornar status 404 (Not Found) quando registro não existe

**RF Relacionado:** [RF-031](../prd.md#rf-031-consultar-status-de-aprovação)  
**Endpoint:** Incluído no `GET /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`

---

## Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Registrar notas |
| PUT | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Atualizar notas |
| GET | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Consultar notas |
| GET | `/api/disciplinas/{disciplinaId}/notas` | Listar notas da disciplina |
| GET | `/api/alunos/{alunoId}/notas` | Listar notas do aluno |

## Regras de Negócio

### Cálculo de Média
- **Fórmula**: Média = (Nota 1 + Nota 2) / 2
- **Precisão**: 2 casas decimais
- **Intervalo**: 0.0 a 10.0
- **Trigger**: Automático após criação ou atualização de notas

### Classificação de Aprovação
- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0
- **Trigger**: Automático após cálculo da média

## Validações

- Notas: obrigatórias, decimais, intervalo [0.0, 10.0]
- Aluno: deve existir e estar matriculado na disciplina
- Disciplina: deve existir
- Registro de notas: deve existir para atualização e consulta

## Sprint
**Sprint 4 - Notas e Médias** (Prioridade Crítica)

---

**Criado por:** Product Manager (PM)  
**Data:** Fase P2 - Planning  
**Status:** ✅ APROVADO - Pronto para P3 (Solutioning)
