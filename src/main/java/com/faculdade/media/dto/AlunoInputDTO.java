package com.faculdade.media.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para entrada de dados ao criar ou atualizar um Aluno.
 */
@Schema(description = "DTO para criação ou atualização de aluno")
public class AlunoInputDTO {
    
    @Schema(
        description = "Nome do aluno",
        example = "Pedro Oliveira",
        required = true,
        maxLength = 100
    )
    @NotBlank(message = "Nome do aluno é obrigatório")
    @Size(max = 100, message = "Nome do aluno deve ter no máximo 100 caracteres")
    private String nome;
    
    @Schema(
        description = "Matrícula do aluno",
        example = "2024001",
        required = true,
        maxLength = 20
    )
    @NotBlank(message = "Matrícula do aluno é obrigatória")
    @Size(max = 20, message = "Matrícula do aluno deve ter no máximo 20 caracteres")
    private String matricula;
    
    public AlunoInputDTO() {
    }
    
    public AlunoInputDTO(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
    }
    
    // Getters e Setters
    
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
}
