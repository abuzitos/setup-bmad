package com.faculdade.media.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Aluno na faculdade.
 * 
 * Um aluno pode estar matriculado em múltiplas disciplinas (via Matricula)
 * e pode ter múltiplas notas (via Nota).
 * A matrícula do aluno deve ser única no sistema.
 */
@Entity
@Table(name = "alunos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "matricula")
})
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome do aluno é obrigatório")
    @Size(max = 100, message = "Nome do aluno deve ter no máximo 100 caracteres")
    private String nome;
    
    @Column(nullable = false, length = 20, unique = true)
    @NotBlank(message = "Matrícula do aluno é obrigatória")
    @Size(max = 20, message = "Matrícula do aluno deve ter no máximo 20 caracteres")
    private String matricula;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Matricula> matriculas = new ArrayList<>();
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Nota> notas = new ArrayList<>();
    
    public Aluno() {
        // Construtor padrão para JPA
    }
    
    public Aluno(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public List<Matricula> getMatriculas() {
        return matriculas;
    }
    
    public void setMatriculas(List<Matricula> matriculas) {
        this.matriculas = matriculas;
    }
    
    public List<Nota> getNotas() {
        return notas;
    }
    
    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }
    
    // Métodos auxiliares
    
    public void adicionarMatricula(Matricula matricula) {
        matriculas.add(matricula);
        matricula.setAluno(this);
    }
    
    public void removerMatricula(Matricula matricula) {
        matriculas.remove(matricula);
        matricula.setAluno(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aluno aluno = (Aluno) o;
        return id != null && id.equals(aluno.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Aluno{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
