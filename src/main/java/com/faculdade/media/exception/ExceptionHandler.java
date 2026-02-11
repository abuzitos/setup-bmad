package com.faculdade.media.exception;

import com.faculdade.media.config.TransactionFilter;
import com.faculdade.media.dto.ErroDTO;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler global de exceções para a API REST.
 * 
 * Converte exceções em respostas HTTP apropriadas com formato JSON padronizado.
 */
@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Context
    private ContainerRequestContext requestContext;
    
    @Override
    public Response toResponse(Exception exception) {
        if (requestContext != null) {
            TransactionFilter.markException(requestContext);
        }
        ErroDTO erro = new ErroDTO();
        erro.setTimestamp(LocalDateTime.now());
        
        if (exception instanceof EntidadeNaoEncontradaException) {
            erro.setCodigo("ENTIDADE_NAO_ENCONTRADA");
            erro.setMensagem(exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(erro)
                    .build();
        }
        
        if (exception instanceof ValidacaoException) {
            erro.setCodigo("ERRO_VALIDACAO");
            erro.setMensagem(exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
        if (exception instanceof IntegridadeReferencialException) {
            erro.setCodigo("ERRO_INTEGRIDADE_REFERENCIAL");
            erro.setMensagem(exception.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            String mensagens = cve.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            
            erro.setCodigo("ERRO_VALIDACAO");
            erro.setMensagem(mensagens);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
        if (exception instanceof PersistenceException) {
            // Pode ser violação de constraint única, etc.
            String mensagem = exception.getMessage();
            if (mensagem != null && mensagem.contains("UNIQUE constraint")) {
                erro.setCodigo("ERRO_DUPLICACAO");
                erro.setMensagem("Registro duplicado. Verifique os dados informados.");
            } else {
                erro.setCodigo("ERRO_PERSISTENCIA");
                erro.setMensagem("Erro ao persistir dados no banco de dados.");
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(erro)
                    .build();
        }
        
        // Exceção genérica - logar e incluir detalhe para diagnóstico
        Logger.getLogger(ExceptionHandler.class.getName())
                .log(Level.SEVERE, "Erro não mapeado na API", exception);
        erro.setCodigo("ERRO_INTERNO");
        erro.setMensagem("Erro interno do servidor. Tente novamente mais tarde.");
        String detalhe = exception.getClass().getName() + ": " + exception.getMessage();
        if (exception.getCause() != null) {
            detalhe += " [Causa: " + exception.getCause().getClass().getName() + ": " + exception.getCause().getMessage() + "]";
        }
        erro.setDetalhe(detalhe);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(erro)
                .build();
    }
}
