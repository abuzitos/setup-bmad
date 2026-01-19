package com.faculdade.media.functional;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Matricula;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.MatriculaDTO;
import com.faculdade.media.dto.MatriculaInputDTO;
import com.faculdade.media.service.MatriculaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes funcionais para MatriculaController.
 */
@DisplayName("Testes Funcionais - MatriculaController")
class MatriculaControllerTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private MatriculaService matriculaService;
    private Aluno aluno;
    private Disciplina disciplina;
    private Curso curso;
    private Professor professor;
    
    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        
        matriculaService = new MatriculaService();
        Field emField = MatriculaService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(matriculaService, em);
        
        // Criar curso, professor, disciplina e aluno para os testes
        em.getTransaction().begin();
        curso = new Curso("Ciência da Computação");
        em.persist(curso);
        professor = new Professor("João Silva", "PROF001");
        em.persist(professor);
        disciplina = new Disciplina("Programação Orientada a Objetos", curso, professor);
        em.persist(disciplina);
        aluno = new Aluno("Pedro Oliveira", "2024001");
        em.persist(aluno);
        em.getTransaction().commit();
        em.clear();
    }
    
    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
    
    @Test
    @DisplayName("POST /api/matriculas deve criar matrícula com sucesso")
    void deveCriarMatriculaViaAPI() {
        // Given
        MatriculaInputDTO inputDTO = new MatriculaInputDTO(aluno.getId(), disciplina.getId());
        em.getTransaction().begin();
        
        // When
        MatriculaDTO resultado = matriculaService.matricular(inputDTO);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getAlunoId()).isEqualTo(aluno.getId());
        assertThat(resultado.getDisciplinaId()).isEqualTo(disciplina.getId());
    }
    
    @Test
    @DisplayName("POST /api/disciplinas/{id}/alunos/{id} deve criar matrícula com sucesso")
    void deveCriarMatriculaViaAPIPathParams() {
        // Given
        em.getTransaction().begin();
        
        // When
        MatriculaDTO resultado = matriculaService.matricular(aluno.getId(), disciplina.getId());
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getAlunoId()).isEqualTo(aluno.getId());
        assertThat(resultado.getDisciplinaId()).isEqualTo(disciplina.getId());
    }
    
    @Test
    @DisplayName("GET /api/matriculas deve listar todas as matrículas")
    void deveListarMatriculasViaAPI() {
        // Given
        em.getTransaction().begin();
        matriculaService.matricular(aluno.getId(), disciplina.getId());
        Disciplina disciplina2 = new Disciplina("Estrutura de Dados", curso, professor);
        em.persist(disciplina2);
        matriculaService.matricular(aluno.getId(), disciplina2.getId());
        em.getTransaction().commit();
        em.clear();
        
        // When
        var matriculas = matriculaService.listarTodos();
        
        // Then
        assertThat(matriculas).isNotNull();
        assertThat(matriculas.size()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("GET /api/matriculas?alunoId={id} deve filtrar por aluno")
    void deveFiltrarMatriculasPorAlunoViaAPI() {
        // Given
        Aluno outroAluno = new Aluno("Ana Costa", "2024002");
        em.getTransaction().begin();
        em.persist(outroAluno);
        matriculaService.matricular(aluno.getId(), disciplina.getId());
        Disciplina disciplina2 = new Disciplina("Estrutura de Dados", curso, professor);
        em.persist(disciplina2);
        matriculaService.matricular(aluno.getId(), disciplina2.getId());
        matriculaService.matricular(outroAluno.getId(), disciplina.getId());
        em.getTransaction().commit();
        em.clear();
        
        // When
        var matriculas = matriculaService.listarPorAluno(aluno.getId());
        
        // Then
        assertThat(matriculas).isNotNull();
        assertThat(matriculas.size()).isGreaterThanOrEqualTo(2);
        assertThat(matriculas).allMatch(m -> m.getAlunoId().equals(aluno.getId()));
    }
    
    @Test
    @DisplayName("GET /api/matriculas?disciplinaId={id} deve filtrar por disciplina")
    void deveFiltrarMatriculasPorDisciplinaViaAPI() {
        // Given
        Aluno outroAluno = new Aluno("Ana Costa", "2024002");
        Disciplina disciplina2 = new Disciplina("Estrutura de Dados", curso, professor);
        em.getTransaction().begin();
        em.persist(outroAluno);
        em.persist(disciplina2);
        matriculaService.matricular(aluno.getId(), disciplina.getId());
        matriculaService.matricular(outroAluno.getId(), disciplina.getId());
        matriculaService.matricular(aluno.getId(), disciplina2.getId());
        em.getTransaction().commit();
        em.clear();
        
        // When
        var matriculas = matriculaService.listarPorDisciplina(disciplina.getId());
        
        // Then
        assertThat(matriculas).isNotNull();
        assertThat(matriculas.size()).isGreaterThanOrEqualTo(2);
        assertThat(matriculas).allMatch(m -> m.getDisciplinaId().equals(disciplina.getId()));
    }
    
    @Test
    @DisplayName("DELETE /api/disciplinas/{id}/alunos/{id} deve desmatricular aluno com sucesso")
    void deveDesmatricularAlunoViaAPI() {
        // Given
        em.getTransaction().begin();
        MatriculaDTO matriculaCriada = matriculaService.matricular(aluno.getId(), disciplina.getId());
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        matriculaService.desmatricular(aluno.getId(), disciplina.getId());
        em.getTransaction().commit();
        em.clear();
        
        // Then - deve lançar exceção ao tentar desmatricular novamente
        assertThatThrownBy(() -> {
            em.getTransaction().begin();
            matriculaService.desmatricular(aluno.getId(), disciplina.getId());
            em.getTransaction().commit();
        }).isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }
}
