package com.faculdade.media.controller;

import com.faculdade.media.dto.DisciplinaDTO;
import com.faculdade.media.dto.DisciplinaInputDTO;
import com.faculdade.media.service.DisciplinaService;
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
 * Controller REST para operações relacionadas a Disciplinas.
 */
@Path("/disciplinas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Disciplinas", description = "Operações relacionadas a disciplinas")
public class DisciplinaController {
    
    @Inject
    DisciplinaService disciplinaService;
    
    @Inject
    com.faculdade.media.service.MatriculaService matriculaService;
    
    @POST
    @Operation(
        summary = "Criar disciplina",
        description = "Cria uma nova disciplina no sistema, vinculada a um curso e um professor."
    )
    @APIResponse(
        responseCode = "201",
        description = "Disciplina criada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = DisciplinaDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados inválidos ou nome já existe no curso"
    )
    @APIResponse(
        responseCode = "404",
        description = "Curso ou professor não encontrado"
    )
    public Response criar(
            @RequestBody(
                description = "Dados da disciplina a ser criada",
                required = true,
                content = @Content(schema = @Schema(implementation = DisciplinaInputDTO.class))
            )
            @Valid DisciplinaInputDTO inputDTO) {
        DisciplinaDTO disciplina = disciplinaService.criar(inputDTO);
        return Response.status(Response.Status.CREATED).entity(disciplina).build();
    }
    
    @GET
    @Operation(
        summary = "Listar disciplinas",
        description = "Retorna uma lista de disciplinas. Suporta filtros opcionais por curso e/ou professor."
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de disciplinas retornada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = DisciplinaDTO[].class)
        )
    )
    public Response listar(
            @Parameter(description = "ID do curso para filtrar disciplinas", required = false)
            @QueryParam("cursoId") Long cursoId,
            @Parameter(description = "ID do professor para filtrar disciplinas", required = false)
            @QueryParam("professorId") Long professorId) {
        
        List<DisciplinaDTO> disciplinas;
        
        if (cursoId != null && professorId != null) {
            disciplinas = disciplinaService.listarPorCursoEPProfessor(cursoId, professorId);
        } else if (cursoId != null) {
            disciplinas = disciplinaService.listarPorCurso(cursoId);
        } else if (professorId != null) {
            disciplinas = disciplinaService.listarPorProfessor(professorId);
        } else {
            disciplinas = disciplinaService.listarTodos();
        }
        
        return Response.ok(disciplinas).build();
    }
    
    @GET
    @Path("/{id}")
    @Operation(
        summary = "Buscar disciplina por ID",
        description = "Retorna os detalhes de uma disciplina específica pelo seu ID."
    )
    @APIResponse(
        responseCode = "200",
        description = "Disciplina encontrada",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = DisciplinaDTO.class)
        )
    )
    @APIResponse(
        responseCode = "404",
        description = "Disciplina não encontrada"
    )
    public Response buscarPorId(
            @Parameter(description = "ID da disciplina", required = true)
            @PathParam("id") Long id) {
        DisciplinaDTO disciplina = disciplinaService.buscarPorId(id);
        return Response.ok(disciplina).build();
    }
    
    @PUT
    @Path("/{id}")
    @Operation(
        summary = "Atualizar disciplina",
        description = "Atualiza os dados de uma disciplina existente."
    )
    @APIResponse(
        responseCode = "200",
        description = "Disciplina atualizada com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = DisciplinaDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados inválidos ou nome já existe no curso"
    )
    @APIResponse(
        responseCode = "404",
        description = "Disciplina, curso ou professor não encontrado"
    )
    public Response atualizar(
            @Parameter(description = "ID da disciplina a ser atualizada", required = true)
            @PathParam("id") Long id,
            @RequestBody(
                description = "Novos dados da disciplina",
                required = true,
                content = @Content(schema = @Schema(implementation = DisciplinaInputDTO.class))
            )
            @Valid DisciplinaInputDTO inputDTO) {
        DisciplinaDTO disciplina = disciplinaService.atualizar(id, inputDTO);
        return Response.ok(disciplina).build();
    }
    
    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Excluir disciplina",
        description = "Remove uma disciplina do sistema. Não é possível excluir se houver alunos matriculados ou notas registradas."
    )
    @APIResponse(
        responseCode = "204",
        description = "Disciplina excluída com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Disciplina possui dependências (matrículas ou notas)"
    )
    @APIResponse(
        responseCode = "404",
        description = "Disciplina não encontrada"
    )
    public Response excluir(
            @Parameter(description = "ID da disciplina a ser excluída", required = true)
            @PathParam("id") Long id) {
        disciplinaService.remover(id);
        return Response.noContent().build();
    }
    
    @POST
    @Path("/{disciplinaId}/alunos/{alunoId}")
    @Operation(
        summary = "Matricular aluno em disciplina",
        description = "Matricula um aluno em uma disciplina. Endpoint alternativo ao POST /api/matriculas."
    )
    @APIResponse(
        responseCode = "201",
        description = "Aluno matriculado com sucesso",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = com.faculdade.media.dto.MatriculaDTO.class)
        )
    )
    @APIResponse(
        responseCode = "400",
        description = "Aluno já está matriculado na disciplina"
    )
    @APIResponse(
        responseCode = "404",
        description = "Aluno ou disciplina não encontrado"
    )
    public Response matricularAluno(
            @Parameter(description = "ID da disciplina", required = true)
            @PathParam("disciplinaId") Long disciplinaId,
            @Parameter(description = "ID do aluno", required = true)
            @PathParam("alunoId") Long alunoId) {
        com.faculdade.media.dto.MatriculaDTO matricula = matriculaService.matricular(alunoId, disciplinaId);
        return Response.status(Response.Status.CREATED).entity(matricula).build();
    }
    
    @DELETE
    @Path("/{disciplinaId}/alunos/{alunoId}")
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
    public Response desmatricularAluno(
            @Parameter(description = "ID da disciplina", required = true)
            @PathParam("disciplinaId") Long disciplinaId,
            @Parameter(description = "ID do aluno", required = true)
            @PathParam("alunoId") Long alunoId) {
        matriculaService.desmatricular(alunoId, disciplinaId);
        return Response.noContent().build();
    }
}
