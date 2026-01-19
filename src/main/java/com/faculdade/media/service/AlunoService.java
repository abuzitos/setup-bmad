package com.faculdade.media.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.dto.AlunoDTO;
import com.faculdade.media.dto.AlunoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.AlunoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Alunos.
 */
@ApplicationScoped
public class AlunoService {
    
    @Inject
    EntityManager em;
    
    /**
     * Cria um novo aluno.
     * 
     * @param inputDTO DTO com os dados do aluno
     * @return DTO do aluno criado
     * @throws ValidacaoException se a matrícula já existe
     */
    @Transactional
    public AlunoDTO criar(AlunoInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        AlunoRepository repository = new AlunoRepository(em);
        
        // Validação de unicidade
        if (repository.existsByMatricula(inputDTO.getMatricula())) {
            throw new ValidacaoException("Já existe um aluno com a matrícula: " + inputDTO.getMatricula());
        }
        
        Aluno aluno = new Aluno(inputDTO.getNome(), inputDTO.getMatricula());
        aluno = repository.save(aluno);
        
        return toDTO(aluno);
    }
    
    /**
     * Busca um aluno pelo ID.
     * 
     * @param id O ID do aluno
     * @return DTO do aluno encontrado
     * @throws EntidadeNaoEncontradaException se o aluno não for encontrado
     */
    public AlunoDTO buscarPorId(Long id) {
        AlunoRepository repository = new AlunoRepository(em);
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado com ID: " + id));
        
        return toDTO(aluno);
    }
    
    /**
     * Lista todos os alunos.
     * 
     * @return Lista de DTOs dos alunos
     */
    public List<AlunoDTO> listarTodos() {
        AlunoRepository repository = new AlunoRepository(em);
        List<Aluno> alunos = repository.findAll();
        
        return alunos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza um aluno existente.
     * 
     * @param id O ID do aluno a ser atualizado
     * @param inputDTO DTO com os novos dados
     * @return DTO do aluno atualizado
     * @throws EntidadeNaoEncontradaException se o aluno não for encontrado
     * @throws ValidacaoException se a matrícula já existe
     */
    @Transactional
    public AlunoDTO atualizar(Long id, AlunoInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        AlunoRepository repository = new AlunoRepository(em);
        
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado com ID: " + id));
        
        // Validação de unicidade (excluindo o próprio aluno)
        if (repository.existsByMatriculaExcluindoId(inputDTO.getMatricula(), id)) {
            throw new ValidacaoException("Já existe outro aluno com a matrícula: " + inputDTO.getMatricula());
        }
        
        aluno.setNome(inputDTO.getNome());
        aluno.setMatricula(inputDTO.getMatricula());
        aluno = repository.save(aluno);
        
        return toDTO(aluno);
    }
    
    /**
     * Remove um aluno.
     * 
     * @param id O ID do aluno a ser removido
     * @throws EntidadeNaoEncontradaException se o aluno não for encontrado
     * @throws IntegridadeReferencialException se o aluno possui matrículas ou notas vinculadas
     */
    @Transactional
    public void remover(Long id) {
        AlunoRepository repository = new AlunoRepository(em);
        
        Aluno aluno = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluno não encontrado com ID: " + id));
        
        // Validação de integridade referencial
        if (!aluno.getMatriculas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover o aluno pois existem matrículas vinculadas a ele."
            );
        }
        if (!aluno.getNotas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover o aluno pois existem notas vinculadas a ele."
            );
        }
        
        repository.delete(aluno);
    }
    
    /**
     * Valida o DTO de entrada.
     */
    private void validarInputDTO(AlunoInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados do aluno não podem ser nulos");
        }
        if (inputDTO.getNome() == null || inputDTO.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do aluno é obrigatório");
        }
        if (inputDTO.getMatricula() == null || inputDTO.getMatricula().trim().isEmpty()) {
            throw new ValidacaoException("Matrícula do aluno é obrigatória");
        }
    }
    
    /**
     * Converte uma entidade Aluno para DTO.
     */
    private AlunoDTO toDTO(Aluno aluno) {
        return new AlunoDTO(aluno.getId(), aluno.getNome(), aluno.getMatricula());
    }
}
