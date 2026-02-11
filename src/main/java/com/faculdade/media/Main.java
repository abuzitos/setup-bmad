package com.faculdade.media;

import com.faculdade.media.config.JerseyConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

/**
 * Ponto de entrada da aplicação. Inicia o servidor HTTP Grizzly com Jersey.
 */
public class Main {

    private static final String BASE_URI = "http://0.0.0.0:8080/api/";

    public static void main(String[] args) {
        try {
            JerseyConfig config = new JerseyConfig();
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(
                    URI.create(BASE_URI), config, true);

            System.out.println("Sistema de Cálculo de Médias iniciado.");
            System.out.println("API REST disponível em: http://localhost:8080/api");
            System.out.println("Swagger UI: http://localhost:8080/swagger-ui");
            System.out.println("Pressione Ctrl+C para encerrar.");

            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Servidor interrompido.");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
