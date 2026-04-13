package com.faculdade.media.config;

import com.faculdade.media.dto.ErroDTO;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.annotation.Priority;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

/**
 * Route Guard / Middleware de autenticação via Basic Auth.
 * Protege todas as rotas da API, exceto paths públicos (/login, /openapi, /swagger-ui).
 * Redireciona usuários não autenticados com 401 + WWW-Authenticate (dispara popup no navegador).
 * Credenciais válidas (hardcoded para teste): admin / 123456
 */
@Provider
@Priority(1000)
public class AuthenticationFilter implements ContainerRequestFilter {
    
    private static final String USUARIO_VALIDO = "admin";
    private static final String SENHA_VALIDA = "123456";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BASIC_PREFIX = "Basic ";
    
    private static final List<String> PATHS_PUBLICOS = List.of(
        "login",
        "openapi",
        "swagger-ui"
    );
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = obterPath(requestContext);
        
        if (isPathPublico(path)) {
            return;
        }
        
        String authHeader = requestContext.getHeaderString(AUTH_HEADER);
        
        if (authHeader == null || !authHeader.toLowerCase(Locale.ROOT).startsWith(BASIC_PREFIX.toLowerCase(Locale.ROOT))) {
            abortarNaoAutenticado(requestContext, "Credenciais ausentes. Use Authorization: Basic <base64(usuario:senha)> ou curl -u usuario:senha");
            return;
        }
        
        String encoded = authHeader.substring(BASIC_PREFIX.length()).trim();
        String usuarioSenha;
        
        try {
            usuarioSenha = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            abortarNaoAutenticado(requestContext, "Header Authorization inválido.");
            return;
        }
        
        int colon = usuarioSenha.indexOf(':');
        if (colon < 0) {
            abortarNaoAutenticado(requestContext, "Formato esperado: usuario:senha em Base64.");
            return;
        }
        
        String usuario = usuarioSenha.substring(0, colon).trim();
        String senha = usuarioSenha.substring(colon + 1);
        
        if (!USUARIO_VALIDO.equals(usuario) || !SENHA_VALIDA.equals(senha)) {
            abortarNaoAutenticado(requestContext, "Credenciais inválidas.");
            return;
        }
        
        // Autenticado — seguir fluxo
    }
    
    private String obterPath(ContainerRequestContext requestContext) {
        if (requestContext.getUriInfo() == null || requestContext.getUriInfo().getPath() == null) {
            return "";
        }
        return requestContext.getUriInfo().getPath().toLowerCase(Locale.ROOT);
    }
    
    private boolean isPathPublico(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        String pathNorm = path.startsWith("/") ? path.substring(1) : path;
        for (String publico : PATHS_PUBLICOS) {
            if (pathNorm.equals(publico) || pathNorm.startsWith(publico + "/")) {
                return true;
            }
        }
        return false;
    }
    
    private void abortarNaoAutenticado(ContainerRequestContext requestContext, String mensagem) {
        ErroDTO erro = new ErroDTO("NAO_AUTENTICADO", mensagem);
        requestContext.abortWith(
            Response.status(Response.Status.UNAUTHORIZED)
                .entity(erro)
                .type(MediaType.APPLICATION_JSON)
                .header("WWW-Authenticate", "Basic realm=\"Sistema de Cálculo de Médias\"")
                .build()
        );
    }
}
