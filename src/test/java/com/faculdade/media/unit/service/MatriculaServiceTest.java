package com.faculdade.media.unit.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Matricula;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.MatriculaDTO;
import com.faculdade.media.dto.MatriculaInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.service.MatriculaService;
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
@DisplayName("Testes Unitários - MatriculaService")
class MatriculaServiceTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityTransaction transaction;
    
    @InjectMocks
    private MatriculaService matriculaService;
    
    private Aluno aluno;
    private Disciplina disciplina;
    private Matricula matricula;
    private Curso curso;
    private Professor professor;
    
    @BeforeEach
    void setUp() throws Exception {
        // Injetar EntityManager mockado no service usando reflection
        Field emField = MatriculaService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(matriculaService, em);
        
        // Configurar mocks básicos
        when(em.getTransaction()).thenReturn(transaction);
        
        curso = new Curso("Ciência da Computação");
        curso.setId(1L);
        professor = new Professor("João Silva", "PROF001");
        professor.setId(1L);
        disciplina = new Disciplina("Programação Orientada a Objetos", curso, professor);
        disciplina.setId(1L);
        aluno = new Aluno("Pedro Oliveira", "2024001");
        aluno.setId(1L);
        matricula = new Matricula(aluno, disciplina);
        matricula.setId(1L);
    }
    
    @Test
    @DisplayName("Deve matricular aluno com dados válidos")
    void deveMatricularAlunoComDadosValidos() {
        // Given
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.contains(any())).thenReturn(false);
        doAnswer(invocation -> {
            Matricula m = invocation.getArgument(0);
            if (m.getId() == null) {
                m.setId(1L);
            }
            return m;
        }).when(em).persist(any(Matricula.class));
        
        // Mock existsByAlunoIdAndDisciplinaId
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Não existe
            return query;
        });
        
        // When
        MatriculaDTO resultado = matriculaService.matricular(1L, 1L);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getAlunoId()).isEqualTo(1L);
        assertThat(resultado.getDisciplinaId()).isEqualTo(1L);
        verify(em, atLeastOnce()).persist(any(Matricula.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando alunoId é nulo")
    void deveLancarExcecaoQuandoAlunoIdNulo() {
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular(null, 1L))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("ID do aluno é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando disciplinaId é nulo")
    void deveLancarExcecaoQuandoDisciplinaIdNulo() {
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular(1L, null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("ID da disciplina é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando aluno não existe")
    void deveLancarExcecaoQuandoAlunoNaoExiste() {
        // Given
        when(em.find(Aluno.class, 999L)).thenReturn(null);
        
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular(999L, 1L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Aluno não encontrado");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando disciplina não existe")
    void deveLancarExcecaoQuandoDisciplinaNaoExiste() {
        // Given
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.find(Disciplina.class, 999L)).thenReturn(null);
        
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular(1L, 999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Disciplina não encontrada");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando aluno já está matriculado")
    void deveLancarExcecaoQuandoAlunoJaEstaMatriculado() {
        // Given
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Já existe
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular(1L, 1L))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("O aluno já está matriculado nesta disciplina");
    }
    
    @Test
    @DisplayName("Deve matricular usando MatriculaInputDTO")
    void deveMatricularUsandoMatriculaInputDTO() {
        // Given
        MatriculaInputDTO inputDTO = new MatriculaInputDTO(1L, 1L);
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        when(em.contains(any())).thenReturn(false);
        doAnswer(invocation -> {
            Matricula m = invocation.getArgument(0);
            if (m.getId() == null) {
                m.setId(1L);
            }
            return m;
        }).when(em).persist(any(Matricula.class));
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L);
            return query;
        });
        
        // When
        MatriculaDTO resultado = matriculaService.matricular(inputDTO);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getAlunoId()).isEqualTo(1L);
        assertThat(resultado.getDisciplinaId()).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando inputDTO é nulo")
    void deveLancarExcecaoQuandoInputDTONulo() {
        // When/Then
        assertThatThrownBy(() -> matriculaService.matricular((MatriculaInputDTO) null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Dados da matrícula não podem ser nulos");
    }
    
    @Test
    @DisplayName("Deve listar todas as matrículas")
    void deveListarTodasAsMatriculas() {
        // Given
        List<Matricula> matriculas = new ArrayList<>();
        matriculas.add(matricula);
        Matricula outraMatricula = new Matricula(aluno, disciplina);
        outraMatricula.setId(2L);
        matriculas.add(outraMatricula);
        
        when(em.createQuery(anyString(), eq(Matricula.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.getResultList()).thenReturn(matriculas);
            return query;
        });
        
        // When
        List<MatriculaDTO> resultado = matriculaService.listarTodos();
        
        // Then
        assertThat(resultado).hasSize(2);
    }
    
    @Test
    @DisplayName("Deve listar matrículas por aluno")
    void deveListarMatriculasPorAluno() {
        // Given
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        List<Matricula> matriculas = new ArrayList<>();
        matriculas.add(matricula);
        
        when(em.createQuery(anyString(), eq(Matricula.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getResultList()).thenReturn(matriculas);
            return query;
        });
        
        // When
        List<MatriculaDTO> resultado = matriculaService.listarPorAluno(1L);
        
        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAlunoId()).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("Deve listar matrículas por disciplina")
    void deveListarMatriculasPorDisciplina() {
        // Given
        when(em.find(Disciplina.class, 1L)).thenReturn(disciplina);
        List<Matricula> matriculas = new ArrayList<>();
        matriculas.add(matricula);
        
        when(em.createQuery(anyString(), eq(Matricula.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getResultList()).thenReturn(matriculas);
            return query;
        });
        
        // When
        List<MatriculaDTO> resultado = matriculaService.listarPorDisciplina(1L);
        
        // Then
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDisciplinaId()).isEqualTo(1L);
    }
    
    @Test
    @DisplayName("Deve desmatricular aluno com sucesso")
    void deveDesmatricularAlunoComSucesso() {
        // Given
        when(em.createQuery(anyString(), eq(Matricula.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(matricula);
            return query;
        });
        when(em.contains(any(Matricula.class))).thenReturn(true);
        doNothing().when(em).remove(any(Matricula.class));
        
        // When
        matriculaService.desmatricular(1L, 1L);
        
        // Then
        verify(em).remove(any(Matricula.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao desmatricular quando matrícula não existe")
    void deveLancarExcecaoAoDesmatricularQuandoMatriculaNaoExiste() {
        // Given
        when(em.createQuery(anyString(), eq(Matricula.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenThrow(new jakarta.persistence.NoResultException());
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> matriculaService.desmatricular(1L, 1L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Matrícula não encontrada");
        verify(em, never()).remove(any(Matricula.class));
    }
}
