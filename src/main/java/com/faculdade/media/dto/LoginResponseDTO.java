package com.faculdade.media.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * DTO de resposta do endpoint de login.
 */
@Schema(description = "Resposta de login bem-sucedido")
public class LoginResponseDTO {
    
    @Schema(description = "Indica se a autenticação foi bem-sucedida")
    private boolean autenticado;
    
    @Schema(description = "Mensagem descritiva")
    private String mensagem;
    
    public LoginResponseDTO() {
    }
    
    public LoginResponseDTO(boolean autenticado, String mensagem) {
        this.autenticado = autenticado;
        this.mensagem = mensagem;
    }
    
    public static LoginResponseDTO sucesso() {
        return new LoginResponseDTO(true, "Autenticação realizada com sucesso.");
    }
    
    public static LoginResponseDTO falha() {
        return new LoginResponseDTO(false, "Usuário ou senha inválidos.");
    }
    
    public boolean isAutenticado() {
        return autenticado;
    }
    
    public void setAutenticado(boolean autenticado) {
        this.autenticado = autenticado;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
