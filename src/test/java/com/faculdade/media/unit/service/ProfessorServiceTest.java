package com.faculdade.media.unit.service;

import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.ProfessorDTO;
import com.faculdade.media.dto.ProfessorInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.service.ProfessorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testes Unitários - ProfessorService")
class ProfessorServiceTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityTransaction transaction;
    
    @InjectMocks
    private ProfessorService professorService;
    
    private ProfessorInputDTO inputDTO;
    private Professor professor;
    
    @BeforeEach
    void setUp() throws Exception {
        // Injetar EntityManager mockado no service usando reflection
        Field emField = ProfessorService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(professorService, em);
        
        // Configurar mocks básicos
        when(em.getTransaction()).thenReturn(transaction);
        
        inputDTO = new ProfessorInputDTO("João Silva", "PROF001");
        professor = new Professor("João Silva", "PROF001");
        professor.setId(1L);
    }
    
    @Test
    @DisplayName("Deve criar professor com dados válidos")
    void deveCriarProfessorComDadosValidos() {
        // Given
        doAnswer(invocation -> {
            Professor p = invocation.getArgument(0);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        }).when(em).persist(any(Professor.class));
        
        // Mock existsByRegistro
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Não existe
            return query;
        });
        
        // When
        ProfessorDTO resultado = professorService.criar(inputDTO);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getRegistro()).isEqualTo("PROF001");
        verify(em, atLeastOnce()).persist(any(Professor.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando inputDTO é nulo")
    void deveLancarExcecaoQuandoInputDTONulo() {
        // When/Then
        assertThatThrownBy(() -> professorService.criar(null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Dados do professor não podem ser nulos");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        // Given
        ProfessorInputDTO inputComNomeNulo = new ProfessorInputDTO(null, "PROF001");
        
        // When/Then
        assertThatThrownBy(() -> professorService.criar(inputComNomeNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Nome do professor é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando registro é nulo")
    void deveLancarExcecaoQuandoRegistroNulo() {
        // Given
        ProfessorInputDTO inputComRegistroNulo = new ProfessorInputDTO("João Silva", null);
        
        // When/Then
        assertThatThrownBy(() -> professorService.criar(inputComRegistroNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Registro do professor é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome excede 100 caracteres")
    void deveLancarExcecaoQuandoNomeExcede100Caracteres() {
        // Given
        String nomeLongo = "A".repeat(101);
        ProfessorInputDTO inputComNomeLongo = new ProfessorInputDTO(nomeLongo, "PROF001");
        
        // When/Then
        assertThatThrownBy(() -> {
            if (inputComNomeLongo.getNome() != null && inputComNomeLongo.getNome().length() > 100) {
                throw new ValidacaoException("Nome do professor deve ter no máximo 100 caracteres");
            }
            professorService.criar(inputComNomeLongo);
        }).isInstanceOf(ValidacaoException.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando registro excede 20 caracteres")
    void deveLancarExcecaoQuandoRegistroExcede20Caracteres() {
        // Given
        String registroLongo = "A".repeat(21);
        ProfessorInputDTO inputComRegistroLongo = new ProfessorInputDTO("João Silva", registroLongo);
        
        // When/Then
        assertThatThrownBy(() -> {
            if (inputComRegistroLongo.getRegistro() != null && inputComRegistroLongo.getRegistro().length() > 20) {
                throw new ValidacaoException("Registro do professor deve ter no máximo 20 caracteres");
            }
            professorService.criar(inputComRegistroLongo);
        }).isInstanceOf(ValidacaoException.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando registro já existe")
    void deveLancarExcecaoQuandoRegistroJaExiste() {
        // Given
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Já existe
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> professorService.criar(inputDTO))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe um professor com o registro");
    }
    
    @Test
    @DisplayName("Deve buscar professor por ID com sucesso")
    void deveBuscarProfessorPorIdComSucesso() {
        // Given
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        
        // When
        ProfessorDTO resultado = professorService.buscarPorId(1L);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getRegistro()).isEqualTo("PROF001");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando professor não encontrado")
    void deveLancarExcecaoQuandoProfessorNaoEncontrado() {
        // Given
        when(em.find(Professor.class, 999L)).thenReturn(null);
        
        // When/Then
        assertThatThrownBy(() -> professorService.buscarPorId(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Professor não encontrado");
    }
    
    @Test
    @DisplayName("Deve listar todos os professores")
    void deveListarTodosOsProfessores() {
        // Given
        List<Professor> professores = new ArrayList<>();
        professores.add(professor);
        Professor outroProfessor = new Professor("Maria Santos", "PROF002");
        outroProfessor.setId(2L);
        professores.add(outroProfessor);
        
        when(em.createQuery(anyString(), eq(Professor.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.getResultList()).thenReturn(professores);
            return query;
        });
        
        // When
        List<ProfessorDTO> resultado = professorService.listarTodos();
        
        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("João Silva");
        assertThat(resultado.get(1).getNome()).isEqualTo("Maria Santos");
    }
    
    @Test
    @DisplayName("Deve atualizar professor com sucesso")
    void deveAtualizarProfessorComSucesso() {
        // Given
        ProfessorInputDTO inputAtualizado = new ProfessorInputDTO("Maria Santos", "PROF002");
        
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Registro não existe em outro professor
            return query;
        });
        when(em.merge(any(Professor.class))).thenReturn(professor);
        
        // When
        ProfessorDTO resultado = professorService.atualizar(1L, inputAtualizado);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Maria Santos");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar professor com registro duplicado")
    void deveLancarExcecaoAoAtualizarProfessorComRegistroDuplicado() {
        // Given
        ProfessorInputDTO inputAtualizado = new ProfessorInputDTO("João Silva", "PROF002");
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Registro já existe em outro professor
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> professorService.atualizar(1L, inputAtualizado))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe outro professor com o registro");
    }
    
    @Test
    @DisplayName("Deve remover professor com sucesso")
    void deveRemoverProfessorComSucesso() {
        // Given
        professor.setDisciplinas(new ArrayList<>());
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.contains(any(Professor.class))).thenReturn(true);
        doNothing().when(em).remove(any(Professor.class));
        
        // When
        professorService.remover(1L);
        
        // Then
        verify(em).remove(professor);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover professor com disciplinas vinculadas")
    void deveLancarExcecaoAoRemoverProfessorComDisciplinas() {
        // Given
        List<Disciplina> disciplinas = new ArrayList<>();
        Disciplina disciplina = new Disciplina();
        disciplinas.add(disciplina);
        professor.setDisciplinas(disciplinas);
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        
        // When/Then
        assertThatThrownBy(() -> professorService.remover(1L))
                .isInstanceOf(IntegridadeReferencialException.class)
                .hasMessageContaining("Não é possível remover o professor pois existem disciplinas vinculadas");
        verify(em, never()).remove(any(Professor.class));
    }
}
