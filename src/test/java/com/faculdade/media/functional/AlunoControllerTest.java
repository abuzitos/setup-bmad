package com.faculdade.media.functional;

import com.faculdade.media.dto.AlunoDTO;
import com.faculdade.media.dto.AlunoInputDTO;
import com.faculdade.media.service.AlunoService;
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
 * Testes funcionais para AlunoController.
 */
@DisplayName("Testes Funcionais - AlunoController")
class AlunoControllerTest {
    
    private EntityManagerFactory emf;
    private EntityManager em;
    private AlunoService alunoService;
    
    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        
        alunoService = new AlunoService();
        Field emField = AlunoService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(alunoService, em);
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
    @DisplayName("POST /api/alunos deve criar aluno com sucesso")
    void deveCriarAlunoViaAPI() {
        // Given
        AlunoInputDTO inputDTO = new AlunoInputDTO("Pedro Oliveira", "2024001");
        em.getTransaction().begin();
        
        // When
        AlunoDTO resultado = alunoService.criar(inputDTO);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Pedro Oliveira");
        assertThat(resultado.getMatricula()).isEqualTo("2024001");
    }
    
    @Test
    @DisplayName("GET /api/alunos deve listar todos os alunos")
    void deveListarAlunosViaAPI() {
        // Given
        em.getTransaction().begin();
        alunoService.criar(new AlunoInputDTO("Pedro Oliveira", "2024001"));
        alunoService.criar(new AlunoInputDTO("Ana Costa", "2024002"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        var alunos = alunoService.listarTodos();
        
        // Then
        assertThat(alunos).isNotNull();
        assertThat(alunos.size()).isGreaterThanOrEqualTo(2);
    }
    
    @Test
    @DisplayName("GET /api/alunos/{id} deve retornar aluno encontrado")
    void deveBuscarAlunoPorIdViaAPI() {
        // Given
        em.getTransaction().begin();
        AlunoDTO alunoCriado = alunoService.criar(new AlunoInputDTO("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        AlunoDTO resultado = alunoService.buscarPorId(alunoCriado.getId());
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(alunoCriado.getId());
        assertThat(resultado.getNome()).isEqualTo("Pedro Oliveira");
    }
    
    @Test
    @DisplayName("PUT /api/alunos/{id} deve atualizar aluno com sucesso")
    void deveAtualizarAlunoViaAPI() {
        // Given
        em.getTransaction().begin();
        AlunoDTO alunoCriado = alunoService.criar(new AlunoInputDTO("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        AlunoInputDTO inputAtualizado = new AlunoInputDTO("Pedro Oliveira Silva", "2024001");
        
        // When
        em.getTransaction().begin();
        AlunoDTO resultado = alunoService.atualizar(alunoCriado.getId(), inputAtualizado);
        em.getTransaction().commit();
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Pedro Oliveira Silva");
    }
    
    @Test
    @DisplayName("DELETE /api/alunos/{id} deve remover aluno com sucesso")
    void deveExcluirAlunoViaAPI() {
        // Given
        em.getTransaction().begin();
        AlunoDTO alunoCriado = alunoService.criar(new AlunoInputDTO("Pedro Oliveira", "2024001"));
        em.getTransaction().commit();
        em.clear();
        
        // When
        em.getTransaction().begin();
        alunoService.remover(alunoCriado.getId());
        em.getTransaction().commit();
        em.clear();
        
        // Then - deve lançar exceção ao buscar aluno removido
        assertThatThrownBy(() -> alunoService.buscarPorId(alunoCriado.getId()))
                .isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }
}
