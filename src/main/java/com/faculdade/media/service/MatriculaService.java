package com.faculdade.media.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Matricula;
import com.faculdade.media.dto.MatriculaDTO;
import com.faculdade.media.dto.MatriculaInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.AlunoRepository;
import com.faculdade.media.repository.DisciplinaRepository;
import com.faculdade.media.repository.MatriculaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Matrículas.
 */
@ApplicationScoped
public class MatriculaService {
    
    @Inject
    EntityManager em;
    
    /**
     * Matricula um aluno em uma disciplina.
     * 
     * @param alunoId O ID do aluno
     * @param disciplinaId O ID da disciplina
     * @return DTO da matrícula criada
     * @throws EntidadeNaoEncontradaException se aluno ou disciplina não existem
     * @throws ValidacaoException se o aluno já está matriculado na disciplina
     */
    @Transactional
    public MatriculaDTO matricular(Long alunoId, Long disciplinaId) {
        validarIds(alunoId, disciplinaId);
        
        MatriculaRepository matriculaRepository = new MatriculaRepository(em);
        AlunoRepository alunoRepository = new AlunoRepository(em);
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository(em);
        
        // Validar que o aluno existe
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Aluno não encontrado com ID: " + alunoId));
        
        // Validar que a disciplina existe
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Disciplina não encontrada com ID: " + disciplinaId));
        
        // Validação de matrícula duplicada
        if (matriculaRepository.existsByAlunoIdAndDisciplinaId(alunoId, disciplinaId)) {
            throw new ValidacaoException(
                "O aluno já está matriculado nesta disciplina.");
        }
        
        Matricula matricula = new Matricula(aluno, disciplina);
        matricula = matriculaRepository.save(matricula);
        
        return toDTO(matricula);
    }
    
    /**
     * Matricula um aluno em uma disciplina usando DTO.
     * 
     * @param inputDTO DTO com os dados da matrícula
     * @return DTO da matrícula criada
     */
    @Transactional
    public MatriculaDTO matricular(MatriculaInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        return matricular(inputDTO.getAlunoId(), inputDTO.getDisciplinaId());
    }
    
    /**
     * Lista todas as matrículas.
     * 
     * @return Lista de DTOs das matrículas
     */
    public List<MatriculaDTO> listarTodos() {
        MatriculaRepository repository = new MatriculaRepository(em);
        List<Matricula> matriculas = repository.findAll();
        
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista matrículas por aluno.
     * 
     * @param alunoId O ID do aluno
     * @return Lista de DTOs das matrículas do aluno
     * @throws EntidadeNaoEncontradaException se o aluno não for encontrado
     */
    public List<MatriculaDTO> listarPorAluno(Long alunoId) {
        AlunoRepository alunoRepository = new AlunoRepository(em);
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Aluno não encontrado com ID: " + alunoId));
        
        MatriculaRepository repository = new MatriculaRepository(em);
        List<Matricula> matriculas = repository.findByAlunoId(alunoId);
        
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista matrículas por disciplina.
     * 
     * @param disciplinaId O ID da disciplina
     * @return Lista de DTOs das matrículas da disciplina
     * @throws EntidadeNaoEncontradaException se a disciplina não for encontrada
     */
    public List<MatriculaDTO> listarPorDisciplina(Long disciplinaId) {
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository(em);
        Disciplina disciplina = disciplinaRepository.findById(disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Disciplina não encontrada com ID: " + disciplinaId));
        
        MatriculaRepository repository = new MatriculaRepository(em);
        List<Matricula> matriculas = repository.findByDisciplinaId(disciplinaId);
        
        return matriculas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Desmatricula um aluno de uma disciplina.
     * 
     * @param alunoId O ID do aluno
     * @param disciplinaId O ID da disciplina
     * @throws EntidadeNaoEncontradaException se a matrícula não for encontrada
     */
    @Transactional
    public void desmatricular(Long alunoId, Long disciplinaId) {
        validarIds(alunoId, disciplinaId);
        
        MatriculaRepository repository = new MatriculaRepository(em);
        
        Matricula matricula = repository.findByAlunoIdAndDisciplinaId(alunoId, disciplinaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Matrícula não encontrada para o aluno ID: " + alunoId + 
                    " e disciplina ID: " + disciplinaId));
        
        repository.delete(matricula);
    }
    
    /**
     * Valida os IDs fornecidos.
     */
    private void validarIds(Long alunoId, Long disciplinaId) {
        if (alunoId == null) {
            throw new ValidacaoException("ID do aluno é obrigatório");
        }
        if (disciplinaId == null) {
            throw new ValidacaoException("ID da disciplina é obrigatório");
        }
    }
    
    /**
     * Valida o DTO de entrada.
     */
    private void validarInputDTO(MatriculaInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados da matrícula não podem ser nulos");
        }
        if (inputDTO.getAlunoId() == null) {
            throw new ValidacaoException("ID do aluno é obrigatório");
        }
        if (inputDTO.getDisciplinaId() == null) {
            throw new ValidacaoException("ID da disciplina é obrigatório");
        }
    }
    
    /**
     * Converte uma entidade Matricula para DTO.
     */
    private MatriculaDTO toDTO(Matricula matricula) {
        return new MatriculaDTO(
            matricula.getId(),
            matricula.getAluno().getId(),
            matricula.getAluno().getNome(),
            matricula.getDisciplina().getId(),
            matricula.getDisciplina().getNome()
        );
    }
}
