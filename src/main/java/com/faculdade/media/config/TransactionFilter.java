package com.faculdade.media.config;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Filtro que gerencia transações JPA por requisição.
 * Inicia transação no início e faz commit/rollback no fim.
 */
@Provider
public class TransactionFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String EXCEPTION_OCCURRED = "transactionFilter.exceptionOccurred";

    @Inject
    private jakarta.inject.Provider<EntityManager> emProvider;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        EntityManager em = emProvider.get();
        if (em != null && em.isOpen()) {
            em.getTransaction().begin();
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        EntityManager em = emProvider.get();
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            try {
                boolean rollback = requestContext.getProperty(EXCEPTION_OCCURRED) != null
                        || (responseContext.getStatus() >= Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
                if (rollback) {
                    em.getTransaction().rollback();
                } else {
                    em.getTransaction().commit();
                }
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
        }
    }

    public static void markException(ContainerRequestContext requestContext) {
        if (requestContext != null) {
            try {
                requestContext.setProperty(EXCEPTION_OCCURRED, true);
            } catch (Exception ignored) {
                // Request context pode estar em estado inválido em alguns cenários
            }
        }
    }
}
