# Testes Unitários (TDD)

## Descrição
Testes unitários isolados seguindo a metodologia TDD (Test-Driven Development). 
Estes testes focam em testar a lógica de negócio sem dependências externas.

## Estrutura
- **Framework**: JUnit 5
- **Padrão**: TDD (escrever teste antes da implementação)
- **Nomenclatura**: `*Test.java`
- **Localização**: `src/test/java/**/unit/`

## Exemplo de Uso

```java
package com.faculdade.media.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes Unitários - Cálculo de Médias")
class MediaServiceTest {
    
    private MediaService mediaService;
    
    @BeforeEach
    void setUp() {
        mediaService = new MediaService();
    }
    
    @Test
    @DisplayName("Deve calcular média corretamente")
    void deveCalcularMediaCorretamente() {
        // Given
        BigDecimal nota1 = new BigDecimal("8.5");
        BigDecimal nota2 = new BigDecimal("7.0");
        
        // When
        BigDecimal media = mediaService.calcularMedia(nota1, nota2);
        
        // Then
        assertThat(media).isEqualByComparingTo(new BigDecimal("7.75"));
    }
    
    @Test
    @DisplayName("Deve classificar como Aprovado quando média >= 7.0")
    void deveClassificarComoAprovado() {
        // Given
        BigDecimal media = new BigDecimal("7.5");
        
        // When
        Classificacao classificacao = mediaService.classificar(media);
        
        // Then
        assertThat(classificacao).isEqualTo(Classificacao.APROVADO);
    }
}
```

## Executar Testes Unitários

```bash
# Executar apenas testes unitários
mvn test -Punit-tests

# Ou executar todos os testes (incluindo unitários)
mvn test
```
