<!--
Sync Impact Report
- Version change: 1.1.0 → 1.2.0
- Modified principles:
  - III. Test-First: expanded with risk-prioritized scenarios
  - IV. Testes em Camadas → IV. Testes Orientados a Risco (redefined)
  - V. Cobertura e Qualidade Mínima → V. Cobertura Proporcional ao Risco (redefined)
- Added sections: Risk tiers table in principle IV; risk assessment in Fluxo de Desenvolvimento
- Removed sections: Fixed per-layer coverage table (replaced by risk tiers)
- Templates requiring updates:
  - .specify/templates/plan-template.md ✅ updated (risk assessment gate + section)
  - .specify/templates/spec-template.md ✅ no changes required
  - .specify/templates/tasks-template.md ✅ updated (risk-driven test selection)
  - .specify/templates/checklist-template.md ✅ no changes required
  - README.md ✅ no changes required
- Follow-up TODOs: None
-->

# cursor-projeto-dojo2 Constitution

## Core Principles

### I. Arquitetura em Camadas

Toda funcionalidade MUST seguir a separação **Controller → Service → Repository →
Domain**, com DTOs na fronteira HTTP e entidades JPA no domínio.

- Controllers MUST ser finos: roteamento, validação de entrada e mapeamento de
  resposta HTTP apenas.
- Services MUST conter regras de negócio e orquestração; MUST NOT acessar HTTP
  diretamente.
- Repositories MUST encapsular persistência; MUST NOT conter regras de negócio.
- Exceções de domínio MUST ser traduzidas para respostas HTTP consistentes via
  `ExceptionHandler`.

**Rationale**: Camadas explícitas facilitam testes isolados, evolução independente
e alinhamento com a estrutura existente em `com.faculdade.media`.

### II. API REST Contratada (OpenAPI)

Endpoints MUST ser expostos sob `/api`, produzir/consumir JSON e documentar
contratos via anotações MicroProfile OpenAPI.

- Novos recursos MUST incluir `@Tag`, `@Operation` e `@APIResponse` nos controllers.
- O contrato MUST permanecer sincronizado com `src/main/resources/META-INF/openapi.yaml`.
- Rotas protegidas MUST respeitar o filtro de autenticação existente; endpoints
  públicos MUST ser explicitamente justificados no plano da feature.
- Respostas de erro MUST usar `ErroDTO` ou formato equivalente já estabelecido.

**Rationale**: Contratos explícitos permitem testes funcionais, Swagger UI e
integração previsível para consumidores da API.

### III. Test-First (NON-NEGOTIABLE)

TDD é obrigatório, **priorizando cenários de maior risco**: **testes escritos →
testes falham (Red) → implementação mínima (Green) → refactor**. Nenhum código de
produção para um cenário MUST ser escrito antes do teste correspondente existir e
falhar.

- Para cada user story, tarefas de teste MUST preceder tarefas de implementação
  em `tasks.md`, ordenadas do **maior para o menor risco**.
- O plano MUST identificar riscos antes de definir testes (ver Princípio IV).
- Testes MUST falhar por motivo correto (comportamento ausente, não erro de
  compilação evitável).
- Ciclo Red-Green-Refactor MUST ser aplicado por unidade de trabalho (método,
  endpoint ou regra de negócio).
- Exceções MUST ser documentadas no Complexity Tracking com justificativa explícita.

**Rationale**: Test-first com priorização por risco concentra esforço onde falhas
teriam maior impacto, sem sacrificar a disciplina TDD.

### IV. Testes Orientados a Risco

A estratégia de testes MUST ser definida por **análise de risco**, não por
cobertura indiscriminada. Cada feature MUST classificar cenários antes de escolher
tipo e profundidade dos testes.

**Dimensões de risco** (avaliar por cenário ou user story):

| Dimensão | Exemplos neste projeto |
|----------|------------------------|
| Impacto de negócio | Cálculo de média, classificação, aprovação/reprovação |
| Integridade de dados | FK, transações, persistência JPA, exclusões em cascata |
| Segurança | Autenticação, autorização, exposição de dados sensíveis |
| Contrato externo | Endpoints REST novos ou alterados, códigos HTTP, payloads |
| Complexidade | Lógica condicional, múltiplas dependências, edge cases |

**Níveis e exigências**:

| Nível | Critério | Unitário | Integração | Funcional | Selenium |
|-------|----------|----------|------------|-----------|----------|
| **Alto** | Impacto crítico ou falha silenciosa provável | MUST (TDD) | MUST | MUST | Se UI |
| **Médio** | Falha visível, recuperável ou CRUD com validação | MUST | SHOULD se persistência | SHOULD se endpoint novo | Se UI |
| **Baixo** | Boilerplate, getter/setter, mapeamento trivial | SHOULD | MAY omitir com justificativa | MAY omitir | Não aplicável |

- Localização e nomenclatura MUST seguir
  `src/test/java/com/faculdade/media/README.md`.
- Testes MUST ser independentes, determinísticos e executáveis via profiles Maven.
- **Não testar por volume**: preferir poucos testes de alto valor sobre muitos
  testes de baixo risco.

**Rationale**: Esforço de teste proporcional ao risco evita falsa segurança e
garante profundidade onde regressões doem mais.

### V. Cobertura Proporcional ao Risco

Metas JaCoCo são **diretrizes por nível de risco**, não metas cegas para todo
código:

| Nível de risco | Cobertura alvo (código no escopo) |
|----------------|----------------------------------|
| Alto | ≥ 90% (domain/services); endpoints críticos 100% dos cenários mapeados |
| Médio | ≥ 80% (services/repositories); contrato HTTP dos endpoints afetados |
| Baixo | Sem meta fixa; omitir MUST ser justificado no plano |

- Cobertura global por camada (domain 90%, services 80%, etc.) permanece como
  **alerta de revisão**, não como motivo para testes de baixo valor.
- Violações MUST ser registradas no Complexity Tracking com risco associado e
  alternativa rejeitada.
- Cenários de falha (4xx, 5xx, validação, exceções de domínio) MUST ser
  priorizados conforme impacto, não apenas happy path.

**Rationale**: Percentuais sem contexto de risco incentivam testes frágeis;
risco + cobertura alinha qualidade com impacto real.

### VI. Simplicidade (YAGNI)

Preferir a solução mais simples que atenda ao requisito.

- MUST NOT introduzir frameworks, camadas ou abstrações além do stack existente
  (Java 21, Jersey, Hibernate, SQLite) sem justificativa no plano.
- MUST NOT duplicar lógica entre services; extrair apenas após segunda ocorrência
  comprovada.
- Dependências Maven MUST ser adicionadas somente quando indispensáveis.
- TDD MUST guiar o design: implementar apenas o necessário para o teste passar.
- MUST NOT adicionar testes de baixo risco só para inflar cobertura.

**Rationale**: Test-first + YAGNI + foco em risco evitam complexidade e testes
cosméticos.

## Restrições Tecnológicas

Stack fixo para implementação:

- **Linguagem**: Java 21
- **Runtime HTTP**: Jersey (Jakarta REST 3.x) embarcado via Grizzly
- **Persistência**: JPA (Hibernate 6) com SQLite (dialect community)
- **Validação**: Jakarta Validation
- **Documentação API**: MicroProfile OpenAPI + Swagger UI
- **Build/Testes**: Maven; JUnit 5, Mockito, AssertJ, REST Assured, Selenium
- **Empacotamento**: JAR executável (`java -jar target/cursor-projeto-dojo2-*.jar`)

Desvios MUST ser aprovados via emenda à constituição ou justificativa explícita no
Complexity Tracking do plano.

## Fluxo de Desenvolvimento

Features MUST seguir o workflow Spec Kit:

1. **Specify** (`/speckit-specify`) — user stories, critérios mensuráveis e edge
   cases que implicam risco.
2. **Plan** (`/speckit-plan`) — **matriz de riscos** por story/cenário,
   Constitution Check e estratégia de testes derivada dos níveis Alto/Médio/Baixo.
3. **Tasks** (`/speckit-tasks`) — testes antes de implementação, ordenados por
   risco decrescente; omitir camadas só com justificativa no plano.
4. **Implement** (`/speckit-implement`) — Red-Green-Refactor por cenário de risco.

Regras operacionais:

- Cada user story MUST ser implementável e testável de forma independente (MVP
  incremental).
- Ordem por story: **identificar riscos → escrever testes de alto risco → Red →
  Green → expandir para riscos médios → refactor**.
- `mvn test` MUST passar antes de merge; `mvn verify` MUST passar quando a
  feature incluir testes de integração ou funcionais exigidos pela matriz de riscos.
- Documentação MUST ser atualizada em `README.md` quando URLs, autenticação ou
  comandos mudarem.

## Governance

Esta constituição supersede práticas ad hoc e MUST ser verificada em todo plano
de feature (Constitution Check) e análise (`/speckit-analyze`).

**Procedimento de emenda**:

1. Propor alteração via `/speckit-constitution` com justificativa e impacto.
2. Incrementar versão conforme semver de governança (MAJOR: remoção/redefinição
   de princípio; MINOR: novo princípio ou expansão material; PATCH: clarificação).
3. Propagar mudanças aos templates em `.specify/templates/` e documentação afetada.
4. Registrar data de emenda e Sync Impact Report no topo do arquivo.

**Conformidade**:

- Planos MUST incluir matriz de riscos e estratégia de testes derivada.
- Tarefas MUST referenciar nível de risco (Alto/Médio/Baixo) quando definirem testes.
- Violações de Test-First ou omissão de testes de risco Alto são CRITICAL.
- Orientação de runtime: `README.md` e `src/test/java/com/faculdade/media/README.md`.

**Version**: 1.2.0 | **Ratified**: 2026-06-26 | **Last Amended**: 2026-06-26
