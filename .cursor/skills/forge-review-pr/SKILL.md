---
name: forge-review-pr
description: "Review de Pull Request. Feedback estruturado, classificado por severidade e acionavel."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Review PR

Como revisar um Pull Request com feedback estruturado, classificado por severidade e acionavel.

## Purpose

Produzir um code review que seja util para o autor: identifica problemas reais,
classifica por impacto e sugere direcao concreta — sem linguagem vaga.

Use quando: revisando qualquer PR antes do merge.
Nao use para: revisao de codigo fora de contexto de PR (use `refactor-function` para isso).

## Inputs

- **PR**: numero e titulo do PR
- **Descricao do PR**: o que esta sendo feito e por que
- **Ticket associado**: (opcional) ID do ticket com criterios de aceite
- **Arquivos modificados**: lista dos arquivos no diff
- **Contexto do projeto**: linguagem, framework, padroes relevantes

## Steps

### 1. Leia a descricao do PR e entenda o objetivo
- O que esta sendo mudado?
- Por que essa mudanca e necessaria?
- Quais sao os criterios de aceite (do ticket ou da descricao)?

### 2. Leia cada arquivo modificado completamente
Nao apenas as linhas modificadas — leia o contexto completo de cada funcao alterada.

### 3. Verifique correctness
- O codigo faz o que a descricao do PR diz?
- Os criterios de aceite sao atendidos?
- Ha casos de erro nao tratados nos paths modificados?
- Ha race conditions ou problemas de concorrencia?

### 4. Verifique security
- Ha input de usuario sendo usado sem sanitizacao?
- Ha secrets hardcoded (chaves de API, senhas, tokens)?
- Ha SQL montado por concatenacao de strings?
- Ha dados sensiveis sendo logados?
- Ha mudanca em permissoes ou autorizacoes?

### 5. Verifique testes
- Ha testes para a logica de negocio nova?
- Os testes existentes ainda passam? (verificar CI)
- Os edge cases estao cobertos?

### 6. Verifique maintainability
- O codigo e legivel sem comentarios?
- Ha duplicacao que deveria ser extraida?
- Os nomes sao descritivos?
- A funcao modificada ficou mais longa que 40 linhas?

### 7. Classifique cada observacao com tier
- **Blocker**: seguranca, data corruption, build quebrado, ausencia de testes criticos
- **Major**: logica incorreta, duplicacao significativa, performance problematica
- **Minor**: nomes ruins, inconsistencia de estilo, funcao longa
- **Suggestion**: alternativa valida, melhoria futura, documentacao adicional

### 8. Escreva o output no formato padrao
Uma observacao por item — nao agrupe problemas diferentes no mesmo comentario.

## Output Format

```markdown
## Code Review — PR #[N]: [titulo]

**Decisao**: [Approve | Request Changes | Reject]

### Resumo
[2-3 frases: qualidade geral, principais pontos, decisao justificada]

---

### Blockers

**[arquivo:linha]** — [titulo do problema]
Problema: [o que esta errado e qual e o impacto]
Sugestao: [alternativa concreta]

---

### Majors

**[arquivo:linha]** — [titulo do problema]
Problema: [o que esta errado e qual e o impacto]
Sugestao: [alternativa concreta]

---

### Minors

**[arquivo:linha]** — [titulo do problema]
Sugestao: [alternativa concreta]

---

### Suggestions

**[arquivo:linha]** — [titulo da sugestao]
[descricao da sugestao com trade-offs]
```

## Example Usage

**Input fornecido**:
- PR #42: "Add payment retry logic"
- Descricao: "Implementa retentativa automatica de pagamentos que falham por timeout"
- Ticket: ENG-891 — Aceite: retry ate 3x com backoff exponencial, log de cada tentativa
- Arquivos: `src/payments/processor.ts`, `src/payments/retry.ts`, `tests/payments/retry.test.ts`

**Output**:
```markdown
## Code Review — PR #42: Add payment retry logic

**Decisao**: Request Changes

### Resumo
A logica de retry esta correta e o backoff exponencial funciona conforme o ticket.
Ha um Blocker de seguranca (dados de cartao sendo logados) e um Major de correctness
(ausencia de tratamento para o caso de retry esgotado). Apos corrigir esses dois pontos,
o PR pode ser aprovado.

---

### Blockers

**src/payments/processor.ts:87** — Dados de cartao expostos em log
Problema: `logger.debug('Payment attempt', { card })` loga o objeto completo do cartao,
incluindo o numero e CVV. Em producao, isso expoe PCI data nos logs.
Sugestao: `logger.debug('Payment attempt', { cardLast4: card.number.slice(-4) })`

---

### Majors

**src/payments/retry.ts:34** — Sem tratamento quando retries se esgotam
Problema: O loop de retry termina silenciosamente se todas as tentativas falharem.
O caller nao recebe erro — recebe `undefined`. Pagamentos falhos podem parecer bem-sucedidos.
Sugestao: Apos esgotar retries, lance `PaymentExhaustedError` com o ultimo erro como causa.

---

### Minors

**src/payments/retry.ts:12** — Variavel `e` no catch nao descreve o que e
Sugestao: Renomeie para `lastError` — comunica que e o erro da ultima tentativa.

**tests/payments/retry.test.ts:45** — Teste nao verifica que retry para no maximo 3x
Sugestao: Adicione `expect(mockCharge).toHaveBeenCalledTimes(3)` no teste de esgotamento.

---

### Suggestions

**src/payments/retry.ts:1** — Considere extrair config de retry para constantes
O `maxRetries: 3` e o fator de backoff `2` estao hardcoded.
Extrair para `PAYMENT_RETRY_CONFIG` facilita ajuste futuro sem buscar no codigo.
```
