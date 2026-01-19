# Testes de Interface com Selenium

## Descrição
Testes de interface gráfica usando Selenium WebDriver para validação end-to-end da UI, interagindo com elementos da página web.

## Estrutura
- **Framework**: Selenium WebDriver
- **Padrão**: UI testing / E2E testing
- **Nomenclatura**: `*Test.java`
- **Localização**: `src/test/java/**/selenium/`

## Pré-requisitos

1. **WebDriver**: Baixe o ChromeDriver ou GeckoDriver (Firefox)
   - ChromeDriver: https://chromedriver.chromium.org/
   - GeckoDriver: https://github.com/mozilla/geckodriver/releases

2. **Ou use WebDriverManager** (recomendado - gerencia automaticamente):
   ```xml
   <dependency>
       <groupId>io.github.bonigarcia</groupId>
       <artifactId>webdrivermanager</artifactId>
       <version>5.6.2</version>
       <scope>test</scope>
   </dependency>
   ```

## Exemplo de Uso

```java
package com.faculdade.media.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes de Interface - Selenium")
class CursoSeleniumTest {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";
    
    @BeforeEach
    void setUp() {
        // Configurar ChromeDriver
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Executar sem interface gráfica (opcional)
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        // Ou usar WebDriverManager (recomendado):
        // WebDriverManager.chromedriver().setup();
        // driver = new ChromeDriver();
    }
    
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @Test
    @DisplayName("Deve acessar página de cursos e listar cursos")
    void deveAcessarPaginaDeCursos() {
        // Given
        driver.get(BASE_URL + "/cursos.html");
        
        // When
        WebElement titulo = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.tagName("h1"))
        );
        
        // Then
        assertThat(titulo.getText()).contains("Cursos");
    }
    
    @Test
    @DisplayName("Deve criar novo curso via interface")
    void deveCriarNovoCursoViaInterface() {
        // Given
        driver.get(BASE_URL + "/cursos.html");
        
        // When
        WebElement nomeInput = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("nome-curso"))
        );
        nomeInput.sendKeys("Ciência da Computação");
        
        WebElement submitButton = driver.findElement(By.id("btn-criar-curso"));
        submitButton.click();
        
        // Then
        WebElement mensagemSucesso = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.className("alert-success"))
        );
        assertThat(mensagemSucesso.getText()).contains("Curso criado com sucesso");
    }
    
    @Test
    @DisplayName("Deve validar formulário de curso")
    void deveValidarFormularioDeCurso() {
        // Given
        driver.get(BASE_URL + "/cursos.html");
        
        // When
        WebElement submitButton = driver.findElement(By.id("btn-criar-curso"));
        submitButton.click();
        
        // Then
        WebElement mensagemErro = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.className("alert-danger"))
        );
        assertThat(mensagemErro.getText()).contains("Nome é obrigatório");
    }
}
```

## Executar Testes Selenium

```bash
# Executar apenas testes Selenium
mvn verify -Pselenium-tests

# Ou executar com interface gráfica (remover --headless)
# Certifique-se de que o servidor está rodando antes de executar os testes
```

## Configuração Adicional

### Adicionar WebDriverManager ao pom.xml (Recomendado)

```xml
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.2</version>
    <scope>test</scope>
</dependency>
```

Com WebDriverManager, você não precisa baixar o driver manualmente:

```java
import io.github.bonigarcia.wdm.WebDriverManager;

@BeforeEach
void setUp() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
}
```

## Notas

- Os testes Selenium requerem que a aplicação esteja rodando
- Use `--headless` para execução em CI/CD sem interface gráfica
- Configure timeouts apropriados para elementos que podem demorar a carregar
- Use Page Object Pattern para testes mais organizados e manuteníveis
