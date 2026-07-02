package com.faculdade.media.service;

import com.faculdade.media.domain.Instituicao;
import com.faculdade.media.dto.InstituicaoDTO;
import com.faculdade.media.dto.InstituicaoInputDTO;
import com.faculdade.media.exception.EntidadeNaoEncontradaException;
import com.faculdade.media.exception.ValidacaoException;
import com.faculdade.media.repository.InstituicaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações de negócio relacionadas a Instituições.
 */
@ApplicationScoped
public class InstituicaoService {

    @Inject
    EntityManager em;

    @Transactional
    public InstituicaoDTO criar(InstituicaoInputDTO inputDTO) {
        validarInputDTO(inputDTO);

        InstituicaoRepository repository = new InstituicaoRepository(em);

        if (repository.existsByNome(inputDTO.getNome())) {
            throw new ValidacaoException("Já existe uma instituição com o nome: " + inputDTO.getNome());
        }

        Instituicao instituicao = new Instituicao(
            inputDTO.getNome(),
            inputDTO.getEndereco(),
            inputDTO.getTelefone1(),
            inputDTO.getTelefone2(),
            inputDTO.getCep()
        );
        instituicao = repository.save(instituicao);

        return toDTO(instituicao);
    }

    public InstituicaoDTO buscarPorId(Long id) {
        InstituicaoRepository repository = new InstituicaoRepository(em);
        Instituicao instituicao = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Instituição não encontrada com ID: " + id));

        return toDTO(instituicao);
    }

    public List<InstituicaoDTO> listarTodos() {
        InstituicaoRepository repository = new InstituicaoRepository(em);
        List<Instituicao> instituicoes = repository.findAll();

        return instituicoes.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public InstituicaoDTO atualizar(Long id, InstituicaoInputDTO inputDTO) {
        validarInputDTO(inputDTO);

        InstituicaoRepository repository = new InstituicaoRepository(em);

        Instituicao instituicao = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Instituição não encontrada com ID: " + id));

        if (repository.existsByNomeExcluindoId(inputDTO.getNome(), id)) {
            throw new ValidacaoException("Já existe outra instituição com o nome: " + inputDTO.getNome());
        }

        instituicao.setNome(inputDTO.getNome());
        instituicao.setEndereco(inputDTO.getEndereco());
        instituicao.setTelefone1(inputDTO.getTelefone1());
        instituicao.setTelefone2(inputDTO.getTelefone2());
        instituicao.setCep(inputDTO.getCep());
        instituicao = repository.save(instituicao);

        return toDTO(instituicao);
    }

    @Transactional
    public void remover(Long id) {
        InstituicaoRepository repository = new InstituicaoRepository(em);

        Instituicao instituicao = repository.findById(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Instituição não encontrada com ID: " + id));

        repository.delete(instituicao);
    }

    private void validarInputDTO(InstituicaoInputDTO inputDTO) {
        if (inputDTO == null) {
            throw new ValidacaoException("Dados da instituição não podem ser nulos");
        }
        if (inputDTO.getNome() == null || inputDTO.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome da instituição é obrigatório");
        }
        if (inputDTO.getEndereco() == null || inputDTO.getEndereco().trim().isEmpty()) {
            throw new ValidacaoException("Endereço da instituição é obrigatório");
        }
        if (inputDTO.getTelefone1() == null || inputDTO.getTelefone1().trim().isEmpty()) {
            throw new ValidacaoException("Telefone principal é obrigatório");
        }
        if (inputDTO.getCep() == null) {
            throw new ValidacaoException("CEP é obrigatório");
        }
        if (inputDTO.getCep() < 0 || inputDTO.getCep() > 99999999) {
            throw new ValidacaoException("CEP deve ter no máximo 8 dígitos");
        }
    }

    private InstituicaoDTO toDTO(Instituicao instituicao) {
        return new InstituicaoDTO(
            instituicao.getId(),
            instituicao.getNome(),
            instituicao.getEndereco(),
            instituicao.getTelefone1(),
            instituicao.getTelefone2(),
            instituicao.getCep()
        );
    }
}
