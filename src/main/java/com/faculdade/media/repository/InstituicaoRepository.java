package com.faculdade.media.repository;

import com.faculdade.media.domain.Instituicao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Camada de acesso a dados da entidade {@link Instituicao}.
 * <p>
 * Encapsula operações de leitura e escrita na tabela {@code instituicoes} do banco SQLite
 * via JPA ({@link EntityManager}). Não é um bean CDI: o {@code InstituicaoService} instancia
 * este repositório passando o {@code EntityManager} injetado.
 * <p>
 * Regras de negócio (validação de campos, nome duplicado, etc.) ficam no service;
 * esta classe concentra apenas persistência.
 */
public class InstituicaoRepository {

    private final EntityManager em;

    /**
     * Cria o repositório com o {@code EntityManager} usado em todas as operações de persistência.
     *
     * @param em instância do EntityManager do contexto de persistência
     */
    public InstituicaoRepository(EntityManager em) {
        this.em = em;
    }

    /**
     * Insere ou atualiza uma instituição.
     * <p>
     * Se {@code id} é nulo, executa {@code persist} (INSERT). Se já existe ID,
     * executa {@code merge} (UPDATE). Retorna a entidade gerenciada pelo contexto
     * de persistência (com ID atribuído após insert).
     *
     * @param instituicao a instituição a ser salva
     * @return a instituição salva
     */
    public Instituicao save(Instituicao instituicao) {
        if (instituicao.getId() == null) {
            em.persist(instituicao);
        } else {
            instituicao = em.merge(instituicao);
        }
        return instituicao;
    }

    /**
     * Busca uma instituição pelo ID primário.
     * <p>
     * Usa {@code em.find}. O {@code InstituicaoService} consome o {@code Optional}
     * para lançar {@code EntidadeNaoEncontradaException} quando o registro não existe.
     *
     * @param id o ID da instituição
     * @return {@code Optional} com a instituição se encontrada, ou vazio se não existe
     */
    public Optional<Instituicao> findById(Long id) {
        return Optional.ofNullable(em.find(Instituicao.class, id));
    }

    /**
     * Lista todas as instituições ordenadas por nome.
     * <p>
     * JPQL: {@code SELECT i FROM Instituicao i ORDER BY i.nome}.
     * Utilizado no endpoint {@code GET /instituicoes}.
     *
     * @return lista de todas as instituições
     */
    public List<Instituicao> findAll() {
        TypedQuery<Instituicao> query = em.createQuery(
            "SELECT i FROM Instituicao i ORDER BY i.nome", Instituicao.class);
        return query.getResultList();
    }

    /**
     * Remove uma instituição do banco de dados.
     * <p>
     * Se a entidade já está no contexto ({@code em.contains}), remove diretamente.
     * Caso contrário (entidade detached), faz {@code merge} antes de {@code remove}
     * para evitar erro ao remover um objeto que o {@code EntityManager} não rastreia.
     *
     * @param instituicao a instituição a ser removida
     */
    public void delete(Instituicao instituicao) {
        if (em.contains(instituicao)) {
            em.remove(instituicao);
        } else {
            em.remove(em.merge(instituicao));
        }
    }

    /**
     * Verifica se já existe instituição com o nome informado.
     * <p>
     * JPQL: {@code SELECT COUNT(i) FROM Instituicao i WHERE i.nome = :nome}.
     * Usado na <strong>criação</strong> para garantir unicidade do nome
     * (constraint {@code uk_instituicoes_nome} na entidade).
     *
     * @param nome o nome da instituição
     * @return {@code true} se já existe outra instituição com esse nome
     */
    public boolean existsByNome(String nome) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(i) FROM Instituicao i WHERE i.nome = :nome", Long.class);
        query.setParameter("nome", nome);
        return query.getSingleResult() > 0;
    }

    /**
     * Verifica se existe instituição com o nome informado, ignorando um ID específico.
     * <p>
     * JPQL: {@code SELECT COUNT(i) FROM Instituicao i WHERE i.nome = :nome AND i.id != :idExcluir}.
     * Usado na <strong>atualização</strong>: permite manter o mesmo nome no registro
     * em edição, mas bloqueia se outro registro já utiliza esse nome.
     *
     * @param nome o nome da instituição
     * @param idExcluir o ID a ser excluído da verificação (registro em edição)
     * @return {@code true} se outra instituição já usa esse nome
     */
    public boolean existsByNomeExcluindoId(String nome, Long idExcluir) {
        TypedQuery<Long> query = em.createQuery(
            "SELECT COUNT(i) FROM Instituicao i WHERE i.nome = :nome AND i.id != :idExcluir", Long.class);
        query.setParameter("nome", nome);
        query.setParameter("idExcluir", idExcluir);
        return query.getSingleResult() > 0;
    }
}
