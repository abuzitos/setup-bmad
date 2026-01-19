package com.faculdade.media.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuração OpenAPI/Swagger para documentação da API REST.
 * 
 * Esta classe configura a documentação automática da API usando OpenAPI 3.0.
 * A documentação estará disponível em:
 * - JSON: http://localhost:8080/openapi
 * - UI Swagger: http://localhost:8080/swagger-ui
 */
@OpenAPIDefinition(
    info = @Info(
        title = "Sistema de Cálculo de Médias - API REST",
        version = "1.0.0",
        description = "API REST para gerenciamento de cálculo de médias acadêmicas de alunos de uma faculdade. " +
                     "Permite gestão de cursos, disciplinas, alunos, professores, matrículas e notas, " +
                     "com cálculo automático de médias e classificação de aprovação.",
        contact = @Contact(
            name = "Equipe de Desenvolvimento",
            email = "dev@faculdade.edu.br"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de Desenvolvimento"
        ),
        @Server(
            url = "http://localhost:8080/api",
            description = "API Base Path"
        )
    },
    tags = {
        @Tag(name = "Cursos", description = "Operações relacionadas a cursos"),
        @Tag(name = "Disciplinas", description = "Operações relacionadas a disciplinas"),
        @Tag(name = "Alunos", description = "Operações relacionadas a alunos"),
        @Tag(name = "Professores", description = "Operações relacionadas a professores"),
        @Tag(name = "Matrículas", description = "Operações relacionadas a matrículas de alunos em disciplinas"),
        @Tag(name = "Notas", description = "Operações relacionadas a notas e cálculo de médias")
    }
)
@ApplicationPath("/api")
public class OpenAPIConfig extends Application {
    // Esta classe serve apenas para configuração do OpenAPI
    // Os recursos REST serão registrados automaticamente via classpath scanning
}
