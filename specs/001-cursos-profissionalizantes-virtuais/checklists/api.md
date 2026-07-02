# API & Domain Requirements Quality Checklist: Cadastro de Cursos Profissionalizantes Virtuais

**Purpose**: Validar qualidade, clareza e completude dos requisitos de API e domínio antes do `/speckit-plan`
**Created**: 2026-06-26
**Feature**: [spec.md](../spec.md)

**Note**: Checklist gerado por `/speckit-checklist` — testa a qualidade dos requisitos escritos, não o comportamento da implementação.

## Requirement Completeness

- [ ] CHK001 - Estão documentados requisitos para todas as operações v1 (cadastro, listagem, consulta por id, atualização, desativação)? [Completeness, Spec §FR-001–FR-007]
- [ ] CHK002 - Está especificado o requisito de consulta somente leitura ao catálogo de áreas profissionalizantes (seed)? [Completeness, Spec §FR-012]
- [ ] CHK003 - Os campos obrigatórios de entrada no cadastro estão listados de forma completa e fechada? [Completeness, Spec §FR-002]
- [ ] CHK004 - Estão definidos requisitos para os campos retornados na listagem padrão e na consulta por identificador? [Completeness, Spec §FR-005, User Story 2]
- [ ] CHK005 - Está documentado quais atributos são editáveis após o cadastro (incluindo exclusões explícitas)? [Completeness, Spec §FR-006]
- [ ] CHK006 - Existe requisito explícito para inclusão de `tipo_curso = PROFISSIONALIZANTE` nas respostas de consulta? [Completeness, Spec §FR-009]
- [ ] CHK007 - Estão documentados requisitos para validação de FK `instituicao_id` (existência e vínculo obrigatório)? [Completeness, Spec §FR-002, IL-04]
- [ ] CHK008 - Estão documentados requisitos para validação de `area_profissionalizante_id` (existência e área ativa)? [Completeness, Spec §IL-05, FR-012]

## Requirement Clarity

- [ ] CHK009 - A regra de listagem padrão (somente `ATIVO`) está definida sem ambiguidade em relação à consulta por identificador (inclui desativados)? [Clarity, Spec §FR-005, Clarifications]
- [ ] CHK010 - O escopo de unicidade de `nome` (apenas CPV, colisão com legado permitida) está explicitado sem termos ambíguos? [Clarity, Spec §FR-003, IL-01]
- [ ] CHK011 - O limite superior de `carga_horaria` ([1, 9999]) está definido como regra de negócio ou apenas sugestão? [Clarity, Spec §Domínios lógicos]
- [ ] CHK012 - O requisito de "mensagens de erro claras e acionáveis" define critérios mensuráveis (ex.: campo identificado, sem erro genérico)? [Clarity, Spec §FR-010, SC-003]
- [ ] CHK013 - A imutabilidade de `modalidade` após cadastro está definida de forma testável (valores rejeitados, operação bloqueada)? [Clarity, Spec §FR-008, User Story 3]
- [ ] CHK014 - O termo "administrador autenticado" está delimitado o suficiente para v1 (sem perfis adicionais)? [Clarity, Spec §FR-011, Assumptions]
- [ ] CHK015 - Está claro se `nome` é editável ou imutável após cadastro, dado que não consta em FR-006? [Clarity, Gap, Spec §FR-006]

## Requirement Consistency

- [ ] CHK016 - Os cenários de aceitação de User Story 2 estão alinhados com FR-005 (filtro `ATIVO` na listagem vs. histórico por id)? [Consistency, Spec §User Story 2, FR-005]
- [ ] CHK017 - A distinção CPV vs. `Curso` legado é consistente entre FR-003, FR-009, IL-07 e cenário de aceitação P1-4? [Consistency, Spec §FR-003, FR-009, IL-07]
- [ ] CHK018 - Os requisitos de autorização (admin-only v1) são consistentes entre FR-011, Assumptions e cenários Given/When? [Consistency, Spec §FR-011]
- [ ] CHK019 - As regras de área profissionalizante (seed, somente leitura, área ativa) são consistentes entre FR-012, Key Entities e modelo lógico? [Consistency, Spec §FR-012, AREA_PROFISSIONALIZANTE]
- [ ] CHK020 - IL-06 (reativação permitida) é consistente com User Story 3 e operação `reativar()` no diagrama DDD? [Consistency, Spec §IL-06, DDD]

## Acceptance Criteria Quality

- [ ] CHK021 - SC-001 ("menos de 3 minutos") define condições de medição ("condições normais de uso") de forma verificável? [Measurability, Spec §SC-001]
- [ ] CHK022 - SC-003 ("95% das tentativas") especifica universo de amostragem e critério de "mensagem específica"? [Measurability, Spec §SC-003]
- [ ] CHK023 - SC-005 está alinhado e mensurável em relação ao filtro de listagem padrão definido em FR-005? [Measurability, Spec §SC-005, FR-005]
- [ ] CHK024 - Cada FR (FR-001 a FR-012) possui cenário de aceitação ou rastreabilidade testável associada? [Traceability, Spec §Rastreabilidade]

## Scenario Coverage

- [ ] CHK025 - Existem requisitos/cenários para fluxo alternativo de cadastro com nome igual ao catálogo legado (permitido)? [Coverage, Spec §User Story 1 cenário 4]
- [ ] CHK026 - Existem requisitos para estado vazio da listagem (nenhum curso ativo)? [Coverage, Spec §User Story 2 cenário 3]
- [ ] CHK027 - Estão especificados requisitos para fluxo de desativação e ausência subsequente na listagem padrão? [Coverage, Spec §User Story 3, FR-007]
- [ ] CHK028 - Estão especificados requisitos para fluxo de reativação (`DESATIVADO` → `ATIVO`) além da menção em IL-06? [Coverage, Gap, Spec §IL-06]
- [ ] CHK029 - Estão definidos requisitos para tentativa de alteração de modalidade após cadastro (fluxo de exceção)? [Coverage, Spec §User Story 3 cenário 3, FR-008]

## Edge Case Coverage

- [ ] CHK030 - Os requisitos definem comportamento esperado para `carga_horaria` zero, negativa ou acima do limite superior? [Edge Case, Gap, Spec §Edge Cases, FR-004]
- [ ] CHK031 - Os requisitos definem tratamento de `nome` com espaços em branco, trim e caracteres especiais? [Edge Case, Gap, Spec §Edge Cases, VO NomeCurso]
- [ ] CHK032 - Está especificado o comportamento para `instituicao_id` inexistente ou inválido no cadastro? [Edge Case, Gap, Spec §Edge Cases]
- [ ] CHK033 - Está especificado o comportamento para consulta por identificador inexistente? [Edge Case, Gap, Spec §Edge Cases]
- [ ] CHK034 - Está especificado o comportamento para usuário não autenticado (além da menção genérica a 401)? [Edge Case, Spec §Edge Cases, FR-011]
- [ ] CHK035 - Está definido o comportamento ao vincular `area_profissionalizante_id` inativa no cadastro ou edição? [Edge Case, Spec §IL-05, FR-012]

## Non-Functional Requirements

- [ ] CHK036 - Os requisitos de autenticação cobrem todas as operações desta feature de forma explícita? [Security, Spec §FR-011]
- [ ] CHK037 - Existe requisito sobre formato/conteúdo mínimo das respostas de erro (campo, código, mensagem)? [Gap, Spec §FR-010]
- [ ] CHK038 - Estão documentados requisitos de desempenho ou latência para operações de consulta e cadastro além de SC-001? [Gap, Spec §Success Criteria]
- [ ] CHK039 - Estão definidos requisitos de observabilidade (auditoria, logs) para cadastro, edição e desativação? [Gap, Spec §PeriodoAuditoria]

## Dependencies & Assumptions

- [ ] CHK040 - A dependência de `INSTITUIÇÃO` pré-cadastrada está documentada com impacto nos fluxos de cadastro? [Dependency, Spec §Assumptions, FR-002]
- [ ] CHK041 - A assunção de seed de áreas profissionalizantes inclui requisito sobre conteúdo mínimo ou lista de referência? [Gap, Assumption, Spec §FR-012]
- [ ] CHK042 - Está documentado que matrículas, turmas, LMS e CRUD de áreas ficam explicitamente fora de escopo v1? [Assumption, Spec §Assumptions]
- [ ] CHK043 - A estratégia de modelo físico (A vs B) está marcada como decisão de plano sem conflitar com requisitos funcionais? [Assumption, Spec §Decisão modelo físico]

## Ambiguities & Conflicts

- [ ] CHK044 - Permanece algum conflito entre "relação completa" (terminologia antiga) e filtro `ATIVO` na listagem padrão após clarificações? [Conflict, Spec §Clarifications, FR-005]
- [ ] CHK045 - Há ambiguidade entre diagrama DDD (`reativar()`) e ausência de FR explícito para reativação? [Ambiguity, Spec §DDD, FR-007]
- [ ] CHK046 - O requisito de "confirmação de modalidade virtual" no cadastro está definido como campo de input ou invariante implícita? [Ambiguity, Spec §FR-002, User Story 1]

## Notes

- Foco: qualidade dos requisitos de API REST e domínio (CPV, áreas, instituição, auth v1).
- Profundidade: padrão (pré-plan review).
- Audiência: revisor de PR / autor antes de `/speckit-plan`.
- `plan.md` ausente — itens de contrato HTTP detalhado (códigos, payloads) marcados como `[Gap]` para resolução no plano.
