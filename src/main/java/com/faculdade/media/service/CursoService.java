package com.faculdade.media.service;

import com.faculdade.media.domain.Curso;
import com.faculdade.media.dto.CursoDTO;
import com.faculdade.media.dto.CursoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.CursoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Cursos.
 */
@ApplicationScoped
public class CursoService {
    
    @Inject
    EntityManager em;
    
    /**
     * Cria um novo curso.
     * 
     * @param inputDTO DTO com os dados do curso
     * @return DTO do curso criado
     * @throws ValidacaoException se o nome já existe
     */
    @Transactional
    public CursoDTO criar(CursoInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        CursoRepository repository = new CursoRepository(em);
        
        // Validação de unicidade
        if (repository.existsByNome(inputDTO.getNome())) {
            throw new ValidacaoException("Já existe um curso com o nome: " + inputDTO.getNome());
        }
        
        Curso curso = new Curso(inputDTO.getNome());
        curso = repository.save(curso);
        
        return toDTO(curso);
    }
    
    /**
     * Busca um curso pelo ID.
     * 
     * @param id O ID do curso
     * @return DTO do curso encontrado
     * @throws EntidadeNaoEncontradaException se o curso não for encontrado
     */
    public CursoDTO buscarPorId(Long id) {
        CursoRepository repository = new CursoRepository(em);
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso não encontrado com ID: " + id));
        
        return toDTO(curso);
    }
    
    /**
     * Lista todos os cursos.
     * 
     * @return Lista de DTOs dos cursos
     */
    public List<CursoDTO> listarTodos() {
        CursoRepository repository = new CursoRepository(em);
        List<Curso> cursos = repository.findAll();
        
        return cursos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza um curso existente.
     * 
     * @param id O ID do curso a ser atualizado
     * @param inputDTO DTO com os novos dados
     * @return DTO do curso atualizado
     * @throws EntidadeNaoEncontradaException se o curso não for encontrado
     * @throws ValidacaoException se o nome já existe
     */
    @Transactional
    public CursoDTO atualizar(Long id, CursoInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        CursoRepository repository = new CursoRepository(em);
        
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso não encontrado com ID: " + id));
        
        // Validação de unicidade (excluindo o próprio curso)
        if (repository.existsByNomeExcluindoId(inputDTO.getNome(), id)) {
            throw new ValidacaoException("Já existe outro curso com o nome: " + inputDTO.getNome());
        }
        
        curso.setNome(inputDTO.getNome());
        curso = repository.save(curso);
        
        return toDTO(curso);
    }
    
    /**
     * Remove um curso.
     * 
     * @param id O ID do curso a ser removido
     * @throws EntidadeNaoEncontradaException se o curso não for encontrado
     * @throws IntegridadeReferencialException se o curso possui disciplinas vinculadas
     */
    @Transactional
    public void remover(Long id) {
        CursoRepository repository = new CursoRepository(em);
        
        Curso curso = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Curso não encontrado com ID: " + id));
        
        // Validação de integridade referencial
        if (!curso.getDisciplinas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover o curso pois existem disciplinas vinculadas a ele."
            );
        }
        
        repository.delete(curso);
    }
    
    /**
     * Valida o DTO de entrada.
     */
    private void validarInputDTO(CursoInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados do curso não podem ser nulos");
        }
        if (inputDTO.getNome() == null || inputDTO.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome do curso é obrigatório");
        }
    }
    
    /**
     * Converte uma entidade Curso para DTO.
     */
    private CursoDTO toDTO(Curso curso) {
        return new CursoDTO(curso.getId(), curso.getNome());
    }
}
