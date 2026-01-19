package com.faculdade.media.repository;

import com.faculdade.media.domain.Disciplina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Disciplina.
 */
public class DisciplinaRepository {
    
    private final EntityManager em;
    
    public DisciplinaRepository(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Salva ou atualiza uma disciplina.
     * 
     * @param disciplina A disciplina a ser salva
     * @return A disciplina salva
     */
    public Disciplina save(Disciplina disciplina) {
        if (disciplina.getId() == null) {
            em.persist(disciplina);
        } else {
            disciplina = em.merge(disciplina);
        }
        return disciplina;
    }
    
    /**
     * Busca uma disciplina pelo ID.
     * 
     * @param id O ID da disciplina
     * @return Optional contendo a disciplina se encontrada
     */
    public Optional<Disciplina> findById(Long id) {
        return Optional.ofNullable(em.find(Disciplina.class, id));
    }
    
    /**
     * Lista todas as disciplinas.
     * 
     * @return Lista de todas as disciplinas
     */
    public List<Disciplina> findAll() {
        TypedQuery<Disciplina> query = em.createQuery(
            "SELECT d FROM Disciplina d ORDER BY d.nome", Disciplina.class);
        return query.getResultList();
    }
    
    /**
     * Busca disciplinas por curso.
     * 
     * @param cursoId O ID do curso
     * @return Lista de disciplinas do curso
     */
    public List<Disciplina> findByCursoId(Long cursoId) {
        TypedQuery<Disciplina> query = em.createQuery(
            "SELECT d FROM Disciplina d WHERE d.curso.id = :cursoId ORDER BY d.nome", Disciplina.class);
        query.setParameter("cursoId", cursoId);
        return query.getResultList();
    }
    
    /**
     * Busca disciplinas por professor.
     * 
     * @param professorId O ID do professor
     * @return Lista de disciplinas do professor
     */
    public List<Disciplina> findByProfessorId(Long professorId) {
        TypedQuery<Disciplina> query = em.createQuery(
            "SELECT d FROM Disciplina d WHERE d.professor.id = :professorId ORDER BY d.nome", Disciplina.class);
        query.setParameter("professorId", professorId);
        return query.getResultList();
    }
    
    /**
     * Busca disciplinas por curso e professor.
     * 
     * @param cursoId O ID do curso
     * @param professorId O ID do professor
     * @return Lista de disciplinas que atendem ambos os critérios
     */
    public List<Disciplina> findByCursoIdAndProfessorId(Long cursoId, Long professorId) {
        TypedQuery<Disciplina> query = em.createQuery(
            "SELECT d FROM Disciplina d WHERE d.curso.id = :cursoId AND d.professor.id = :professorId ORDER BY d.nome", 
            Disciplina.class);
        query.setParameter("cursoId", cursoId);
        query.setParameter("professorId", professorId);
        return query.getResultList();
    }
    
    /**
     * Verifica se existe uma disciplina com o nome informado no curso especificado.
     * 
     * @param nome O nome da disciplina
     * @param cursoId O ID do curso
     * @return true se existe, false caso contrário
     */
    public boolean existsByNomeAndCursoId(String nome, Long cursoId) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(d) FROM Disciplina d WHERE d.nome = :nome AND d.curso.id = :cursoId", Long.class);
        query.setParameter("nome", nome);
        query.setParameter("cursoId", cursoId);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Verifica se existe uma disciplina com o nome informado no curso especificado, excluindo um ID específico.
     * Útil para validação de unicidade na atualização.
     * 
     * @param nome O nome da disciplina
     * @param cursoId O ID do curso
     * @param idExcluir O ID a ser excluído da verificação
     * @return true se existe, false caso contrário
     */
    public boolean existsByNomeAndCursoIdExcluindoId(String nome, Long cursoId, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(d) FROM Disciplina d WHERE d.nome = :nome AND d.curso.id = :cursoId AND d.id != :idExcluir", 
            Long.class);
        query.setParameter("nome", nome);
        query.setParameter("cursoId", cursoId);
        query.setParameter("idExcluir", idExcluir);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Remove uma disciplina do banco de dados.
     * 
     * @param disciplina A disciplina a ser removida
     */
    public void delete(Disciplina disciplina) {
        if (em.contains(disciplina)) {
            em.remove(disciplina);
        } else {
            em.remove(em.merge(disciplina));
        }
    }
}
