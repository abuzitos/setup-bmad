package com.faculdade.media.exception;

/**
 * Exceção lançada quando uma entidade não é encontrada no sistema.
 */
public class EntidadeNaoEncontradaException extends RuntimeException {
    
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
    
    public EntidadeNaoEncontradaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
