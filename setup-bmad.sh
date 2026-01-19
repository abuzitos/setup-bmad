#!/bin/bash

# --- Configurações de Cores ---
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}=======================================${NC}"
echo -e "${BLUE}BMAD METHOD - SETUP AUTOMATIZADO${NC}"
echo -e "${BLUE}=======================================${NC}"

echo "Escolha o tipo de inicialização:"
echo "1) Projeto Novo (Greenfield)"
echo "2) Projeto Existente (Legacy Sync)"
read -p "Opção: " OPTION

# --- 1. Criação da Estrutura de Pastas ---
echo -e "${GREEN}[1/4] Criando pastas...${NC}"
mkdir -p _bmad/bmm/agents
mkdir -p _bmad/bmm/workflows
mkdir -p _bmad-output/epics
mkdir -p src/main/java
mkdir -p src/test/java

# --- 2. Criação dos Agentes (YAML) ---
echo -e "${GREEN}[2/4] Configurando Agentes...${NC}"

cat <<EOF > _bmad/bmm/agents/pm.yaml
agent_name: Product Manager (PM)
role_description: Responsável por transformar a visão bruta em requisitos.
active_phases: [PHASE_1, PHASE_2]
responsibilities:
- Definir regras de cálculo de média escolar.
- Criar Brief e PRD focados em persistência SQLite.
EOF

cat <<EOF > _bmad/bmm/agents/architect.yaml
agent_name: Software Architect
role_description: Responsável pelo design técnico e stack Java/SQLite.
active_phases: [PHASE_3]
responsibilities:
- Definir estrutura JDBC puro e Singleton para SQLite.
- Garantir que o código seja testável e modular.
EOF

cat <<EOF > _bmad/bmm/agents/developer.yaml
agent_name: Java Developer (DEV)
role_description: Implementador do código Java.
active_phases: [PHASE_4]
responsibilities:
- Criar as classes Java para cálculo e persistência.
- Gerar mensagens de Pull Request detalhadas.
EOF

cat <<EOF > _bmad/bmm/agents/tea.yaml
agent_name: Test Engineering Agent (TEA)
role_description: Guardião da qualidade e automação de testes.
active_phases: [PHASE_3, PHASE_4]
responsibilities:
- Validar lógica de médias e integração com SQLite.
- Realizar Code Review rigoroso.
EOF

# --- 3. Governança e Status ---
echo -e "${GREEN}[3/4] Criando Governança e Status...${NC}"

cat <<EOF > .cursorrules
# PROTOCOLO BMAD
- Siga as fases do diagrama Standard Greenfield.
- P1 (Discovery) -> P2 (Planning) -> P3 (Solutioning) -> P4 (Implementation).
- Proibido codar sem Architecture Doc e Test Design aprovados na P3.
- Stack: Java 17+, SQLite, JDBC puro.
EOF

if [ "$OPTION" == "1" ]; then
    CURRENT_PHASE="PHASE_1_DISCOVERY"
    STATUS="IN_PROGRESS"
else
    CURRENT_PHASE="PHASE_1_DISCOVERY" # Inicia P1 para ler o legado
    STATUS="LEGACY_SYNC"
fi

cat <<EOF > _bmad-output/sprint-status.yaml
project_name: Sistema de Calculo de Medias Java-SQLite
current_phase: $CURRENT_PHASE
phase_status:
P1_Discovery: $STATUS
P2_Planning: PENDING
P3_Solutioning: PENDING
P4_Implementation: PENDING
EOF

# --- 4. Workflow Main Process ---
cat <<EOF > _bmad/bmm/workflows/main-process.yaml
workflow: Standard-Greenfield
phases: [P1_Discovery, P2_Planning, P3_Solutioning, P4_Implementation]
EOF

echo -e "${BLUE}=======================================${NC}"
echo -e "${GREEN}Estrutura BMAD criada com sucesso!${NC}"
echo -e "Próximo passo: Abra o Cursor e peça ao PM para iniciar a Phase 1."
echo -e "${BLUE}=======================================${NC}"
