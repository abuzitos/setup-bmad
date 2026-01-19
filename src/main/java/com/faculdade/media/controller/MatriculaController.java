package com.faculdade.media.controller;

import com.faculdade.media.dto.MatriculaDTO;
import com.faculdade.media.dto.MatriculaInputDTO;
import com.faculdade.media.service.MatriculaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * Controller REST para operações relacionadas a Matrículas.
 */
@Path("/matriculas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Matrículas", description = "Operações relacionadas a matrículas de alunos em disciplinas")
public class MatriculaController {
    
    @Inject
    MatriculaService matriculaService;
    
    @POST
    @Operation(
        summary = "Matricular aluno em disciplina",
        description = "Matricula um aluno em uma disciplina. O aluno não pode estar já matriculado na mesma disciplina."
    )
    @APIResponse(
        responseCode = "201",
        description = "Aluno matriculado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MatriculaDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados inválidos ou aluno já está matriculado na disciplina"
    )
    @APIResponse(
        responseCode = "404",
        description = "Aluno ou disciplina não encontrado"
    )
    public Response matricular(
            @RequestBody(
                description = "Dados da matrícula a ser criada",
                required = true,
                content = @Content(schema = @Schema(implementation = MatriculaInputDTO.class))
            )
            @Valid MatriculaInputDTO inputDTO) {
        MatriculaDTO matricula = matriculaService.matricular(inputDTO);
        return Response.status(Response.Status.CREATED).entity(matricula).build();
    }
    
    
    @GET
    @Operation(
        summary = "Listar matrículas",
        description = "Retorna uma lista de matrículas. Suporta filtros opcionais por aluno e/ou disciplina."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de matrículas retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MatriculaDTO[].class)
        )
    )
    public Response listar(
            @Parameter(description = "ID do aluno para filtrar matrículas", required = false)
            @QueryParam("alunoId") Long alunoId,
            @Parameter(description = "ID da disciplina para filtrar matrículas", required = false)
            @QueryParam("disciplinaId") Long disciplinaId) {
        
        List<MatriculaDTO> matriculas;
        
        if (alunoId != null && disciplinaId != null) {
            // Se ambos os filtros estão presentes, buscar matrícula específica
            MatriculaDTO matricula = matriculaService.listarPorAluno(alunoId).stream()
                    .filter(m -> m.getDisciplinaId().equals(disciplinaId))
                    .findFirst()
                    .orElse(null);
            if (matricula != null) {
                matriculas = List.of(matricula);
            } else {
                matriculas = List.of();
            }
        } else if (alunoId != null) {
            matriculas = matriculaService.listarPorAluno(alunoId);
        } else if (disciplinaId != null) {
            matriculas = matriculaService.listarPorDisciplina(disciplinaId);
        } else {
            matriculas = matriculaService.listarTodos();
        }
        
        return Response.ok(matriculas).build();
    }
    
    @DELETE
    @Path("/disciplinas/{disciplinaId}/alunos/{alunoId}")
    @Operation(
        summary = "Desmatricular aluno",
        description = "Remove a matrícula de um aluno em uma disciplina."
    )
    @APIResponse(
        responseCode = "204",
        description = "Aluno desmatriculado com sucesso"
    )
    @APIResponse(
        responseCode = "404",
        description = "Matrícula não encontrada"
    )
    public Response desmatricular(
            @Parameter(description = "ID da disciplina", required = true)
            @PathParam("disciplinaId") Long disciplinaId,
            @Parameter(description = "ID do aluno", required = true)
            @PathParam("alunoId") Long alunoId) {
        matriculaService.desmatricular(alunoId, disciplinaId);
        return Response.noContent().build();
    }
}
