package com.faculdade.media.util;

import com.faculdade.media.domain.Aluno;
import com.faculdade.media.domain.Curso;
import com.faculdade.media.domain.Disciplina;
import com.faculdade.media.domain.Matricula;
import com.faculdade.media.domain.Professor;

/**
 * Fixtures e builders para testes.
 * 
 * Fornece métodos estáticos para criar entidades de teste de forma consistente.
 */
public class TestFixtures {
    
    /**
     * Cria um curso de teste padrão.
     * 
     * @return Curso com nome "Ciência da Computação"
     */
    public static Curso createCurso() {
        return new Curso("Ciência da Computação");
    }
    
    /**
     * Cria um curso de teste com nome customizado.
     * 
     * @param nome Nome do curso
     * @return Curso com o nome especificado
     */
    public static Curso createCurso(String nome) {
        return new Curso(nome);
    }
    
    /**
     * Cria um professor de teste padrão.
     * 
     * @return Professor com nome "João Silva" e registro "PROF001"
     */
    public static Professor createProfessor() {
        return new Professor("João Silva", "PROF001");
    }
    
    /**
     * Cria um professor de teste com dados customizados.
     * 
     * @param nome Nome do professor
     * @param registro Registro do professor
     * @return Professor com os dados especificados
     */
    public static Professor createProfessor(String nome, String registro) {
        return new Professor(nome, registro);
    }
    
    /**
     * Cria um aluno de teste padrão.
     * 
     * @return Aluno com nome "Pedro Oliveira" e matrícula "2024001"
     */
    public static Aluno createAluno() {
        return new Aluno("Pedro Oliveira", "2024001");
    }
    
    /**
     * Cria um aluno de teste com dados customizados.
     * 
     * @param nome Nome do aluno
     * @param matricula Matrícula do aluno
     * @return Aluno com os dados especificados
     */
    public static Aluno createAluno(String nome, String matricula) {
        return new Aluno(nome, matricula);
    }
    
    /**
     * Cria uma disciplina de teste padrão.
     * 
     * @param curso Curso ao qual a disciplina pertence
     * @param professor Professor responsável pela disciplina
     * @return Disciplina com nome "Programação Orientada a Objetos"
     */
    public static Disciplina createDisciplina(Curso curso, Professor professor) {
        return new Disciplina("Programação Orientada a Objetos", curso, professor);
    }
    
    /**
     * Cria uma disciplina de teste com dados customizados.
     * 
     * @param nome Nome da disciplina
     * @param curso Curso ao qual a disciplina pertence
     * @param professor Professor responsável pela disciplina
     * @return Disciplina com os dados especificados
     */
    public static Disciplina createDisciplina(String nome, Curso curso, Professor professor) {
        return new Disciplina(nome, curso, professor);
    }
    
    /**
     * Cria uma matrícula de teste padrão.
     * 
     * @param aluno Aluno a ser matriculado
     * @param disciplina Disciplina na qual o aluno será matriculado
     * @return Matrícula criada
     */
    public static Matricula createMatricula(Aluno aluno, Disciplina disciplina) {
        return new Matricula(aluno, disciplina);
    }
}
