package com.faculdade.media.controller;

import com.faculdade.media.dto.AlunoDTO;
import com.faculdade.media.dto.AlunoInputDTO;
import com.faculdade.media.service.AlunoService;
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
 * Controller REST para operações relacionadas a Alunos.
 */
@Path("/alunos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Alunos", description = "Operações relacionadas a alunos")
public class AlunoController {
    
    @Inject
    AlunoService alunoService;
    
    @GET
    @Operation(
        summary = "Listar todos os alunos",
        description = "Retorna uma lista de todos os alunos cadastrados no sistema."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de alunos retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = AlunoDTO[].class)
        )
    )
    public Response listar() {
        List<AlunoDTO> alunos = alunoService.listarTodos();
        return Response.ok(alunos).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar aluno por ID",
        description = "Retorna um aluno específico pelo seu ID."
    )
    @APIResponse(
        responseCode = "200",
        description = "Aluno encontrado",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = AlunoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Aluno não encontrado"
    )
    public Response buscarPorId(@PathParam("id") Long id) {
        AlunoDTO aluno = alunoService.buscarPorId(id);
        return Response.ok(aluno).build();
    }
    
    @POST
    @Operation(
        summary = "Criar um novo aluno",
        description = "Cria um novo aluno no sistema. A matrícula do aluno deve ser única."
    )
    @APIResponse(
        responseCode = "201",
        description = "Aluno criado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = AlunoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do aluno inválidos ou matrícula já existe"
    )
    public Response criar(
        @RequestBody(
            description = "Dados do novo aluno a ser criado",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AlunoInputDTO.class)
        )
        )
        @Valid AlunoInputDTO inputDTO) {
        AlunoDTO aluno = alunoService.criar(inputDTO);
        return Response.status(Response.Status.CREATED).entity(aluno).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar um aluno existente",
        description = "Atualiza os dados de um aluno existente. A matrícula do aluno deve ser única."
    )
    @APIResponse(
        responseCode = "200",
        description = "Aluno atualizado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = AlunoDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados do aluno inválidos ou matrícula já existe"
    )
    @APIResponse(
        responseCode = "404",
        description = "Aluno não encontrado"
    )
    public Response atualizar(
        @PathParam("id") Long id,
        @RequestBody(
            description = "Dados atualizados do aluno",
            required = true,
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = AlunoInputDTO.class)
            )
        )
        @Valid AlunoInputDTO inputDTO) {
        AlunoDTO aluno = alunoService.atualizar(id, inputDTO);
        return Response.ok(aluno).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Remover um aluno",
        description = "Remove um aluno do sistema. Não é possível remover um aluno que possui matrículas ou notas vinculadas."
    )
    @APIResponse(
        responseCode = "204",
        description = "Aluno removido com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Não é possível remover o aluno pois possui matrículas ou notas vinculadas"
    )
    @APIResponse(
        responseCode = "404",
        description = "Aluno não encontrado"
    )
    public Response remover(@PathParam("id") Long id) {
        alunoService.remover(id);
        return Response.noContent().build();
    }
}
