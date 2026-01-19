package com.faculdade.media.dto;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para receber dados de entrada de uma Matrícula (request).
 */
@Schema(description = "Dados de entrada para criar uma matrícula")
public class MatriculaInputDTO {
    
    @Schema(description = "ID do aluno a ser matriculado", example = "1", required = true)
    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;
    
    @Schema(description = "ID da disciplina", example = "1", required = true)
    @NotNull(message = "ID da disciplina é obrigatório")
    private Long disciplinaId;
    
    public MatriculaInputDTO() {
    }
    
    public MatriculaInputDTO(Long alunoId, Long disciplinaId) {
        this.alunoId = alunoId;
        this.disciplinaId = disciplinaId;
    }
    
    // Getters e Setters
    
    public Long getAlunoId() {
        return alunoId;
    }
    
    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }
    
    public Long getDisciplinaId() {
        return disciplinaId;
    }
    
    public void setDisciplinaId(Long disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
}
