package com.faculdade.media.integration.db.repository;

import com.faculdade.media.domain.Professor;
import com.faculdade.media.repository.ProfessorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Testes de Integração - ProfessorRepository")
class ProfessorRepositoryIT {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private ProfessorRepository repository;
    
    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        repository = new ProfessorRepository(em);
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
    @DisplayName("Deve salvar e recuperar professor do banco de dados")
    void deveSalvarERecuperarProfessor() {
        // Given
        Professor professor = new Professor("João Silva", "PROF001");
        
        // When
        professor = repository.save(professor);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var professorRecuperado = repository.findById(professor.getId());
        assertThat(professorRecuperado).isPresent();
        assertThat(professorRecuperado.get().getNome()).isEqualTo("João Silva");
        assertThat(professorRecuperado.get().getRegistro()).isEqualTo("PROF001");
    }
    
    @Test
    @DisplayName("Deve verificar se registro de professor existe")
    void deveVerificarSeRegistroExiste() {
        // Given
        repository.save(new Professor("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        assertThat(repository.existsByRegistro("PROF001")).isTrue();
        assertThat(repository.existsByRegistro("PROF002")).isFalse();
    }
    
    @Test
    @DisplayName("Deve listar todos os professores")
    void deveListarTodosOsProfessores() {
        // Given
        Professor professor1 = repository.save(new Professor("João Silva", "PROF001"));
        Professor professor2 = repository.save(new Professor("Maria Santos", "PROF002"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var professores = repository.findAll();
        
        // Then
        assertThat(professores).hasSize(2);
        assertThat(professores).extracting(Professor::getNome)
                .containsExactlyInAnyOrder("João Silva", "Maria Santos");
    }
    
    @Test
    @DisplayName("Deve falhar ao criar professor com registro duplicado")
    void deveFalharAoCriarProfessorComRegistroDuplicado() {
        // Given
        repository.save(new Professor("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        em.getTransaction().begin();
        assertThatThrownBy(() -> {
            Professor professor2 = new Professor("Maria Santos", "PROF001");
            repository.save(professor2);
            em.getTransaction().commit();
        }).isInstanceOf(Exception.class);
        
        em.getTransaction().rollback();
    }
    
    @Test
    @DisplayName("Deve atualizar professor")
    void deveAtualizarProfessor() {
        // Given
        Professor professor = repository.save(new Professor("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var professorRecuperado = repository.findById(professor.getId()).orElseThrow();
        professorRecuperado.setNome("João Silva Santos");
        repository.save(professorRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var professorAtualizado = repository.findById(professor.getId());
        assertThat(professorAtualizado).isPresent();
        assertThat(professorAtualizado.get().getNome()).isEqualTo("João Silva Santos");
    }
    
    @Test
    @DisplayName("Deve remover professor")
    void deveRemoverProfessor() {
        // Given
        Professor professor = repository.save(new Professor("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var professorRecuperado = repository.findById(professor.getId()).orElseThrow();
        repository.delete(professorRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        assertThat(repository.findById(professor.getId())).isEmpty();
    }
    
    @Test
    @DisplayName("Deve verificar existsByRegistroExcluindoId")
    void deveVerificarExistsByRegistroExcluindoId() {
        // Given
        Professor professor1 = repository.save(new Professor("João Silva", "PROF001"));
        repository.save(new Professor("Maria Santos", "PROF002"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        Long professor1Id = professor1.getId();
        Professor professor2 = repository.findById(repository.findAll().stream()
                .filter(p -> !p.getId().equals(professor1Id))
                .findFirst().orElseThrow().getId()).orElseThrow();
        assertThat(repository.existsByRegistroExcluindoId("PROF001", professor1Id)).isFalse();
        assertThat(repository.existsByRegistroExcluindoId("PROF001", professor2.getId())).isTrue();
        assertThat(repository.existsByRegistroExcluindoId("PROF003", professor1Id)).isFalse();
    }
}
