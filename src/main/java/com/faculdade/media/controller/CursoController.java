package com.faculdade.media.controller;

import com.faculdade.media.dto.CursoDTO;
import com.faculdade.media.dto.CursoInputDTO;
import com.faculdade.media.service.CursoService;
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
 * Controller REST para operações relacionadas a Cursos.
 */
@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cursos", description = "Operações relacionadas a cursos")
public class CursoController {
    
    @Inject
    CursoService cursoService;
    
    @GET
    @Operation(
        summary = "Listar todos os cursos",
        description = "Retorna uma lista de todos os cursos cadastrados no sistema."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de cursos retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CursoDTO[].class)
        )
    )
    public Response listar() {
        List<CursoDTO> cursos = cursoService.listarTodos();
        return Response.ok(cursos).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar curso por ID",
        description = "Retorna um curso específico pelo seu ID."
    )
    @APIResponse(
        responseCode = "200",
        description = "Curso encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CursoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Curso não encontrado"
    )
    public Response buscarPorId(@PathParam("id") Long id) {
        CursoDTO curso = cursoService.buscarPorId(id);
        return Response.ok(curso).build();
    }
    
    @POST
    @Operation(
        summary = "Criar um novo curso",
        description = "Cria um novo curso no sistema. O nome do curso deve ser único."
    )
    @APIResponse(
        responseCode = "201",
        description = "Curso criado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CursoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do curso inválidos ou nome já existe"
    )
    public Response criar(
        @RequestBody(
            description = "Dados do novo curso a ser criado",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = CursoInputDTO.class)
            )
        )
        @Valid CursoInputDTO inputDTO) {
        CursoDTO curso = cursoService.criar(inputDTO);
        return Response.status(Response.Status.CREATED).entity(curso).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar um curso existente",
        description = "Atualiza os dados de um curso existente. O nome do curso deve ser único."
    )
    @APIResponse(
        responseCode = "200",
        description = "Curso atualizado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CursoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do curso inválidos ou nome já existe"
    )
    @APIResponse(
        responseCode = "404",
        description = "Curso não encontrado"
    )
    public Response atualizar(
        @PathParam("id") Long id,
        @RequestBody(
            description = "Dados atualizados do curso",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = CursoInputDTO.class)
            )
        )
        @Valid CursoInputDTO inputDTO) {
        CursoDTO curso = cursoService.atualizar(id, inputDTO);
        return Response.ok(curso).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Remover um curso",
        description = "Remove um curso do sistema. Não é possível remover um curso que possui disciplinas vinculadas."
    )
    @APIResponse(
        responseCode = "204",
        description = "Curso removido com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Não é possível remover o curso pois possui disciplinas vinculadas"
    )
    @APIResponse(
        responseCode = "404",
        description = "Curso não encontrado"
    )
    public Response remover(@PathParam("id") Long id) {
        cursoService.remover(id);
        return Response.noContent().build();
    }
}
