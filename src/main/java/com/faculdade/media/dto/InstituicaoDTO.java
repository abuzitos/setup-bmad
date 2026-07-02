package com.faculdade.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para representar uma Instituição na resposta da API.
 */
@Schema(description = "Representa uma instituição de ensino no sistema")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InstituicaoDTO {

    @Schema(description = "ID único da instituição", example = "1")
    private Long id;

    @Schema(description = "Nome da instituição", example = "Faculdade de Tecnologia de São Paulo", maxLength = 100)
    private String nome;

    @Schema(description = "Endereço da instituição", example = "Rua das Flores, 123", maxLength = 200)
    private String endereco;

    @Schema(description = "Telefone principal", example = "11 99999-9999", maxLength = 20)
    private String telefone1;

    @Schema(description = "Telefone secundário", example = "11 88888-8888", maxLength = 20)
    private String telefone2;

    @Schema(description = "CEP (até 8 dígitos)", example = "1234567")
    private Integer cep;

    public InstituicaoDTO() {
    }

    public InstituicaoDTO(Long id, String nome, String endereco, String telefone1, String telefone2, Integer cep) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.cep = cep;
    }

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }
}
