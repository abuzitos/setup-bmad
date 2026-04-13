---
name: forge-refactor-function
description: "Refatoracao segura de funcoes. Como refatorar preservando comportamento e melhorando legibilidade."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Refactor Function

Como refatorar uma funcao com seguranca, preservando comportamento e melhorando legibilidade.

## Purpose

Refatorar uma funcao existente que esta dificil de entender, testar ou modificar.
Use quando: funcao longa (>30 linhas), multiplas responsabilidades, nomes confusos, ou ausencia de testes.
Nao use quando: a funcao tem bugs — corrija bugs primeiro, refatore depois.

## Inputs

- **Arquivo e localizacao**: caminho do arquivo e nome da funcao a refatorar
- **Motivo da refatoracao**: o que esta dificil (legibilidade, testabilidade, duplicacao)
- **Testes existentes**: ha testes cobrindo esta funcao? (sim/nao/parcial)
- **Restricoes**: ha dependencias externas que limitam a refatoracao?

## Steps

### 1. Leia e entenda a funcao atual
- Leia o arquivo completo, nao apenas a funcao
- Identifique: o que a funcao faz? Quais sao seus inputs e outputs?
- Mapeie os callers: quem chama esta funcao e com quais argumentos?

### 2. Identifique o problema principal
Escolha um dos padroes problematicos:
- **Funcao longa**: >30 linhas fazendo coisas distintas → extraia subfuncoes
- **Multiplas responsabilidades**: mistura IO com logica → separe camadas
- **Nomes genericos**: `data`, `result`, `temp` → nomeie pelo significado de negocio
- **Logica duplicada**: codigo repetido → extraia e centralize
- **Parametros demais**: >4 parametros → agrupe em objeto

### 3. Escreva testes antes de refatorar (se nao existirem)
- Identifique o comportamento observavel atual (outputs para cada input)
- Escreva testes que documentam esse comportamento
- Confirme que os testes passam antes de mudar qualquer coisa

### 4. Refatore em passos pequenos
- Um passo por vez — nao refatore tudo de uma vez
- Apos cada passo: confirme que os testes ainda passam
- Se os testes quebrarem: desfaca o ultimo passo e tente diferente

### 5. Aplique as mudancas
**Para funcao longa**: extraia blocos coesos em funcoes privadas com nomes descritivos
**Para multiplas responsabilidades**: separe a funcao em funcoes menores com responsabilidade unica
**Para nomes genericos**: renomeie — use find/replace no arquivo para consistencia
**Para logica duplicada**: crie uma funcao utilitaria e substitua as duplicatas

### 6. Valide o resultado
- Todos os testes passam?
- A funcao original ficou mais curta e mais legivel?
- Os callers precisam de mudanca? (se sim, atualize-os)
- Ha algo que deveria ser documentado em `docs/design-decisions.md`?

## Output Format

```
## Refatoracao: [nome da funcao] em [arquivo]

### Problema identificado
[descricao do problema principal]

### Mudancas realizadas
- [mudanca 1 com justificativa]
- [mudanca 2 com justificativa]

### Testes
- [N] testes existentes: todos passando
- [N] testes novos adicionados

### Resultado
Antes: [N] linhas, [problema]
Depois: [N] linhas divididas em [N] funcoes
```

## Example Usage

**Input fornecido**:
- Arquivo: `src/payments/processor.ts`, funcao `processPayment`
- Motivo: funcao com 80 linhas que valida dados, chama API externa, salva no banco e envia email
- Testes existentes: parcial (apenas happy path)
- Restricoes: a assinatura da funcao e publica e usada em 3 lugares

**Execucao**:
1. Le `src/payments/processor.ts` completo
2. Identifica problema: multiplas responsabilidades (validacao + IO + notificacao)
3. Escreve testes para: pagamento valido, cartao invalido, falha de API, falha de banco
4. Extrai: `validatePaymentData()`, `chargeCard()`, `saveTransaction()`, `sendConfirmationEmail()`
5. `processPayment` passa a orquestrar as 4 funcoes — 15 linhas vs 80 originais
6. Confirma: todos os 4 testes novos + testes existentes passam

**Output**:
```
## Refatoracao: processPayment em src/payments/processor.ts

### Problema identificado
Funcao com 80 linhas combinando 4 responsabilidades distintas: validacao,
cobranca, persistencia e notificacao — impossivel testar isoladamente.

### Mudancas realizadas
- Extraida `validatePaymentData(data)`: validacao de campos obrigatorios e formato
- Extraida `chargeCard(card, amount)`: chamada a API de pagamento
- Extraida `saveTransaction(paymentId, data)`: persistencia no banco
- Extraida `sendConfirmationEmail(email, transactionId)`: envio de notificacao
- `processPayment` agora orquestra as 4 funcoes em sequencia

### Testes
- 2 testes existentes: todos passando
- 4 testes novos adicionados (um por funcao extraida)

### Resultado
Antes: 80 linhas, 1 funcao com 4 responsabilidades
Depois: 15 linhas na funcao principal + 4 funcoes de 10-20 linhas cada
```
