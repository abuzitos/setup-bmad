package com.faculdade.media.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Exemplo de Controller documentado com Swagger/OpenAPI.
 * 
 * Este é um exemplo de como documentar endpoints REST usando annotations OpenAPI.
 * Use este arquivo como referência ao criar seus controllers.
 * 
 * Após implementar, a documentação estará disponível em:
 * - Swagger UI: http://localhost:8080/swagger-ui
 * - OpenAPI JSON: http://localhost:8080/openapi
 */
@Path("/exemplo")
@Tag(name = "Exemplo", description = "Endpoints de exemplo para documentação Swagger")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExemploControllerDocumentado {
    
    @GET
    @Operation(
        summary = "Listar itens",
        description = "Retorna uma lista de todos os itens cadastrados no sistema"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = String.class)
        )
    )
    public Response listar() {
        // Implementação aqui
        return Response.ok().build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar item por ID",
        description = "Retorna um item específico pelo seu ID"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Item encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Item não encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public Response buscarPorId(
        @Parameter(description = "ID do item", required = true, example = "1")
        @PathParam("id") Long id
    ) {
        // Implementação aqui
        return Response.ok().build();
    }
    
    @POST
    @Operation(
        summary = "Criar novo item",
        description = "Cria um novo item no sistema"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Item criado com sucesso",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public Response criar(
        @Valid
        @org.eclipse.microprofile.openapi.annotations.parameters.RequestBody(
            description = "Dados do item a ser criado",
            required = true,
            content = @Content(
                schema = @Schema(implementation = String.class)
            )
        )
        String item
    ) {
        // Implementação aqui
        return Response.status(Response.Status.CREATED).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar item",
        description = "Atualiza um item existente"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Item atualizado com sucesso"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Item não encontrado"
        ),
        @APIResponse(
            responseCode = "400",
            description = "Dados inválidos"
        )
    })
    public Response atualizar(
        @Parameter(description = "ID do item", required = true)
        @PathParam("id") Long id,
        @Valid String item
    ) {
        // Implementação aqui
        return Response.ok().build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Excluir item",
        description = "Exclui um item do sistema"
    )
    @APIResponses({
        @APIResponse(
            responseCode = "204",
            description = "Item excluído com sucesso"
        ),
        @APIResponse(
            responseCode = "404",
            description = "Item não encontrado"
        )
    })
    public Response excluir(
        @Parameter(description = "ID do item", required = true)
        @PathParam("id") Long id
    ) {
        // Implementação aqui
        return Response.noContent().build();
    }
}
