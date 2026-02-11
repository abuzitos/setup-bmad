package com.faculdade.media.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.CloseableService;

import jakarta.inject.Inject;

/**
 * Factory HK2 que fornece EntityManager por requisição.
 */
public class EntityManagerProvider implements Factory<EntityManager> {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("faculdadePU");

    private final CloseableService closeableService;

    @Inject
    public EntityManagerProvider(CloseableService closeableService) {
        this.closeableService = closeableService;
    }

    @Override
    public EntityManager provide() {
        EntityManager em = EMF.createEntityManager();
        closeableService.add(() -> {
            if (em.isOpen()) {
                em.close();
            }
        });
        return em;
    }

    @Override
    public void dispose(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}
