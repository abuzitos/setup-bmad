package com.faculdade.media.repository;

import com.faculdade.media.domain.Matricula;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repository para operações de persistência da entidade Matricula.
 */
public class MatriculaRepository {
    
    private final EntityManager em;
    
    public MatriculaRepository(EntityManager em) {
        this.em = em;
    }
    
    /**
     * Salva ou atualiza uma matrícula.
     * 
     * @param matricula A matrícula a ser salva
     * @return A matrícula salva
     */
    public Matricula save(Matricula matricula) {
        if (matricula.getId() == null) {
            em.persist(matricula);
        } else {
            matricula = em.merge(matricula);
        }
        return matricula;
    }
    
    /**
     * Busca uma matrícula pelo ID.
     * 
     * @param id O ID da matrícula
     * @return Optional contendo a matrícula se encontrada
     */
    public Optional<Matricula> findById(Long id) {
        return Optional.ofNullable(em.find(Matricula.class, id));
    }
    
    /**
     * Lista todas as matrículas.
     * 
     * @return Lista de todas as matrículas
     */
    public List<Matricula> findAll() {
        TypedQuery<Matricula> query = em.createQuery(
            "SELECT m FROM Matricula m ORDER BY m.id", Matricula.class);
        return query.getResultList();
    }
    
    /**
     * Busca matrículas por aluno.
     * 
     * @param alunoId O ID do aluno
     * @return Lista de matrículas do aluno
     */
    public List<Matricula> findByAlunoId(Long alunoId) {
        TypedQuery<Matricula> query = em.createQuery(
            "SELECT m FROM Matricula m WHERE m.aluno.id = :alunoId ORDER BY m.id", Matricula.class);
        query.setParameter("alunoId", alunoId);
        return query.getResultList();
    }
    
    /**
     * Busca matrículas por disciplina.
     * 
     * @param disciplinaId O ID da disciplina
     * @return Lista de matrículas da disciplina
     */
    public List<Matricula> findByDisciplinaId(Long disciplinaId) {
        TypedQuery<Matricula> query = em.createQuery(
            "SELECT m FROM Matricula m WHERE m.disciplina.id = :disciplinaId ORDER BY m.id", Matricula.class);
        query.setParameter("disciplinaId", disciplinaId);
        return query.getResultList();
    }
    
    /**
     * Busca uma matrícula específica por aluno e disciplina.
     * 
     * @param alunoId O ID do aluno
     * @param disciplinaId O ID da disciplina
     * @return Optional contendo a matrícula se encontrada
     */
    public Optional<Matricula> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId) {
        TypedQuery<Matricula> query = em.createQuery(
            "SELECT m FROM Matricula m WHERE m.aluno.id = :alunoId AND m.disciplina.id = :disciplinaId", 
            Matricula.class);
        query.setParameter("alunoId", alunoId);
        query.setParameter("disciplinaId", disciplinaId);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }
    
    /**
     * Verifica se existe uma matrícula para o aluno e disciplina especificados.
     * 
     * @param alunoId O ID do aluno
     * @param disciplinaId O ID da disciplina
     * @return true se existe, false caso contrário
     */
    public boolean existsByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(m) FROM Matricula m WHERE m.aluno.id = :alunoId AND m.disciplina.id = :disciplinaId", 
            Long.class);
        query.setParameter("alunoId", alunoId);
        query.setParameter("disciplinaId", disciplinaId);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Remove uma matrícula do banco de dados.
     * 
     * @param matricula A matrícula a ser removida
     */
    public void delete(Matricula matricula) {
        if (em.contains(matricula)) {
            em.remove(matricula);
        } else {
            em.remove(em.merge(matricula));
        }
    }
}
