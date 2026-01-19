package com.faculdade.media.integration.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Classe base para testes de integração com banco de dados.
 * 
 * Fornece setup e teardown comum para EntityManager e transações.
 */
public abstract class BaseTestIT {
    
    protected EntityManagerFactory emf;
    protected EntityManager em;
    
    @BeforeEach
    void setUpBase() {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }
    
    @AfterEach
    void tearDownBase() {
        if (em != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
