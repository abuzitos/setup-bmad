package com.faculdade.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para receber dados de entrada de uma Disciplina (request).
 */
@Schema(description = "Dados de entrada para criar ou atualizar uma disciplina")
public class DisciplinaInputDTO {
    
    @Schema(description = "Nome da disciplina", example = "Programação Orientada a Objetos", required = true)
    @NotBlank(message = "Nome da disciplina é obrigatório")
    @Size(max = 100, message = "Nome da disciplina deve ter no máximo 100 caracteres")
    private String nome;
    
    @Schema(description = "ID do curso ao qual a disciplina pertence", example = "1", required = true)
    @NotNull(message = "ID do curso é obrigatório")
    private Long cursoId;
    
    @Schema(description = "ID do professor responsável pela disciplina", example = "1", required = true)
    @NotNull(message = "ID do professor é obrigatório")
    private Long professorId;
    
    public DisciplinaInputDTO() {
    }
    
    public DisciplinaInputDTO(String nome, Long cursoId, Long professorId) {
        this.nome = nome;
        this.cursoId = cursoId;
        this.professorId = professorId;
    }
    
    // Getters e Setters
    
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
    
    public Long getProfessorId() {
        return professorId;
    }
    
    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }
}
