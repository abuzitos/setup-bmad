package com.faculdade.media.domain;

import java.math.BigDecimal;

/**
 * Enum que representa a classificação de aprovação de um aluno em uma disciplina.
 * 
 * As regras de classificação são:
 * - APROVADO: Média >= 7.0
 * - EXAME: Média >= 5.0 e < 7.0
 * - REPROVADO: Média < 5.0
 */
public enum Classificacao {
    APROVADO("Aprovado"),
    EXAME("Exame"),
    REPROVADO("Reprovado");
    
    private final String descricao;
    
    Classificacao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Classifica a aprovação baseado na média calculada.
     * 
     * @param media A média calculada (entre 0.0 e 10.0)
     * @return A classificação correspondente à média
     */
    public static Classificacao classificar(BigDecimal media) {
        if (media == null) {
            throw new IllegalArgumentException("Média não pode ser nula");
        }
        
        if (media.compareTo(new BigDecimal("7.0")) >= 0) {
            return APROVADO;
        } else if (media.compareTo(new BigDecimal("5.0")) >= 0) {
            return EXAME;
        } else {
            return REPROVADO;
        }
    }
    
    public String getDescricao() {
        return descricao;
    }
}
