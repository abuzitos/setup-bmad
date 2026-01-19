package com.faculdade.media.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 * Configuração do Jersey para registrar recursos REST e OpenAPI.
 * 
 * Esta classe configura o servidor Jersey e registra:
 * - Recursos REST (Controllers)
 * - Configuração OpenAPI para documentação Swagger
 * - Providers para serialização JSON
 */
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
        // Configurar pacote base para scan automático de recursos REST
        packages("com.faculdade.media.controller");
        
        // Registrar configuração OpenAPI
        register(OpenAPIConfig.class);
        
        // Habilitar validação de Bean Validation
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        
        // Configurar JSON
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, false);
    }
}
