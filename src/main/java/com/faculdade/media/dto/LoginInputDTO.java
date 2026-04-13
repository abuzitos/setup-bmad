package com.faculdade.media.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO para entrada de dados no endpoint de login.
 */
@Schema(description = "DTO para autenticação de usuário")
public class LoginInputDTO {
    
    @Schema(
        description = "Nome de usuário",
        example = "admin",
        required = true
    )
    @NotBlank(message = "Usuário é obrigatório")
    private String usuario;
    
    @Schema(
        description = "Senha",
        example = "123456",
        required = true
    )
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    
    public LoginInputDTO() {
    }
    
    public LoginInputDTO(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
