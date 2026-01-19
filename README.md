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
- **SmallRye OpenAPI**: 3.2.0 (Documentação Swagger/OpenAPI)
- **JaCoCo**: 0.8.11 (Cobertura de testes)
- **Mockito**: 5.11.0 (Mocking em testes)
- **AssertJ**: 3.25.1 (Asserções fluentes)
- **REST Assured**: 5.4.0 (Testes de API)

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
│   │   │       ├── exception/   # Exceções customizadas
│   │   │       └── config/      # Configurações (Jersey, OpenAPI)
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   ├── persistence.xml  # Configuração JPA
│   │       │   └── openapi.yaml     # Especificação OpenAPI
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

5. Execute os testes de integração:
   ```bash
   mvn verify
   ```

6. Gere relatório de cobertura de testes:
   ```bash
   mvn clean test jacoco:report
   # Relatório disponível em: target/site/jacoco/index.html
   ```

### Executando a Aplicação

1. Inicie o servidor (se houver classe Main configurada):
   ```bash
   mvn exec:java
   ```

2. Acesse a documentação Swagger:
   - **Swagger UI**: http://localhost:8080/swagger-ui
   - **OpenAPI JSON**: http://localhost:8080/openapi
   - **OpenAPI YAML**: http://localhost:8080/openapi?format=yaml

3. A API estará disponível em:
   - **Base URL**: http://localhost:8080/api

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

**Fase Atual**: P4 - Implementation (IN_PROGRESS)

### Fases do Processo

- ✅ **P1 - Discovery**: COMPLETED
  - Brief e PRD criados
  - Requisitos e regras de negócio definidos
  
- ✅ **P2 - Planning**: COMPLETED
  - 6 Épicos criados
  - 31 User Stories definidas
  - 4 Sprints planejados
  
- ✅ **P3 - Solutioning**: COMPLETED
  - Architecture Doc aprovado
  - Test Design aprovado
  
- ⏳ **P4 - Implementation**: IN_PROGRESS
  - Sprint 1 (Fundação): ✅ COMPLETED
  - Sprint 2 (Relacionamentos): ✅ COMPLETED
  - Sprint 3 (Matrículas): ✅ COMPLETED
  - Sprint 4 (Notas e Médias): ⏳ EM ANDAMENTO

### Implementações Concluídas

#### Sprint 1 - Fundação
- ✅ Camada de domínio (entidades JPA)
- ✅ Camada de repositório
- ✅ Camada de serviço
- ✅ Camada de controller (CRUD básico)
- ✅ Camada de DTOs
- ✅ Tratamento de exceções
- ✅ Testes unitários e de integração
- ✅ **Cobertura**: 94% Services, 93% Repositories
- ✅ **Total de testes**: 57 (todos passando)

#### Sprint 2 - Relacionamentos
- ✅ Gestão de Disciplinas (vinculadas a Cursos e Professores)
- ✅ Relacionamentos JPA implementados
- ✅ Validações de negócio
- ✅ Testes de integração
- ✅ **Cobertura**: 89% Services, 95% Repositories
- ✅ **Total de testes**: 38 (todos passando)

#### Sprint 3 - Matrículas
- ✅ Sistema de matrículas de alunos em disciplinas
- ✅ Validações de regras de negócio
- ✅ Testes completos
- ✅ **Cobertura**: 91% Services, 84% Repositories
- ✅ **Total de testes**: 102 (todos passando)

### Métricas de Qualidade

- **Total de Testes**: 197+ testes
- **Taxa de Sucesso**: 100% (todos os testes passando)
- **Cobertura de Código**:
  - Services: 89-94%
  - Repositories: 84-95%
  - Controllers: Em implementação
- **Documentação**: Swagger/OpenAPI configurado e funcionando

## 🤝 Como Contribuir

1. Siga o protocolo BMad definido em `.cursorrules`
2. Respeite as fases do processo (não pule fases)
3. Aguarde aprovação de Architecture Doc e Test Design antes de implementar
4. Mantenha os testes atualizados
5. Siga os padrões de código definidos pelo Architect

## 📄 Licença

Este projeto é um exemplo de aplicação educacional.

## 🌐 API REST

### Endpoints Implementados

#### Cursos (`/api/cursos`)
- `GET /api/cursos` - Listar todos os cursos
- `GET /api/cursos/{id}` - Buscar curso por ID
- `POST /api/cursos` - Criar novo curso
- `PUT /api/cursos/{id}` - Atualizar curso
- `DELETE /api/cursos/{id}` - Excluir curso

#### Disciplinas (`/api/disciplinas`)
- `GET /api/disciplinas` - Listar todas as disciplinas
- `GET /api/disciplinas?cursoId={id}` - Filtrar por curso
- `GET /api/disciplinas?professorId={id}` - Filtrar por professor
- `GET /api/disciplinas/{id}` - Buscar disciplina por ID
- `POST /api/disciplinas` - Criar nova disciplina
- `PUT /api/disciplinas/{id}` - Atualizar disciplina
- `DELETE /api/disciplinas/{id}` - Excluir disciplina

#### Alunos (`/api/alunos`)
- `GET /api/alunos` - Listar todos os alunos
- `GET /api/alunos/{id}` - Buscar aluno por ID
- `POST /api/alunos` - Criar novo aluno
- `PUT /api/alunos/{id}` - Atualizar aluno
- `DELETE /api/alunos/{id}` - Excluir aluno

#### Professores (`/api/professores`)
- `GET /api/professores` - Listar todos os professores
- `GET /api/professores/{id}` - Buscar professor por ID
- `POST /api/professores` - Criar novo professor
- `PUT /api/professores/{id}` - Atualizar professor
- `DELETE /api/professores/{id}` - Excluir professor

#### Matrículas (`/api/matriculas`)
- `GET /api/matriculas` - Listar todas as matrículas
- `GET /api/matriculas?alunoId={id}` - Filtrar por aluno
- `GET /api/matriculas?disciplinaId={id}` - Filtrar por disciplina
- `GET /api/matriculas/{id}` - Buscar matrícula por ID
- `POST /api/matriculas` - Criar nova matrícula
- `DELETE /api/matriculas/{id}` - Cancelar matrícula

#### Notas (`/api/notas`)
- Em implementação (Sprint 4)

### Documentação da API

A documentação completa da API está disponível via Swagger/OpenAPI:

- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI JSON**: http://localhost:8080/openapi
- **OpenAPI YAML**: http://localhost:8080/openapi?format=yaml

Todos os endpoints estão documentados com:
- Descrições detalhadas
- Exemplos de requisição e resposta
- Códigos de status HTTP
- Validações e regras de negócio

## 📊 Cobertura de Testes

O projeto utiliza **JaCoCo** para análise de cobertura de código.

### Metas de Cobertura

- **Cobertura de Linha**: Mínimo 80%
- **Cobertura de Branch**: Mínimo 75%
- **Cobertura de Classe**: Mínimo 70%

### Comandos

```bash
# Gerar relatório de cobertura
mvn clean test jacoco:report

# Verificar se metas foram atingidas
mvn jacoco:check

# Visualizar relatório
# Abra: target/site/jacoco/index.html
```

### Cobertura Atual

- **Services**: 89-94%
- **Repositories**: 84-95%
- **Controllers**: Em implementação
- **Classes Excluídas**: DTOs e Exceptions (conforme configuração)

## 🔗 Links Úteis

- [Jakarta EE](https://jakarta.ee/)
- [Hibernate](https://hibernate.org/)
- [SQLite](https://www.sqlite.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Maven](https://maven.apache.org/)
- [JaCoCo](https://www.jacoco.org/jacoco/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [SmallRye OpenAPI](https://smallrye.io/smallrye-open-api/)