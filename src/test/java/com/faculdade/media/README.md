# Estrutura de Testes - cursor-projeto-dojo2

Este documento descreve a estrutura de testes configurada no projeto, seguindo as melhores práticas e o protocolo BMAD.

## 📁 Estrutura de Pastas

```
src/test/java/com/faculdade/media/
├── unit/              # Testes unitários (TDD)
├── integration/
│   ├── mock/         # Testes de integração com mocks
│   ├── nomock/       # Testes de integração sem mocks
│   └── db/           # Testes de integração com banco de dados
├── functional/       # Testes funcionais (API REST)
└── selenium/         # Testes de interface (Selenium WebDriver)
```

## 🧪 Tipos de Testes

### 1. Testes Unitários (TDD)
- **Localização**: `unit/`
- **Framework**: JUnit 5
- **Abordagem**: TDD (Test-Driven Development)
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn test -Punit-tests`

### 2. Testes de Integração com Mock
- **Localização**: `integration/mock/`
- **Framework**: JUnit 5 + Mockito
- **Abordagem**: Mock dependencies
- **Nomenclatura**: `*IT.java`
- **Executar**: `mvn verify -Pintegration-tests-mock`

### 3. Testes de Integração sem Mock
- **Localização**: `integration/nomock/`
- **Framework**: JUnit 5
- **Abordagem**: Real integration
- **Nomenclatura**: `*IT.java`
- **Executar**: `mvn verify -Pintegration-tests-no-mock`

### 4. Testes de Integração com Banco de Dados
- **Localização**: `integration/db/`
- **Framework**: JUnit 5 + SQLite in-memory
- **Abordagem**: Database integration
- **Nomenclatura**: `*IT.java`
- **Executar**: `mvn verify -Pintegration-tests-db`

### 5. Testes Funcionais
- **Localização**: `functional/`
- **Framework**: REST Assured / Jersey Test
- **Abordagem**: API testing
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn verify -Pfunctional-tests`

### 6. Testes de Interface (Selenium)
- **Localização**: `selenium/`
- **Framework**: Selenium WebDriver
- **Abordagem**: UI testing / E2E
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn verify -Pselenium-tests`

## 🚀 Comandos Maven

### Executar Todos os Testes
```bash
mvn test          # Testes unitários (Surefire)
mvn verify        # Testes unitários + integração (Surefire + Failsafe)
```

### Executar por Tipo
```bash
# Testes unitários
mvn test -Punit-tests

# Testes de integração com mock
mvn verify -Pintegration-tests-mock

# Testes de integração sem mock
mvn verify -Pintegration-tests-no-mock

# Testes de integração com banco de dados
mvn verify -Pintegration-tests-db

# Testes funcionais
mvn verify -Pfunctional-tests

# Testes Selenium
mvn verify -Pselenium-tests
```

### Executar Testes Específicos
```bash
# Executar uma classe de teste específica
mvn test -Dtest=MediaServiceTest

# Executar um método específico
mvn test -Dtest=MediaServiceTest#deveCalcularMediaCorretamente
```

## 📋 Convenções

### Nomenclatura
- **Testes unitários**: `*Test.java` (ex: `MediaServiceTest.java`)
- **Testes de integração**: `*IT.java` (ex: `AlunoServiceIT.java`)
- **Testes funcionais**: `*Test.java` (ex: `CursoFunctionalTest.java`)
- **Testes Selenium**: `*Test.java` (ex: `CursoSeleniumTest.java`)

### Estrutura de Teste (AAA Pattern)
```java
@Test
@DisplayName("Descrição clara do teste")
void nomeDoTeste() {
    // Arrange / Given
    // Preparar dados e contexto
    
    // Act / When
    // Executar ação sendo testada
    
    // Assert / Then
    // Validar resultado esperado
}
```

### Annotations JUnit 5
- `@Test`: Marca método como teste
- `@DisplayName`: Nome descritivo do teste
- `@BeforeEach`: Executa antes de cada teste
- `@AfterEach`: Executa após cada teste
- `@BeforeAll`: Executa uma vez antes de todos os testes
- `@AfterAll`: Executa uma vez após todos os testes
- `@ExtendWith(MockitoExtension.class)`: Habilita Mockito

## 🔧 Configurações

### Persistence Unit para Testes
O arquivo `src/test/resources/META-INF/persistence.xml` está configurado para usar SQLite em memória:
- URL: `jdbc:sqlite::memory:`
- Hibernate: `create-drop` (cria e remove schema a cada execução)

### Profiles Maven
Os profiles estão configurados no `pom.xml` para executar diferentes tipos de teste de forma isolada.

## 📚 Documentação Detalhada

Cada tipo de teste possui um README específico com exemplos detalhados:
- [Testes Unitários](./unit/README.md)
- [Testes de Integração com Mock](./integration/mock/README.md)
- [Testes de Integração sem Mock](./integration/nomock/README.md)
- [Testes de Integração com Banco de Dados](./integration/db/README.md)
- [Testes Funcionais](./functional/README.md)
- [Testes Selenium](./selenium/README.md)

## ✅ Checklist de Qualidade

Antes de commitar testes, verifique:
- [ ] Teste segue padrão AAA (Arrange-Act-Assert)
- [ ] Nome do teste é descritivo e claro
- [ ] Usa `@DisplayName` para melhor legibilidade
- [ ] Teste é independente (não depende de outros testes)
- [ ] Teste é determinístico (sempre produz mesmo resultado)
- [ ] Teste é rápido (especialmente testes unitários)
- [ ] Teste valida comportamento, não implementação
- [ ] Código de teste está limpo e legível
- [ ] Teste cobre casos de sucesso e falha
- [ ] Teste valida exceções quando apropriado

## 🎯 Cobertura de Testes

O objetivo é manter alta cobertura de testes em todas as camadas:
- **Services**: Cobertura mínima de 80%
- **Controllers**: Cobertura mínima de 70%
- **Repositories**: Cobertura mínima de 80%
- **Domain**: Cobertura mínima de 90%

## 🔗 Referências

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [REST Assured Documentation](https://rest-assured.io/)
- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Testcontainers Documentation](https://www.testcontainers.org/)
