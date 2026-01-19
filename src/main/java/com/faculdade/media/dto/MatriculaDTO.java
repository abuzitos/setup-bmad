package com.faculdade.media.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para representar uma Matrícula completa (response).
 */
@Schema(description = "Matrícula completa com todos os dados")
public class MatriculaDTO {
    
    @Schema(description = "ID da matrícula", example = "1")
    private Long id;
    
    @Schema(description = "ID do aluno", example = "1")
    private Long alunoId;
    
    @Schema(description = "Nome do aluno", example = "Pedro Oliveira")
    private String alunoNome;
    
    @Schema(description = "ID da disciplina", example = "1")
    private Long disciplinaId;
    
    @Schema(description = "Nome da disciplina", example = "Programação Orientada a Objetos")
    private String disciplinaNome;
    
    public MatriculaDTO() {
    }
    
    public MatriculaDTO(Long id, Long alunoId, String alunoNome, Long disciplinaId, String disciplinaNome) {
        this.id = id;
        this.alunoId = alunoId;
        this.alunoNome = alunoNome;
        this.disciplinaId = disciplinaId;
        this.disciplinaNome = disciplinaNome;
    }
    
    // Getters e Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getAlunoId() {
        return alunoId;
    }
    
    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
    
    public String getAlunoNome() {
        return alunoNome;
    }
    
    public void setAlunoNome(String alunoNome) {
        this.alunoNome = alunoNome;
    }
    
    public Long getDisciplinaId() {
        return disciplinaId;
    }
    
    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
    
    public String getDisciplinaNome() {
        return disciplinaNome;
    }
    
    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }
}
