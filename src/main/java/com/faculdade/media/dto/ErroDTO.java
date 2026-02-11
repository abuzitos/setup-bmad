package com.faculdade.media.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * DTO para representar erros retornados pela API.
 */
@Schema(description = "Representa um erro retornado pela API")
public class ErroDTO {
    
    @Schema(description = "Código do erro", example = "ERRO_VALIDACAO")
    private String codigo;
    
    @Schema(description = "Mensagem descritiva do erro", example = "Nome é obrigatório")
    private String mensagem;
    
    @Schema(description = "Detalhe técnico (apenas em modo debug)")
    private String detalhe;
    
    @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    public ErroDTO() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErroDTO(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters e Setters
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public String getDetalhe() {
        return detalhe;
    }
    
    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
