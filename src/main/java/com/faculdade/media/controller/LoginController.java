package com.faculdade.media.controller;

import com.faculdade.media.dto.ErroDTO;
import com.faculdade.media.dto.LoginInputDTO;
import com.faculdade.media.dto.LoginResponseDTO;
import com.faculdade.media.service.AuthService;
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

/**
 * Controller REST para autenticação.
 * Endpoint POST /login recebe credenciais e chama o serviço de autenticação.
 */
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Login", description = "Autenticação de usuário")
public class LoginController {
    
    @Inject
    AuthService authService;
    
    @POST
    @Operation(
        summary = "Autenticar usuário",
        description = "Valida credenciais de usuário e senha. Retorna 200 se válido, 401 se inválido."
    )
    @APIResponse(
        responseCode = "200",
        description = "Autenticação bem-sucedida",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = LoginResponseDTO.class)
        )
    )
    @APIResponse(
        responseCode = "401",
        description = "Credenciais inválidas",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = ErroDTO.class)
        )
    )
    public Response login(
            @RequestBody(
                description = "Credenciais de login",
                required = true
            )
            @Valid LoginInputDTO input) {
        
        boolean autenticado = authService.autenticar(input.getUsuario(), input.getSenha());
        
        if (autenticado) {
            return Response.ok(LoginResponseDTO.sucesso()).build();
        }
        
        ErroDTO erro = new ErroDTO("NAO_AUTENTICADO", "Usuário ou senha inválidos.");
        return Response.status(Response.Status.UNAUTHORIZED).entity(erro).build();
    }
}
