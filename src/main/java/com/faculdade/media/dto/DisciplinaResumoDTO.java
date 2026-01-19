package com.faculdade.media.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO resumido para representar uma Disciplina em listagens.
 * Usado em relacionamentos (ex: lista de disciplinas de um curso).
 */
@Schema(description = "Resumo de uma disciplina")
public class DisciplinaResumoDTO {
    
    @Schema(description = "ID da disciplina", example = "1")
    private Long id;
    
    @Schema(description = "Nome da disciplina", example = "Programação Orientada a Objetos")
    private String nome;
    
    public DisciplinaResumoDTO() {
    }
    
    public DisciplinaResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
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
}
