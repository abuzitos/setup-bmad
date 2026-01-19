package com.faculdade.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

/**
 * DTO para representar um Curso na resposta da API.
 */
@Schema(description = "Representa um curso no sistema")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CursoDTO {
    
    @Schema(description = "ID único do curso", example = "1")
    private Long id;
    
    @Schema(description = "Nome do curso", example = "Ciência da Computação", maxLength = 100)
    private String nome;
    
    @Schema(description = "Lista de disciplinas vinculadas ao curso")
    private List<DisciplinaResumoDTO> disciplinas;
    
    public CursoDTO() {
    }
    
    public CursoDTO(Long id, String nome) {
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
    
    public List<DisciplinaResumoDTO> getDisciplinas() {
        return disciplinas;
    }
    
    public void setDisciplinas(List<DisciplinaResumoDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
