---
name: forge-setup-env-gcp-dyna
description: "Verifica e configura gcloud, kubectl e dtctl com projetos GCP, clusters GKE e ambientes Dynatrace."
user-invocable: true
risk: safe
source: "Forge Kit v2"
---

## Purpose

Configurar o ambiente local de um desenvolvedor para operar workspaces GCP com Kubernetes e Dynatrace. A skill verifica cada CLI, instala se necessario, configura projetos/clusters/ambientes e valida conectividade.

Usar quando: onboarding de novo dev, setup de maquina nova, ou troubleshooting de ambiente.

## Inputs

- **Projetos GCP**: Nome da configuracao, Project ID, conta GCP e zona padrao (por projeto)
- **Clusters GKE**: Nome do cluster, zona e Project ID (por cluster)
- **Dynatrace**: URL do ambiente, nome do contexto, OAuth client ID e secret

## Steps

### 1. Detectar SO

```bash
uname -s 2>/dev/null || echo "Windows"
```

### 2. Verificar e instalar gcloud CLI

```bash
gcloud version
```

Se nao instalado:

| SO | Comando |
|----|---------|
| macOS | `brew install google-cloud-sdk` |
| Windows (winget) | `winget install Google.CloudSDK` |
| Windows (choco) | `choco install gcloudsdk` |

Verificar plugin GKE auth:
```bash
gke-gcloud-auth-plugin --version
# Se nao instalado:
gcloud components install gke-gcloud-auth-plugin
```

Autenticar — pedir ao usuario para rodar interativamente:
```
! gcloud auth login
```

### 3. Configurar projetos GCP

Perguntar ao usuario por cada projeto:
- **Nome da configuracao** (ex: `meu-projeto-hmg`)
- **Project ID** (ex: `meu-projeto-123`)
- **Conta GCP** (ex: `usuario@empresa.com.br`)
- **Zona padrao** (ex: `southamerica-east1-a`)

Criar configuracao:
```bash
gcloud config configurations create <NOME_CONFIG> 2>/dev/null
gcloud config set project <PROJECT_ID> --configuration=<NOME_CONFIG>
gcloud config set account <CONTA> --configuration=<NOME_CONFIG>
gcloud config set compute/zone <ZONA> --configuration=<NOME_CONFIG>
```

Verificar:
```bash
gcloud config configurations list
```

### 4. Verificar e instalar kubectl

```bash
kubectl version --client
```

Se nao instalado:

| SO | Comando |
|----|---------|
| macOS | `brew install kubectl` |
| Windows (winget) | `winget install Kubernetes.kubectl` |
| Windows (choco) | `choco install kubernetes-cli` |

### 5. Configurar credenciais GKE

Perguntar ao usuario por cada cluster:
- **Nome do cluster** (ex: `meu-cluster`)
- **Zona** (ex: `southamerica-east1-a`)
- **Project ID** (ex: `meu-projeto-123`)

Obter credenciais:
```bash
gcloud container clusters get-credentials <CLUSTER> \
  --zone=<ZONA> --project=<PROJECT_ID>
```

Verificar:
```bash
kubectl config get-contexts
```

### 6. Verificar e instalar dtctl (Dynatrace CLI)

```bash
dtctl version
```

Se nao instalado:

| SO | Comando |
|----|---------|
| macOS | `brew tap dynatrace/dynatrace && brew install dtctl` |
| Windows (winget) | `winget install Dynatrace.dtctl` |

Configurar — pedir ao usuario:
- **URL do ambiente Dynatrace** (ex: `https://xyz.apps.dynatrace.com/`)
- **Nome do contexto** (ex: `meu-env`)
- Credenciais OAuth (rodar interativamente):

```
! dtctl config set-context <NOME_CONTEXTO> --environment <URL_AMBIENTE>
! dtctl config set-credentials <NOME_CONTEXTO>-oauth --oauth-client-id <CLIENT_ID> --oauth-client-secret <CLIENT_SECRET>
```

Verificar:
```bash
dtctl config view
dtctl get environments
```

### 7. Configurar permissoes Claude Code

Verificar se `.claude/settings.local.json` tem as permissoes para as CLIs:

```json
{
  "permissions": {
    "allow": [
      "Bash(gcloud projects:*)",
      "Bash(gcloud logging:*)",
      "Bash(gcloud container:*)",
      "Bash(kubectl:*)",
      "Bash(dtctl:*)"
    ]
  }
}
```

Se nao existir, perguntar ao usuario se deseja criar/atualizar.

### 8. Teste de conectividade

Validar todos os projetos, clusters e ambientes configurados:

```bash
for proj in $(gcloud config configurations list --format='value(properties.core.project)' 2>/dev/null); do
  echo "=== $proj ==="
  gcloud logging read 'resource.type="k8s_container"' \
    --project=$proj --limit=1 --format='value(timestamp)' 2>&1 | head -1
done

for ctx in $(kubectl config get-contexts -o name); do
  echo "=== $ctx ==="
  kubectl --context=$ctx get ns --no-headers 2>&1 | head -3
done

dtctl get environments 2>&1 | head -3
```

## Output Format

Tabela de status de cada componente:

| Componente | Status | Versao | Observacao |
|-----------|--------|--------|------------|
| gcloud | OK/FALHA | x.y.z | |
| gke-gcloud-auth-plugin | OK/FALHA | x.y.z | |
| kubectl | OK/FALHA | x.y.z | |
| dtctl | OK/FALHA | x.y.z | |
| Config: [nome] | OK/FALHA | | por config criada |
| Cluster: [nome] | OK/FALHA | | por cluster configurado |
| Dynatrace | OK/FALHA | | |

## Example Usage

```
Dev: "Preciso configurar meu ambiente para acessar o GKE do projeto certisign-hmg e o Dynatrace"

Agent executa a skill:
1. Detecta macOS arm64
2. gcloud ja instalado (v487.0.0) -> OK
3. Pergunta dados do projeto -> cria config "certisign-hmg"
4. kubectl ja instalado (v1.31) -> OK
5. Pergunta cluster -> configura credenciais GKE
6. dtctl nao instalado -> instala via brew
7. Pergunta URL Dynatrace -> configura contexto
8. Valida conectividade em todos os componentes
9. Reporta tabela de status
```
