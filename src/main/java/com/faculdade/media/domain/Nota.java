package com.faculdade.media.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Entidade que representa as notas de um Aluno em uma Disciplina.
 * 
 * Cada aluno em uma disciplina possui exatamente 2 notas.
 * A média e classificação são calculadas automaticamente via @PrePersist e @PreUpdate.
 * A constraint única (aluno, disciplina) garante um único registro de notas por aluno/disciplina.
 */
@Entity
@Table(name = "notas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_id", "disciplina_id"})
})
public class Nota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    @NotNull(message = "Aluno é obrigatório")
    private Aluno aluno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disciplina_id", nullable = false)
    @NotNull(message = "Disciplina é obrigatória")
    private Disciplina disciplina;
    
    @Column(nullable = false, precision = 3, scale = 1)
    @NotNull(message = "Nota 1 é obrigatória")
    @DecimalMin(value = "0.0", message = "Nota 1 deve ser maior ou igual a 0.0")
    @DecimalMax(value = "10.0", message = "Nota 1 deve ser menor ou igual a 10.0")
    private BigDecimal nota1;
    
    @Column(nullable = false, precision = 3, scale = 1)
    @NotNull(message = "Nota 2 é obrigatória")
    @DecimalMin(value = "0.0", message = "Nota 2 deve ser maior ou igual a 0.0")
    @DecimalMax(value = "10.0", message = "Nota 2 deve ser menor ou igual a 10.0")
    private BigDecimal nota2;
    
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal media;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Classificacao classificacao;
    
    @PrePersist
    @PreUpdate
    private void calcularMediaEClassificacao() {
        if (nota1 != null && nota2 != null) {
            this.media = nota1.add(nota2)
                    .divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP);
            this.classificacao = Classificacao.classificar(media);
        }
    }
    
    public Nota() {
        // Construtor padrão para JPA
    }
    
    public Nota(Aluno aluno, Disciplina disciplina, BigDecimal nota1, BigDecimal nota2) {
        this.aluno = aluno;
        this.disciplina = disciplina;
        this.nota1 = nota1;
        this.nota2 = nota2;
        calcularMediaEClassificacao();
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
    
    public BigDecimal getNota1() {
        return nota1;
    }
    
    public void setNota1(BigDecimal nota1) {
        this.nota1 = nota1;
        calcularMediaEClassificacao();
    }
    
    public BigDecimal getNota2() {
        return nota2;
    }
    
    public void setNota2(BigDecimal nota2) {
        this.nota2 = nota2;
        calcularMediaEClassificacao();
    }
    
    public BigDecimal getMedia() {
        return media;
    }
    
    public void setMedia(BigDecimal media) {
        this.media = media;
    }
    
    public Classificacao getClassificacao() {
        return classificacao;
    }
    
    public void setClassificacao(Classificacao classificacao) {
        this.classificacao = classificacao;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return id != null && id.equals(nota.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", aluno=" + (aluno != null ? aluno.getId() : null) +
                ", disciplina=" + (disciplina != null ? disciplina.getId() : null) +
                ", nota1=" + nota1 +
                ", nota2=" + nota2 +
                ", media=" + media +
                ", classificacao=" + classificacao +
                '}';
    }
}
