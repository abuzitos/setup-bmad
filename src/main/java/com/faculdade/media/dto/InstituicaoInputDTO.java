package com.faculdade.media.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para entrada de dados ao criar ou atualizar uma Instituição.
 */
@Schema(description = "DTO para criação ou atualização de instituição")
public class InstituicaoInputDTO {

    @Schema(
        description = "Nome da instituição",
        example = "Faculdade de Tecnologia de São Paulo",
        required = true,
        maxLength = 100
    )
    @NotBlank(message = "Nome da instituição é obrigatório")
    @Size(max = 100, message = "Nome da instituição deve ter no máximo 100 caracteres")
    private String nome;

    @Schema(
        description = "Endereço da instituição",
        example = "Rua das Flores, 123",
        required = true,
        maxLength = 200
    )
    @NotBlank(message = "Endereço da instituição é obrigatório")
    @Size(max = 200, message = "Endereço da instituição deve ter no máximo 200 caracteres")
    private String endereco;

    @Schema(
        description = "Telefone principal",
        example = "11 99999-9999",
        required = true,
        maxLength = 20
    )
    @NotBlank(message = "Telefone principal é obrigatório")
    @Size(max = 20, message = "Telefone principal deve ter no máximo 20 caracteres")
    private String telefone1;

    @Schema(
        description = "Telefone secundário",
        example = "11 88888-8888",
        maxLength = 20
    )
    @Size(max = 20, message = "Telefone secundário deve ter no máximo 20 caracteres")
    private String telefone2;

    @Schema(
        description = "CEP (até 8 dígitos)",
        example = "1234567",
        required = true
    )
    @NotNull(message = "CEP é obrigatório")
    @Min(value = 0, message = "CEP não pode ser negativo")
    @Max(value = 99999999, message = "CEP deve ter no máximo 8 dígitos")
    private Integer cep;

    public InstituicaoInputDTO() {
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
