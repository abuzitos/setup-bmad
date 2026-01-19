package com.faculdade.media.exception;

/**
 * Exceção lançada quando ocorre erro de validação de dados.
 */
public class ValidacaoException extends RuntimeException {
    
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
    
    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
