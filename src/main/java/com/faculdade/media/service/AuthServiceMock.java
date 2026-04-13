package com.faculdade.media.service;

/**
 * Implementação mock do AuthService.
 * Valida credenciais contra valores hardcoded para testes.
 * 
 * Credenciais válidas: admin / 123456
 */
public class AuthServiceMock implements AuthService {
    
    private static final String USUARIO_VALIDO = "admin";
    private static final String SENHA_VALIDA = "123456";
    
    @Override
    public boolean autenticar(String usuario, String senha) {
        if (usuario == null || senha == null) {
            return false;
        }
        return USUARIO_VALIDO.equals(usuario.trim()) && SENHA_VALIDA.equals(senha);
    }
}
