package com.faculdade.media.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/**
 * Servidor do Swagger UI.
 * Retorna uma página HTML que carrega o Swagger UI via CDN e aponta para o OpenAPI da API.
 */
@Path("/swagger-ui")
public class SwaggerUIController {

    @GET
    @Produces("text/html;charset=UTF-8")
    public String swaggerUi() {
        // URL relativa: se a página está em /api/swagger-ui, openapi está em /api/openapi
        String openApiUrl = "./openapi";

        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Sistema de Cálculo de Médias - API Documentation</title>
                <link rel="stylesheet" href="https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui.css">
            </head>
            <body>
                <div id="swagger-ui"></div>
                <script src="https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-bundle.js"></script>
                <script src="https://unpkg.com/swagger-ui-dist@5.9.0/swagger-ui-standalone-preset.js"></script>
                <script>
                    window.onload = function() {
                        window.ui = SwaggerUIBundle({
                            url: "%s",
                            dom_id: '#swagger-ui',
                            deepLinking: true,
                            presets: [
                                SwaggerUIBundle.presets.apis,
                                SwaggerUIStandalonePreset
                            ],
                            layout: "StandaloneLayout"
                        });
                    };
                </script>
            </body>
            </html>
            """.formatted(openApiUrl);
    }
}
