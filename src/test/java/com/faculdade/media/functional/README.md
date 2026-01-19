# Testes Funcionais

## Descrição
Testes funcionais de API REST que validam endpoints, payloads, respostas HTTP e comportamento completo da API.

## Estrutura
- **Framework**: REST Assured / Jersey Test Framework
- **Padrão**: API testing
- **Nomenclatura**: `*Test.java`
- **Localização**: `src/test/java/**/functional/`

## Exemplo de Uso com REST Assured

```java
package com.faculdade.media.functional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes Funcionais - API REST")
class CursoFunctionalTest {
    
    private static final String BASE_URL = "http://localhost:8080/api";
    
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
    
    @Test
    @DisplayName("Deve criar curso via API REST")
    void deveCriarCursoViaAPI() {
        // Given
        String cursoJson = """
            {
                "nome": "Ciência da Computação"
            }
            """;
        
        // When & Then
        given()
            .contentType(ContentType.JSON)
            .body(cursoJson)
        .when()
            .post("/cursos")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("nome", equalTo("Ciência da Computação"));
    }
    
    @Test
    @DisplayName("Deve listar cursos via API REST")
    void deveListarCursosViaAPI() {
        // When & Then
        given()
        .when()
            .get("/cursos")
        .then()
            .statusCode(200)
            .body("$", isA(List.class));
    }
    
    @Test
    @DisplayName("Deve retornar 404 para curso inexistente")
    void deveRetornar404ParaCursoInexistente() {
        // When & Then
        given()
        .when()
            .get("/cursos/99999")
        .then()
            .statusCode(404);
    }
}
```

## Exemplo de Uso com Jersey Test Framework

```java
package com.faculdade.media.functional;

import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Testes Funcionais - Jersey Test")
class CursoJerseyTest extends JerseyTest {
    
    @Override
    protected Application configure() {
        return new ResourceConfig()
            .packages("com.faculdade.media.controller");
    }
    
    @Test
    @DisplayName("Deve criar curso via Jersey Test")
    void deveCriarCurso() {
        // Given
        String cursoJson = """
            {
                "nome": "Ciência da Computação"
            }
            """;
        
        // When
        var response = target("/api/cursos")
            .request()
            .post(javax.ws.rs.client.Entity.json(cursoJson));
        
        // Then
        assertEquals(201, response.getStatus());
        assertNotNull(response.readEntity(String.class));
    }
}
```

## Executar Testes Funcionais

```bash
# Executar apenas testes funcionais
mvn verify -Pfunctional-tests

# Ou executar com servidor rodando
# 1. Inicie o servidor: mvn exec:java
# 2. Execute os testes: mvn verify -Pfunctional-tests
```
