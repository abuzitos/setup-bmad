package com.faculdade.media.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa uma Instituição de ensino.
 *
 * O nome da instituição deve ser único no sistema.
 */
@Entity
@Table(name = "instituicoes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "nome")
})
public class Instituicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome da instituição é obrigatório")
    @Size(max = 100, message = "Nome da instituição deve ter no máximo 100 caracteres")
    private String nome;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Endereço da instituição é obrigatório")
    @Size(max = 200, message = "Endereço da instituição deve ter no máximo 200 caracteres")
    private String endereco;

    @Column(name = "telefone_1", nullable = false, length = 20)
    @NotBlank(message = "Telefone principal é obrigatório")
    @Size(max = 20, message = "Telefone principal deve ter no máximo 20 caracteres")
    private String telefone1;

    @Column(name = "telefone_2", length = 20)
    @Size(max = 20, message = "Telefone secundário deve ter no máximo 20 caracteres")
    private String telefone2;

    @Column(nullable = false)
    @NotNull(message = "CEP é obrigatório")
    @Min(value = 0, message = "CEP não pode ser negativo")
    @Max(value = 99999999, message = "CEP deve ter no máximo 8 dígitos")
    private Integer cep;

    public Instituicao() {
    }

    public Instituicao(String nome, String endereco, String telefone1, String telefone2, Integer cep) {
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

    @Override
    /**
     * Compara esta instância de Instituicao com outro objeto para verificar se são iguais.
     * Duas Instituicao são consideradas iguais se:
     *   - Forem a mesma instância (comparação de referência), OU
     *   - O outro objeto não for nulo, for da mesma classe e ambos possuírem
     *     o campo 'id' não nulo e igual.
     *
     * @param o o objeto a ser comparado
     * @return true se os objetos forem considerados iguais, false caso contrário
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instituicao that = (Instituicao) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return """
                Instituicao{
                    id=%s,
                    nome='%s',
                    endereco='%s',
                    telefone1='%s',
                    telefone2='%s',
                    cep=%s
                }
                """.formatted(id, nome, endereco, telefone1, telefone2, cep);
    }
}
