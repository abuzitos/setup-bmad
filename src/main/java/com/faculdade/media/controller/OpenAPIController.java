package com.faculdade.media.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Expõe o documento OpenAPI da API.
 * Lê o arquivo META-INF/openapi.yaml e retorna para o Swagger UI consumir.
 */
@Path("/openapi")
public class OpenAPIController {

    private static final String OPENAPI_RESOURCE = "/META-INF/openapi.yaml";

    @GET
    @Produces({"application/x-yaml", "text/yaml", "text/vnd.yaml", "application/json"})
    public String openApi() throws IOException {
        try (InputStream is = getClass().getResourceAsStream(OPENAPI_RESOURCE)) {
            if (is == null) {
                throw new IOException("openapi.yaml não encontrado em " + OPENAPI_RESOURCE);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
