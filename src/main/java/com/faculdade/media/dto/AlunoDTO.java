package com.faculdade.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.util.List;

/**
 * DTO para representar um Aluno na resposta da API.
 */
@Schema(description = "Representa um aluno no sistema")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlunoDTO {
    
    @Schema(description = "ID único do aluno", example = "1")
    private Long id;
    
    @Schema(description = "Nome do aluno", example = "Pedro Oliveira", maxLength = 100)
    private String nome;
    
    @Schema(description = "Matrícula do aluno", example = "2024001", maxLength = 20)
    private String matricula;
    
    @Schema(description = "Lista de disciplinas matriculadas")
    private List<DisciplinaResumoDTO> disciplinas;
    
    public AlunoDTO() {
    }
    
    public AlunoDTO(Long id, String nome, String matricula) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
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
    
    public String getMatricula() {
        return matricula;
    }
    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    public List<DisciplinaResumoDTO> getDisciplinas() {
        return disciplinas;
    }
    
    public void setDisciplinas(List<DisciplinaResumoDTO> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
