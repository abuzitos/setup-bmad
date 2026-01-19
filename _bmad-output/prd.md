# PRD - Product Requirements Document
## Sistema de Cálculo de Médias

---

## 1. Introdução

### 1.1 Propósito
Este documento especifica os requisitos funcionais e não funcionais do Sistema de Cálculo de Médias, uma aplicação para gerenciar o cálculo de médias acadêmicas de alunos de uma faculdade.

### 1.2 Escopo
Este PRD cobre todas as funcionalidades do MVP (Minimum Viable Product) do sistema, incluindo gestão de entidades acadêmicas, registro de notas, cálculo de médias e classificação de aprovação.

### 1.3 Referências
- Brief do Projeto (`_bmad-output/brief.md`)
- Protocolo BMad (`.cursorrules`)
- Definição do Product Manager (`_bmad/bmm/agents/pm.yaml`)

---

## 2. Requisitos Funcionais

### 2.1 Gestão de Cursos

#### RF-001: Criar Curso
- **Descrição**: O sistema deve permitir criar um novo curso.
- **Entrada**: Nome do curso (obrigatório, string, máximo 100 caracteres)
- **Saída**: Curso criado com ID gerado automaticamente
- **Endpoint**: `POST /api/cursos`
- **Validações**:
  - Nome não pode ser vazio ou nulo
  - Nome deve ser único (não pode haver dois cursos com o mesmo nome)

#### RF-002: Listar Cursos
- **Descrição**: O sistema deve permitir listar todos os cursos cadastrados.
- **Entrada**: Nenhuma (opcional: parâmetros de paginação)
- **Saída**: Lista de cursos com ID, nome
- **Endpoint**: `GET /api/cursos`

#### RF-003: Buscar Curso por ID
- **Descrição**: O sistema deve permitir buscar um curso específico pelo ID.
- **Entrada**: ID do curso (path parameter)
- **Saída**: Curso com ID, nome e lista de disciplinas vinculadas
- **Endpoint**: `GET /api/cursos/{id}`
- **Validações**: ID deve existir no sistema

#### RF-004: Atualizar Curso
- **Descrição**: O sistema deve permitir atualizar informações de um curso existente.
- **Entrada**: ID do curso (path parameter) e dados atualizados (body)
- **Saída**: Curso atualizado
- **Endpoint**: `PUT /api/cursos/{id}`
- **Validações**:
  - ID deve existir
  - Nome não pode ser vazio
  - Nome deve ser único (exceto para o próprio curso)

#### RF-005: Excluir Curso
- **Descrição**: O sistema deve permitir excluir um curso.
- **Entrada**: ID do curso (path parameter)
- **Saída**: Confirmação de exclusão
- **Endpoint**: `DELETE /api/cursos/{id}`
- **Validações**:
  - ID deve existir
  - Curso não pode ter disciplinas vinculadas (restrição de integridade)

### 2.2 Gestão de Disciplinas

#### RF-006: Criar Disciplina
- **Descrição**: O sistema deve permitir criar uma nova disciplina.
- **Entrada**: 
  - Nome da disciplina (obrigatório, string, máximo 100 caracteres)
  - ID do curso (obrigatório, referência)
  - ID do professor (obrigatório, referência)
- **Saída**: Disciplina criada com ID gerado automaticamente
- **Endpoint**: `POST /api/disciplinas`
- **Validações**:
  - Nome não pode ser vazio
  - Curso deve existir
  - Professor deve existir
  - Nome deve ser único dentro do mesmo curso

#### RF-007: Listar Disciplinas
- **Descrição**: O sistema deve permitir listar todas as disciplinas.
- **Entrada**: Nenhuma (opcional: filtros por curso ou professor)
- **Saída**: Lista de disciplinas com ID, nome, curso e professor
- **Endpoint**: `GET /api/disciplinas`
- **Filtros opcionais**:
  - `?cursoId={id}`: Filtrar por curso
  - `?professorId={id}`: Filtrar por professor

#### RF-008: Buscar Disciplina por ID
- **Descrição**: O sistema deve permitir buscar uma disciplina específica pelo ID.
- **Entrada**: ID da disciplina (path parameter)
- **Saída**: Disciplina com ID, nome, curso, professor e lista de alunos matriculados
- **Endpoint**: `GET /api/disciplinas/{id}`
- **Validações**: ID deve existir

#### RF-009: Atualizar Disciplina
- **Descrição**: O sistema deve permitir atualizar informações de uma disciplina.
- **Entrada**: ID da disciplina (path parameter) e dados atualizados (body)
- **Saída**: Disciplina atualizada
- **Endpoint**: `PUT /api/disciplinas/{id}`
- **Validações**:
  - ID deve existir
  - Curso deve existir (se informado)
  - Professor deve existir (se informado)
  - Nome deve ser único dentro do curso (se alterado)

#### RF-010: Excluir Disciplina
- **Descrição**: O sistema deve permitir excluir uma disciplina.
- **Entrada**: ID da disciplina (path parameter)
- **Saída**: Confirmação de exclusão
- **Endpoint**: `DELETE /api/disciplinas/{id}`
- **Validações**:
  - ID deve existir
  - Disciplina não pode ter alunos matriculados ou notas registradas

### 2.3 Gestão de Alunos

#### RF-011: Criar Aluno
- **Descrição**: O sistema deve permitir criar um novo aluno.
- **Entrada**: 
  - Nome do aluno (obrigatório, string, máximo 100 caracteres)
  - Matrícula (obrigatório, string, único, máximo 20 caracteres)
- **Saída**: Aluno criado com ID gerado automaticamente
- **Endpoint**: `POST /api/alunos`
- **Validações**:
  - Nome não pode ser vazio
  - Matrícula não pode ser vazia e deve ser única

#### RF-012: Listar Alunos
- **Descrição**: O sistema deve permitir listar todos os alunos cadastrados.
- **Entrada**: Nenhuma (opcional: parâmetros de paginação)
- **Saída**: Lista de alunos com ID, nome e matrícula
- **Endpoint**: `GET /api/alunos`

#### RF-013: Buscar Aluno por ID
- **Descrição**: O sistema deve permitir buscar um aluno específico pelo ID.
- **Entrada**: ID do aluno (path parameter)
- **Saída**: Aluno com ID, nome, matrícula e lista de disciplinas matriculadas
- **Endpoint**: `GET /api/alunos/{id}`
- **Validações**: ID deve existir

#### RF-014: Atualizar Aluno
- **Descrição**: O sistema deve permitir atualizar informações de um aluno.
- **Entrada**: ID do aluno (path parameter) e dados atualizados (body)
- **Saída**: Aluno atualizado
- **Endpoint**: `PUT /api/alunos/{id}`
- **Validações**:
  - ID deve existir
  - Nome não pode ser vazio
  - Matrícula deve ser única (exceto para o próprio aluno)

#### RF-015: Excluir Aluno
- **Descrição**: O sistema deve permitir excluir um aluno.
- **Entrada**: ID do aluno (path parameter)
- **Saída**: Confirmação de exclusão
- **Endpoint**: `DELETE /api/alunos/{id}`
- **Validações**:
  - ID deve existir
  - Aluno não pode ter notas registradas (opcional: pode excluir em cascata)

### 2.4 Gestão de Professores

#### RF-016: Criar Professor
- **Descrição**: O sistema deve permitir criar um novo professor.
- **Entrada**: 
  - Nome do professor (obrigatório, string, máximo 100 caracteres)
  - Registro (obrigatório, string, único, máximo 20 caracteres)
- **Saída**: Professor criado com ID gerado automaticamente
- **Endpoint**: `POST /api/professores`
- **Validações**:
  - Nome não pode ser vazio
  - Registro não pode ser vazio e deve ser único

#### RF-017: Listar Professores
- **Descrição**: O sistema deve permitir listar todos os professores cadastrados.
- **Entrada**: Nenhuma (opcional: parâmetros de paginação)
- **Saída**: Lista de professores com ID, nome e registro
- **Endpoint**: `GET /api/professores`

#### RF-018: Buscar Professor por ID
- **Descrição**: O sistema deve permitir buscar um professor específico pelo ID.
- **Entrada**: ID do professor (path parameter)
- **Saída**: Professor com ID, nome, registro e lista de disciplinas que leciona
- **Endpoint**: `GET /api/professores/{id}`
- **Validações**: ID deve existir

#### RF-019: Atualizar Professor
- **Descrição**: O sistema deve permitir atualizar informações de um professor.
- **Entrada**: ID do professor (path parameter) e dados atualizados (body)
- **Saída**: Professor atualizado
- **Endpoint**: `PUT /api/professores/{id}`
- **Validações**:
  - ID deve existir
  - Nome não pode ser vazio
  - Registro deve ser único (exceto para o próprio professor)

#### RF-020: Excluir Professor
- **Descrição**: O sistema deve permitir excluir um professor.
- **Entrada**: ID do professor (path parameter)
- **Saída**: Confirmação de exclusão
- **Endpoint**: `DELETE /api/professores/{id}`
- **Validações**:
  - ID deve existir
  - Professor não pode estar vinculado a disciplinas

### 2.5 Gestão de Matrículas

#### RF-021: Matricular Aluno em Disciplina
- **Descrição**: O sistema deve permitir matricular um aluno em uma disciplina.
- **Entrada**: 
  - ID do aluno (obrigatório, path parameter ou body)
  - ID da disciplina (obrigatório, path parameter ou body)
- **Saída**: Matrícula criada
- **Endpoint**: `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}` ou `POST /api/matriculas`
- **Validações**:
  - Aluno deve existir
  - Disciplina deve existir
  - Aluno não pode estar já matriculado na disciplina (evitar duplicatas)

#### RF-022: Listar Matrículas
- **Descrição**: O sistema deve permitir listar todas as matrículas.
- **Entrada**: Nenhuma (opcional: filtros por aluno ou disciplina)
- **Saída**: Lista de matrículas com aluno e disciplina
- **Endpoint**: `GET /api/matriculas`
- **Filtros opcionais**:
  - `?alunoId={id}`: Filtrar por aluno
  - `?disciplinaId={id}`: Filtrar por disciplina

#### RF-023: Desmatricular Aluno
- **Descrição**: O sistema deve permitir desmatricular um aluno de uma disciplina.
- **Entrada**: 
  - ID do aluno (path parameter)
  - ID da disciplina (path parameter)
- **Saída**: Confirmação de desmatrícula
- **Endpoint**: `DELETE /api/disciplinas/{disciplinaId}/alunos/{alunoId}`
- **Validações**:
  - Matrícula deve existir
  - Se houver notas registradas, pode ser necessário excluir as notas também (definir política)

### 2.6 Gestão de Notas

#### RF-024: Registrar Notas
- **Descrição**: O sistema deve permitir registrar as 2 notas de um aluno em uma disciplina.
- **Entrada**: 
  - ID do aluno (obrigatório)
  - ID da disciplina (obrigatório)
  - Nota 1 (obrigatório, decimal entre 0.0 e 10.0)
  - Nota 2 (obrigatório, decimal entre 0.0 e 10.0)
- **Saída**: Notas registradas com média calculada automaticamente
- **Endpoint**: `POST /api/notas` ou `POST /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`
- **Validações**:
  - Aluno deve existir
  - Disciplina deve existir
  - Aluno deve estar matriculado na disciplina
  - Nota 1 e Nota 2 devem estar no intervalo [0.0, 10.0]
  - Não pode haver mais de um registro de notas para o mesmo aluno/disciplina (ou permitir atualização)

#### RF-025: Atualizar Notas
- **Descrição**: O sistema deve permitir atualizar as notas de um aluno em uma disciplina.
- **Entrada**: 
  - ID do aluno (path parameter)
  - ID da disciplina (path parameter)
  - Nota 1 (obrigatório, decimal entre 0.0 e 10.0)
  - Nota 2 (obrigatório, decimal entre 0.0 e 10.0)
- **Saída**: Notas atualizadas com média recalculada automaticamente
- **Endpoint**: `PUT /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`
- **Validações**:
  - Registro de notas deve existir
  - Notas devem estar no intervalo [0.0, 10.0]

#### RF-026: Consultar Notas
- **Descrição**: O sistema deve permitir consultar as notas de um aluno em uma disciplina.
- **Entrada**: 
  - ID do aluno (path parameter)
  - ID da disciplina (path parameter)
- **Saída**: Notas (Nota 1, Nota 2), média calculada e classificação (Aprovado/Exame/Reprovado)
- **Endpoint**: `GET /api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas`
- **Validações**: Registro de notas deve existir

#### RF-027: Listar Notas por Disciplina
- **Descrição**: O sistema deve permitir listar todas as notas de uma disciplina.
- **Entrada**: ID da disciplina (path parameter)
- **Saída**: Lista de alunos com suas respectivas notas, médias e classificações
- **Endpoint**: `GET /api/disciplinas/{disciplinaId}/notas`
- **Validações**: Disciplina deve existir

#### RF-028: Listar Notas por Aluno
- **Descrição**: O sistema deve permitir listar todas as notas de um aluno em todas as disciplinas.
- **Entrada**: ID do aluno (path parameter)
- **Saída**: Lista de disciplinas com respectivas notas, médias e classificações
- **Endpoint**: `GET /api/alunos/{alunoId}/notas`
- **Validações**: Aluno deve existir

### 2.7 Cálculo de Médias e Classificação

#### RF-029: Calcular Média Automaticamente
- **Descrição**: O sistema deve calcular automaticamente a média quando as notas são registradas ou atualizadas.
- **Lógica**: Média = (Nota 1 + Nota 2) / 2
- **Trigger**: Automático após criação ou atualização de notas
- **Validações**: Média deve ser um valor decimal entre 0.0 e 10.0

#### RF-030: Classificar Aprovação Automaticamente
- **Descrição**: O sistema deve classificar automaticamente o status de aprovação baseado na média calculada.
- **Regras**:
  - **Aprovado**: Média >= 7.0
  - **Exame**: Média >= 5.0 e < 7.0
  - **Reprovado**: Média < 5.0
- **Trigger**: Automático após cálculo da média
- **Representação**: Enum ou String ("APROVADO", "EXAME", "REPROVADO")

#### RF-031: Consultar Status de Aprovação
- **Descrição**: O sistema deve permitir consultar o status de aprovação de um aluno em uma disciplina.
- **Entrada**: ID do aluno e ID da disciplina
- **Saída**: Média e classificação (Aprovado/Exame/Reprovado)
- **Endpoint**: Incluído no RF-026 (consultar notas)

---

## 3. Requisitos Não Funcionais

### 3.1 Stack Tecnológica

#### RNF-001: Linguagem e Versão
- **Requisito**: O sistema deve ser desenvolvido em **Java 21**
- **Justificativa**: Versão LTS com recursos modernos e suporte a longo prazo

#### RNF-002: Gerenciador de Dependências
- **Requisito**: O projeto deve usar **Maven 3.8+** para gerenciamento de dependências
- **Justificativa**: Padrão da indústria para projetos Java

#### RNF-003: Framework Enterprise
- **Requisito**: O sistema deve utilizar **Jakarta EE 10.0** com:
  - Jakarta REST 3.1 (API REST)
  - Jakarta Persistence 3.1 (JPA)
  - Jakarta Validation 3.0
  - Jakarta CDI 4.0
- **Justificativa**: Padrão moderno para aplicações enterprise Java

#### RNF-004: Provider JPA
- **Requisito**: Utilizar **Hibernate 6.4.1** como provider JPA
- **Justificativa**: Provider mais maduro e amplamente utilizado

#### RNF-005: Banco de Dados
- **Requisito**: Utilizar **SQLite 3.44.1.0** como banco de dados
- **Justificativa**: Banco leve, sem necessidade de servidor, ideal para desenvolvimento e MVP

#### RNF-006: Implementação REST
- **Requisito**: Utilizar **Jersey 3.1.3** como implementação de Jakarta REST
- **Justificativa**: Implementação de referência do Jakarta REST

#### RNF-007: Framework de Testes
- **Requisito**: Utilizar **JUnit 5.10.1** para testes automatizados
- **Justificativa**: Framework padrão para testes em Java

### 3.2 Ambiente de Desenvolvimento

#### RNF-008: DevContainer
- **Requisito**: O projeto deve executar em **DevContainer** configurado
- **Justificativa**: Ambiente padronizado e isolado para todos os desenvolvedores
- **Especificações**:
  - Container com Java 21 pré-instalado
  - Maven configurado
  - Extensões do VS Code para desenvolvimento Java

### 3.3 Arquitetura e Design

#### RNF-009: Arquitetura em Camadas
- **Requisito**: O sistema deve seguir arquitetura em camadas:
  - **Controller**: Endpoints REST (Jakarta REST)
  - **Service**: Lógica de negócio
  - **Repository**: Acesso a dados (JPA Repository)
  - **Domain**: Entidades JPA
  - **DTO**: Objetos de transferência de dados
  - **Exception**: Exceções customizadas
- **Justificativa**: Separação de responsabilidades e manutenibilidade

#### RNF-010: Padrão de Nomenclatura
- **Requisito**: Seguir convenções Java:
  - Classes em PascalCase
  - Métodos e variáveis em camelCase
  - Constantes em UPPER_SNAKE_CASE
  - Pacotes em lowercase
- **Justificativa**: Padrão da indústria Java

### 3.4 Persistência de Dados

#### RNF-011: Configuração JPA
- **Requisito**: Utilizar `persistence.xml` para configuração JPA
- **Especificações**:
  - Unidade de persistência configurada
  - Provider Hibernate
  - Dialeto SQLite
  - Configuração de schema (create/update)

#### RNF-012: Mapeamento de Entidades
- **Requisito**: Todas as entidades devem ser mapeadas com JPA annotations
- **Especificações**:
  - `@Entity` para entidades
  - `@Id` e `@GeneratedValue` para chaves primárias
  - `@ManyToOne`, `@OneToMany`, `@ManyToMany` para relacionamentos
  - `@Column` para especificações de coluna

### 3.5 API REST

#### RNF-013: Padrão REST
- **Requisito**: API deve seguir padrões REST:
  - URLs semânticas e hierárquicas
  - Métodos HTTP apropriados (GET, POST, PUT, DELETE)
  - Códigos de status HTTP corretos (200, 201, 400, 404, 500)
  - JSON como formato de dados
- **Justificativa**: Padrão amplamente adotado e fácil de consumir

#### RNF-014: Versionamento de API
- **Requisito**: API deve usar prefixo `/api` para versionamento futuro
- **Justificativa**: Facilita evolução da API sem quebrar clientes existentes

#### RNF-015: Tratamento de Erros
- **Requisito**: API deve retornar mensagens de erro padronizadas em JSON
- **Especificações**:
  - Código de erro
  - Mensagem descritiva
  - Timestamp (opcional)
- **Exemplo**:
  ```json
  {
    "codigo": "ERRO_VALIDACAO",
    "mensagem": "Nota deve estar entre 0.0 e 10.0",
    "timestamp": "2024-01-15T10:30:00Z"
  }
  ```

### 3.6 Validação de Dados

#### RNF-016: Validação com Jakarta Validation
- **Requisito**: Utilizar Jakarta Validation para validação de dados
- **Especificações**:
  - `@NotNull`, `@NotEmpty`, `@NotBlank` para campos obrigatórios
  - `@Min`, `@Max`, `@DecimalMin`, `@DecimalMax` para valores numéricos
  - `@Size` para tamanho de strings
  - Validação automática em endpoints REST

### 3.7 Testes

#### RNF-017: Cobertura de Testes
- **Requisito**: Sistema deve ter cobertura de testes automatizados
- **Especificações**:
  - Testes unitários para lógica de negócio (Services)
  - Testes de integração para repositórios (JPA)
  - Testes de API para endpoints REST
- **Justificativa**: Garantia de qualidade e regressão

#### RNF-018: Banco de Dados de Testes
- **Requisito**: Testes devem usar banco de dados SQLite em memória ou arquivo temporário
- **Justificativa**: Isolamento e velocidade de execução

### 3.8 Performance

#### RNF-019: Tempo de Resposta
- **Requisito**: Endpoints devem responder em menos de 500ms para operações simples (GET, POST básicos)
- **Justificativa**: Experiência do usuário adequada

#### RNF-020: Consultas Eficientes
- **Requisito**: Utilizar `@Query` ou `EntityManager` para consultas otimizadas quando necessário
- **Justificativa**: Evitar N+1 queries e melhorar performance

### 3.9 Segurança (Futuro)

#### RNF-021: Validação de Entrada
- **Requisito**: Todas as entradas devem ser validadas antes do processamento
- **Status**: Implementado no MVP através de Jakarta Validation

#### RNF-022: Autenticação e Autorização
- **Requisito**: Sistema deve suportar autenticação e autorização (futuro)
- **Status**: Fora do escopo do MVP

### 3.10 Documentação

#### RNF-023: Documentação de API
- **Requisito**: API deve ter documentação clara (futuro: OpenAPI/Swagger)
- **Status**: Documentação manual no PRD para MVP

#### RNF-024: Código Documentado
- **Requisito**: Código deve ter JavaDoc para classes e métodos públicos
- **Justificativa**: Facilita manutenção e uso da API

---

## 4. Regras de Negócio Detalhadas

### 4.1 Validações de Dados

#### RN-001: Validação de Notas
- Notas devem ser valores decimais
- Intervalo válido: [0.0, 10.0]
- Precisão: 1 casa decimal (ex: 7.5, 8.0, 9.3)
- Não aceita valores negativos ou maiores que 10.0

#### RN-002: Validação de Nomes
- Nomes (curso, disciplina, aluno, professor) não podem ser vazios
- Máximo de 100 caracteres
- Aceita caracteres alfanuméricos, espaços e caracteres especiais comuns (acentos)

#### RN-003: Validação de Códigos Únicos
- Matrícula de aluno deve ser única no sistema
- Registro de professor deve ser único no sistema
- Nome de curso deve ser único no sistema
- Nome de disciplina deve ser único dentro do mesmo curso

### 4.2 Regras de Cálculo

#### RN-004: Cálculo de Média
- Fórmula: `Média = (Nota 1 + Nota 2) / 2`
- Precisão: 2 casas decimais (ex: 7.50, 6.75)
- Arredondamento: Padrão matemático (0.5 arredonda para cima)
- Média é calculada automaticamente, não pode ser editada manualmente

#### RN-005: Classificação de Aprovação
- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0
- Classificação é calculada automaticamente após cálculo da média
- Classificação não pode ser editada manualmente

### 4.3 Regras de Relacionamentos

#### RN-006: Relacionamento Curso-Disciplina
- Uma disciplina deve pertencer a exatamente um curso
- Um curso pode ter múltiplas disciplinas
- Não é permitido excluir curso que possui disciplinas vinculadas

#### RN-007: Relacionamento Disciplina-Professor
- Uma disciplina deve ter exatamente um professor responsável
- Um professor pode lecionar múltiplas disciplinas
- Não é permitido excluir professor que possui disciplinas vinculadas

#### RN-008: Relacionamento Aluno-Disciplina
- Um aluno pode estar matriculado em múltiplas disciplinas
- Uma disciplina pode ter múltiplos alunos matriculados
- Aluno deve estar matriculado na disciplina para receber notas
- Não é permitido registrar notas para aluno não matriculado

#### RN-009: Relacionamento Aluno-Disciplina-Notas
- Cada aluno em uma disciplina possui exatamente 2 notas
- Notas são obrigatórias (não pode ter apenas 1 nota)
- Não é permitido ter mais de 2 notas por aluno/disciplina
- Ao desmatricular aluno, definir política: excluir notas ou manter histórico

### 4.4 Regras de Integridade

#### RN-010: Integridade Referencial
- Não é permitido excluir entidade que possui relacionamentos dependentes
- Exceções devem ser tratadas com mensagens claras
- Operações de exclusão devem validar dependências antes de executar

#### RN-011: Consistência de Dados
- Média deve sempre refletir as notas atuais
- Classificação deve sempre refletir a média atual
- Dados não podem ficar inconsistentes após operações

---

## 5. Modelo de Dados

### 5.1 Entidades Principais

#### Curso
- `id` (Long, PK, auto-gerado)
- `nome` (String, obrigatório, único, max 100)

#### Disciplina
- `id` (Long, PK, auto-gerado)
- `nome` (String, obrigatório, max 100)
- `curso` (Curso, ManyToOne, obrigatório)
- `professor` (Professor, ManyToOne, obrigatório)

#### Aluno
- `id` (Long, PK, auto-gerado)
- `nome` (String, obrigatório, max 100)
- `matricula` (String, obrigatório, único, max 20)

#### Professor
- `id` (Long, PK, auto-gerado)
- `nome` (String, obrigatório, max 100)
- `registro` (String, obrigatório, único, max 20)

#### Matrícula (Tabela de Associação)
- `id` (Long, PK, auto-gerado)
- `aluno` (Aluno, ManyToOne, obrigatório)
- `disciplina` (Disciplina, ManyToOne, obrigatório)
- Constraint única: (aluno, disciplina)

#### Nota
- `id` (Long, PK, auto-gerado)
- `aluno` (Aluno, ManyToOne, obrigatório)
- `disciplina` (Disciplina, ManyToOne, obrigatório)
- `nota1` (BigDecimal, obrigatório, 0.0-10.0)
- `nota2` (BigDecimal, obrigatório, 0.0-10.0)
- `media` (BigDecimal, calculado automaticamente)
- `classificacao` (Enum/String, calculado automaticamente: APROVADO/EXAME/REPROVADO)
- Constraint única: (aluno, disciplina)

### 5.2 Relacionamentos

```
Curso (1) ----< (N) Disciplina
Professor (1) ----< (N) Disciplina
Aluno (N) ----< (N) Disciplina [via Matrícula]
Aluno (N) ----< (N) Disciplina [via Nota]
```

---

## 6. Endpoints REST - Especificação Completa

### 6.1 Cursos

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/cursos` | Criar curso | 201 Created, 400 Bad Request |
| GET | `/api/cursos` | Listar cursos | 200 OK |
| GET | `/api/cursos/{id}` | Buscar curso por ID | 200 OK, 404 Not Found |
| PUT | `/api/cursos/{id}` | Atualizar curso | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/api/cursos/{id}` | Excluir curso | 204 No Content, 400 Bad Request, 404 Not Found |

### 6.2 Disciplinas

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/disciplinas` | Criar disciplina | 201 Created, 400 Bad Request |
| GET | `/api/disciplinas` | Listar disciplinas | 200 OK |
| GET | `/api/disciplinas?cursoId={id}` | Filtrar por curso | 200 OK |
| GET | `/api/disciplinas?professorId={id}` | Filtrar por professor | 200 OK |
| GET | `/api/disciplinas/{id}` | Buscar disciplina por ID | 200 OK, 404 Not Found |
| PUT | `/api/disciplinas/{id}` | Atualizar disciplina | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/api/disciplinas/{id}` | Excluir disciplina | 204 No Content, 400 Bad Request, 404 Not Found |

### 6.3 Alunos

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/alunos` | Criar aluno | 201 Created, 400 Bad Request |
| GET | `/api/alunos` | Listar alunos | 200 OK |
| GET | `/api/alunos/{id}` | Buscar aluno por ID | 200 OK, 404 Not Found |
| PUT | `/api/alunos/{id}` | Atualizar aluno | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/api/alunos/{id}` | Excluir aluno | 204 No Content, 400 Bad Request, 404 Not Found |

### 6.4 Professores

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/professores` | Criar professor | 201 Created, 400 Bad Request |
| GET | `/api/professores` | Listar professores | 200 OK |
| GET | `/api/professores/{id}` | Buscar professor por ID | 200 OK, 404 Not Found |
| PUT | `/api/professores/{id}` | Atualizar professor | 200 OK, 400 Bad Request, 404 Not Found |
| DELETE | `/api/professores/{id}` | Excluir professor | 204 No Content, 400 Bad Request, 404 Not Found |

### 6.5 Matrículas

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}` | Matricular aluno | 201 Created, 400 Bad Request, 404 Not Found |
| GET | `/api/matriculas` | Listar matrículas | 200 OK |
| GET | `/api/matriculas?alunoId={id}` | Filtrar por aluno | 200 OK |
| GET | `/api/matriculas?disciplinaId={id}` | Filtrar por disciplina | 200 OK |
| DELETE | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}` | Desmatricular aluno | 204 No Content, 404 Not Found |

### 6.6 Notas

| Método | Endpoint | Descrição | Status Codes |
|--------|----------|-----------|--------------|
| POST | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Registrar notas | 201 Created, 400 Bad Request, 404 Not Found |
| PUT | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Atualizar notas | 200 OK, 400 Bad Request, 404 Not Found |
| GET | `/api/disciplinas/{disciplinaId}/alunos/{alunoId}/notas` | Consultar notas | 200 OK, 404 Not Found |
| GET | `/api/disciplinas/{disciplinaId}/notas` | Listar notas da disciplina | 200 OK, 404 Not Found |
| GET | `/api/alunos/{alunoId}/notas` | Listar notas do aluno | 200 OK, 404 Not Found |

---

## 7. Exemplos de Payloads

### 7.1 Criar Curso
```json
POST /api/cursos
{
  "nome": "Ciência da Computação"
}
```

### 7.2 Criar Disciplina
```json
POST /api/disciplinas
{
  "nome": "Programação Orientada a Objetos",
  "cursoId": 1,
  "professorId": 1
}
```

### 7.3 Registrar Notas
```json
POST /api/disciplinas/1/alunos/1/notas
{
  "nota1": 8.5,
  "nota2": 7.0
}
```

### 7.4 Resposta - Consultar Notas
```json
GET /api/disciplinas/1/alunos/1/notas
{
  "aluno": {
    "id": 1,
    "nome": "João Silva",
    "matricula": "2024001"
  },
  "disciplina": {
    "id": 1,
    "nome": "Programação Orientada a Objetos"
  },
  "nota1": 8.5,
  "nota2": 7.0,
  "media": 7.75,
  "classificacao": "APROVADO"
}
```

---

## 8. Próximas Fases

### Fase P2 - Planning
- Criação de Epics baseados nos requisitos funcionais
- Criação de User Stories detalhadas
- Planejamento de sprints

### Fase P3 - Solutioning
- Software Architect criará Architecture Doc
- Test Engineering Agent criará Test Design
- Aprovação dos documentos antes da implementação

### Fase P4 - Implementation
- Developer implementará o código seguindo Architecture Doc
- Test Engineering Agent validará testes e realizará Code Review

---

**Documento criado por**: Product Manager (PM)  
**Data**: Fase P1 - Discovery  
**Status**: ✅ APROVADO - Pronto para fase P2 (Planning)
