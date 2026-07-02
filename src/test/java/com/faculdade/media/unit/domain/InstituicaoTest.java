package com.faculdade.media.unit.domain;

import com.faculdade.media.domain.Instituicao;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes Unitários - Instituicao")
class InstituicaoTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private Instituicao criarInstituicaoValida() {
        return new Instituicao(
            "Faculdade de Tecnologia de São Paulo",
            "Rua das Flores, 123",
            "11 99999-9999",
            "11 88888-8888",
            12345678
        );
    }

    @Nested
    @DisplayName("Construtor e accessors")
  class ConstrutorEAccessors {

        @Test
        @DisplayName("construtor com todos os campos atribui valores corretamente")
        void construtorComTodosOsCampos() {
            Instituicao instituicao = criarInstituicaoValida();

            assertThat(instituicao.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo");
            assertThat(instituicao.getEndereco()).isEqualTo("Rua das Flores, 123");
            assertThat(instituicao.getTelefone1()).isEqualTo("11 99999-9999");
            assertThat(instituicao.getTelefone2()).isEqualTo("11 88888-8888");
            assertThat(instituicao.getCep()).isEqualTo(12345678);
        }

        @Test
        @DisplayName("setters atualizam os campos")
        void settersAtualizamCampos() {
            Instituicao instituicao = new Instituicao();

            instituicao.setId(1L);
            instituicao.setNome("Nova Instituição");
            instituicao.setEndereco("Av. Paulista, 1000");
            instituicao.setTelefone1("11 11111-1111");
            instituicao.setTelefone2("11 22222-2222");
            instituicao.setCep(87654321);

            assertThat(instituicao.getId()).isEqualTo(1L);
            assertThat(instituicao.getNome()).isEqualTo("Nova Instituição");
            assertThat(instituicao.getEndereco()).isEqualTo("Av. Paulista, 1000");
            assertThat(instituicao.getTelefone1()).isEqualTo("11 11111-1111");
            assertThat(instituicao.getTelefone2()).isEqualTo("11 22222-2222");
            assertThat(instituicao.getCep()).isEqualTo(87654321);
        }
    }

    @Nested
    @DisplayName("equals e hashCode")
    class EqualsEHashCode {

        @Test
        @DisplayName("instituições com mesmo ID são iguais")
        void mesmoIdSaoIguais() {
            Instituicao a = criarInstituicaoValida();
            a.setId(1L);
            Instituicao b = criarInstituicaoValida();
            b.setId(1L);

            assertThat(a).isEqualTo(b);
            assertThat(a.hashCode()).isEqualTo(b.hashCode());
        }

        @Test
        @DisplayName("instituições com IDs diferentes não são iguais")
        void idsDiferentesNaoSaoIguais() {
            Instituicao a = criarInstituicaoValida();
            a.setId(1L);
            Instituicao b = criarInstituicaoValida();
            b.setId(2L);

            assertThat(a).isNotEqualTo(b);
        }

        @Test
        @DisplayName("instituição sem ID não é igual a outra sem ID")
        void semIdNaoSaoIguais() {
            Instituicao a = criarInstituicaoValida();
            Instituicao b = criarInstituicaoValida();

            assertThat(a).isNotEqualTo(b);
        }

        @Test
        @DisplayName("equals retorna true para a mesma referência")
        void mesmaReferencia() {
            Instituicao instituicao = criarInstituicaoValida();

            assertThat(instituicao).isEqualTo(instituicao);
        }

        @Test
        @DisplayName("equals retorna false para null e para outro tipo")
        void nullEOutroTipo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setId(1L);

            assertThat(instituicao).isNotEqualTo(null);
            assertThat(instituicao).isNotEqualTo("texto");
        }
    }

    @Nested
    @DisplayName("toString")
    class ToString {

        @Test
        @DisplayName("inclui os campos principais")
        void incluiCamposPrincipais() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setId(1L);

            String texto = instituicao.toString();

            assertThat(texto)
                .contains("id=1")
                .contains("nome='Faculdade de Tecnologia de São Paulo'")
                .contains("endereco='Rua das Flores, 123'")
                .contains("telefone1='11 99999-9999'")
                .contains("telefone2='11 88888-8888'")
                .contains("cep=12345678");
        }
    }

    @Nested
    @DisplayName("Validação Bean Validation")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Validacao {

        @Test
        @DisplayName("instituição válida não gera violações")
        void deveAceitarSemViolacoes() {
            Set<ConstraintViolation<Instituicao>> violacoes = validator.validate(criarInstituicaoValida());

            assertThat(violacoes).isEmpty();
        }

        @Test
        @DisplayName("nome em branco gera violação")
        void nomeEmBranco() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setNome("");

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("nome"));
        }

        @Test
        @DisplayName("nome com mais de 100 caracteres gera violação")
        void nomeMuitoLongo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setNome("a".repeat(101));

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("nome"));
        }

        @Test
        @DisplayName("endereço em branco gera violação")
        void enderecoEmBranco() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setEndereco("");

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("endereco"));
        }

        @Test
        @DisplayName("endereço com mais de 200 caracteres gera violação")
        void enderecoMuitoLongo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setEndereco("a".repeat(201));

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("endereco"));
        }

        @Test
        @DisplayName("telefone principal em branco gera violação")
        void telefone1EmBranco() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setTelefone1("");

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("telefone1"));
        }

        @Test
        @DisplayName("telefone principal com mais de 20 caracteres gera violação")
        void telefone1MuitoLongo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setTelefone1("1".repeat(21));

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("telefone1"));
        }

        @Test
        @DisplayName("telefone secundário com mais de 20 caracteres gera violação")
        void telefone2MuitoLongo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setTelefone2("1".repeat(21));

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("telefone2"));
        }

        @Test
        @DisplayName("CEP nulo gera violação")
        void cepNulo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setCep(null);

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("cep"));
        }

        @Test
        @DisplayName("CEP negativo gera violação")
        void cepNegativo() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setCep(-1);

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("cep"));
        }

        @Test
        @DisplayName("CEP com mais de 8 dígitos gera violação")
        void cepMuitoGrande() {
            Instituicao instituicao = criarInstituicaoValida();
            instituicao.setCep(100000000);

            assertThat(violacoes(instituicao))
                .anyMatch(v -> v.getPropertyPath().toString().equals("cep"));
        }

        @Test
        @DisplayName("CEP zero e máximo de 8 dígitos são válidos")
        void cepLimitesValidos() {
            Instituicao cepZero = criarInstituicaoValida();
            cepZero.setCep(0);

            Instituicao cepMaximo = criarInstituicaoValida();
            cepMaximo.setCep(99999999);

            assertThat(violacoes(cepZero)).isEmpty();
            assertThat(violacoes(cepMaximo)).isEmpty();
        }

        private Set<ConstraintViolation<Instituicao>> violacoes(Instituicao instituicao) {
            return validator.validate(instituicao);
        }
    }
}
