# Sistema de Cálculo de Médias

## Compilação

```bash
mvn clean compile
```

## Execução

```bash
mvn clean package
java -jar target/sistema-calculo-medias-1.0.0-SNAPSHOT.jar
```

## URLs

- API REST: http://localhost:8080/api
- OpenAPI: http://localhost:8080/api/openapi
- Swagger UI: http://localhost:8080/api/swagger-ui

## Login

- URL: http://localhost:8080/api/login
- Usuário: admin
- Senha: 123456

## Chamada autenticada

### Com curl

```bash
curl -u admin:123456 http://localhost:8080/api/cursos
```

### No navegador

1. Acesse uma rota protegida, por exemplo: http://localhost:8080/api/cursos
2. Quando o navegador pedir autenticação, use:
	- Usuário: admin
	- Senha: 123456
