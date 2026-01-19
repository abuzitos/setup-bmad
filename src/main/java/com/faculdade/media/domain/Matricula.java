package com.faculdade.media.domain;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidade que representa a matrícula de um Aluno em uma Disciplina.
 * 
 * Esta é uma entidade de associação que relaciona Aluno e Disciplina.
 * A constraint única (aluno, disciplina) garante que um aluno não pode
 * estar matriculado duas vezes na mesma disciplina.
 */
@Entity
@Table(name = "matriculas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "disciplina_id"})
})
public class Matricula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;
    
    public Matricula() {
        // Construtor padrão para JPA
    }
    
    public Matricula(Aluno aluno, Disciplina disciplina) {
        this.aluno = aluno;
        this.disciplina = disciplina;
    }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Aluno getAluno() {
        return aluno;
    }
    
    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
    
    public Disciplina getDisciplina() {
        return disciplina;
    }
    
    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matricula matricula = (Matricula) o;
        return id != null && id.equals(matricula.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Matricula{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getId() : null) +
                ", disciplina=" + (disciplina != null ? disciplina.getId() : null) +
                '}';
    }
}
