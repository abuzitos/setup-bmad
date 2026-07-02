package com.faculdade.media.functional;

import com.faculdade.media.dto.InstituicaoDTO;
import com.faculdade.media.dto.InstituicaoInputDTO;
import com.faculdade.media.service.InstituicaoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes funcionais para InstituicaoController.
 *
 * Valida a integração entre Controller e Service com banco SQLite em memória.
 * Não inicia servidor HTTP; a lógica é exercitada via service, como nos outros controllers.
 */
@DisplayName("Testes Funcionais - InstituicaoController")
class InstituicaoControllerTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private InstituicaoService instituicaoService;

    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();

        instituicaoService = new InstituicaoService();
        Field emField = InstituicaoService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(instituicaoService, em);
    }

    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    @DisplayName("POST /api/instituicoes deve criar instituição com sucesso")
    void deveCriarInstituicaoViaAPI() {
        em.getTransaction().begin();

        InstituicaoDTO resultado = instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));

        em.getTransaction().commit();

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo");
        assertThat(resultado.getCep()).isEqualTo(12345678);
    }

    @Test
    @DisplayName("GET /api/instituicoes deve listar todas as instituições")
    void deveListarInstituicoesViaAPI() {
        em.getTransaction().begin();
        instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de Rio de Janeiro"));
        em.getTransaction().commit();
        em.clear();

        var instituicoes = instituicaoService.listarTodos();

        assertThat(instituicoes).isNotNull();
        assertThat(instituicoes.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("GET /api/instituicoes/{id} deve retornar instituição encontrada")
    void deveBuscarInstituicaoPorIdViaAPI() {
        em.getTransaction().begin();
        InstituicaoDTO criada = instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        em.getTransaction().commit();
        em.clear();

        InstituicaoDTO resultado = instituicaoService.buscarPorId(criada.getId());

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(criada.getId());
        assertThat(resultado.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo");
    }

    @Test
    @DisplayName("PUT /api/instituicoes/{id} deve atualizar instituição com sucesso")
    void deveAtualizarInstituicaoViaAPI() {
        em.getTransaction().begin();
        InstituicaoDTO criada = instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        em.getTransaction().commit();
        em.clear();

        InstituicaoInputDTO inputAtualizado = criarInputDTO("Faculdade de Tecnologia de São Paulo - Unidade Centro");
        inputAtualizado.setCep(87654321);

        em.getTransaction().begin();
        InstituicaoDTO resultado = instituicaoService.atualizar(criada.getId(), inputAtualizado);
        em.getTransaction().commit();

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo - Unidade Centro");
        assertThat(resultado.getCep()).isEqualTo(87654321);
    }

    @Test
    @DisplayName("DELETE /api/instituicoes/{id} deve remover instituição com sucesso")
    void deveExcluirInstituicaoViaAPI() {
        em.getTransaction().begin();
        InstituicaoDTO criada = instituicaoService.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        em.getTransaction().commit();
        em.clear();

        em.getTransaction().begin();
        instituicaoService.remover(criada.getId());
        em.getTransaction().commit();
        em.clear();

        assertThatThrownBy(() -> instituicaoService.buscarPorId(criada.getId()))
            .isInstanceOf(com.faculdade.media.exception.EntidadeNaoEncontradaException.class);
    }

    private InstituicaoInputDTO criarInputDTO(String nome) {
        InstituicaoInputDTO dto = new InstituicaoInputDTO();
        dto.setNome(nome);
        dto.setEndereco("Rua das Flores, 123");
        dto.setTelefone1("11 99999-9999");
        dto.setTelefone2("11 88888-8888");
        dto.setCep(12345678);
        return dto;
    }
}
