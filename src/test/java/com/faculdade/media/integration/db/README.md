# Testes de Integração com Banco de Dados

## Descrição
Testes de integração que usam banco de dados SQLite em memória para testar persistência, queries JPA e operações de banco de dados reais.

## Estrutura
- **Framework**: JUnit 5 + SQLite in-memory
- **Padrão**: Database integration
- **Nomenclatura**: `*IT.java`
- **Localização**: `src/test/java/**/integration/db/`
- **Configuração**: `src/test/resources/META-INF/persistence.xml`

## Exemplo de Uso

```java
package com.faculdade.media.integration.db.repository;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.repository.AlunoRepository;
import com.faculdade.media.repository.CursoRepository;
import com.faculdade.media.repository.DisciplinaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração com Banco de Dados")
class AlunoRepositoryDBIT {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private AlunoRepository alunoRepository;
    private CursoRepository cursoRepository;
    private DisciplinaRepository disciplinaRepository;
    
    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        alunoRepository = new AlunoRepository(em);
        cursoRepository = new CursoRepository(em);
        disciplinaRepository = new DisciplinaRepository(em);
        
        em.getTransaction().begin();
    }
    
    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
        emf.close();
    }
    
    @Test
    @DisplayName("Deve persistir e recuperar aluno do banco")
    void devePersistirERecuperarAlunoDoBanco() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setMatricula("2024001");
        
        // When
        alunoRepository.save(aluno);
        em.getTransaction().commit();
        em.clear();
        
        Aluno encontrado = alunoRepository.findById(aluno.getId());
        
        // Then
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo("João Silva");
        assertThat(encontrado.getMatricula()).isEqualTo("2024001");
    }
    
    @Test
    @DisplayName("Deve matricular aluno em disciplina e persistir relacionamento")
    void deveMatricularAlunoEmDisciplina() {
        // Given
        Curso curso = new Curso();
        curso.setNome("Ciência da Computação");
        cursoRepository.save(curso);
        
        Disciplina disciplina = new Disciplina();
        disciplina.setNome("POO");
        disciplina.setCurso(curso);
        disciplinaRepository.save(disciplina);
        
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setMatricula("2024001");
        alunoRepository.save(aluno);
        
        // When
        disciplina.getAlunos().add(aluno);
        aluno.getDisciplinas().add(disciplina);
        
        em.getTransaction().commit();
        em.clear();
        
        // Then
        Aluno alunoRecuperado = alunoRepository.findById(aluno.getId());
        assertThat(alunoRecuperado.getDisciplinas()).hasSize(1);
        assertThat(alunoRecuperado.getDisciplinas().get(0).getNome()).isEqualTo("POO");
    }
}
```

## Executar Testes de Integração com Banco de Dados

```bash
# Executar apenas testes de integração com banco de dados
mvn verify -Pintegration-tests-db
```

## Configuração do Banco de Dados em Memória

O arquivo `src/test/resources/META-INF/persistence.xml` já está configurado para usar SQLite em memória:

```xml
<property name="jakarta.persistence.jdbc.url" value="jdbc:sqlite::memory:"/>
<property name="hibernate.hbm2ddl.auto" value="create-drop"/>
```
