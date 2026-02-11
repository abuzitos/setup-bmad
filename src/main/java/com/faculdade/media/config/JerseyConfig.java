package com.faculdade.media.config;

import com.faculdade.media.service.AlunoService;
import com.faculdade.media.service.CursoService;
import com.faculdade.media.service.DisciplinaService;
import com.faculdade.media.service.MatriculaService;
import com.faculdade.media.service.ProfessorService;
import jakarta.persistence.EntityManager;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 * Configuração do Jersey para registrar recursos REST e OpenAPI.
 * 
 * Esta classe configura o servidor Jersey e registra:
 * - Recursos REST (Controllers)
 * - Configuração OpenAPI para documentação Swagger
 * - Providers para serialização JSON
 * - Injeção de EntityManager e Services
 */
public class JerseyConfig extends ResourceConfig {
    
    public JerseyConfig() {
        // Configurar pacote base para scan automático de recursos REST
        packages("com.faculdade.media.controller", "com.faculdade.media.exception");
        
        // Registrar configuração OpenAPI
        register(OpenAPIConfig.class);
        
        // Registrar filtro de transação e providers
        register(TransactionFilter.class);
        
        // Bindings HK2 para EntityManager e Services
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(EntityManagerProvider.class)
                        .to(EntityManager.class)
                        .proxy(true)
                        .in(org.glassfish.jersey.process.internal.RequestScoped.class);
                
                bind(CursoService.class).to(CursoService.class);
                bind(DisciplinaService.class).to(DisciplinaService.class);
                bind(AlunoService.class).to(AlunoService.class);
                bind(ProfessorService.class).to(ProfessorService.class);
                bind(MatriculaService.class).to(MatriculaService.class);
            }
        });
        
        // Habilitar validação de Bean Validation
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        
        // Configurar JSON (Jackson) - usar Jackson em vez de Moxy
        register(JacksonFeature.class);
        register(JacksonObjectMapperProvider.class);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
    }
}
