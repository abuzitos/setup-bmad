# Quickstart: Cursos Profissionalizantes Virtuais

Guia de validação end-to-end da feature **após implementação** (`/speckit-implement`).
Contratos: [contracts/cursos-profissionalizantes-virtuais.openapi.yaml](./contracts/cursos-profissionalizantes-virtuais.openapi.yaml)  
Modelo: [data-model.md](./data-model.md)

## Pré-requisitos

- Java 21, Maven 3.9+
- Repositório clonado em `/home/abu/Desenvolvimento/cursor-projeto-dojo2`
- Pelo menos uma **instituição** cadastrada (pré-requisito de cadastro CPV)

## 1. Build e testes automatizados

```bash
cd /home/abu/Desenvolvimento/cursor-projeto-dojo2

# Testes unitários CPV (após implementação)
mvn test -Punit-tests -Dtest="*CursoProfissionalizante*,*AreaProfissionalizante*"

# Integração sem mock (SQLite :memory:)
mvn verify -Pintegration-tests-no-mock -Dit.test="*CursoProfissionalizante*"

# Suite completa antes de merge
mvn verify
```

**Resultado esperado**: todos os testes CPV passam; cenários de risco Alto (unicidade, listagem ATIVO, auth 401) cobertos.

## 2. Subir a aplicação

```bash
mvn package -DskipTests
java -jar target/cursor-projeto-dojo2-*.jar
```

Base URL: `http://localhost:8080/api`  
Swagger UI: `http://localhost:8080/api/swagger-ui`  
OpenAPI: `http://localhost:8080/api/openapi`

Credenciais v1: `admin` / `123456` (Basic Auth).

## 3. Preparar dados de apoio

### 3.1 Verificar áreas profissionalizantes (seed)

```bash
curl -s -u admin:123456 \
  http://localhost:8080/api/areas-profissionalizantes | jq
```

**Esperado**: lista com áreas ativas (ex.: TEC, SAU, GES, SER). Lista vazia indica falha no seed.

### 3.2 Criar instituição (se necessário)

```bash
curl -s -u admin:123456 -X POST http://localhost:8080/api/instituicoes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Faculdade Demo CPV",
    "endereco": "Rua Teste, 1",
    "telefone1": "11 99999-0000",
    "cep": 1234567
  }' | jq
```

Anote o `id` retornado como `INSTITUICAO_ID` e um `areaProfissionalizanteId` da listagem de áreas.

## 4. Cenários de validação manual

Substitua `INSTITUICAO_ID` e `AREA_ID` pelos valores reais.

### C1 — Cadastro happy path (US1, SC-002)

```bash
curl -s -u admin:123456 -X POST \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais \
  -H "Content-Type: application/json" \
  -d "{
    \"instituicaoId\": INSTITUICAO_ID,
    \"nome\": \"Técnico em Redes Virtuais\",
    \"areaProfissionalizanteId\": AREA_ID,
    \"cargaHoraria\": 320,
    \"descricao\": \"Formação 100% remota em redes.\"
  }" | jq
```

**Esperado**: HTTP 201; `modalidade` = `VIRTUAL`; `tipoCurso` = `PROFISSIONALIZANTE`; `status` = `ATIVO`.

### C2 — Nome duplicado no escopo CPV (FR-003)

Repetir C1 com o mesmo `nome`.

**Esperado**: HTTP 400; `codigo` relacionado a nome duplicado.

### C3 — Nome igual ao catálogo legado `Curso` (FR-003, US1 cenário 4)

```bash
# Criar curso legado
curl -s -u admin:123456 -X POST http://localhost:8080/api/cursos \
  -H "Content-Type: application/json" \
  -d '{"nome": "Curso Legado Compartilhado"}' | jq

# Cadastrar CPV com mesmo nome
curl -s -u admin:123456 -X POST \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais \
  -H "Content-Type: application/json" \
  -d "{
    \"instituicaoId\": INSTITUICAO_ID,
    \"nome\": \"Curso Legado Compartilhado\",
    \"areaProfissionalizanteId\": AREA_ID,
    \"cargaHoraria\": 200
  }" | jq
```

**Esperado**: HTTP 201 no CPV (colisão com legado permitida).

### C4 — Listagem somente ATIVO (FR-005, SC-005)

```bash
curl -s -u admin:123456 \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais | jq
```

**Esperado**: apenas cursos com `status` = `ATIVO`.

### C5 — Desativação e ausência na listagem (US3, FR-007)

```bash
CPV_ID=<id do curso criado em C1>

curl -s -u admin:123456 -X PATCH \
  "http://localhost:8080/api/cursos-profissionalizantes-virtuais/${CPV_ID}/desativar" | jq

curl -s -u admin:123456 \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais | jq
```

**Esperado**: desativar retorna `status` = `DESATIVADO`; listagem não inclui o curso.

### C6 — Consulta por id inclui desativado (FR-005)

```bash
curl -s -u admin:123456 \
  "http://localhost:8080/api/cursos-profissionalizantes-virtuais/${CPV_ID}" | jq
```

**Esperado**: HTTP 200 com `status` = `DESATIVADO`.

### C7 — Reativação (IL-06)

```bash
curl -s -u admin:123456 -X PATCH \
  "http://localhost:8080/api/cursos-profissionalizantes-virtuais/${CPV_ID}/reativar" | jq
```

**Esperado**: `status` = `ATIVO`; curso volta na listagem padrão.

### C8 — Não autenticado (FR-011)

```bash
curl -s -o /dev/null -w "%{http_code}" \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais
```

**Esperado**: HTTP 401.

### C9 — Validação carga horária inválida (FR-004)

```bash
curl -s -u admin:123456 -X POST \
  http://localhost:8080/api/cursos-profissionalizantes-virtuais \
  -H "Content-Type: application/json" \
  -d "{
    \"instituicaoId\": INSTITUICAO_ID,
    \"nome\": \"Curso Carga Inválida\",
    \"areaProfissionalizanteId\": AREA_ID,
    \"cargaHoraria\": 0
  }" | jq
```

**Esperado**: HTTP 400 com mensagem sobre carga horária.

## 5. Critérios de sucesso da feature

| Critério | Como verificar |
|----------|----------------|
| SC-001 | Cadastro C1 concluído em < 3 min (manual) |
| SC-002 | C1 retorna `tipoCurso` e `modalidade` corretos |
| SC-003 | C2, C9 retornam mensagens específicas (não genéricas) |
| SC-004 | Listagem ou GET por id localiza qualquer CPV |
| SC-005 | C5 confirma ausência na listagem após desativar |

## 6. Referências

- Spec: [spec.md](./spec.md)
- Plano: [plan.md](./plan.md)
- Testes: `src/test/java/com/faculdade/media/README.md`
