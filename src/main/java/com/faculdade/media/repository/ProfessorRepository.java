package com.faculdade.media.repository;

import com.faculdade.media.domain.Professor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Professor.
 */
public class ProfessorRepository {
    
    private final EntityManager em;
    
    public ProfessorRepository(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Salva ou atualiza um professor.
     * 
     * @param professor O professor a ser salvo
     * @return O professor salvo
     */
    public Professor save(Professor professor) {
        if (professor.getId() == null) {
            em.persist(professor);
        } else {
            professor = em.merge(professor);
        }
        return professor;
    }
    
    /**
     * Busca um professor pelo ID.
     * 
     * @param id O ID do professor
     * @return Optional contendo o professor se encontrado
     */
    public Optional<Professor> findById(Long id) {
        return Optional.ofNullable(em.find(Professor.class, id));
    }
    
    /**
     * Lista todos os professores.
     * 
     * @return Lista de todos os professores
     */
    public List<Professor> findAll() {
        TypedQuery<Professor> query = em.createQuery("SELECT p FROM Professor p ORDER BY p.nome", Professor.class);
        return query.getResultList();
    }
    
    /**
     * Remove um professor do banco de dados.
     * 
     * @param professor O professor a ser removido
     */
    public void delete(Professor professor) {
        if (em.contains(professor)) {
            em.remove(professor);
        } else {
            em.remove(em.merge(professor));
        }
    }
    
    /**
     * Verifica se existe um professor com o registro informado.
     * 
     * @param registro O registro do professor
     * @return true se existe, false caso contrário
     */
    public boolean existsByRegistro(String registro) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Professor p WHERE p.registro = :registro", Long.class);
        query.setParameter("registro", registro);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Verifica se existe um professor com o registro informado, excluindo um ID específico.
     * Útil para validação de unicidade na atualização.
     * 
     * @param registro O registro do professor
     * @param idExcluir O ID a ser excluído da verificação
     * @return true se existe, false caso contrário
     */
    public boolean existsByRegistroExcluindoId(String registro, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(p) FROM Professor p WHERE p.registro = :registro AND p.id != :idExcluir", Long.class);
        query.setParameter("registro", registro);
        query.setParameter("idExcluir", idExcluir);
        return query.getSingleResult() > 0;
    }
}
