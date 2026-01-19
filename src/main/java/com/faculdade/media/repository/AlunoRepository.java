package com.faculdade.media.repository;

import com.faculdade.media.domain.Aluno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Aluno.
 */
public class AlunoRepository {
    
    private final EntityManager em;
    
    public AlunoRepository(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Salva ou atualiza um aluno.
     * 
     * @param aluno O aluno a ser salvo
     * @return O aluno salvo
     */
    public Aluno save(Aluno aluno) {
        if (aluno.getId() == null) {
            em.persist(aluno);
        } else {
            aluno = em.merge(aluno);
        }
        return aluno;
    }
    
    /**
     * Busca um aluno pelo ID.
     * 
     * @param id O ID do aluno
     * @return Optional contendo o aluno se encontrado
     */
    public Optional<Aluno> findById(Long id) {
        return Optional.ofNullable(em.find(Aluno.class, id));
    }
    
    /**
     * Lista todos os alunos.
     * 
     * @return Lista de todos os alunos
     */
    public List<Aluno> findAll() {
        TypedQuery<Aluno> query = em.createQuery("SELECT a FROM Aluno a ORDER BY a.nome", Aluno.class);
        return query.getResultList();
    }
    
    /**
     * Remove um aluno do banco de dados.
     * 
     * @param aluno O aluno a ser removido
     */
    public void delete(Aluno aluno) {
        if (em.contains(aluno)) {
            em.remove(aluno);
        } else {
            em.remove(em.merge(aluno));
        }
    }
    
    /**
     * Verifica se existe um aluno com a matrícula informada.
     * 
     * @param matricula A matrícula do aluno
     * @return true se existe, false caso contrário
     */
    public boolean existsByMatricula(String matricula) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(a) FROM Aluno a WHERE a.matricula = :matricula", Long.class);
        query.setParameter("matricula", matricula);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Verifica se existe um aluno com a matrícula informada, excluindo um ID específico.
     * Útil para validação de unicidade na atualização.
     * 
     * @param matricula A matrícula do aluno
     * @param idExcluir O ID a ser excluído da verificação
     * @return true se existe, false caso contrário
     */
    public boolean existsByMatriculaExcluindoId(String matricula, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(a) FROM Aluno a WHERE a.matricula = :matricula AND a.id != :idExcluir", Long.class);
        query.setParameter("matricula", matricula);
        query.setParameter("idExcluir", idExcluir);
        return query.getSingleResult() > 0;
    }
}
