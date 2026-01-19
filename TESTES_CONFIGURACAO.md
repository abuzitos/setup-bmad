# Configuração de Testes - BMAD Framework

## ✅ Configuração Completa

O projeto foi configurado para suportar todos os tipos de testes solicitados:

### 1. ✅ Teste Unitário TDD
- **Status**: Configurado
- **Framework**: JUnit 5
- **Localização**: `src/test/java/**/unit/`
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn test -Punit-tests`

### 2. ✅ Teste Integrado Mock
- **Status**: Configurado
- **Framework**: JUnit 5 + Mockito
- **Localização**: `src/test/java/**/integration/mock/`
- **Nomenclatura**: `*IT.java`
- **Executar**: `mvn verify -Pintegration-tests-mock`

### 3. ✅ Teste Integrado Não Mock
- **Status**: Configurado
- **Framework**: JUnit 5
- **Localização**: `src/test/java/**/integration/nomock/`
- **Nomenclatura**: `*IT.java`
- **Executar**: `mvn verify -Pintegration-tests-no-mock`

### 4. ✅ Teste Integrado com Banco de Dados em Memória
- **Status**: Configurado
- **Framework**: JUnit 5 + SQLite in-memory
- **Localização**: `src/test/java/**/integration/db/`
- **Nomenclatura**: `*IT.java`
- **Configuração**: `src/test/resources/META-INF/persistence.xml`
- **Executar**: `mvn verify -Pintegration-tests-db`

### 5. ✅ Teste Funcional
- **Status**: Configurado
- **Framework**: REST Assured + Jersey Test Framework
- **Localização**: `src/test/java/**/functional/`
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn verify -Pfunctional-tests`

### 6. ✅ Teste de Interface com Selenium
- **Status**: Configurado
- **Framework**: Selenium WebDriver + WebDriverManager
- **Localização**: `src/test/java/**/selenium/`
- **Nomenclatura**: `*Test.java`
- **Executar**: `mvn verify -Pselenium-tests`

## 📦 Dependências Adicionadas

### Testes
- **JUnit 5.10.1**: Framework de testes (já existia)
- **Mockito 5.11.0**: Framework de mocking
- **AssertJ 3.25.1**: Assertions fluentes
- **REST Assured 5.4.0**: Testes de API REST
- **Jersey Test Framework 3.1.3**: Testes de API REST (Jersey)
- **Selenium 4.16.1**: Testes de interface web
- **WebDriverManager 5.6.2**: Gerenciamento automático de drivers
- **Testcontainers 1.19.3**: Containers para testes de integração

### Plugins Maven
- **maven-surefire-plugin 3.2.2**: Execução de testes unitários
- **maven-failsafe-plugin 3.2.2**: Execução de testes de integração

## 📁 Estrutura Criada

```
src/test/java/com/faculdade/media/
├── README.md                    # Documentação geral
├── unit/                        # Testes unitários TDD
│   └── README.md
├── integration/
│   ├── mock/                    # Testes com mocks
│   │   └── README.md
│   ├── nomock/                  # Testes sem mocks
│   │   └── README.md
│   └── db/                      # Testes com banco de dados
│       └── README.md
├── functional/                  # Testes funcionais
│   └── README.md
└── selenium/                    # Testes de interface
    └── README.md
```

## 🔧 Configurações

### Profiles Maven
Foram criados 6 profiles no `pom.xml`:
1. `unit-tests`: Executa apenas testes unitários
2. `integration-tests-mock`: Executa testes de integração com mock
3. `integration-tests-no-mock`: Executa testes de integração sem mock
4. `integration-tests-db`: Executa testes de integração com banco de dados
5. `functional-tests`: Executa testes funcionais
6. `selenium-tests`: Executa testes Selenium

### Configuração de Testes
- **Surefire Plugin**: Executa testes `*Test.java` e `*Tests.java`, exclui `*IT.java` e `*E2ETest.java`
- **Failsafe Plugin**: Executa testes `*IT.java` e `*E2ETest.java`

### Persistence Unit para Testes
O arquivo `src/test/resources/META-INF/persistence.xml` está configurado para:
- Usar SQLite em memória: `jdbc:sqlite::memory:`
- Criar e remover schema automaticamente: `create-drop`
- Usar dialeto SQLite do Hibernate

## 📝 Arquivos Atualizados

1. **pom.xml**
   - Adicionadas dependências de testes
   - Configurados plugins Surefire e Failsafe
   - Criados profiles Maven para cada tipo de teste

2. **_bmad/bmm/agents/tea.yaml**
   - Atualizado com responsabilidades para todos os tipos de teste
   - Adicionada seção `test_types` com configurações detalhadas

3. **Estrutura de Pastas**
   - Criadas pastas para cada tipo de teste
   - Criados READMEs com exemplos e documentação

## 🚀 Como Usar

### Executar Todos os Testes
```bash
mvn clean test      # Testes unitários
mvn clean verify    # Todos os testes (unitários + integração)
```

### Executar por Tipo
```bash
# Testes unitários TDD
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

### Executar Teste Específico
```bash
mvn test -Dtest=NomeDaClasseTest
mvn test -Dtest=NomeDaClasseTest#nomeDoMetodo
```

## 📚 Documentação

Cada tipo de teste possui documentação detalhada com exemplos:
- [Documentação Geral de Testes](src/test/java/com/faculdade/media/README.md)
- [Testes Unitários](src/test/java/com/faculdade/media/unit/README.md)
- [Testes de Integração com Mock](src/test/java/com/faculdade/media/integration/mock/README.md)
- [Testes de Integração sem Mock](src/test/java/com/faculdade/media/integration/nomock/README.md)
- [Testes de Integração com Banco de Dados](src/test/java/com/faculdade/media/integration/db/README.md)
- [Testes Funcionais](src/test/java/com/faculdade/media/functional/README.md)
- [Testes Selenium](src/test/java/com/faculdade/media/selenium/README.md)

## ✅ Checklist de Validação

- [x] Dependências adicionadas ao pom.xml
- [x] Plugins Maven configurados (Surefire e Failsafe)
- [x] Profiles Maven criados para cada tipo de teste
- [x] Estrutura de pastas criada
- [x] tea.yaml atualizado com responsabilidades
- [x] READMEs criados com exemplos e documentação
- [x] Configuração de banco de dados em memória para testes
- [x] WebDriverManager configurado para Selenium

## 🎯 Próximos Passos

1. **P3 - Solutioning**: O Test Engineering Agent (TEA) criará o Test Design documentando a estratégia de testes
2. **P4 - Implementation**: Durante a implementação, os testes serão criados seguindo a estrutura configurada
3. **Criação de Testes**: Seguir os exemplos nos READMEs de cada tipo de teste

## 📖 Referências

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://site.mockito.org/)
- [AssertJ Documentation](https://assertj.github.io/doc/)
- [REST Assured Documentation](https://rest-assured.io/)
- [Selenium Documentation](https://www.selenium.dev/)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Maven Failsafe Plugin](https://maven.apache.org/surefire/maven-failsafe-plugin/)

---

**Configuração realizada em**: 2024  
**Status**: ✅ Completo e pronto para uso
