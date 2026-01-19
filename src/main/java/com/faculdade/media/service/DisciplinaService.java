package com.faculdade.media.service;

import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Professor;
import com.faculdade.media.dto.DisciplinaDTO;
import com.faculdade.media.dto.DisciplinaInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.IntegridadeReferencialException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.CursoRepository;
import com.faculdade.media.repository.DisciplinaRepository;
import com.faculdade.media.repository.ProfessorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Disciplinas.
 */
@ApplicationScoped
public class DisciplinaService {
    
    @Inject
    EntityManager em;
    
    /**
     * Cria uma nova disciplina.
     * 
     * @param inputDTO DTO com os dados da disciplina
     * @return DTO da disciplina criada
     * @throws ValidacaoException se o nome já existe no curso ou se curso/professor não existe
     */
    @Transactional
    public DisciplinaDTO criar(DisciplinaInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository(em);
        CursoRepository cursoRepository = new CursoRepository(em);
        ProfessorRepository professorRepository = new ProfessorRepository(em);
        
        // Validar que o curso existe
        Curso curso = cursoRepository.findById(inputDTO.getCursoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Curso não encontrado com ID: " + inputDTO.getCursoId()));
        
        // Validar que o professor existe
        Professor professor = professorRepository.findById(inputDTO.getProfessorId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Professor não encontrado com ID: " + inputDTO.getProfessorId()));
        
        // Validação de unicidade: nome único dentro do mesmo curso
        if (disciplinaRepository.existsByNomeAndCursoId(inputDTO.getNome(), inputDTO.getCursoId())) {
            throw new ValidacaoException(
                "Já existe uma disciplina com o nome '" + inputDTO.getNome() + "' no curso informado.");
        }
        
        Disciplina disciplina = new Disciplina(inputDTO.getNome(), curso, professor);
        disciplina = disciplinaRepository.save(disciplina);
        
        return toDTO(disciplina);
    }
    
    /**
     * Lista todas as disciplinas.
     * 
     * @return Lista de DTOs das disciplinas
     */
    public List<DisciplinaDTO> listarTodos() {
        DisciplinaRepository repository = new DisciplinaRepository(em);
        List<Disciplina> disciplinas = repository.findAll();
        
        return disciplinas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista disciplinas por curso.
     * 
     * @param cursoId O ID do curso
     * @return Lista de DTOs das disciplinas do curso
     * @throws EntidadeNaoEncontradaException se o curso não for encontrado
     */
    public List<DisciplinaDTO> listarPorCurso(Long cursoId) {
        CursoRepository cursoRepository = new CursoRepository(em);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Curso não encontrado com ID: " + cursoId));
        
        DisciplinaRepository repository = new DisciplinaRepository(em);
        List<Disciplina> disciplinas = repository.findByCursoId(cursoId);
        
        return disciplinas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista disciplinas por professor.
     * 
     * @param professorId O ID do professor
     * @return Lista de DTOs das disciplinas do professor
     * @throws EntidadeNaoEncontradaException se o professor não for encontrado
     */
    public List<DisciplinaDTO> listarPorProfessor(Long professorId) {
        ProfessorRepository professorRepository = new ProfessorRepository(em);
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Professor não encontrado com ID: " + professorId));
        
        DisciplinaRepository repository = new DisciplinaRepository(em);
        List<Disciplina> disciplinas = repository.findByProfessorId(professorId);
        
        return disciplinas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Lista disciplinas por curso e professor.
     * 
     * @param cursoId O ID do curso
     * @param professorId O ID do professor
     * @return Lista de DTOs das disciplinas que atendem ambos os critérios
     * @throws EntidadeNaoEncontradaException se o curso ou professor não for encontrado
     */
    public List<DisciplinaDTO> listarPorCursoEPProfessor(Long cursoId, Long professorId) {
        CursoRepository cursoRepository = new CursoRepository(em);
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Curso não encontrado com ID: " + cursoId));
        
        ProfessorRepository professorRepository = new ProfessorRepository(em);
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Professor não encontrado com ID: " + professorId));
        
        DisciplinaRepository repository = new DisciplinaRepository(em);
        List<Disciplina> disciplinas = repository.findByCursoIdAndProfessorId(cursoId, professorId);
        
        return disciplinas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca uma disciplina pelo ID.
     * 
     * @param id O ID da disciplina
     * @return DTO da disciplina encontrada
     * @throws EntidadeNaoEncontradaException se a disciplina não for encontrada
     */
    public DisciplinaDTO buscarPorId(Long id) {
        DisciplinaRepository repository = new DisciplinaRepository(em);
        Disciplina disciplina = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Disciplina não encontrada com ID: " + id));
        
        return toDTO(disciplina);
    }
    
    /**
     * Atualiza uma disciplina existente.
     * 
     * @param id O ID da disciplina a ser atualizada
     * @param inputDTO DTO com os novos dados
     * @return DTO da disciplina atualizada
     * @throws EntidadeNaoEncontradaException se a disciplina, curso ou professor não for encontrado
     * @throws ValidacaoException se o nome já existe no curso
     */
    @Transactional
    public DisciplinaDTO atualizar(Long id, DisciplinaInputDTO inputDTO) {
        validarInputDTO(inputDTO);
        
        DisciplinaRepository disciplinaRepository = new DisciplinaRepository(em);
        CursoRepository cursoRepository = new CursoRepository(em);
        ProfessorRepository professorRepository = new ProfessorRepository(em);
        
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Disciplina não encontrada com ID: " + id));
        
        // Validar que o curso existe (se informado)
        Curso curso = cursoRepository.findById(inputDTO.getCursoId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Curso não encontrado com ID: " + inputDTO.getCursoId()));
        
        // Validar que o professor existe (se informado)
        Professor professor = professorRepository.findById(inputDTO.getProfessorId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Professor não encontrado com ID: " + inputDTO.getProfessorId()));
        
        // Validação de unicidade (excluindo a própria disciplina)
        if (disciplinaRepository.existsByNomeAndCursoIdExcluindoId(
                inputDTO.getNome(), inputDTO.getCursoId(), id)) {
            throw new ValidacaoException(
                "Já existe outra disciplina com o nome '" + inputDTO.getNome() + "' no curso informado.");
        }
        
        disciplina.setNome(inputDTO.getNome());
        disciplina.setCurso(curso);
        disciplina.setProfessor(professor);
        disciplina = disciplinaRepository.save(disciplina);
        
        return toDTO(disciplina);
    }
    
    /**
     * Remove uma disciplina.
     * 
     * @param id O ID da disciplina a ser removida
     * @throws EntidadeNaoEncontradaException se a disciplina não for encontrada
     * @throws IntegridadeReferencialException se a disciplina possui matrículas ou notas vinculadas
     */
    @Transactional
    public void remover(Long id) {
        DisciplinaRepository repository = new DisciplinaRepository(em);
        
        Disciplina disciplina = repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                    "Disciplina não encontrada com ID: " + id));
        
        // Validação de integridade referencial
        if (!disciplina.getMatriculas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover a disciplina pois existem alunos matriculados nela.");
        }
        if (!disciplina.getNotas().isEmpty()) {
            throw new IntegridadeReferencialException(
                "Não é possível remover a disciplina pois existem notas registradas para ela.");
        }
        
        repository.delete(disciplina);
    }
    
    /**
     * Valida o DTO de entrada.
     */
    private void validarInputDTO(DisciplinaInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados da disciplina não podem ser nulos");
        }
        if (inputDTO.getNome() == null || inputDTO.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome da disciplina é obrigatório");
        }
        if (inputDTO.getCursoId() == null) {
            throw new ValidacaoException("ID do curso é obrigatório");
        }
        if (inputDTO.getProfessorId() == null) {
            throw new ValidacaoException("ID do professor é obrigatório");
        }
    }
    
    /**
     * Converte uma entidade Disciplina para DTO.
     */
    private DisciplinaDTO toDTO(Disciplina disciplina) {
        return new DisciplinaDTO(
            disciplina.getId(),
            disciplina.getNome(),
            disciplina.getCurso().getId(),
            disciplina.getCurso().getNome(),
            disciplina.getProfessor().getId(),
            disciplina.getProfessor().getNome()
        );
    }
}
