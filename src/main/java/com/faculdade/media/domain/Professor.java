package com.faculdade.media.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Professor na faculdade.
 * 
 * Um professor pode lecionar múltiplas disciplinas.
 * O registro do professor deve ser único no sistema.
 */
@Entity
@Table(name = "professores", uniqueConstraints = {
    @UniqueConstraint(columnNames = "registro")
})
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome do professor é obrigatório")
    @Size(max = 100, message = "Nome do professor deve ter no máximo 100 caracteres")
    private String nome;
    
    @Column(nullable = false, length = 20, unique = true)
    @NotBlank(message = "Registro do professor é obrigatório")
    @Size(max = 20, message = "Registro do professor deve ter no máximo 20 caracteres")
    private String registro;
    
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Disciplina> disciplinas = new ArrayList<>();
    
    public Professor() {
        // Construtor padrão para JPA
    }
    
    public Professor(String nome, String registro) {
        this.nome = nome;
        this.registro = registro;
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
    
    public String getRegistro() {
        return registro;
    }
    
    public void setRegistro(String registro) {
        this.registro = registro;
    }
    
    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    
    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    // Métodos auxiliares
    
    public void adicionarDisciplina(Disciplina disciplina) {
        disciplinas.add(disciplina);
        disciplina.setProfessor(this);
    }
    
    public void removerDisciplina(Disciplina disciplina) {
        disciplinas.remove(disciplina);
        disciplina.setProfessor(null);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Professor professor = (Professor) o;
        return id != null && id.equals(professor.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", registro='" + registro + '\'' +
                '}';
    }
}
