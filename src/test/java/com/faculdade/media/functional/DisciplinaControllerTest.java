package com.faculdade.media.functional;

import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.DisciplinaDTO;
import com.faculdade.media.dto.DisciplinaInputDTO;
import com.faculdade.media.service.DisciplinaService;
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
 * Testes funcionais para DisciplinaController.
 */
@DisplayName("Testes Funcionais - DisciplinaController")
class DisciplinaControllerTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private DisciplinaService disciplinaService;
    private Curso curso;
    private Professor professor;
    
    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        
        disciplinaService = new DisciplinaService();
        Field emField = DisciplinaService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(disciplinaService, em);
        
        // Criar curso e professor para os testes
        em.getTransaction().begin();
        curso = new Curso("Ciência da Computação");
        em.persist(curso);
        professor = new Professor("João Silva", "PROF001");
        em.persist(professor);
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
    @DisplayName("POST /api/disciplinas deve criar disciplina com sucesso")
    void deveCriarDisciplinaViaAPI() {
        // Given
        DisciplinaInputDTO inputDTO = new DisciplinaInputDTO("Programação Orientada a Objetos", curso.getId(), professor.getId());
        em.getTransaction().begin();
        
        // When
        DisciplinaDTO resultado = disciplinaService.criar(inputDTO);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");
        assertThat(resultado.getCursoId()).isEqualTo(curso.getId());
        assertThat(resultado.getProfessorId()).isEqualTo(professor.getId());
    }
    
    @Test
    @DisplayName("GET /api/disciplinas deve listar todas as disciplinas")
    void deveListarDisciplinasViaAPI() {
        // Given
        em.getTransaction().begin();
        disciplinaService.criar(new DisciplinaInputDTO("POO", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("ED", curso.getId(), professor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var disciplinas = disciplinaService.listarTodos();
        
        // Then
        assertThat(disciplinas).isNotNull();
        assertThat(disciplinas.size()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("GET /api/disciplinas?cursoId={id} deve filtrar por curso")
    void deveFiltrarDisciplinasPorCursoViaAPI() {
        // Given
        Curso outroCurso = new Curso("Engenharia de Software");
        em.getTransaction().begin();
        em.persist(outroCurso);
        disciplinaService.criar(new DisciplinaInputDTO("POO", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("ED", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("BD", outroCurso.getId(), professor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var disciplinas = disciplinaService.listarPorCurso(curso.getId());
        
        // Then
        assertThat(disciplinas).isNotNull();
        assertThat(disciplinas.size()).isGreaterThanOrEqualTo(2);
        assertThat(disciplinas).allMatch(d -> d.getCursoId().equals(curso.getId()));
    }
    
    @Test
    @DisplayName("GET /api/disciplinas?professorId={id} deve filtrar por professor")
    void deveFiltrarDisciplinasPorProfessorViaAPI() {
        // Given
        Professor outroProfessor = new Professor("Maria Santos", "PROF002");
        em.getTransaction().begin();
        em.persist(outroProfessor);
        disciplinaService.criar(new DisciplinaInputDTO("POO", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("ED", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("BD", curso.getId(), outroProfessor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var disciplinas = disciplinaService.listarPorProfessor(professor.getId());
        
        // Then
        assertThat(disciplinas).isNotNull();
        assertThat(disciplinas.size()).isGreaterThanOrEqualTo(2);
        assertThat(disciplinas).allMatch(d -> d.getProfessorId().equals(professor.getId()));
    }
    
    @Test
    @DisplayName("GET /api/disciplinas?cursoId={id}&professorId={id} deve filtrar por ambos")
    void deveFiltrarDisciplinasPorCursoEPProfessorViaAPI() {
        // Given
        Professor outroProfessor = new Professor("Maria Santos", "PROF002");
        em.getTransaction().begin();
        em.persist(outroProfessor);
        disciplinaService.criar(new DisciplinaInputDTO("POO", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("ED", curso.getId(), professor.getId()));
        disciplinaService.criar(new DisciplinaInputDTO("BD", curso.getId(), outroProfessor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var disciplinas = disciplinaService.listarPorCursoEPProfessor(curso.getId(), professor.getId());
        
        // Then
        assertThat(disciplinas).isNotNull();
        assertThat(disciplinas.size()).isGreaterThanOrEqualTo(2);
        assertThat(disciplinas).allMatch(d -> 
            d.getCursoId().equals(curso.getId()) && d.getProfessorId().equals(professor.getId()));
    }
    
    @Test
    @DisplayName("GET /api/disciplinas/{id} deve retornar disciplina encontrada")
    void deveBuscarDisciplinaPorIdViaAPI() {
        // Given
        em.getTransaction().begin();
        DisciplinaDTO disciplinaCriada = disciplinaService.criar(
            new DisciplinaInputDTO("Programação Orientada a Objetos", curso.getId(), professor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        DisciplinaDTO resultado = disciplinaService.buscarPorId(disciplinaCriada.getId());
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(disciplinaCriada.getId());
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");
    }
    
    @Test
    @DisplayName("PUT /api/disciplinas/{id} deve atualizar disciplina com sucesso")
    void deveAtualizarDisciplinaViaAPI() {
        // Given
        em.getTransaction().begin();
        DisciplinaDTO disciplinaCriada = disciplinaService.criar(
            new DisciplinaInputDTO("POO", curso.getId(), professor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        DisciplinaInputDTO inputAtualizado = new DisciplinaInputDTO(
            "Programação Orientada a Objetos", curso.getId(), professor.getId());
        
        // When
        em.getTransaction().begin();
        DisciplinaDTO resultado = disciplinaService.atualizar(disciplinaCriada.getId(), inputAtualizado);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");
    }
    
    @Test
    @DisplayName("DELETE /api/disciplinas/{id} deve remover disciplina com sucesso")
    void deveExcluirDisciplinaViaAPI() {
        // Given
        em.getTransaction().begin();
        DisciplinaDTO disciplinaCriada = disciplinaService.criar(
            new DisciplinaInputDTO("Programação Orientada a Objetos", curso.getId(), professor.getId()));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        disciplinaService.remover(disciplinaCriada.getId());
        em.getTransaction().commit();
        em.clear();
        
        // Then - deve lançar exceção ao buscar disciplina removida
        assertThatThrownBy(() -> disciplinaService.buscarPorId(disciplinaCriada.getId()))
                .isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }
}
