# Brief - Sistema de Cálculo de Médias

## Visão Geral

O **Sistema de Cálculo de Médias** é uma aplicação desenvolvida para gerenciar o cálculo de médias dos alunos de uma faculdade. O sistema permite a gestão completa do ciclo acadêmico, desde o cadastro de cursos e disciplinas até o registro de notas e cálculo automático de médias com classificação de aprovação.

## Objetivos

### Objetivo Principal
Desenvolver um sistema robusto e escalável para automatizar o processo de cálculo de médias acadêmicas, eliminando cálculos manuais e reduzindo erros no processo de avaliação.

### Objetivos Específicos
- Facilitar a gestão acadêmica através de uma API REST completa
- Automatizar o cálculo de médias e classificação de aprovação
- Fornecer uma base de dados persistente para histórico acadêmico
- Garantir rastreabilidade e auditoria das operações
- Suportar múltiplos cursos, disciplinas, alunos e professores

## Contexto e Stakeholders

### Stakeholders Principais
- **Administradores Acadêmicos**: Responsáveis pela gestão de cursos e disciplinas
- **Professores**: Responsáveis pelo registro de notas dos alunos
- **Alunos**: Beneficiários finais do sistema (visualização de médias e status)
- **Equipe de TI**: Responsável pela manutenção e evolução do sistema

### Contexto de Uso
O sistema será utilizado em ambiente acadêmico para:
- Cadastro e manutenção de informações acadêmicas
- Registro de avaliações e notas
- Consulta de resultados e status de aprovação
- Geração de relatórios acadêmicos (futuro)

## Escopo Inicial

### Incluso no Escopo (MVP)
- Gestão de **Cursos**: CRUD completo
- Gestão de **Disciplinas**: CRUD com vinculação a cursos e professores
- Gestão de **Alunos**: CRUD completo
- Gestão de **Professores**: CRUD completo
- Gestão de **Matrículas**: Vinculação de alunos a disciplinas
- Registro de **Notas**: 2 notas por aluno em cada disciplina
- **Cálculo automático de médias**: Média aritmética das 2 notas
- **Classificação automática**: Aprovado, Exame ou Reprovado
- **API REST**: Endpoints para todas as operações CRUD
- **Persistência de dados**: Banco SQLite com JPA

### Fora do Escopo (Futuro)
- Sistema de autenticação e autorização
- Interface web (frontend)
- Relatórios e dashboards
- Notificações e alertas
- Integração com sistemas externos
- Histórico de alterações (auditoria detalhada)
- Exportação de dados

## Premissas

1. **Tecnológica**
   - O sistema será desenvolvido em Java 21 com Maven
   - Utilizará Jakarta EE (Jakarta REST, Jakarta Persistence)
   - Banco de dados SQLite para persistência
   - Execução em ambiente DevContainer

2. **Funcional**
   - Cada disciplina possui exatamente 2 notas por aluno
   - O cálculo de média é sempre aritmético simples (soma das 2 notas / 2)
   - As regras de classificação são fixas e não configuráveis
   - Um professor pode lecionar múltiplas disciplinas
   - Um aluno pode estar matriculado em múltiplas disciplinas

3. **Processo**
   - Desenvolvimento seguirá metodologia BMad Framework
   - Será necessário aprovação de Architecture Doc e Test Design antes da implementação
   - Testes automatizados são obrigatórios

## Restrições

### Técnicas
- **Stack obrigatória**: Java 21, Maven, SQLite, JPA (Jakarta Persistence), Jakarta EE, Jakarta REST
- **Ambiente**: Deve executar em DevContainer
- **Banco de dados**: SQLite (sem necessidade de servidor de banco separado)
- **Testes**: Cobertura obrigatória com testes automatizados

### Funcionais
- **Notas**: Valores entre 0.0 e 10.0 (escala padrão brasileira)
- **Média**: Calculada automaticamente, não pode ser editada manualmente
- **Classificação**: Baseada exclusivamente na média calculada
- **Relacionamentos**: 
  - Disciplina deve pertencer a um curso
  - Disciplina deve ter um professor responsável
  - Aluno deve estar matriculado para receber notas

### Processuais
- **Protocolo BMad**: Não é permitido codar sem Architecture Doc e Test Design aprovados na fase P3
- **Fases obrigatórias**: P1 (Discovery) → P2 (Planning) → P3 (Solutioning) → P4 (Implementation)

## Regras de Negócio Fundamentais

### Cálculo de Média
- Cada disciplina possui **2 notas** por aluno
- Média = (Nota 1 + Nota 2) / 2
- Média é calculada automaticamente após registro/atualização de notas

### Classificação de Aprovação
- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0

### Validações Básicas
- Notas devem estar no intervalo [0.0, 10.0]
- Disciplina deve estar vinculada a um curso válido
- Disciplina deve ter um professor responsável
- Aluno deve estar matriculado na disciplina para receber notas

## Próximos Passos

Após a aprovação deste Brief, será criado o **PRD (Product Requirements Document)** com especificação detalhada de requisitos funcionais e não funcionais, incluindo:
- Detalhamento completo de cada funcionalidade
- Especificação de endpoints REST
- Regras de validação detalhadas
- Requisitos não funcionais (performance, segurança, etc.)

---

**Documento criado por**: Product Manager (PM)  
**Data**: Fase P1 - Discovery  
**Status**: ✅ APROVADO
