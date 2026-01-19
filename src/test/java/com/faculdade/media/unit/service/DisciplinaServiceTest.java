package com.faculdade.media.unit.service;

import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.DisciplinaDTO;
import com.faculdade.media.dto.DisciplinaInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.service.DisciplinaService;
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
@DisplayName("Testes Unitários - DisciplinaService")
class DisciplinaServiceTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityTransaction transaction;
    
    @InjectMocks
    private DisciplinaService disciplinaService;
    
    private DisciplinaInputDTO inputDTO;
    private Curso curso;
    private Professor professor;
    private Disciplina disciplina;
    
    @BeforeEach
    void setUp() throws Exception {
        // Injetar EntityManager mockado no service usando reflection
        Field emField = DisciplinaService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(disciplinaService, em);
        
        // Configurar mocks básicos
        when(em.getTransaction()).thenReturn(transaction);
        
        curso = new Curso("Ciência da Computação");
        curso.setId(1L);
        professor = new Professor("João Silva", "PROF001");
        professor.setId(1L);
        
        inputDTO = new DisciplinaInputDTO("Programação Orientada a Objetos", 1L, 1L);
        disciplina = new Disciplina("Programação Orientada a Objetos", curso, professor);
        disciplina.setId(1L);
    }
    
    @Test
    @DisplayName("Deve criar disciplina com dados válidos")
    void deveCriarDisciplinaComDadosValidos() {
        // Given
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.contains(any())).thenReturn(false);
        doAnswer(invocation -> {
            Disciplina d = invocation.getArgument(0);
            if (d.getId() == null) {
                d.setId(1L);
            }
            return d;
        }).when(em).persist(any(Disciplina.class));
        
        // Mock existsByNomeAndCursoId
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Não existe
            return query;
        });
        
        // When
        DisciplinaDTO resultado = disciplinaService.criar(inputDTO);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");
        verify(em, atLeastOnce()).persist(any(Disciplina.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando inputDTO é nulo")
    void deveLancarExcecaoQuandoInputDTONulo() {
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Dados da disciplina não podem ser nulos");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        // Given
        DisciplinaInputDTO inputComNomeNulo = new DisciplinaInputDTO(null, 1L, 1L);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputComNomeNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Nome da disciplina é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando cursoId é nulo")
    void deveLancarExcecaoQuandoCursoIdNulo() {
        // Given
        DisciplinaInputDTO inputComCursoIdNulo = new DisciplinaInputDTO("POO", null, 1L);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputComCursoIdNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("ID do curso é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando professorId é nulo")
    void deveLancarExcecaoQuandoProfessorIdNulo() {
        // Given
        DisciplinaInputDTO inputComProfessorIdNulo = new DisciplinaInputDTO("POO", 1L, null);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputComProfessorIdNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("ID do professor é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando curso não existe")
    void deveLancarExcecaoQuandoCursoNaoExiste() {
        // Given
        when(em.find(Curso.class, 999L)).thenReturn(null);
        
        DisciplinaInputDTO inputComCursoInexistente = new DisciplinaInputDTO("POO", 999L, 1L);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputComCursoInexistente))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Curso não encontrado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando professor não existe")
    void deveLancarExcecaoQuandoProfessorNaoExiste() {
        // Given
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        when(em.find(Professor.class, 999L)).thenReturn(null);
        
        DisciplinaInputDTO inputComProfessorInexistente = new DisciplinaInputDTO("POO", 1L, 999L);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputComProfessorInexistente))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Professor não encontrado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome já existe no curso")
    void deveLancarExcecaoQuandoNomeJaExisteNoCurso() {
        // Given
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Já existe
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.criar(inputDTO))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe uma disciplina com o nome");
    }
    
    @Test
    @DisplayName("Deve buscar disciplina por ID com sucesso")
    void deveBuscarDisciplinaPorIdComSucesso() {
        // Given
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        
        // When
        DisciplinaDTO resultado = disciplinaService.buscarPorId(1L);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Programação Orientada a Objetos");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando disciplina não encontrada")
    void deveLancarExcecaoQuandoDisciplinaNaoEncontrada() {
        // Given
        when(em.find(Disciplina.class, 999L)).thenReturn(null);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.buscarPorId(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Disciplina não encontrada");
    }
    
    @Test
    @DisplayName("Deve listar todas as disciplinas")
    void deveListarTodasAsDisciplinas() {
        // Given
        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(disciplina);
        Disciplina outraDisciplina = new Disciplina("Estrutura de Dados", curso, professor);
        outraDisciplina.setId(2L);
        disciplinas.add(outraDisciplina);
        
        when(em.createQuery(anyString(), eq(Disciplina.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.getResultList()).thenReturn(disciplinas);
            return query;
        });
        
        // When
        List<DisciplinaDTO> resultado = disciplinaService.listarTodos();
        
        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Programação Orientada a Objetos");
        assertThat(resultado.get(1).getNome()).isEqualTo("Estrutura de Dados");
    }
    
    @Test
    @DisplayName("Deve listar disciplinas por curso")
    void deveListarDisciplinasPorCurso() {
        // Given
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(disciplina);
        
        when(em.createQuery(anyString(), eq(Disciplina.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getResultList()).thenReturn(disciplinas);
            return query;
        });
        
        // When
        List<DisciplinaDTO> resultado = disciplinaService.listarPorCurso(1L);
        
        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Programação Orientada a Objetos");
    }
    
    @Test
    @DisplayName("Deve listar disciplinas por professor")
    void deveListarDisciplinasPorProfessor() {
        // Given
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        List<Disciplina> disciplinas = new ArrayList<>();
        disciplinas.add(disciplina);
        
        when(em.createQuery(anyString(), eq(Disciplina.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getResultList()).thenReturn(disciplinas);
            return query;
        });
        
        // When
        List<DisciplinaDTO> resultado = disciplinaService.listarPorProfessor(1L);
        
        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Programação Orientada a Objetos");
    }
    
    @Test
    @DisplayName("Deve atualizar disciplina com sucesso")
    void deveAtualizarDisciplinaComSucesso() {
        // Given
        DisciplinaInputDTO inputAtualizado = new DisciplinaInputDTO("Estrutura de Dados", 1L, 1L);
        
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Nome não existe em outra disciplina
            return query;
        });
        when(em.merge(any(Disciplina.class))).thenReturn(disciplina);
        
        // When
        DisciplinaDTO resultado = disciplinaService.atualizar(1L, inputAtualizado);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Estrutura de Dados");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar disciplina com nome duplicado")
    void deveLancarExcecaoAoAtualizarDisciplinaComNomeDuplicado() {
        // Given
        DisciplinaInputDTO inputAtualizado = new DisciplinaInputDTO("Estrutura de Dados", 1L, 1L);
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.find(Curso.class, 1L)).thenReturn(curso);
        when(em.find(Professor.class, 1L)).thenReturn(professor);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Nome já existe em outra disciplina
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.atualizar(1L, inputAtualizado))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe outra disciplina com o nome");
    }
    
    @Test
    @DisplayName("Deve remover disciplina com sucesso")
    void deveRemoverDisciplinaComSucesso() {
        // Given
        disciplina.setMatriculas(new ArrayList<>());
        disciplina.setNotas(new ArrayList<>());
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.contains(any(Disciplina.class))).thenReturn(true);
        doNothing().when(em).remove(any(Disciplina.class));
        
        // When
        disciplinaService.remover(1L);
        
        // Then
        verify(em).remove(disciplina);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover disciplina com matrículas vinculadas")
    void deveLancarExcecaoAoRemoverDisciplinaComMatriculas() {
        // Given
        List<com.faculdade.media.domain.Matricula> matriculas = new ArrayList<>();
        com.faculdade.media.domain.Matricula matricula = new com.faculdade.media.domain.Matricula();
        matriculas.add(matricula);
        disciplina.setMatriculas(matriculas);
        disciplina.setNotas(new ArrayList<>());
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.remover(1L))
                .isInstanceOf(IntegridadeReferencialException.class)
                .hasMessageContaining("Não é possível remover a disciplina pois existem alunos matriculados");
        verify(em, never()).remove(any(Disciplina.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover disciplina com notas vinculadas")
    void deveLancarExcecaoAoRemoverDisciplinaComNotas() {
        // Given
        List<com.faculdade.media.domain.Nota> notas = new ArrayList<>();
        com.faculdade.media.domain.Nota nota = new com.faculdade.media.domain.Nota();
        notas.add(nota);
        disciplina.setMatriculas(new ArrayList<>());
        disciplina.setNotas(notas);
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        
        // When/Then
        assertThatThrownBy(() -> disciplinaService.remover(1L))
                .isInstanceOf(IntegridadeReferencialException.class)
                .hasMessageContaining("Não é possível remover a disciplina pois existem notas registradas");
        verify(em, never()).remove(any(Disciplina.class));
    }
}
