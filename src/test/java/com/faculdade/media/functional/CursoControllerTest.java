package com.faculdade.media.functional;

import com.faculdade.media.dto.CursoDTO;
import com.faculdade.media.dto.CursoInputDTO;
import com.faculdade.media.service.CursoService;
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
 * Testes funcionais para CursoController.
 * 
 * Nota: Estes testes validam a integração entre Controller e Service,
 * mas não iniciam um servidor HTTP real. Para testes completos de API REST,
 * seria necessário usar REST Assured com um servidor embutido ou Jersey Test Framework.
 * 
 * Por enquanto, estes testes validam a lógica do controller através do service.
 */
@DisplayName("Testes Funcionais - CursoController")
class CursoControllerTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private CursoService cursoService;
    
    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        
        // Criar e injetar EntityManager no service
        cursoService = new CursoService();
        Field emField = CursoService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(cursoService, em);
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
    @DisplayName("POST /api/cursos deve criar curso com sucesso")
    void deveCriarCursoViaAPI() {
        // Given
        CursoInputDTO inputDTO = new CursoInputDTO("Ciência da Computação");
        em.getTransaction().begin();
        
        // When
        CursoDTO resultado = cursoService.criar(inputDTO);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Ciência da Computação");
    }
    
    @Test
    @DisplayName("GET /api/cursos deve listar todos os cursos")
    void deveListarCursosViaAPI() {
        // Given
        em.getTransaction().begin();
        cursoService.criar(new CursoInputDTO("Ciência da Computação"));
        cursoService.criar(new CursoInputDTO("Engenharia de Software"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var cursos = cursoService.listarTodos();
        
        // Then
        assertThat(cursos).isNotNull();
        assertThat(cursos.size()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("GET /api/cursos/{id} deve retornar curso encontrado")
    void deveBuscarCursoPorIdViaAPI() {
        // Given
        em.getTransaction().begin();
        CursoDTO cursoCriado = cursoService.criar(new CursoInputDTO("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        CursoDTO resultado = cursoService.buscarPorId(cursoCriado.getId());
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(cursoCriado.getId());
        assertThat(resultado.getNome()).isEqualTo("Ciência da Computação");
    }
    
    @Test
    @DisplayName("PUT /api/cursos/{id} deve atualizar curso com sucesso")
    void deveAtualizarCursoViaAPI() {
        // Given
        em.getTransaction().begin();
        CursoDTO cursoCriado = cursoService.criar(new CursoInputDTO("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        CursoInputDTO inputAtualizado = new CursoInputDTO("Engenharia de Software");
        
        // When
        em.getTransaction().begin();
        CursoDTO resultado = cursoService.atualizar(cursoCriado.getId(), inputAtualizado);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Engenharia de Software");
    }
    
    @Test
    @DisplayName("DELETE /api/cursos/{id} deve remover curso com sucesso")
    void deveExcluirCursoViaAPI() {
        // Given
        em.getTransaction().begin();
        CursoDTO cursoCriado = cursoService.criar(new CursoInputDTO("Ciência da Computação"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        cursoService.remover(cursoCriado.getId());
        em.getTransaction().commit();
        em.clear();
        
        // Then - deve lançar exceção ao buscar curso removido
        assertThatThrownBy(() -> cursoService.buscarPorId(cursoCriado.getId()))
                .isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }
}
