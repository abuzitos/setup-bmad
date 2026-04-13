package com.faculdade.media.service;

/**
 * Serviço de autenticação.
 * Responsável por validar credenciais de usuário.
 * 
 * Implementação atual: mock com credenciais hardcoded (admin/123456).
 */
public interface AuthService {
    
    /**
     * Autentica o usuário com as credenciais informadas.
     * 
     * @param usuario nome de usuário
     * @param senha senha (em texto plano - apenas para teste)
     * @return true se as credenciais forem válidas, false caso contrário
     */
    boolean autenticar(String usuario, String senha);
}
