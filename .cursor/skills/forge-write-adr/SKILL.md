---
name: forge-write-adr
description: "Escrita de ADR (Architecture Decision Record). Documentacao de decisoes arquiteturais."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

# Skill: Write ADR

Como escrever uma Architecture Decision Record (ADR) clara e util.

## Purpose

Documentar uma decisao arquitetural significativa de forma que o contexto, as alternativas
e as consequencias sejam compreensiveis por quem nao estava presente na discussao.

Use quando:
- Voce escolhe entre duas ou mais abordagens tecnicas igualmente validas
- Uma decisao tem consequencias dificeis de reverter
- A decisao vai afetar como outros desenvolvedores trabalham
- Voce precisa justificar uma escolha nao-obvia para stakeholders

Nao use para:
- Decisoes triviais com solucao obvia
- Preferencias pessoais de estilo sem impacto arquitetural

## Inputs

- **Decisao**: o que foi decidido em uma frase
- **Contexto**: qual problema estava sendo resolvido e por que era necessario decidir agora
- **Alternativas**: pelo menos 2 opcoes que foram consideradas (incluindo a escolhida)
- **Criterios de decisao**: o que pesou na escolha (performance, custo, expertise do time, etc.)
- **Status**: Proposed / Accepted / Deprecated / Superseded

## Steps

### 1. Escreva o titulo
Formato: `[ID] [Verbo no passado] [o que]`
Exemplos:
- `ADR-001 Adotamos PostgreSQL como banco de dados principal`
- `ADR-015 Migramos autenticacao para JWT`
- `ADR-023 Separamos o servico de notificacoes em microsservico`

### 2. Defina o status
- **Proposed**: em discussao, ainda nao implementado
- **Accepted**: decisao tomada e em implementacao
- **Deprecated**: foi a decisao certa, mas o contexto mudou
- **Superseded by ADR-XXX**: substituida por decisao mais recente

### 3. Descreva o contexto
Responda: Por que essa decisao precisava ser tomada agora?
- Qual era o estado anterior do sistema?
- Qual problema ou oportunidade forcou a decisao?
- Quais restricoes existiam? (prazo, orcamento, expertise, sistema legado)

### 4. Descreva a decisao
Uma frase clara: "Decidimos [X] porque [Y]."
Nao explique o que vai ser feito — explique o que foi decidido.

### 5. Liste as alternativas consideradas
Para cada alternativa (incluindo a escolhida):
- Nome da alternativa
- Como ela resolve o problema
- Por que foi escolhida ou descartada

### 6. Descreva as consequencias
Seja honesto sobre os trade-offs:
- Positivas: o que melhora com essa decisao
- Negativas: o que piora ou fica mais dificil
- Neutras: o que muda sem ser melhor ou pior

### 7. Revise antes de commitar
- O titulo deixa claro o que foi decidido sem ler o resto?
- Alguem que nao estava na reuniao entenderia o contexto?
- As consequencias negativas estao documentadas honestamente?

## Output Format

```markdown
# ADR-[ID]: [Titulo]

**Status**: [Proposed | Accepted | Deprecated | Superseded by ADR-XXX]
**Data**: [YYYY-MM-DD]
**Autores**: [nomes ou times]

## Contexto

[O que estava acontecendo no sistema/time que tornou essa decisao necessaria.
Qual era o problema? Quais eram as restricoes? Por que agora?]

## Decisao

[Uma frase direta: "Decidimos [X] porque [Y]."]

## Alternativas Consideradas

### Opcao 1: [nome] — Escolhida
[Como resolve o problema]
**Pros**: [lista]
**Contras**: [lista]

### Opcao 2: [nome]
[Como resolve o problema]
**Pros**: [lista]
**Contras**: [lista — por que foi descartada]

## Consequencias

### Positivas
- [consequencia positiva 1]
- [consequencia positiva 2]

### Negativas
- [consequencia negativa 1 — seja honesto]
- [consequencia negativa 2]

### Neutras
- [mudanca que nao e melhor nem pior]
```

## Example Usage

**Input fornecido**:
- Decisao: usar Redis para cache de sessoes em vez de armazenar no banco PostgreSQL
- Contexto: sistema com 50k usuarios ativos, queries de validacao de sessao estao causando 30% da carga do banco
- Alternativas: Redis, Memcached, cache em memoria no processo, manter no PostgreSQL com indice
- Criterios: latencia <5ms, custo operacional, expertise do time (ja usa Redis para filas)
- Status: Accepted

**Output**:
```markdown
# ADR-007: Migramos sessoes de usuario para Redis

**Status**: Accepted
**Data**: 2024-03-15
**Autores**: Time de Platform

## Contexto

Com 50k usuarios ativos simultaneos, as queries de validacao de sessao no PostgreSQL
representam 30% da carga total do banco. Cada request autenticado executa uma query
SELECT na tabela `sessions` — tabela sem particionamento e com crescimento linear.
O banco esta com CPU media de 70%, deixando pouca margem para picos.

## Decisao

Decidimos armazenar sessoes no Redis porque ja operamos Redis para filas de jobs,
o time tem expertise na ferramenta e a latencia de leitura (<1ms local) resolve o
problema de carga sem adicionar nova infraestrutura.

## Alternativas Consideradas

### Opcao 1: Redis — Escolhida
Cache dedicado com TTL nativo para expiracao de sessoes.
**Pros**: Latencia sub-milissegundo, TTL nativo, ja na infraestrutura, expertise do time
**Contras**: Mais um componente stateful para operar, sessoes perdidas em restart sem persistencia

### Opcao 2: Memcached
Cache simples e rapido sem estruturas de dados complexas.
**Pros**: Extremamente simples, alta performance
**Contras**: Nao esta na infraestrutura atual, sem persistencia, sem estruturas de dados

### Opcao 3: Manter no PostgreSQL com otimizacao
Adicionar indice composto e particionamento por data de expiracao.
**Pros**: Sem nova infraestrutura, sessoes persistidas em disco
**Contras**: Nao resolve o problema de latencia, apenas adia o crescimento do problema

## Consequencias

### Positivas
- Queries de sessao saem do PostgreSQL: carga esperada cai de 70% para ~45%
- Latencia de autenticacao: de ~15ms para <2ms
- TTL nativo do Redis elimina job de limpeza de sessoes expiradas

### Negativas
- Sessoes sao perdidas em caso de restart do Redis sem AOF habilitado
- Adiciona dependencia de disponibilidade do Redis para todas as requisicoes autenticadas
- Necessario habilitar Redis AOF ou replicacao para durabilidade

### Neutras
- Sessoes nao sao mais consultaveis via SQL (impacto: queries de admin precisam ir ao Redis)
- Escala de sessoes agora independente do banco (pode ser positivo ou negativo conforme crescimento)
```
