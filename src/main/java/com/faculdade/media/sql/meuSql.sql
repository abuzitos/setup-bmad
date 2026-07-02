-- SGBD: SQLite 3
-- Tabela: instituicoes (ainda não documentada em docs/dicionario-de-dados.md)

CREATE TABLE instituicoes (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    nome        VARCHAR(100) NOT NULL,
    endereco    VARCHAR(200) NOT NULL,
    telefone_1  VARCHAR(20)  NOT NULL,
    telefone_2  VARCHAR(20),
    cep         INTEGER      NOT NULL,
    CONSTRAINT uk_instituicoes_nome UNIQUE (nome)
);

CREATE INDEX idx_instituicoes_nome ON instituicoes(nome);

// inserir 10 instituicoes em comando sql
INSERT INTO instituicoes (nome, endereco, telefone_1, telefone_2, cep) VALUES
('Faculdade de Tecnologia de São Paulo', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Rio de Janeiro', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Belo Horizonte', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Curitiba', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Brasília', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Porto Alegre', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Fortaleza', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Recife', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Salvador', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678),
('Faculdade de Tecnologia de Manaus', 'Rua das Flores, 123', '11 99999-9999', '11 99999-9999', 12345678);

// consultar disciplinas e cursos de uma instituicao
SELECT d.nome AS disciplina, c.nome AS curso
FROM disciplinas d
JOIN cursos c ON d.curso_id = c.id
WHERE d.instituicao_id = 1;

// consultar professores de uma instituicao
SELECT p.nome AS professor
FROM professores p
WHERE p.instituicao_id = 1;

// consultar alunos de uma instituicao