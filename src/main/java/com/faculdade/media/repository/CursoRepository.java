package com.faculdade.media.repository;

import com.faculdade.media.domain.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Curso.
 */
public class CursoRepository {
    
    private final EntityManager em;
    
    public CursoRepository(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Salva ou atualiza um curso.
     * 
     * @param curso O curso a ser salvo
     * @return O curso salvo
     */
    public Curso save(Curso curso) {
        if (curso.getId() == null) {
            em.persist(curso);
        } else {
            curso = em.merge(curso);
        }
        return curso;
    }
    
    /**
     * Busca um curso pelo ID.
     * 
     * @param id O ID do curso
     * @return Optional contendo o curso se encontrado
     */
    public Optional<Curso> findById(Long id) {
        return Optional.ofNullable(em.find(Curso.class, id));
    }
    
    /**
     * Lista todos os cursos.
     * 
     * @return Lista de todos os cursos
     */
    public List<Curso> findAll() {
        TypedQuery<Curso> query = em.createQuery("SELECT c FROM Curso c ORDER BY c.nome", Curso.class);
        return query.getResultList();
    }
    
    /**
     * Remove um curso do banco de dados.
     * 
     * @param curso O curso a ser removido
     */
    public void delete(Curso curso) {
        if (em.contains(curso)) {
            em.remove(curso);
        } else {
            em.remove(em.merge(curso));
        }
    }
    
    /**
     * Verifica se existe um curso com o nome informado.
     * 
     * @param nome O nome do curso
     * @return true se existe, false caso contrário
     */
    public boolean existsByNome(String nome) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Curso c WHERE c.nome = :nome", Long.class);
        query.setParameter("nome", nome);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Verifica se existe um curso com o nome informado, excluindo um ID específico.
     * Útil para validação de unicidade na atualização.
     * 
     * @param nome O nome do curso
     * @param idExcluir O ID a ser excluído da verificação
     * @return true se existe, false caso contrário
     */
    public boolean existsByNomeExcluindoId(String nome, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(c) FROM Curso c WHERE c.nome = :nome AND c.id != :idExcluir", Long.class);
        query.setParameter("nome", nome);
        query.setParameter("idExcluir", idExcluir);
        return query.getSingleResult() > 0;
    }
}
