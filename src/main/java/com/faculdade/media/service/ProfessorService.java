package com.faculdade.media.service;

import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.ProfessorDTO;
import com.faculdade.media.dto.ProfessorInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.ProfessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Professores.
 */
@ApplicationScoped
public class ProfessorService {
    
    @Inject
    EntityManager em;
    
    /**
     * Cria um novo professor.
     * 
     * @param inputDTO DTO com os dados do professor
     * @return DTO do professor criado
     * @throws ValidacaoException se o registro já existe
     */
    @Transactional
    public ProfessorDTO criar(ProfessorInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        ProfessorRepository repository = new ProfessorRepository(em);
        
        // Validação de unicidade
        if (repository.existsByRegistro(inputDTO.getRegistro())) {
            throw new ValidacaoException("Já existe um professor com o registro: " + inputDTO.getRegistro());
        }
        
        Professor professor = new Professor(inputDTO.getNome(), inputDTO.getRegistro());
        professor = repository.save(professor);
        
        return toDTO(professor);
    }
    
    /**
     * Busca um professor pelo ID.
     * 
     * @param id O ID do professor
     * @return DTO do professor encontrado
     * @throws EntidadeNaoEncontradaException se o professor não for encontrado
     */
    public ProfessorDTO buscarPorId(Long id) {
        ProfessorRepository repository = new ProfessorRepository(em);
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor não encontrado com ID: " + id));
        
        return toDTO(professor);
    }
    
    /**
     * Lista todos os professores.
     * 
     * @return Lista de DTOs dos professores
     */
    public List<ProfessorDTO> listarTodos() {
        ProfessorRepository repository = new ProfessorRepository(em);
        List<Professor> professores = repository.findAll();
        
        return professores.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza um professor existente.
     * 
     * @param id O ID do professor a ser atualizado
     * @param inputDTO DTO com os novos dados
     * @return DTO do professor atualizado
     * @throws EntidadeNaoEncontradaException se o professor não for encontrado
     * @throws ValidacaoException se o registro já existe
     */
    @Transactional
    public ProfessorDTO atualizar(Long id, ProfessorInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        ProfessorRepository repository = new ProfessorRepository(em);
        
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor não encontrado com ID: " + id));
        
        // Validação de unicidade (excluindo o próprio professor)
        if (repository.existsByRegistroExcluindoId(inputDTO.getRegistro(), id)) {
            throw new ValidacaoException("Já existe outro professor com o registro: " + inputDTO.getRegistro());
        }
        
        professor.setNome(inputDTO.getNome());
        professor.setRegistro(inputDTO.getRegistro());
        professor = repository.save(professor);
        
        return toDTO(professor);
    }
    
    /**
     * Remove um professor.
     * 
     * @param id O ID do professor a ser removido
     * @throws EntidadeNaoEncontradaException se o professor não for encontrado
     * @throws IntegridadeReferencialException se o professor possui disciplinas vinculadas
     */
    @Transactional
    public void remover(Long id) {
        ProfessorRepository repository = new ProfessorRepository(em);
        
        Professor professor = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Professor não encontrado com ID: " + id));
        
        // Validação de integridade referencial
        if (!professor.getDisciplinas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover o professor pois existem disciplinas vinculadas a ele."
            );
        }
        
        repository.delete(professor);
    }
    
    /**
     * Valida o DTO de entrada.
     */
    private void validarInputDTO(ProfessorInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados do professor não podem ser nulos");
        }
        if (inputDTO.getNome() == null || inputDTO.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do professor é obrigatório");
        }
        if (inputDTO.getRegistro() == null || inputDTO.getRegistro().trim().isEmpty()) {
            throw new ValidacaoException("Registro do professor é obrigatório");
        }
    }
    
    /**
     * Converte uma entidade Professor para DTO.
     */
    private ProfessorDTO toDTO(Professor professor) {
        return new ProfessorDTO(professor.getId(), professor.getNome(), professor.getRegistro());
    }
}
