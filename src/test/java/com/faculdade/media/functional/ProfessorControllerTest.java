package com.faculdade.media.functional;

import com.faculdade.media.dto.ProfessorDTO;
import com.faculdade.media.dto.ProfessorInputDTO;
import com.faculdade.media.service.ProfessorService;
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
 * Testes funcionais para ProfessorController.
 */
@DisplayName("Testes Funcionais - ProfessorController")
class ProfessorControllerTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private ProfessorService professorService;
    
    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        
        professorService = new ProfessorService();
        Field emField = ProfessorService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(professorService, em);
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
    @DisplayName("POST /api/professores deve criar professor com sucesso")
    void deveCriarProfessorViaAPI() {
        // Given
        ProfessorInputDTO inputDTO = new ProfessorInputDTO("João Silva", "PROF001");
        em.getTransaction().begin();
        
        // When
        ProfessorDTO resultado = professorService.criar(inputDTO);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getRegistro()).isEqualTo("PROF001");
    }
    
    @Test
    @DisplayName("GET /api/professores deve listar todos os professores")
    void deveListarProfessoresViaAPI() {
        // Given
        em.getTransaction().begin();
        professorService.criar(new ProfessorInputDTO("João Silva", "PROF001"));
        professorService.criar(new ProfessorInputDTO("Maria Santos", "PROF002"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var professores = professorService.listarTodos();
        
        // Then
        assertThat(professores).isNotNull();
        assertThat(professores.size()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("GET /api/professores/{id} deve retornar professor encontrado")
    void deveBuscarProfessorPorIdViaAPI() {
        // Given
        em.getTransaction().begin();
        ProfessorDTO professorCriado = professorService.criar(new ProfessorInputDTO("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        ProfessorDTO resultado = professorService.buscarPorId(professorCriado.getId());
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(professorCriado.getId());
        assertThat(resultado.getNome()).isEqualTo("João Silva");
    }
    
    @Test
    @DisplayName("PUT /api/professores/{id} deve atualizar professor com sucesso")
    void deveAtualizarProfessorViaAPI() {
        // Given
        em.getTransaction().begin();
        ProfessorDTO professorCriado = professorService.criar(new ProfessorInputDTO("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        ProfessorInputDTO inputAtualizado = new ProfessorInputDTO("João Silva Santos", "PROF001");
        
        // When
        em.getTransaction().begin();
        ProfessorDTO resultado = professorService.atualizar(professorCriado.getId(), inputAtualizado);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva Santos");
    }
    
    @Test
    @DisplayName("DELETE /api/professores/{id} deve remover professor com sucesso")
    void deveExcluirProfessorViaAPI() {
        // Given
        em.getTransaction().begin();
        ProfessorDTO professorCriado = professorService.criar(new ProfessorInputDTO("João Silva", "PROF001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        professorService.remover(professorCriado.getId());
        em.getTransaction().commit();
        em.clear();
        
        // Then - deve lançar exceção ao buscar professor removido
        assertThatThrownBy(() -> professorService.buscarPorId(professorCriado.getId()))
                .isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }
}
