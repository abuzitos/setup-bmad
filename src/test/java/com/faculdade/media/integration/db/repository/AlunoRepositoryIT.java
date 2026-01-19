package com.faculdade.media.integration.db.repository;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.repository.AlunoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Testes de Integração - AlunoRepository")
class AlunoRepositoryIT {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private AlunoRepository repository;
    
    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        repository = new AlunoRepository(em);
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
    @DisplayName("Deve salvar e recuperar aluno do banco de dados")
    void deveSalvarERecuperarAluno() {
        // Given
        Aluno aluno = new Aluno("Pedro Oliveira", "2024001");
        
        // When
        aluno = repository.save(aluno);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var alunoRecuperado = repository.findById(aluno.getId());
        assertThat(alunoRecuperado).isPresent();
        assertThat(alunoRecuperado.get().getNome()).isEqualTo("Pedro Oliveira");
        assertThat(alunoRecuperado.get().getMatricula()).isEqualTo("2024001");
    }
    
    @Test
    @DisplayName("Deve verificar se matrícula de aluno existe")
    void deveVerificarSeMatriculaExiste() {
        // Given
        repository.save(new Aluno("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        assertThat(repository.existsByMatricula("2024001")).isTrue();
        assertThat(repository.existsByMatricula("2024002")).isFalse();
    }
    
    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        // Given
        Aluno aluno1 = repository.save(new Aluno("Pedro Oliveira", "2024001"));
        Aluno aluno2 = repository.save(new Aluno("Ana Costa", "2024002"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var alunos = repository.findAll();
        
        // Then
        assertThat(alunos).hasSize(2);
        assertThat(alunos).extracting(Aluno::getNome)
                .containsExactlyInAnyOrder("Pedro Oliveira", "Ana Costa");
    }
    
    @Test
    @DisplayName("Deve falhar ao criar aluno com matrícula duplicada")
    void deveFalharAoCriarAlunoComMatriculaDuplicada() {
        // Given
        repository.save(new Aluno("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        em.getTransaction().begin();
        assertThatThrownBy(() -> {
            Aluno aluno2 = new Aluno("Ana Costa", "2024001");
            repository.save(aluno2);
            em.getTransaction().commit();
        }).isInstanceOf(Exception.class);
        
        em.getTransaction().rollback();
    }
    
    @Test
    @DisplayName("Deve atualizar aluno")
    void deveAtualizarAluno() {
        // Given
        Aluno aluno = repository.save(new Aluno("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var alunoRecuperado = repository.findById(aluno.getId()).orElseThrow();
        alunoRecuperado.setNome("Pedro Oliveira Silva");
        repository.save(alunoRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        var alunoAtualizado = repository.findById(aluno.getId());
        assertThat(alunoAtualizado).isPresent();
        assertThat(alunoAtualizado.get().getNome()).isEqualTo("Pedro Oliveira Silva");
    }
    
    @Test
    @DisplayName("Deve remover aluno")
    void deveRemoverAluno() {
        // Given
        Aluno aluno = repository.save(new Aluno("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        var alunoRecuperado = repository.findById(aluno.getId()).orElseThrow();
        repository.delete(alunoRecuperado);
        em.getTransaction().commit();
        em.clear();
        
        // Then
        assertThat(repository.findById(aluno.getId())).isEmpty();
    }
    
    @Test
    @DisplayName("Deve verificar existsByMatriculaExcluindoId")
    void deveVerificarExistsByMatriculaExcluindoId() {
        // Given
        Aluno aluno1 = repository.save(new Aluno("Pedro Oliveira", "2024001"));
        Aluno aluno2 = repository.save(new Aluno("Ana Costa", "2024002"));
        em.getTransaction().commit();
        em.clear();
        
        // When/Then
        assertThat(repository.existsByMatriculaExcluindoId("2024001", aluno1.getId())).isFalse();
        assertThat(repository.existsByMatriculaExcluindoId("2024001", aluno2.getId())).isTrue();
        assertThat(repository.existsByMatriculaExcluindoId("2024003", aluno1.getId())).isFalse();
    }
}
