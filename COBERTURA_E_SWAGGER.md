# Configuração de Cobertura de Testes e Swagger/OpenAPI

## ✅ Configuração Completa

O projeto foi configurado para suportar:
1. **Cobertura de Testes** usando JaCoCo
2. **Documentação Swagger/OpenAPI** para API REST

---

## 📊 Cobertura de Testes (JaCoCo)

### Configuração

O JaCoCo foi configurado no `pom.xml` com as seguintes metas de cobertura:

- **Cobertura de Linha**: Mínimo 80%
- **Cobertura de Branch**: Mínimo 75%
- **Cobertura de Classe**: Mínimo 70%

### Classes Excluídas

As seguintes classes são automaticamente excluídas da análise de cobertura:
- `*DTO` - Objetos de transferência de dados
- `*Exception` - Classes de exceção

### Comandos

#### Gerar Relatório de Cobertura

```bash
# Executar testes e gerar relatório
mvn clean test jacoco:report

# O relatório estará disponível em:
# target/site/jacoco/index.html
```

#### Verificar Cobertura (com validação de metas)

```bash
# Executar testes e verificar se metas foram atingidas
mvn clean test jacoco:check

# Se as metas não forem atingidas, o build falhará
```

#### Executar com Cobertura Completa

```bash
# Testes unitários + integração com cobertura
mvn clean verify jacoco:report jacoco:check
```

### Relatórios Gerados

Após executar os comandos, os seguintes relatórios serão gerados:

- **HTML Report**: `target/site/jacoco/index.html`
- **XML Report**: `target/site/jacoco/jacoco.xml`
- **CSV Report**: `target/site/jacoco/jacoco.csv`

### Visualizar Relatório

1. Execute: `mvn clean test jacoco:report`
2. Abra no navegador: `target/site/jacoco/index.html`
3. Navegue pela estrutura de pacotes para ver cobertura detalhada

### Metas de Cobertura por Camada

| Camada | Cobertura Mínima | Justificativa |
|--------|------------------|---------------|
| **Services** | 80% | Lógica de negócio crítica |
| **Controllers** | 70% | Validação de endpoints |
| **Repositories** | 80% | Operações de persistência |
| **Domain** | 90% | Entidades e regras de negócio |
| **DTOs** | Excluído | Apenas estruturas de dados |
| **Exceptions** | Excluído | Classes de exceção simples |

### Integração com CI/CD

Para integração contínua, adicione ao pipeline:

```yaml
# Exemplo GitHub Actions
- name: Run tests with coverage
  run: mvn clean test jacoco:report

- name: Check coverage
  run: mvn jacoco:check

- name: Upload coverage report
  uses: codecov/codecov-action@v3
  with:
    files: target/site/jacoco/jacoco.xml
```

---

## 📚 Documentação Swagger/OpenAPI

### Configuração

O projeto está configurado com **SmallRye OpenAPI** para documentação automática da API REST.

### Dependências

- `microprofile-openapi-api` - API OpenAPI
- `smallrye-open-api` - Implementação SmallRye
- `smallrye-open-api-jaxrs` - Integração com Jakarta REST

### Arquivos de Configuração

1. **OpenAPIConfig.java** - Configuração principal do OpenAPI
   - Localização: `src/main/java/com/faculdade/media/config/OpenAPIConfig.java`
   - Define informações da API, servidores, tags

2. **JerseyConfig.java** - Configuração do Jersey
   - Localização: `src/main/java/com/faculdade/media/config/JerseyConfig.java`
   - Registra filtros OpenAPI e recursos REST

3. **openapi.yaml** - Especificação OpenAPI base
   - Localização: `src/main/resources/META-INF/openapi.yaml`
   - Define schemas e exemplos iniciais

### URLs da Documentação

Quando a aplicação estiver rodando:

- **Swagger UI**: http://localhost:8080/swagger-ui
- **OpenAPI JSON**: http://localhost:8080/openapi
- **OpenAPI YAML**: http://localhost:8080/openapi?format=yaml

### Como Usar Annotations

#### Documentar Endpoint

```java
@Path("/cursos")
@Tag(name = "Cursos", description = "Operações relacionadas a cursos")
public class CursoController {
    
    @GET
    @Operation(
        summary = "Listar todos os cursos",
        description = "Retorna uma lista de todos os cursos cadastrados no sistema"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de cursos retornada com sucesso",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CursoDTO.class)
        )
    )
    public Response listarCursos() {
        // implementação
    }
    
    @POST
    @Operation(summary = "Criar novo curso")
    @APIResponse(
        responseCode = "201",
        description = "Curso criado com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados inválidos",
        content = @Content(
            schema = @Schema(implementation = ErroDTO.class)
        )
    )
    public Response criarCurso(@Valid CursoDTO cursoDTO) {
        // implementação
    }
}
```

#### Documentar DTO

```java
@Schema(description = "Representa um curso no sistema")
public class CursoDTO {
    
    @Schema(description = "ID único do curso", example = "1", required = true)
    private Long id;
    
    @Schema(
        description = "Nome do curso",
        example = "Ciência da Computação",
        maxLength = 100,
        required = true
    )
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;
    
    // getters e setters
}
```

#### Documentar Resposta de Erro

```java
@Schema(description = "Representa um erro retornado pela API")
public class ErroDTO {
    
    @Schema(description = "Código do erro", example = "ERRO_VALIDACAO")
    private String codigo;
    
    @Schema(description = "Mensagem descritiva do erro", example = "Nome é obrigatório")
    private String mensagem;
    
    @Schema(description = "Timestamp do erro", example = "2024-01-15T10:30:00Z")
    private LocalDateTime timestamp;
    
    // getters e setters
}
```

### Annotations OpenAPI Disponíveis

- `@OpenAPIDefinition` - Configuração geral da API
- `@Tag` - Tags para agrupar endpoints
- `@Operation` - Documentar operação (GET, POST, etc.)
- `@APIResponse` - Documentar resposta HTTP
- `@Schema` - Documentar modelo/DTO
- `@Parameter` - Documentar parâmetro
- `@RequestBody` - Documentar corpo da requisição
- `@Content` - Especificar conteúdo da resposta

### Validação e Documentação

As validações Jakarta Validation são automaticamente incluídas na documentação:

```java
@Schema(description = "DTO para criação de curso")
public class CursoInputDTO {
    
    @NotBlank
    @Size(max = 100)
    @Schema(
        description = "Nome do curso",
        example = "Ciência da Computação",
        required = true,
        maxLength = 100
    )
    private String nome;
}
```

### Atualizar Documentação

1. **Automático**: Annotations nas classes são automaticamente incluídas
2. **Manual**: Edite `src/main/resources/META-INF/openapi.yaml` para adicionar informações extras
3. **Verificar**: Acesse http://localhost:8080/swagger-ui após iniciar a aplicação

### Arquivo de Exemplo

Um arquivo de exemplo completo está disponível em:
- `src/main/java/com/faculdade/media/controller/ExemploControllerDocumentado.java`

Este arquivo demonstra como documentar todos os tipos de endpoints (GET, POST, PUT, DELETE) com annotations OpenAPI.

### Exemplo de Uso Completo

```java
package com.faculdade.media.controller;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import com.faculdade.media.dto.CursoDTO;

@Path("/cursos")
@Tag(name = "Cursos", description = "Operações relacionadas a cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoController {
    
    @GET
    @Operation(
        summary = "Listar todos os cursos",
        description = "Retorna uma lista paginada de todos os cursos cadastrados"
    )
    @APIResponse(
        responseCode = "200",
        description = "Lista de cursos retornada com sucesso"
    )
    public Response listarCursos() {
        // implementação
        return Response.ok().build();
    }
    
    @POST
    @Operation(
        summary = "Criar novo curso",
        description = "Cria um novo curso no sistema. O nome deve ser único."
    )
    @APIResponse(
        responseCode = "201",
        description = "Curso criado com sucesso"
    )
    @APIResponse(
        responseCode = "400",
        description = "Dados inválidos ou nome já existe"
    )
    public Response criarCurso(@Valid CursoDTO cursoDTO) {
        // implementação
        return Response.status(Response.Status.CREATED).build();
    }
}
```

---

## 🔧 Integração com BMAD

### Test Engineering Agent (TEA)

O TEA é responsável por:
- Monitorar cobertura de código usando JaCoCo
- Garantir que metas de cobertura sejam atingidas
- Validar que documentação Swagger está atualizada
- Verificar que endpoints estão documentados

### Software Architect

O Architect é responsável por:
- Configurar estrutura de documentação Swagger/OpenAPI
- Garantir que arquitetura da API seja documentável
- Definir schemas e modelos para documentação

---

## 📋 Checklist

### Cobertura de Testes

- [x] JaCoCo configurado no pom.xml
- [x] Metas de cobertura definidas (80% linha, 75% branch)
- [x] Classes excluídas configuradas (DTOs, Exceptions)
- [x] Relatórios HTML/XML/CSV configurados
- [ ] Testes criados para atingir metas de cobertura
- [ ] Integração com CI/CD configurada

### Documentação Swagger

- [x] SmallRye OpenAPI adicionado ao projeto
- [x] OpenAPIConfig.java criado
- [x] JerseyConfig.java criado
- [x] openapi.yaml base criado
- [ ] Endpoints documentados com annotations
- [ ] DTOs documentados com @Schema
- [ ] Exemplos de request/response adicionados
- [ ] Swagger UI testado e funcionando

---

## 🚀 Próximos Passos

1. **Durante P3 (Solutioning)**:
   - Architect definirá estrutura completa de documentação
   - TEA definirá estratégia de cobertura de testes

2. **Durante P4 (Implementation)**:
   - Developer implementará código com annotations OpenAPI
   - TEA criará testes para atingir metas de cobertura
   - TEA validará documentação Swagger

3. **Validação Contínua**:
   - Executar `mvn jacoco:check` antes de commits
   - Verificar Swagger UI após cada endpoint implementado
   - Manter documentação atualizada

---

## 📖 Referências

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [SmallRye OpenAPI](https://smallrye.io/smallrye-open-api/)
- [MicroProfile OpenAPI](https://microprofile.io/project/eclipse/microprofile-open-api)
- [Jakarta REST Annotations](https://jakarta.ee/specifications/restful-ws/)

---

**Configuração realizada em**: 2024  
**Status**: ✅ Completo e pronto para uso
