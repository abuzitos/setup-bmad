package com.faculdade.media.integration.db.repository;

import com.faculdade.media.domain.Curso;
import com.faculdade.media.repository.CursoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Testes de Integração - CursoRepository")
class CursoRepositoryIT {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private CursoRepository repository;
    
    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        repository = new CursoRepository(em);
        em.getTransaction().begin();
    }
    
    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
        if (emf.isOpen()) {
            emf.close();
        }
    }
    
    @Test
    @DisplayName("Deve salvar e recuperar curso do banco de dados")
    void deveSalvarERecuperarCurso() {
        // Given
        Curso curso = new Curso("Ciência da Computação");
        
        // When
        curso = repository.save(curso);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var cursoRecuperado = repository.findById(curso.getId());
        assertThat(cursoRecuperado).isPresent();
        assertThat(cursoRecuperado.get().getNome()).isEqualTo("Ciência da Computação");
    }
    
    @Test
    @DisplayName("Deve listar todos os cursos")
    void deveListarTodosOsCursos() {
        // Given
        repository.save(new Curso("Ciência da Computação"));
        repository.save(new Curso("Engenharia de Software"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var cursos = repository.findAll();
        
        // Then
        assertThat(cursos).hasSize(2);
        assertThat(cursos).extracting(Curso::getNome)
                .containsExactlyInAnyOrder("Ciência da Computação", "Engenharia de Software");
    }
    
    @Test
    @DisplayName("Deve verificar se nome de curso existe")
    void deveVerificarSeNomeExiste() {
        // Given
        repository.save(new Curso("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        assertThat(repository.existsByNome("Ciência da Computação")).isTrue();
        assertThat(repository.existsByNome("Engenharia de Software")).isFalse();
    }
    
    @Test
    @DisplayName("Deve remover curso do banco de dados")
    void deveRemoverCurso() {
        // Given
        Curso curso = repository.save(new Curso("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var cursoRecuperado = repository.findById(curso.getId()).orElseThrow();
        repository.delete(cursoRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        assertThat(repository.findById(curso.getId())).isEmpty();
    }
    
    @Test
    @DisplayName("Deve falhar ao criar curso com nome duplicado")
    void deveFalharAoCriarCursoComNomeDuplicado() {
        // Given
        Curso curso1 = repository.save(new Curso("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        em.getTransaction().begin();
        assertThatThrownBy(() -> {
            Curso curso2 = new Curso("Ciência da Computação");
            repository.save(curso2);
            em.getTransaction().commit();
        }).isInstanceOf(Exception.class); // Pode ser PersistenceException ou ConstraintViolationException
        
        em.getTransaction().rollback();
    }
    
    @Test
    @DisplayName("Deve atualizar curso mantendo relacionamentos")
    void deveAtualizarCursoMantendoRelacionamentos() {
        // Given
        Curso curso = repository.save(new Curso("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var cursoRecuperado = repository.findById(curso.getId()).orElseThrow();
        cursoRecuperado.setNome("Engenharia de Software");
        repository.save(cursoRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var cursoAtualizado = repository.findById(curso.getId());
        assertThat(cursoAtualizado).isPresent();
        assertThat(cursoAtualizado.get().getNome()).isEqualTo("Engenharia de Software");
        assertThat(cursoAtualizado.get().getId()).isEqualTo(curso.getId());
    }
    
    @Test
    @DisplayName("Deve verificar existsByNomeExcluindoId")
    void deveVerificarExistsByNomeExcluindoId() {
        // Given
        Curso curso1 = repository.save(new Curso("Ciência da Computação"));
        Curso curso2 = repository.save(new Curso("Engenharia de Software"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        assertThat(repository.existsByNomeExcluindoId("Ciência da Computação", curso1.getId())).isFalse();
        assertThat(repository.existsByNomeExcluindoId("Ciência da Computação", curso2.getId())).isTrue();
        assertThat(repository.existsByNomeExcluindoId("Sistemas de Informação", curso1.getId())).isFalse();
    }
}
