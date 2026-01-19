package com.faculdade.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para entrada de dados ao criar ou atualizar um Curso.
 */
@Schema(description = "DTO para criação ou atualização de curso")
public class CursoInputDTO {
    
    @Schema(
        description = "Nome do curso",
        example = "Ciência da Computação",
        required = true,
        maxLength = 100
    )
    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(max = 100, message = "Nome do curso deve ter no máximo 100 caracteres")
    private String nome;
    
    public CursoInputDTO() {
    }
    
    public CursoInputDTO(String nome) {
        this.nome = nome;
    }
    
    // Getters e Setters
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
}
