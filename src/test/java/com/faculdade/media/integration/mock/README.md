# Testes de Integração com Mock

## Descrição
Testes de integração que usam mocks (Mockito) para isolar componentes e testar interações entre camadas sem dependências externas reais.

## Estrutura
- **Framework**: JUnit 5 + Mockito
- **Padrão**: Mock dependencies
- **Nomenclatura**: `*IT.java`
- **Localização**: `src/test/java/**/integration/mock/`

## Exemplo de Uso

```java
package com.faculdade.media.integration.mock.service;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.repository.AlunoRepository;
import com.faculdade.media.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Integração com Mock - AlunoService")
class AlunoServiceMockIT {
    
    @Mock
    private AlunoRepository alunoRepository;
    
    @InjectMocks
    private AlunoService alunoService;
    
    private Aluno aluno;
    
    @BeforeEach
    void setUp() {
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setMatricula("2024001");
    }
    
    @Test
    @DisplayName("Deve criar aluno com sucesso")
    void deveCriarAlunoComSucesso() {
        // Given
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        
        // When
        Aluno resultado = alunoService.criar(aluno);
        
        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        verify(alunoRepository).save(any(Aluno.class));
    }
}
```

## Executar Testes de Integração com Mock

```bash
# Executar apenas testes de integração com mock
mvn verify -Pintegration-tests-mock
```
