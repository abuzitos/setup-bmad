package com.faculdade.media.integration.nomock.controller;

import com.faculdade.media.controller.InstituicaoController;
import com.faculdade.media.dto.InstituicaoDTO;
import com.faculdade.media.dto.InstituicaoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.service.InstituicaoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Testes de integração sem mock para InstituicaoController.
 *
 * Exercita o controller com service e repositório reais contra SQLite em memória.
 */
@DisplayName("Testes de Integração sem Mock - InstituicaoController")
class InstituicaoControllerIT {

    private EntityManagerFactory emf;
    private EntityManager em;
    private InstituicaoController controller;

    @BeforeEach
    void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("faculdadePU-test");
        em = emf.createEntityManager();

        InstituicaoService service = new InstituicaoService();
        Field emField = InstituicaoService.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(service, em);

        controller = new InstituicaoController();
        Field serviceField = InstituicaoController.class.getDeclaredField("instituicaoService");
        serviceField.setAccessible(true);
        serviceField.set(controller, service);
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
    @DisplayName("criar deve retornar HTTP 201 com a instituição criada")
    void criarDeveRetornar201() {
        em.getTransaction().begin();

        Response response = controller.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));

        em.getTransaction().commit();

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        InstituicaoDTO dto = (InstituicaoDTO) response.getEntity();
        assertThat(dto.getId()).isNotNull();
        assertThat(dto.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo");
        assertThat(dto.getCep()).isEqualTo(12345678);
    }

    @Test
    @DisplayName("listar deve retornar HTTP 200 com todas as instituições")
    void listarDeveRetornar200() {
        em.getTransaction().begin();
        controller.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        controller.criar(criarInputDTO("Faculdade de Tecnologia de Rio de Janeiro"));
        em.getTransaction().commit();
        em.clear();

        Response response = controller.listar();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        List<InstituicaoDTO> instituicoes = (List<InstituicaoDTO>) response.getEntity();
        assertThat(instituicoes).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("buscarPorId deve retornar HTTP 200 com a instituição")
    void buscarPorIdDeveRetornar200() {
        em.getTransaction().begin();
        Response criada = controller.criar(criarInputDTO("Faculdade de Tecnologia de São Paulo"));
        em.getTransaction().commit();
        em.clear();

        InstituicaoDTO dtoCriado = (InstituicaoDTO) criada.getEntity();
        Response response = controller.buscarPorId(dtoCriado.getId());

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        InstituicaoDTO dto = (InstituicaoDTO) response.getEntity();
        assertThat(dto.getId()).isEqualTo(dtoCriado.getId());
        assertThat(dto.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo");
    }

    @Test
    @DisplayName("buscarPorId deve lançar exceção quando instituição não existe")
    void buscarPorIdInexistenteDeveLancarExcecao() {
        assertThatThrownBy(() -> controller.buscarPorId(999L))
            .isInstanceOf(EntidadeNaoEncontradaException.class);
    }

    @Test
    @DisplayName("atualizar deve retornar HTTP 200 com dados atualizados")
    void atualizarDeveRetornar200() {
        em.getTransaction().begin();
        InstituicaoDTO criada = (InstituicaoDTO) controller.criar(
            criarInputDTO("Faculdade de Tecnologia de São Paulo")).getEntity();
        em.getTransaction().commit();
        em.clear();

        InstituicaoInputDTO inputAtualizado = criarInputDTO("Faculdade de Tecnologia de São Paulo - Unidade Centro");
        inputAtualizado.setCep(87654321);

        em.getTransaction().begin();
        Response response = controller.atualizar(criada.getId(), inputAtualizado);
        em.getTransaction().commit();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        InstituicaoDTO dto = (InstituicaoDTO) response.getEntity();
        assertThat(dto.getNome()).isEqualTo("Faculdade de Tecnologia de São Paulo - Unidade Centro");
        assertThat(dto.getCep()).isEqualTo(87654321);
    }

    @Test
    @DisplayName("remover deve retornar HTTP 204")
    void removerDeveRetornar204() {
        em.getTransaction().begin();
        InstituicaoDTO criada = (InstituicaoDTO) controller.criar(
            criarInputDTO("Faculdade de Tecnologia de São Paulo")).getEntity();
        em.getTransaction().commit();
        em.clear();

        em.getTransaction().begin();
        Response response = controller.remover(criada.getId());
        em.getTransaction().commit();
        em.clear();

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        assertThatThrownBy(() -> controller.buscarPorId(criada.getId()))
            .isInstanceOf(EntidadeNaoEncontradaException.class);
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
