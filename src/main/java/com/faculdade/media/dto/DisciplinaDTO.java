package com.faculdade.media.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para representar uma Disciplina completa (response).
 */
@Schema(description = "Disciplina completa com todos os dados")
public class DisciplinaDTO {
    
    @Schema(description = "ID da disciplina", example = "1")
    private Long id;
    
    @Schema(description = "Nome da disciplina", example = "Programação Orientada a Objetos")
    private String nome;
    
    @Schema(description = "ID do curso", example = "1")
    private Long cursoId;
    
    @Schema(description = "Nome do curso", example = "Ciência da Computação")
    private String cursoNome;
    
    @Schema(description = "ID do professor", example = "1")
    private Long professorId;
    
    @Schema(description = "Nome do professor", example = "João Silva")
    private String professorNome;
    
    public DisciplinaDTO() {
    }
    
    public DisciplinaDTO(Long id, String nome, Long cursoId, String cursoNome, Long professorId, String professorNome) {
        this.id = id;
        this.nome = nome;
        this.cursoId = cursoId;
        this.cursoNome = cursoNome;
        this.professorId = professorId;
        this.professorNome = professorNome;
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
    
    public Long getCursoId() {
        return cursoId;
    }
    
    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }
    
    public String getCursoNome() {
        return cursoNome;
    }
    
    public void setCursoNome(String cursoNome) {
        this.cursoNome = cursoNome;
    }
    
    public Long getProfessorId() {
        return professorId;
    }
    
    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }
    
    public String getProfessorNome() {
        return professorNome;
    }
    
    public void setProfessorNome(String professorNome) {
        this.professorNome = professorNome;
    }
}
