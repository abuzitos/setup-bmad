package com.faculdade.media.exception;

/**
 * Exceção lançada quando uma operação viola a integridade referencial do banco de dados.
 * Por exemplo, tentar excluir uma entidade que possui relacionamentos dependentes.
 */
public class IntegridadeReferencialException extends RuntimeException {
    
    public IntegridadeReferencialException(String mensagem) {
        super(mensagem);
    }
    
    public IntegridadeReferencialException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
