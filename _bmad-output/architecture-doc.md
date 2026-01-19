# Architecture Doc - Sistema de Cálculo de Médias

## 1. Visão Geral da Arquitetura

### 1.1 Arquitetura em Camadas

O sistema segue uma arquitetura em camadas (Layered Architecture), garantindo separação de responsabilidades e facilitando manutenção, testes e evolução do código.

```
┌─────────────────────────────────────────┐
│         Controller Layer                │  ← Endpoints REST (Jakarta REST)
│      (API Interface)                    │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Service Layer                   │  ← Lógica de Negócio
│    (Business Logic)                     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│        Repository Layer                 │  ← Acesso a Dados (JPA)
│      (Data Access)                      │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│         Domain Layer                    │  ← Entidades JPA
│      (Domain Model)                     │
└─────────────────────────────────────────┘
```

### 1.2 Princípios Arquiteturais

- **SOLID**: Aplicação dos princípios SOLID em todas as camadas
- **Separação de Responsabilidades**: Cada camada tem uma responsabilidade única e bem definida
- **Dependency Inversion**: Camadas superiores dependem de abstrações, não de implementações
- **Single Responsibility**: Cada classe tem uma única razão para mudar
- **Open/Closed**: Aberto para extensão, fechado para modificação

### 1.3 Fluxo de Dados

```
Cliente HTTP
    │
    ▼
Controller (recebe requisição REST)
    │
    ▼
Service (aplica regras de negócio)
    │
    ▼
Repository (persiste/consulta dados)
    │
    ▼
Domain (entidades JPA)
    │
    ▼
SQLite Database
```

## 2. Estrutura de Pacotes

```
com.faculdade.media/
├── controller/           # Endpoints REST (Jakarta REST)
│   ├── CursoController.java
│   ├── DisciplinaController.java
│   ├── AlunoController.java
│   ├── ProfessorController.java
│   └── NotaController.java
│
├── service/              # Lógica de negócio
│   ├── CursoService.java
│   ├── DisciplinaService.java
│   ├── AlunoService.java
│   ├── ProfessorService.java
│   ├── MatriculaService.java
│   └── NotaService.java
│
├── repository/           # Acesso a dados (JPA)
│   ├── CursoRepository.java
│   ├── DisciplinaRepository.java
│   ├── AlunoRepository.java
│   ├── ProfessorRepository.java
│   ├── MatriculaRepository.java
│   └── NotaRepository.java
│
├── domain/               # Entidades JPA
│   ├── Curso.java
│   ├── Disciplina.java
│   ├── Aluno.java
│   ├── Professor.java
│   ├── Matricula.java
│   ├── Nota.java
│   └── Classificacao.java (Enum)
│
├── dto/                  # Objetos de transferência
│   ├── CursoDTO.java
│   ├── CursoInputDTO.java
│   ├── DisciplinaDTO.java
│   ├── DisciplinaInputDTO.java
│   ├── AlunoDTO.java
│   ├── AlunoInputDTO.java
│   ├── ProfessorDTO.java
│   ├── ProfessorInputDTO.java
│   ├── NotaDTO.java
│   ├── NotaInputDTO.java
│   └── MatriculaDTO.java
│
├── exception/            # Exceções customizadas
│   ├── EntidadeNaoEncontradaException.java
│   ├── ValidacaoException.java
│   ├── IntegridadeReferencialException.java
│   └── ExceptionHandler.java
│
└── config/               # Configurações
    ├── OpenAPIConfig.java
    ├── JerseyConfig.java
    └── PersistenceConfig.java
```

## 3. Entidades JPA e Relacionamentos

### 3.1 Entidade: Curso

```java
@Entity
@Table(name = "cursos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nome")
})
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disciplina> disciplinas = new ArrayList<>();
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `nome`: String, obrigatório, máximo 100 caracteres, único

**Relacionamentos:**
- 1:N com Disciplina (um curso tem várias disciplinas)

### 3.2 Entidade: Professor

```java
@Entity
@Table(name = "professores", uniqueConstraints = {
    @UniqueConstraint(columnNames = "registro")
})
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 20, unique = true)
    private String registro;
    
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disciplina> disciplinas = new ArrayList<>();
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `nome`: String, obrigatório, máximo 100 caracteres
- `registro`: String, obrigatório, máximo 20 caracteres, único

**Relacionamentos:**
- 1:N com Disciplina (um professor leciona várias disciplinas)

### 3.3 Entidade: Aluno

```java
@Entity
@Table(name = "alunos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "matricula")
})
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false, length = 20, unique = true)
    private String matricula;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas = new ArrayList<>();
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `nome`: String, obrigatório, máximo 100 caracteres
- `matricula`: String, obrigatório, máximo 20 caracteres, único

**Relacionamentos:**
- 1:N com Matricula (um aluno tem várias matrículas)
- 1:N com Nota (um aluno tem várias notas)

### 3.4 Entidade: Disciplina

```java
@Entity
@Table(name = "disciplinas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nome", "curso_id"})
})
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Matricula> matriculas = new ArrayList<>();
    
    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Nota> notas = new ArrayList<>();
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `nome`: String, obrigatório, máximo 100 caracteres
- `curso`: Curso, obrigatório (ManyToOne)
- `professor`: Professor, obrigatório (ManyToOne)

**Relacionamentos:**
- N:1 com Curso (muitas disciplinas pertencem a um curso)
- N:1 com Professor (muitas disciplinas são lecionadas por um professor)
- 1:N com Matricula (uma disciplina tem várias matrículas)
- 1:N com Nota (uma disciplina tem várias notas)

**Constraints:**
- Nome único dentro do mesmo curso

### 3.5 Entidade: Matricula

```java
@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "disciplina_id"})
})
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `aluno`: Aluno, obrigatório (ManyToOne)
- `disciplina`: Disciplina, obrigatório (ManyToOne)

**Relacionamentos:**
- N:1 com Aluno (muitas matrículas pertencem a um aluno)
- N:1 com Disciplina (muitas matrículas pertencem a uma disciplina)

**Constraints:**
- Constraint única (aluno, disciplina) - evita matrícula duplicada

### 3.6 Entidade: Nota

```java
@Entity
@Table(name = "notas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "disciplina_id"})
})
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;
    
    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal nota1;
    
    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal nota2;
    
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal media;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Classificacao classificacao;
    
    @PrePersist
    @PreUpdate
    private void calcularMediaEClassificacao() {
        this.media = nota1.add(nota2).divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
        this.classificacao = Classificacao.classificar(media);
    }
    
    // constructors, getters, setters
}
```

**Atributos:**
- `id`: Long, chave primária, auto-gerado
- `aluno`: Aluno, obrigatório (ManyToOne)
- `disciplina`: Disciplina, obrigatório (ManyToOne)
- `nota1`: BigDecimal, obrigatório, intervalo [0.0, 10.0]
- `nota2`: BigDecimal, obrigatório, intervalo [0.0, 10.0]
- `media`: BigDecimal, calculado automaticamente
- `classificacao`: Classificacao (Enum), calculado automaticamente

**Relacionamentos:**
- N:1 com Aluno (muitas notas pertencem a um aluno)
- N:1 com Disciplina (muitas notas pertencem a uma disciplina)

**Constraints:**
- Constraint única (aluno, disciplina) - um aluno tem apenas um registro de notas por disciplina

**Cálculos Automáticos:**
- Média: (nota1 + nota2) / 2 (calculado via `@PrePersist` e `@PreUpdate`)
- Classificação: baseada na média (calculado via `@PrePersist` e `@PreUpdate`)

### 3.7 Enum: Classificacao

```java
public enum Classificacao {
    APROVADO("Aprovado"),
    EXAME("Exame"),
    REPROVADO("Reprovado");
    
    private final String descricao;
    
    Classificacao(String descricao) {
        this.descricao = descricao;
    }
    
    public static Classificacao classificar(BigDecimal media) {
        if (media.compareTo(new BigDecimal("7.0")) >= 0) {
            return APROVADO;
        } else if (media.compareTo(new BigDecimal("5.0")) >= 0) {
            return EXAME;
        } else {
            return REPROVADO;
        }
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

## 4. Diagrama de Relacionamentos

```
┌──────────┐         ┌──────────────┐
│  Curso   │◄───1:N──┤  Disciplina  │
└──────────┘         └──────┬───────┘
                            │
                            │ N:1
                            │
┌──────────┐         ┌──────▼───────┐
│Professor │◄───1:N──┤  Disciplina  │
└──────────┘         └──────┬───────┘
                            │
                            │ 1:N
                            │
                    ┌───────┴───────┐
                    │               │
                    │               │
            ┌───────▼──────┐ ┌─────▼──────┐
            │  Matricula   │ │    Nota    │
            └──────┬───────┘ └─────┬──────┘
                   │               │
                   │ N:1           │ N:1
                   │               │
            ┌──────▼───────────────▼──────┐
            │           Aluno             │
            └─────────────────────────────┘
```

## 5. Padrões de Design

### 5.1 Repository Pattern

Cada entidade terá um repositório que encapsula a lógica de acesso a dados:

```java
public class CursoRepository {
    private EntityManager em;
    
    public Curso save(Curso curso) {
        if (curso.getId() == null) {
            em.persist(curso);
        } else {
            curso = em.merge(curso);
        }
        return curso;
    }
    
    public Optional<Curso> findById(Long id) {
        return Optional.ofNullable(em.find(Curso.class, id));
    }
    
    public List<Curso> findAll() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class)
                .getResultList();
    }
    
    public void delete(Curso curso) {
        em.remove(em.contains(curso) ? curso : em.merge(curso));
    }
}
```

### 5.2 Service Layer Pattern

Services contêm a lógica de negócio e coordenam operações entre repositórios:

```java
public class CursoService {
    private CursoRepository cursoRepository;
    
    public Curso criar(CursoInputDTO dto) {
        validarNomeUnico(dto.getNome());
        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        return cursoRepository.save(curso);
    }
    
    private void validarNomeUnico(String nome) {
        if (cursoRepository.existsByNome(nome)) {
            throw new ValidacaoException("Nome de curso já existe");
        }
    }
}
```

### 5.3 DTO Pattern

DTOs são usados para transferência de dados entre camadas:

```java
public class CursoDTO {
    private Long id;
    private String nome;
    
    // constructors, getters, setters
}

public class CursoInputDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    // constructors, getters, setters
}
```

### 5.4 Exception Handling

Exceções customizadas para tratamento de erros:

```java
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {
    
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof EntidadeNaoEncontradaException) {
            return Response.status(404)
                    .entity(new ErroDTO("ENTIDADE_NAO_ENCONTRADA", exception.getMessage()))
                    .build();
        }
        if (exception instanceof ValidacaoException) {
            return Response.status(400)
                    .entity(new ErroDTO("ERRO_VALIDACAO", exception.getMessage()))
                    .build();
        }
        // ...
    }
}
```

## 6. Configuração JPA

### 6.1 persistence.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence
             https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">

    <persistence-unit name="faculdadePU" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        
        <class>com.faculdade.media.domain.Curso</class>
        <class>com.faculdade.media.domain.Disciplina</class>
        <class>com.faculdade.media.domain.Aluno</class>
        <class>com.faculdade.media.domain.Professor</class>
        <class>com.faculdade.media.domain.Matricula</class>
        <class>com.faculdade.media.domain.Nota</class>
        
        <properties>
            <!-- Database connection -->
            <property name="jakarta.persistence.jdbc.driver" value="org.sqlite.JDBC"/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:sqlite:faculdade.db"/>
            
            <!-- Hibernate properties -->
            <property name="hibernate.dialect" value="org.hibernate.community.dialect.SQLiteDialect"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.show_sql" value="false"/>
            <property name="hibernate.format_sql" value="true"/>
        </properties>
    </persistence-unit>
</persistence>
```

### 6.2 Estratégia de Schema

- **Desenvolvimento**: `update` - atualiza schema automaticamente
- **Produção**: `validate` - apenas valida schema
- **Testes**: `create-drop` - cria e remove schema a cada execução

## 7. Documentação Swagger/OpenAPI

### 7.1 Annotations em Controllers

```java
@Path("/cursos")
@Tag(name = "Cursos", description = "Operações relacionadas a cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoController {
    
    @GET
    @Operation(
        summary = "Listar todos os cursos",
        description = "Retorna uma lista de todos os cursos cadastrados"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de cursos retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CursoDTO.class)
        )
    )
    public Response listar() {
        // implementação
    }
}
```

### 7.2 Annotations em DTOs

```java
@Schema(description = "Representa um curso no sistema")
public class CursoDTO {
    
    @Schema(description = "ID único do curso", example = "1")
    private Long id;
    
    @Schema(
        description = "Nome do curso",
        example = "Ciência da Computação",
        maxLength = 100
    )
    private String nome;
}
```

## 8. Regras de Negócio

### 8.1 Cálculo de Média

- **Fórmula**: `Média = (Nota 1 + Nota 2) / 2`
- **Precisão**: 2 casas decimais
- **Arredondamento**: HALF_UP (0.5 arredonda para cima)
- **Trigger**: Automático via `@PrePersist` e `@PreUpdate`

### 8.2 Classificação de Aprovação

- **Aprovado**: Média >= 7.0
- **Exame**: Média >= 5.0 e < 7.0
- **Reprovado**: Média < 5.0
- **Trigger**: Automático após cálculo da média

### 8.3 Validações de Integridade Referencial

- Curso não pode ser excluído se possuir disciplinas vinculadas
- Professor não pode ser excluído se possuir disciplinas vinculadas
- Disciplina não pode ser excluída se possuir alunos matriculados ou notas registradas
- Aluno não pode ser excluído se possuir notas registradas (política a definir)

### 8.4 Validações de Unicidade

- Nome de curso: único no sistema
- Registro de professor: único no sistema
- Matrícula de aluno: única no sistema
- Nome de disciplina: único dentro do mesmo curso
- Matrícula (aluno, disciplina): única (evita duplicatas)
- Nota (aluno, disciplina): única (um registro por aluno/disciplina)

## 9. Convenções de Código

### 9.1 Nomenclatura

- **Classes**: PascalCase (ex: `CursoService`)
- **Métodos**: camelCase (ex: `criarCurso`)
- **Variáveis**: camelCase (ex: `nomeCurso`)
- **Constantes**: UPPER_SNAKE_CASE (ex: `MAX_NOME_LENGTH`)
- **Pacotes**: lowercase (ex: `com.faculdade.media`)

### 9.2 Estrutura de Métodos

```java
public class CursoService {
    // 1. Métodos públicos (API)
    public Curso criar(CursoInputDTO dto) {
        // 2. Validações
        validar(dto);
        
        // 3. Transformação DTO -> Entity
        Curso curso = converter(dto);
        
        // 4. Persistência
        return cursoRepository.save(curso);
    }
    
    // 5. Métodos privados (helpers)
    private void validar(CursoInputDTO dto) {
        // validações
    }
}
```

## 10. Exemplo de Implementação Completa

### 10.1 Controller

```java
@Path("/cursos")
@Tag(name = "Cursos")
public class CursoController {
    
    private CursoService cursoService;
    
    @POST
    @Operation(summary = "Criar novo curso")
    public Response criar(@Valid CursoInputDTO dto) {
        Curso curso = cursoService.criar(dto);
        return Response.status(201)
                .entity(converterParaDTO(curso))
                .build();
    }
    
    @GET
    @Operation(summary = "Listar cursos")
    public Response listar() {
        List<Curso> cursos = cursoService.listar();
        return Response.ok(converterParaDTOs(cursos)).build();
    }
}
```

### 10.2 Service

```java
public class CursoService {
    
    private CursoRepository cursoRepository;
    
    public Curso criar(CursoInputDTO dto) {
        validarNomeUnico(dto.getNome());
        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        return cursoRepository.save(curso);
    }
    
    public List<Curso> listar() {
        return cursoRepository.findAll();
    }
    
    private void validarNomeUnico(String nome) {
        if (cursoRepository.existsByNome(nome)) {
            throw new ValidacaoException("Nome de curso já existe");
        }
    }
}
```

### 10.3 Repository

```java
public class CursoRepository {
    
    private EntityManager em;
    
    public Curso save(Curso curso) {
        if (curso.getId() == null) {
            em.persist(curso);
        } else {
            curso = em.merge(curso);
        }
        return curso;
    }
    
    public List<Curso> findAll() {
        return em.createQuery("SELECT c FROM Curso c", Curso.class)
                .getResultList();
    }
    
    public boolean existsByNome(String nome) {
        Long count = em.createQuery(
            "SELECT COUNT(c) FROM Curso c WHERE c.nome = :nome", Long.class)
            .setParameter("nome", nome)
            .getSingleResult();
        return count > 0;
    }
}
```

## 11. Considerações de Performance

### 11.1 Lazy Loading

- Relacionamentos `@ManyToOne` e `@OneToMany` devem usar `FetchType.LAZY`
- Evitar N+1 queries usando `JOIN FETCH` quando necessário

### 11.2 Queries Otimizadas

```java
// Evitar N+1
@Query("SELECT d FROM Disciplina d JOIN FETCH d.curso JOIN FETCH d.professor")
List<Disciplina> findAllComRelacionamentos();
```

## 12. Segurança (Futuro)

- Autenticação e autorização (fora do escopo do MVP)
- Validação de entrada (implementada via Jakarta Validation)
- Sanitização de dados (implementada via Jakarta Validation)

---

**Criado por:** Software Architect  
**Data:** Fase P3 - Solutioning  
**Status:** ✅ APROVADO - Pronto para P4 (Implementation)
