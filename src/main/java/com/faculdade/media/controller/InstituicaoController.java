package com.faculdade.media.controller;

import com.faculdade.media.dto.InstituicaoDTO;
import com.faculdade.media.dto.InstituicaoInputDTO;
import com.faculdade.media.service.InstituicaoService;
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
 * Controller REST para operações relacionadas a Instituições.
 */
@Path("/instituicoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Instituições", description = "Operações relacionadas a instituições de ensino")
public class InstituicaoController {

    @Inject
    InstituicaoService instituicaoService;

    @GET
    @Operation(
        summary = "Listar todas as instituições",
        description = "Retorna uma lista de todas as instituições cadastradas no sistema."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de instituições retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = InstituicaoDTO[].class)
        )
    )
    public Response listar() {
        List<InstituicaoDTO> instituicoes = instituicaoService.listarTodos();
        return Response.ok(instituicoes).build();
    }

    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar instituição por ID",
        description = "Retorna uma instituição específica pelo seu ID."
    )
    @APIResponse(
        responseCode = "200",
        description = "Instituição encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = InstituicaoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Instituição não encontrada"
    )
    public Response buscarPorId(@PathParam("id") Long id) {
        InstituicaoDTO instituicao = instituicaoService.buscarPorId(id);
        return Response.ok(instituicao).build();
    }

    @POST
    @Operation(
        summary = "Criar uma nova instituição",
        description = "Cria uma nova instituição no sistema. O nome da instituição deve ser único."
    )
    @APIResponse(
        responseCode = "201",
        description = "Instituição criada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = InstituicaoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados da instituição inválidos ou nome já existe"
    )
    public Response criar(
        @RequestBody(
            description = "Dados da nova instituição a ser criada",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = InstituicaoInputDTO.class)
            )
        )
        @Valid InstituicaoInputDTO inputDTO) {
        InstituicaoDTO instituicao = instituicaoService.criar(inputDTO);
        return Response.status(Response.Status.CREATED).entity(instituicao).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar uma instituição existente",
        description = "Atualiza os dados de uma instituição existente. O nome da instituição deve ser único."
    )
    @APIResponse(
        responseCode = "200",
        description = "Instituição atualizada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = InstituicaoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados da instituição inválidos ou nome já existe"
    )
    @APIResponse(
        responseCode = "404",
        description = "Instituição não encontrada"
    )
    public Response atualizar(
        @PathParam("id") Long id,
        @RequestBody(
            description = "Dados atualizados da instituição",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = InstituicaoInputDTO.class)
            )
        )
        @Valid InstituicaoInputDTO inputDTO) {
        InstituicaoDTO instituicao = instituicaoService.atualizar(id, inputDTO);
        return Response.ok(instituicao).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Remover uma instituição",
        description = "Remove uma instituição do sistema."
    )
    @APIResponse(
        responseCode = "204",
        description = "Instituição removida com sucesso"
    )
    @APIResponse(
        responseCode = "404",
        description = "Instituição não encontrada"
    )
    public Response remover(@PathParam("id") Long id) {
        instituicaoService.remover(id);
        return Response.noContent().build();
    }
}
