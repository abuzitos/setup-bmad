package com.faculdade.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para entrada de dados ao criar ou atualizar um Professor.
 */
@Schema(description = "DTO para criação ou atualização de professor")
public class ProfessorInputDTO {
    
    @Schema(
        description = "Nome do professor",
        example = "João Silva",
        required = true,
        maxLength = 100
    )
    @NotBlank(message = "Nome do professor é obrigatório")
    @Size(max = 100, message = "Nome do professor deve ter no máximo 100 caracteres")
    private String nome;
    
    @Schema(
        description = "Registro do professor",
        example = "PROF001",
        required = true,
        maxLength = 20
    )
    @NotBlank(message = "Registro do professor é obrigatório")
    @Size(max = 20, message = "Registro do professor deve ter no máximo 20 caracteres")
    private String registro;
    
    public ProfessorInputDTO() {
    }
    
    public ProfessorInputDTO(String nome, String registro) {
        this.nome = nome;
        this.registro = registro;
    }
    
    // Getters e Setters
    
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
}
