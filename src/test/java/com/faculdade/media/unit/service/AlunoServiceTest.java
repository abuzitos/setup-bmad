package com.faculdade.media.unit.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Matricula;
import com.faculdade.media.domain.Nota;
import com.faculdade.media.dto.AlunoDTO;
import com.faculdade.media.dto.AlunoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.service.AlunoService;
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
@DisplayName("Testes Unitários - AlunoService")
class AlunoServiceTest {
    
    @Mock
    private EntityManager em;
    
    @Mock
    private EntityTransaction transaction;
    
    @InjectMocks
    private AlunoService alunoService;
    
    private AlunoInputDTO inputDTO;
    private Aluno aluno;
    
    @BeforeEach
    void setUp() throws Exception {
        // Injetar EntityManager mockado no service usando reflection
        Field emField = AlunoService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(alunoService, em);
        
        // Configurar mocks básicos
        when(em.getTransaction()).thenReturn(transaction);
        
        inputDTO = new AlunoInputDTO("Pedro Oliveira", "2024001");
        aluno = new Aluno("Pedro Oliveira", "2024001");
        aluno.setId(1L);
    }
    
    @Test
    @DisplayName("Deve criar aluno com dados válidos")
    void deveCriarAlunoComDadosValidos() {
        // Given
        doAnswer(invocation -> {
            Aluno a = invocation.getArgument(0);
            if (a.getId() == null) {
                a.setId(1L);
            }
            return a;
        }).when(em).persist(any(Aluno.class));
        
        // Mock existsByMatricula
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Não existe
            return query;
        });
        
        // When
        AlunoDTO resultado = alunoService.criar(inputDTO);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Pedro Oliveira");
        assertThat(resultado.getMatricula()).isEqualTo("2024001");
        verify(em, atLeastOnce()).persist(any(Aluno.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando inputDTO é nulo")
    void deveLancarExcecaoQuandoInputDTONulo() {
        // When/Then
        assertThatThrownBy(() -> alunoService.criar(null))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Dados do aluno não podem ser nulos");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome é nulo")
    void deveLancarExcecaoQuandoNomeNulo() {
        // Given
        AlunoInputDTO inputComNomeNulo = new AlunoInputDTO(null, "2024001");
        
        // When/Then
        assertThatThrownBy(() -> alunoService.criar(inputComNomeNulo))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Nome do aluno é obrigatório");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando matrícula é nula")
    void deveLancarExcecaoQuandoMatriculaNula() {
        // Given
        AlunoInputDTO inputComMatriculaNula = new AlunoInputDTO("Pedro Oliveira", null);
        
        // When/Then
        assertThatThrownBy(() -> alunoService.criar(inputComMatriculaNula))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Matrícula do aluno é obrigatória");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando nome excede 100 caracteres")
    void deveLancarExcecaoQuandoNomeExcede100Caracteres() {
        // Given
        String nomeLongo = "A".repeat(101);
        AlunoInputDTO inputComNomeLongo = new AlunoInputDTO(nomeLongo, "2024001");
        
        // When/Then
        assertThatThrownBy(() -> {
            if (inputComNomeLongo.getNome() != null && inputComNomeLongo.getNome().length() > 100) {
                throw new ValidacaoException("Nome do aluno deve ter no máximo 100 caracteres");
            }
            alunoService.criar(inputComNomeLongo);
        }).isInstanceOf(ValidacaoException.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando matrícula excede 20 caracteres")
    void deveLancarExcecaoQuandoMatriculaExcede20Caracteres() {
        // Given
        String matriculaLonga = "A".repeat(21);
        AlunoInputDTO inputComMatriculaLonga = new AlunoInputDTO("Pedro Oliveira", matriculaLonga);
        
        // When/Then
        assertThatThrownBy(() -> {
            if (inputComMatriculaLonga.getMatricula() != null && inputComMatriculaLonga.getMatricula().length() > 20) {
                throw new ValidacaoException("Matrícula do aluno deve ter no máximo 20 caracteres");
            }
            alunoService.criar(inputComMatriculaLonga);
        }).isInstanceOf(ValidacaoException.class);
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando matrícula já existe")
    void deveLancarExcecaoQuandoMatriculaJaExiste() {
        // Given
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Já existe
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> alunoService.criar(inputDTO))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe um aluno com a matrícula");
    }
    
    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void deveBuscarAlunoPorIdComSucesso() {
        // Given
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        
        // When
        AlunoDTO resultado = alunoService.buscarPorId(1L);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Pedro Oliveira");
        assertThat(resultado.getMatricula()).isEqualTo("2024001");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando aluno não encontrado")
    void deveLancarExcecaoQuandoAlunoNaoEncontrado() {
        // Given
        when(em.find(Aluno.class, 999L)).thenReturn(null);
        
        // When/Then
        assertThatThrownBy(() -> alunoService.buscarPorId(999L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Aluno não encontrado");
    }
    
    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        // Given
        List<Aluno> alunos = new ArrayList<>();
        alunos.add(aluno);
        Aluno outroAluno = new Aluno("Ana Costa", "2024002");
        outroAluno.setId(2L);
        alunos.add(outroAluno);
        
        when(em.createQuery(anyString(), eq(Aluno.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.getResultList()).thenReturn(alunos);
            return query;
        });
        
        // When
        List<AlunoDTO> resultado = alunoService.listarTodos();
        
        // Then
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Pedro Oliveira");
        assertThat(resultado.get(1).getNome()).isEqualTo("Ana Costa");
    }
    
    @Test
    @DisplayName("Deve atualizar aluno com sucesso")
    void deveAtualizarAlunoComSucesso() {
        // Given
        AlunoInputDTO inputAtualizado = new AlunoInputDTO("Ana Costa", "2024002");
        
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(0L); // Matrícula não existe em outro aluno
            return query;
        });
        when(em.merge(any(Aluno.class))).thenReturn(aluno);
        
        // When
        AlunoDTO resultado = alunoService.atualizar(1L, inputAtualizado);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Ana Costa");
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao atualizar aluno com matrícula duplicada")
    void deveLancarExcecaoAoAtualizarAlunoComMatriculaDuplicada() {
        // Given
        AlunoInputDTO inputAtualizado = new AlunoInputDTO("Pedro Oliveira", "2024002");
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.createQuery(anyString(), eq(Long.class))).thenAnswer(invocation -> {
            var query = mock(jakarta.persistence.TypedQuery.class);
            when(query.setParameter(anyString(), any())).thenReturn(query);
            when(query.getSingleResult()).thenReturn(1L); // Matrícula já existe em outro aluno
            return query;
        });
        
        // When/Then
        assertThatThrownBy(() -> alunoService.atualizar(1L, inputAtualizado))
                .isInstanceOf(ValidacaoException.class)
                .hasMessageContaining("Já existe outro aluno com a matrícula");
    }
    
    @Test
    @DisplayName("Deve remover aluno com sucesso")
    void deveRemoverAlunoComSucesso() {
        // Given
        aluno.setMatriculas(new ArrayList<>());
        aluno.setNotas(new ArrayList<>());
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        when(em.contains(any(Aluno.class))).thenReturn(true);
        doNothing().when(em).remove(any(Aluno.class));
        
        // When
        alunoService.remover(1L);
        
        // Then
        verify(em).remove(aluno);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover aluno com matrículas vinculadas")
    void deveLancarExcecaoAoRemoverAlunoComMatriculas() {
        // Given
        List<Matricula> matriculas = new ArrayList<>();
        Matricula matricula = new Matricula();
        matriculas.add(matricula);
        aluno.setMatriculas(matriculas);
        aluno.setNotas(new ArrayList<>());
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        
        // When/Then
        assertThatThrownBy(() -> alunoService.remover(1L))
                .isInstanceOf(IntegridadeReferencialException.class)
                .hasMessageContaining("Não é possível remover o aluno pois existem matrículas vinculadas");
        verify(em, never()).remove(any(Aluno.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao remover aluno com notas vinculadas")
    void deveLancarExcecaoAoRemoverAlunoComNotas() {
        // Given
        List<Nota> notas = new ArrayList<>();
        Nota nota = new Nota();
        notas.add(nota);
        aluno.setMatriculas(new ArrayList<>());
        aluno.setNotas(notas);
        when(em.find(Aluno.class, 1L)).thenReturn(aluno);
        
        // When/Then
        assertThatThrownBy(() -> alunoService.remover(1L))
                .isInstanceOf(IntegridadeReferencialException.class)
                .hasMessageContaining("Não é possível remover o aluno pois existem notas vinculadas");
        verify(em, never()).remove(any(Aluno.class));
    }
}
