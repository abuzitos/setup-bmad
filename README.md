# Sistema de Cálculo de Médias

Sistema para cálculo de médias de alunos de uma faculdade, desenvolvido seguindo a metodologia BMad Framework.

## 📋 Descrição

Este projeto gerencia o cálculo de médias dos alunos de uma faculdade, permitindo:
- Gestão de **Cursos**
- Gestão de **Disciplinas** (vinculadas a cursos e professores)
- Gestão de **Alunos** (matriculados em disciplinas)
- Gestão de **Professores** (que lecionam disciplinas)
- Registro de **Notas** (cada disciplina possui 2 notas por aluno)
- **Cálculo automático de médias** com classificação de aprovação

## 🎯 Regras de Negócio

### Cálculo de Média

Cada disciplina possui **2 notas** por aluno. A média é calculada como a média aritmética dessas duas notas.

### Classificação

- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0

## 🛠️ Tecnologias

- **Java**: 21
- **Maven**: 3.8+
- **Jakarta EE**: 10.0
  - Jakarta REST 3.1 (API REST)
  - Jakarta Persistence 3.1 (JPA)
  - Jakarta Validation 3.0
  - Jakarta CDI 4.0
- **Hibernate**: 6.4.1 (Provider JPA)
- **SQLite**: 3.44.1.0 (Banco de dados)
- **JUnit**: 5.10.1 (Testes automatizados)
- **Jersey**: 3.1.3 (Implementação Jakarta REST)

## 📁 Estrutura do Projeto

```
setup-bmad/
├── .devcontainer/          # Configuração DevContainer
│   └── devcontainer.json
├── _bmad/                   # Configurações BMad Framework
│   └── bmm/
│       ├── agents/          # Definições dos agentes
│       └── workflows/       # Workflows do processo
├── _bmad-output/           # Saídas do processo BMad
│   ├── epics/              # Épicos do projeto
│   └── sprint-status.yaml  # Status das fases
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/faculdade/media/
│   │   │       ├── domain/      # Entidades JPA
│   │   │       ├── repository/  # Repositórios JPA
│   │   │       ├── service/     # Lógica de negócio
│   │   │       ├── controller/  # Endpoints REST
│   │   │       ├── dto/         # Objetos de transferência
│   │   │       └── exception/   # Exceções customizadas
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── persistence.xml  # Configuração JPA
│   │       └── application.properties
│   └── test/
│       ├── java/            # Testes unitários e de integração
│       └── resources/
│           └── META-INF/
│               └── persistence.xml  # Configuração JPA para testes
├── pom.xml                  # Configuração Maven
├── .cursorrules             # Protocolo BMad
└── README.md
```

## 🚀 Como Executar

### Pré-requisitos

- Docker e Docker Compose (para DevContainer)
- VS Code com extensão "Dev Containers"
- Ou localmente: Java 21 e Maven 3.8+

### Usando DevContainer (Recomendado)

1. Abra o projeto no VS Code
2. Quando solicitado, clique em "Reopen in Container" ou use o comando:
   - `Ctrl+Shift+P` (ou `Cmd+Shift+P` no Mac)
   - Digite "Dev Containers: Reopen in Container"
3. Aguarde a construção do container (primeira vez pode demorar)
4. O container já terá Java 21 e Maven pré-configurados

### Executando Localmente

1. Certifique-se de ter Java 21 instalado:
   ```bash
   java -version
   ```

2. Certifique-se de ter Maven instalado:
   ```bash
   mvn -version
   ```

3. Compile o projeto:
   ```bash
   mvn clean install
   ```

4. Execute os testes:
   ```bash
   mvn test
   ```

### Troubleshooting DevContainer

Se encontrar erros ao iniciar o DevContainer:

1. **Erro ao instalar Cursor server**: 
   - Tente atualizar o Cursor para a versão mais recente
   - Limpe o diretório `.cursor-server` e tente novamente
   - Verifique se o Docker está rodando corretamente

2. **Erro com a imagem base**:
   - Use a versão alternativa: renomeie `devcontainer-alternative.json` para `devcontainer.json`
   - Ou edite o `devcontainer.json` e altere a imagem para: `mcr.microsoft.com/devcontainers/java:1-21`

3. **Problemas com mounts**:
   - O arquivo atual usa uma configuração simplificada
   - Se necessário, remova a seção `mounts` do `devcontainer.json`

4. **Container não inicia**:
   - Verifique os logs do Docker: `docker logs <container-id>`
   - Tente reconstruir o container: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

## 📊 Domínio do Sistema

### Entidades Principais

- **Curso**: Representa um curso da faculdade
- **Disciplina**: Pertence a um curso, possui um professor responsável
- **Aluno**: Pode estar matriculado em múltiplas disciplinas
- **Professor**: Leciona uma ou mais disciplinas
- **Nota**: Registro de nota de um aluno em uma disciplina (2 notas por disciplina)

### Relacionamentos

- Um **Curso** possui várias **Disciplinas**
- Uma **Disciplina** pertence a um **Curso** e tem um **Professor**
- Um **Aluno** pode estar matriculado em várias **Disciplinas**
- Uma **Disciplina** pode ter vários **Alunos** matriculados
- Cada **Aluno** em uma **Disciplina** possui 2 **Notas**

## 🔄 Metodologia BMad

Este projeto segue o framework BMad (Business Model and Development) com o workflow **Standard Greenfield**.

### Fases do Processo

1. **P1 - Discovery** (Descoberta)
   - Product Manager (PM) cria Brief e PRD
   - Definição de requisitos e regras de negócio

2. **P2 - Planning** (Planejamento)
   - PM cria Epics e User Stories
   - Planejamento de sprints

3. **P3 - Solutioning** (Solução)
   - Software Architect cria Architecture Doc
   - Test Engineering Agent (TEA) cria Test Design
   - **Proibido codar sem aprovação destes documentos**

4. **P4 - Implementation** (Implementação)
   - Developer implementa o código
   - TEA valida testes e realiza Code Review

### Agentes BMad

- **Product Manager (PM)**: Fases P1 e P2
- **Software Architect**: Fase P3
- **Test Engineering Agent (TEA)**: Fases P3 e P4
- **Java Developer (DEV)**: Fase P4

## 📝 Status do Projeto

**Fase Atual**: P1 - Discovery (IN_PROGRESS)

- ✅ Setup inicial do projeto
- ✅ Configuração BMad
- ✅ Estrutura Maven
- ⏳ P1: Discovery (em andamento)
- ⏸️ P2: Planning (pendente)
- ⏸️ P3: Solutioning (pendente)
- ⏸️ P4: Implementation (pendente)

## 🤝 Como Contribuir

1. Siga o protocolo BMad definido em `.cursorrules`
2. Respeite as fases do processo (não pule fases)
3. Aguarde aprovação de Architecture Doc e Test Design antes de implementar
4. Mantenha os testes atualizados
5. Siga os padrões de código definidos pelo Architect

## 📄 Licença

Este projeto é um exemplo de aplicação educacional.

## 🔗 Links Úteis

- [Jakarta EE](https://jakarta.ee/)
- [Hibernate](https://hibernate.org/)
- [SQLite](https://www.sqlite.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Maven](https://maven.apache.org/)
