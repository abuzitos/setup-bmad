---
name: P1 Discovery - Brief e PRD
overview: Criar os documentos Brief e PRD na fase P1 (Discovery) seguindo o protocolo BMad, documentando requisitos funcionais e não funcionais do sistema de cálculo de médias.
todos:
  - id: create-brief
    content: Criar documento Brief.md com visão geral, objetivos, contexto e escopo do projeto
    status: completed
  - id: create-prd
    content: Criar documento PRD.md com requisitos funcionais detalhados (CRUDs, cálculo de médias, endpoints REST) e não funcionais (stack técnica, testes, DevContainer)
    status: completed
    dependencies:
      - create-brief
  - id: validate-documents
    content: Validar que Brief e PRD estão completos e alinhados com as responsabilidades do PM definidas em _bmad/bmm/agents/pm.yaml
    status: completed
    dependencies:
      - create-brief
      - create-prd
---

# P1 Discovery - Criação de Brief e PRD

## Objetivo

Criar os documentos fundamentais da fase P1 (Discovery) conforme protocolo BMad:

- **Brief**: Documento inicial com visão geral, objetivos e contexto do projeto
- **PRD (Product Requirements Document)**: Documento detalhado com requisitos funcionais e não funcionais

## Contexto do Projeto

Sistema de cálculo de médias de alunos de uma faculdade com:

- **Entidades**: Cursos, Disciplinas, Alunos, Professores, Notas
- **Regras de Negócio**: 
- Cada disciplina possui 2 notas por aluno
- Média >= 7.0: Aprovado
- Média >= 5.0 e < 7.0: Exame
- Média < 5.0: Reprovado
- **Stack**: Java 21, Maven, SQLite, JPA (Jakarta Persistence), Jakarta EE, Jakarta REST

## Arquivos a Criar

### 1. Brief (`_bmad-output/brief.md`)

Documento inicial contendo:

- Visão geral do projeto
- Objetivos e propósito
- Contexto e stakeholders
- Escopo inicial
- Premissas e restrições

### 2. PRD (`_bmad-output/prd.md`)

Documento detalhado contendo:

- **Requisitos Funcionais**:
- Gestão de Cursos (CRUD)
- Gestão de Disciplinas (CRUD, vinculação a cursos e professores)
- Gestão de Alunos (CRUD)
- Gestão de Professores (CRUD)
- Gestão de Matrículas (Alunos em Disciplinas)
- Registro de Notas (2 notas por aluno/disciplina)
- Cálculo automático de médias
- Classificação de aprovação (Aprovado/Exame/Reprovado)
- Endpoints REST para todas as operações

- **Requisitos Não Funcionais**:
- Stack técnica (Java 21, Maven, SQLite, JPA, Jakarta EE)
- Execução em DevContainer
- Testes automatizados
- API REST com Jakarta REST
- Persistência com JPA/Hibernate e SQLite

- **Regras de Negócio Detalhadas**:
- Validações de dados
- Regras de cálculo de média
- Regras de classificação
- Relacionamentos entre entidades

### 3. Atualização do Status (`_bmad-output/sprint-status.yaml`)

Manter status atual: `P1_Discovery: IN_PROGRESS`

## Estrutura dos Documentos

Os documentos seguirão padrões profissionais de documentação de produto, com seções claras e objetivas, focando nos requisitos necessários para as próximas fases (P2 Planning, P3 Solutioning, P4 Implementation).