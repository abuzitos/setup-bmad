package com.faculdade.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

/**
 * DTO para representar um Professor na resposta da API.
 */
@Schema(description = "Representa um professor no sistema")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessorDTO {
    
    @Schema(description = "ID único do professor", example = "1")
    private Long id;
    
    @Schema(description = "Nome do professor", example = "João Silva", maxLength = 100)
    private String nome;
    
    @Schema(description = "Registro do professor", example = "PROF001", maxLength = 20)
    private String registro;
    
    @Schema(description = "Lista de disciplinas lecionadas pelo professor")
    private List<DisciplinaResumoDTO> disciplinas;
    
    public ProfessorDTO() {
    }
    
    public ProfessorDTO(Long id, String nome, String registro) {
        this.id = id;
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
    
    public List<DisciplinaResumoDTO> getDisciplinas() {
        return disciplinas;
    }
    
    public void setDisciplinas(List<DisciplinaResumoDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
