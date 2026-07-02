package com.faculdade.media.integration.mock.controller;

import com.faculdade.media.controller.InstituicaoController;
import com.faculdade.media.dto.InstituicaoDTO;
import com.faculdade.media.dto.InstituicaoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.service.InstituicaoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes de integração com mock para InstituicaoController.
 *
 * Isola o controller mockando InstituicaoService e valida status HTTP e delegação ao service.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes de Integração com Mock - InstituicaoController")
class InstituicaoControllerMockIT {

    @Mock
    private InstituicaoService instituicaoService;

    private InstituicaoController controller;

    @BeforeEach
    void setUp() throws Exception {
        controller = new InstituicaoController();
        Field field = InstituicaoController.class.getDeclaredField("instituicaoService");
        field.setAccessible(true);
        field.set(controller, instituicaoService);
    }

    @Test
    @DisplayName("listar deve retornar HTTP 200 e delegar ao service")
    void listarDeveRetornar200() {
        InstituicaoDTO dto = new InstituicaoDTO(1L, "Faculdade de Tecnologia de São Paulo",
            "Rua das Flores, 123", "11 99999-9999", "11 88888-8888", 12345678);
        when(instituicaoService.listarTodos()).thenReturn(List.of(dto));

        Response response = controller.listar();

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat((List<InstituicaoDTO>) response.getEntity()).containsExactly(dto);
        verify(instituicaoService).listarTodos();
    }

    @Test
    @DisplayName("buscarPorId deve retornar HTTP 200 e delegar ao service")
    void buscarPorIdDeveRetornar200() {
        InstituicaoDTO dto = new InstituicaoDTO(1L, "Faculdade de Tecnologia de São Paulo",
            "Rua das Flores, 123", "11 99999-9999", "11 88888-8888", 12345678);
        when(instituicaoService.buscarPorId(1L)).thenReturn(dto);

        Response response = controller.buscarPorId(1L);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(dto);
        verify(instituicaoService).buscarPorId(1L);
    }

    @Test
    @DisplayName("buscarPorId deve propagar EntidadeNaoEncontradaException do service")
    void buscarPorIdInexistenteDevePropagarExcecao() {
        when(instituicaoService.buscarPorId(99L))
            .thenThrow(new EntidadeNaoEncontradaException("Instituição não encontrada com ID: 99"));

        assertThatThrownBy(() -> controller.buscarPorId(99L))
            .isInstanceOf(EntidadeNaoEncontradaException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("criar deve retornar HTTP 201 e delegar ao service")
    void criarDeveRetornar201() {
        InstituicaoInputDTO input = criarInputDTO();
        InstituicaoDTO dto = new InstituicaoDTO(1L, input.getNome(), input.getEndereco(),
            input.getTelefone1(), input.getTelefone2(), input.getCep());
        when(instituicaoService.criar(input)).thenReturn(dto);

        Response response = controller.criar(input);

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(dto);
        verify(instituicaoService).criar(input);
    }

    @Test
    @DisplayName("criar deve propagar ValidacaoException do service")
    void criarComNomeDuplicadoDevePropagarExcecao() {
        InstituicaoInputDTO input = criarInputDTO();
        when(instituicaoService.criar(any(InstituicaoInputDTO.class)))
            .thenThrow(new ValidacaoException("Já existe uma instituição com o nome: " + input.getNome()));

        assertThatThrownBy(() -> controller.criar(input))
            .isInstanceOf(ValidacaoException.class)
            .hasMessageContaining(input.getNome());
    }

    @Test
    @DisplayName("atualizar deve retornar HTTP 200 e delegar ao service")
    void atualizarDeveRetornar200() {
        InstituicaoInputDTO input = criarInputDTO();
        input.setNome("Faculdade de Tecnologia de São Paulo - Unidade Centro");
        InstituicaoDTO dto = new InstituicaoDTO(1L, input.getNome(), input.getEndereco(),
            input.getTelefone1(), input.getTelefone2(), input.getCep());
        when(instituicaoService.atualizar(eq(1L), eq(input))).thenReturn(dto);

        Response response = controller.atualizar(1L, input);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.getEntity()).isEqualTo(dto);
        verify(instituicaoService).atualizar(1L, input);
    }

    @Test
    @DisplayName("remover deve retornar HTTP 204 e delegar ao service")
    void removerDeveRetornar204() {
        Response response = controller.remover(1L);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(response.getEntity()).isNull();
        verify(instituicaoService).remover(1L);
    }

    @Test
    @DisplayName("remover deve propagar EntidadeNaoEncontradaException do service")
    void removerInexistenteDevePropagarExcecao() {
        doThrow(new EntidadeNaoEncontradaException("Instituição não encontrada com ID: 99"))
            .when(instituicaoService).remover(99L);

        assertThatThrownBy(() -> controller.remover(99L))
            .isInstanceOf(EntidadeNaoEncontradaException.class)
            .hasMessageContaining("99");
    }

    private InstituicaoInputDTO criarInputDTO() {
        InstituicaoInputDTO dto = new InstituicaoInputDTO();
        dto.setNome("Faculdade de Tecnologia de São Paulo");
        dto.setEndereco("Rua das Flores, 123");
        dto.setTelefone1("11 99999-9999");
        dto.setTelefone2("11 88888-8888");
        dto.setCep(12345678);
        return dto;
    }
}
