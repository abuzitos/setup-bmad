# Testes de Integração sem Mock

## Descrição
Testes de integração que testam componentes reais integrados, sem uso de mocks. 
Testam a interação real entre camadas (Service, Repository, etc.).

## Estrutura
- **Framework**: JUnit 5
- **Padrão**: Real integration
- **Nomenclatura**: `*IT.java`
- **Localização**: `src/test/java/**/integration/nomock/`

## Exemplo de Uso

```java
package com.faculdade.media.integration.nomock.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.repository.AlunoRepository;
import com.faculdade.media.service.AlunoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Integração sem Mock - AlunoService")
class AlunoServiceNoMockIT {
    
    private EntityManager em;
    private AlunoRepository alunoRepository;
    private AlunoService alunoService;
    
    @BeforeEach
    void setUp() {
        var emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();
        alunoRepository = new AlunoRepository(em);
        alunoService = new AlunoService(alunoRepository);
        
        em.getTransaction().begin();
    }
    
    @AfterEach
    void tearDown() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }
    
    @Test
    @DisplayName("Deve criar e buscar aluno com sucesso")
    void deveCriarEBuscarAlunoComSucesso() {
        // Given
        Aluno aluno = new Aluno();
        aluno.setNome("João Silva");
        aluno.setMatricula("2024001");
        
        // When
        Aluno criado = alunoService.criar(aluno);
        em.getTransaction().commit();
        em.clear();
        
        Aluno encontrado = alunoService.buscarPorId(criado.getId());
        
        // Then
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getNome()).isEqualTo("João Silva");
        assertThat(encontrado.getMatricula()).isEqualTo("2024001");
    }
}
```

## Executar Testes de Integração sem Mock

```bash
# Executar apenas testes de integração sem mock
mvn verify -Pintegration-tests-no-mock
```
