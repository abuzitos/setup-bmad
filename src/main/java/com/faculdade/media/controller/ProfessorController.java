package com.faculdade.media.controller;

import com.faculdade.media.dto.ProfessorDTO;
import com.faculdade.media.dto.ProfessorInputDTO;
import com.faculdade.media.service.ProfessorService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * Controller REST para operações relacionadas a Professores.
 */
@Path("/professores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Professores", description = "Operações relacionadas a professores")
public class ProfessorController {
    
    @Inject
    ProfessorService professorService;
    
    @GET
    @Operation(
        summary = "Listar todos os professores",
        description = "Retorna uma lista de todos os professores cadastrados no sistema."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de professores retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ProfessorDTO[].class)
        )
    )
    public Response listar() {
        List<ProfessorDTO> professores = professorService.listarTodos();
        return Response.ok(professores).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar professor por ID",
        description = "Retorna um professor específico pelo seu ID."
    )
    @APIResponse(
        responseCode = "200",
        description = "Professor encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ProfessorDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Professor não encontrado"
    )
    public Response buscarPorId(@PathParam("id") Long id) {
        ProfessorDTO professor = professorService.buscarPorId(id);
        return Response.ok(professor).build();
    }
    
    @POST
    @Operation(
        summary = "Criar um novo professor",
        description = "Cria um novo professor no sistema. O registro do professor deve ser único."
    )
    @APIResponse(
        responseCode = "201",
        description = "Professor criado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ProfessorDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do professor inválidos ou registro já existe"
    )
    public Response criar(
        @RequestBody(
            description = "Dados do novo professor a ser criado",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProfessorInputDTO.class)
            )
        )
        @Valid ProfessorInputDTO inputDTO) {
        ProfessorDTO professor = professorService.criar(inputDTO);
        return Response.status(Response.Status.CREATED).entity(professor).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar um professor existente",
        description = "Atualiza os dados de um professor existente. O registro do professor deve ser único."
    )
    @APIResponse(
        responseCode = "200",
        description = "Professor atualizado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ProfessorDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do professor inválidos ou registro já existe"
    )
    @APIResponse(
        responseCode = "404",
        description = "Professor não encontrado"
    )
    public Response atualizar(
        @PathParam("id") Long id,
        @RequestBody(
            description = "Dados atualizados do professor",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = ProfessorInputDTO.class)
            )
        )
        @Valid ProfessorInputDTO inputDTO) {
        ProfessorDTO professor = professorService.atualizar(id, inputDTO);
        return Response.ok(professor).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Remover um professor",
        description = "Remove um professor do sistema. Não é possível remover um professor que possui disciplinas vinculadas."
    )
    @APIResponse(
        responseCode = "204",
        description = "Professor removido com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Não é possível remover o professor pois possui disciplinas vinculadas"
    )
    @APIResponse(
        responseCode = "404",
        description = "Professor não encontrado"
    )
    public Response remover(@PathParam("id") Long id) {
        professorService.remover(id);
        return Response.noContent().build();
    }
}
