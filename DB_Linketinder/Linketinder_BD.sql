CREATE TABLE "candidato" (
	"id" SERIAL PRIMARY KEY,
	"nome" VARCHAR(50) NOT NULL,
	"sobrenome" VARCHAR(50) NOT NULL,
	"data_nascimento" DATE NOT NULL,
	"e_mail" VARCHAR(50) NOT NULL,
	"cpf" VARCHAR(15) NOT NULL,
	"pais" VARCHAR(55) NOT NULL,
	"cep" VARCHAR(9) NOT NULL,
	"descricao" VARCHAR(180) NOT NULL
);

CREATE TABLE "empresa" (
	"id" SERIAL PRIMARY KEY,
	"nome" VARCHAR(50) NOT NULL,
	"cnpj" VARCHAR(18) NOT NULL,
	"e_mail" VARCHAR(50) NOT NULL,
	"descricao" VARCHAR(180) NOT NULL,
	"pais" VARCHAR(55) NOT NULL,
	"cep" VARCHAR(9) NOT NULL
);

CREATE TABLE "competencias" (
	"id" SERIAL PRIMARY KEY,
	"competencia" VARCHAR(50) NOT NULL
);

CREATE TABLE "candidato_competencia" (
	"id_candidato" INT NOT NULL,
	"id_competencia" INT NOT NULL,
	PRIMARY KEY ("id_candidato", "id_competencia")
);

CREATE TABLE "vagas" (
	"id" SERIAL PRIMARY KEY,
	"nome" VARCHAR(100) NOT NULL,
	"descricao" VARCHAR(180) NOT NULL,
	"endereco" VARCHAR(100) NOT NULL,
	"id_empresa" INT NOT NULL
);

CREATE TABLE "vaga_competencia" (
	"id_competencia" INT NOT NULL,
	"id_vaga" INT NOT NULL,
	PRIMARY KEY ("id_vaga", "id_competencia")
);

CREATE TABLE "interacao_candidato" (
	"id_candidato" INT NOT NULL,
	"id_vaga" INT NOT NULL,
	"tipo" VARCHAR(10) NOT NULL CHECK (TIPO IN ('LIKE', 'DISLIKE')),
	"data" TIMESTAMP NOT NULL DEFAULT NOW(),
	PRIMARY KEY ("id_candidato", "id_vaga")
);

CREATE TABLE "interacao_empresa" (
	"id_empresa" INT NOT NULL,
	"id_candidato" INT NOT NULL,
	"tipo" VARCHAR(10) NOT NULL CHECK (TIPO IN ('LIKE', 'DISLIKE')),
	"data" TIMESTAMP NOT NULL DEFAULT NOW(),
	PRIMARY KEY ("id_empresa", "id_candidato")
);

CREATE UNIQUE INDEX ON "candidato" ("cpf");

CREATE UNIQUE INDEX ON "empresa" ("cnpj");

ALTER TABLE "candidato_competencia"
ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "candidato_competencia"
ADD FOREIGN KEY ("id_competencia") REFERENCES "competencias" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "vagas"
ADD FOREIGN KEY ("id_empresa") REFERENCES "empresa" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "vaga_competencia"
ADD FOREIGN KEY ("id_competencia") REFERENCES "competencias" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "vaga_competencia"
ADD FOREIGN KEY ("id_vaga") REFERENCES "vagas" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "interacao_candidato"
ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "interacao_candidato"
ADD FOREIGN KEY ("id_vaga") REFERENCES "vagas" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "interacao_empresa"
ADD FOREIGN KEY ("id_candidato") REFERENCES "candidato" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "interacao_empresa"
ADD FOREIGN KEY ("id_empresa") REFERENCES "empresa" ("id") DEFERRABLE INITIALLY IMMEDIATE;

------------------------------------------------------------------------------------
--                        INSERTS  - Candidatos
------------------------------------------------------------------------------------
INSERT INTO
	CANDIDATO (
		NOME,
		SOBRENOME,
		DATA_NASCIMENTO,
		E_MAIL,
		CPF,
		PAIS,
		CEP,
		DESCRICAO
	)
VALUES
	(
		'João Victor',
		'Cajado',
		'2000-09-02',
		'joao@gmail.com',
		'111.111.111-11',
		'Brasil',
		'11111-111',
		'Programador Full Stack'
	);

INSERT INTO
	CANDIDATO (
		NOME,
		SOBRENOME,
		DATA_NASCIMENTO,
		E_MAIL,
		CPF,
		PAIS,
		CEP,
		DESCRICAO
	)
VALUES
	(
		'Batatevis',
		'Sousa',
		'2001-05-22',
		'batata@gmail.com',
		'222.222.222-22',
		'Brasil',
		'22222-222',
		'Programador Frontend'
	);

INSERT INTO
	CANDIDATO (
		NOME,
		SOBRENOME,
		DATA_NASCIMENTO,
		E_MAIL,
		CPF,
		PAIS,
		CEP,
		DESCRICAO
	)
VALUES
	(
		'Sandubinha',
		'Silva',
		'2005-04-14',
		'Sand@gmail.com',
		'333.333.333-33',
		'Brasil',
		'33333-333',
		'Programador Backend'
	);

INSERT INTO
	CANDIDATO (
		NOME,
		SOBRENOME,
		DATA_NASCIMENTO,
		E_MAIL,
		CPF,
		PAIS,
		CEP,
		DESCRICAO
	)
VALUES
	(
		'Pedro',
		'Araujo',
		'1979-06-01',
		'Pedrin@gmail.com',
		'444.444.444-44',
		'Brasil',
		'44444-444',
		'Analista de Dados'
	);

INSERT INTO
	CANDIDATO (
		NOME,
		SOBRENOME,
		DATA_NASCIMENTO,
		E_MAIL,
		CPF,
		PAIS,
		CEP,
		DESCRICAO
	)
VALUES
	(
		'Cleitinho',
		'Santos',
		'2002-03-17',
		'Cleit@gmail.com',
		'555.555.555-55',
		'Brasil',
		'55555-555',
		'UX/UI Designer'
	);

------------------------------------------------------------------------------------
--                        INSERTS  - Empresas
------------------------------------------------------------------------------------
INSERT INTO
	EMPRESA (NOME, CNPJ, E_MAIL, DESCRICAO, PAIS, CEP)
VALUES
	(
		'Evils Corp',
		'11.111.111/0001-11',
		'evilscorp@gmail.com',
		'Líder em tecnologia no Brasil',
		'Brasil',
		'11111-111'
	);

INSERT INTO
	EMPRESA (NOME, CNPJ, E_MAIL, DESCRICAO, PAIS, CEP)
VALUES
	(
		'Dois Irmãos',
		'22.222.222/0002-22',
		'doisirmaos@gmail.com',
		'Líder em tecnologia no Brasil',
		'Brasil',
		'22222-222'
	);

INSERT INTO
	EMPRESA (NOME, CNPJ, E_MAIL, DESCRICAO, PAIS, CEP)
VALUES
	(
		'Potato Industries',
		'33.333.333/0003-33',
		'potato@gmail.com',
		'Líder em tecnologia no Brasil',
		'Brasil',
		'33333-333'
	);

INSERT INTO
	EMPRESA (NOME, CNPJ, E_MAIL, DESCRICAO, PAIS, CEP)
VALUES
	(
		'NexaTech Soluções',
		'44.444.444/0004-44',
		'nexatech@gmail.com',
		'Líder em tecnologia no Brasil',
		'Brasil',
		'44444-444'
	);

INSERT INTO
	EMPRESA (NOME, CNPJ, E_MAIL, DESCRICAO, PAIS, CEP)
VALUES
	(
		'Aurora Sistemas',
		'55.555.555/0005-55',
		'aurorasys@gmail.com',
		'Líder em tecnologia no Brasil',
		'Brasil',
		'55555-555'
	);

------------------------------------------------------------------------------------
--                        INSERTS  - Competencias
------------------------------------------------------------------------------------
INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Python');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Java');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('JavaScript');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('TypeScript');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Groovy');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('HTML');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('CSS');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Figma');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('SQL');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Pandas');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('UX/UI');

INSERT INTO
	COMPETENCIAS (COMPETENCIA)
VALUES
	('Wireframing');

------------------------------------------------------------------------------------
--                        INSERTS  - Vagas
------------------------------------------------------------------------------------
INSERT INTO
	VAGAS (NOME, DESCRICAO, ENDERECO, ID_EMPRESA)
VALUES
	(
		'Desenvolvedor Front-end Júnior',
		'Desenvolvimento de interfaces web utilizando HTML, CSS, JavaScript e React.',
		'São Paulo - SP',
		1
	);

INSERT INTO
	VAGAS (NOME, DESCRICAO, ENDERECO, ID_EMPRESA)
VALUES
	(
		'Analista de Dados Júnior',
		'Análise de dados, criação de relatórios e apoio na tomada de decisões.',
		'São Paulo - SP',
		2
	);

INSERT INTO
	VAGAS (NOME, DESCRICAO, ENDERECO, ID_EMPRESA)
VALUES
	(
		'Desenvolvedor Back-end Júnior',
		'Desenvolvimento e manutenção de APIs e serviços utilizando Java e PostgreSQL.',
		'São Paulo - SP',
		3
	);

INSERT INTO
	VAGAS (NOME, DESCRICAO, ENDERECO, ID_EMPRESA)
VALUES
	(
		'Analista de Sistemas',
		'Análise de requisitos, documentação e suporte ao desenvolvimento de sistemas.',
		'São Paulo - SP',
		4
	);

INSERT INTO
	VAGAS (NOME, DESCRICAO, ENDERECO, ID_EMPRESA)
VALUES
	(
		'Engenheiro de Software Júnior',
		'Desenvolvimento de aplicações, testes automatizados e manutenção de sistemas.',
		'São Paulo - SP',
		5
	);

------------------------------------------------------------------------------------
--                        INSERTS  - Vagas -> Competencia
------------------------------------------------------------------------------------
INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(3, 1);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(6, 1);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(7, 1);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(2, 2);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(9, 2);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(10, 2);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(2, 3);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(5, 3);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(2, 4);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(4, 4);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(2, 5);

INSERT INTO
	VAGA_COMPETENCIA (ID_COMPETENCIA, ID_VAGA)
VALUES
	(1, 5);

------------------------------------------------------------------------------------
--                        INSERTS  - Candidato -> Competencia
------------------------------------------------------------------------------------
INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(1, 2);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(1, 3);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(1, 6);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(1, 7);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(2, 3);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(2, 4);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(2, 6);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(2, 7);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(3, 1);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(3, 5);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(3, 2);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(4, 2);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(4, 1);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(4, 10);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(4, 9);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(5, 8);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(5, 11);

INSERT INTO
	CANDIDATO_COMPETENCIA (ID_CANDIDATO, ID_COMPETENCIA)
VALUES
	(5, 12);

------------------------------------------------------------------------------------
--                        INSERTS  - Interações
------------------------------------------------------------------------------------
INSERT INTO
	INTERACAO_CANDIDATO (ID_CANDIDATO, ID_VAGA, TIPO)
VALUES
	(1, 5, 'LIKE');

INSERT INTO
	INTERACAO_CANDIDATO (ID_CANDIDATO, ID_VAGA, TIPO)
VALUES
	(2, 1, 'LIKE');

INSERT INTO
	INTERACAO_CANDIDATO (ID_CANDIDATO, ID_VAGA, TIPO)
VALUES
	(3, 3, 'LIKE');

INSERT INTO
	INTERACAO_CANDIDATO (ID_CANDIDATO, ID_VAGA, TIPO)
VALUES
	(4, 2, 'LIKE');

INSERT INTO
	INTERACAO_CANDIDATO (ID_CANDIDATO, ID_VAGA, TIPO)
VALUES
	(5, 1, 'DISLIKE');

INSERT INTO
	INTERACAO_EMPRESA (ID_EMPRESA, ID_CANDIDATO, TIPO)
VALUES
	(5, 1, 'LIKE');

INSERT INTO
	INTERACAO_EMPRESA (ID_EMPRESA, ID_CANDIDATO, TIPO)
VALUES
	(2, 1, 'LIKE');

INSERT INTO
	INTERACAO_EMPRESA (ID_EMPRESA, ID_CANDIDATO, TIPO)
VALUES
	(3, 3, 'DISLIKE');

INSERT INTO
	INTERACAO_EMPRESA (ID_EMPRESA, ID_CANDIDATO, TIPO)
VALUES
	(2, 4, 'LIKE');

INSERT INTO
	INTERACAO_EMPRESA (ID_EMPRESA, ID_CANDIDATO, TIPO)
VALUES
	(5, 1, 'DISLIKE');

------------------------------------------------------------------------------------
--                        SELECTS
------------------------------------------------------------------------------------
SELECT
	*
FROM
	CANDIDATO;

SELECT
	*
FROM
	EMPRESA;

SELECT
	*
FROM
	VAGAS;

SELECT
	*
FROM
	COMPETENCIAS;

SELECT
	*
FROM
	CANDIDATO_COMPETENCIA;

SELECT
	*
FROM
	MATCHES;

------------------------------------------------------------------------------------
--                        JOIN - competencias - candidatos - vagas
------------------------------------------------------------------------------------
-- Visualiza relação de candidatos e competencias
SELECT
	C.ID AS ID_CANDIDATO,
	C.NOME AS CANDIDATO,
	COMP.ID AS ID_COMPETENCIA,
	COMP.COMPETENCIA AS COMPETENCIA
FROM
	CANDIDATO C
	JOIN CANDIDATO_COMPETENCIA CC ON CC.ID_CANDIDATO = C.ID
	JOIN COMPETENCIAS COMP ON COMP.ID = CC.ID_COMPETENCIA
ORDER BY
	C.ID;

-- Visualiza relação empresa e vagas
SELECT
	E.ID AS ID_EMPRESA,
	E.NOME AS EMPRESA,
	V.NOME AS VAGA,
	V.DESCRICAO AS DESCRICAO
FROM
	EMPRESA E
	JOIN VAGAS V ON V.ID_EMPRESA = E.ID;

-- visualiza competencias das vagas
SELECT
	V.NOME AS VAGA,
	V.DESCRICAO AS DESCRICAO,
	COMP.COMPETENCIA AS COMPETENCIA
FROM
	VAGAS V
	JOIN VAGA_COMPETENCIA VC ON VC.ID_VAGA = V.ID
	JOIN COMPETENCIAS COMP ON COMP.ID = VC.ID_COMPETENCIA;

-- visualiza interacoes de candidatos e empresas
SELECT
	C.NOME AS CANDIDATO,
	V.NOME AS VAGA,
	IC.TIPO AS TIPO,
	IC.DATA AS DATA_ACAO
FROM
	INTERACAO_CANDIDATO IC
	JOIN CANDIDATO C ON C.ID = IC.ID_CANDIDATO
	JOIN VAGAS V ON V.ID = IC.ID_VAGA;

SELECT
	E.NOME AS EMPRESA,
	C.NOME AS CANDIDATO,
	IE.TIPO AS TIPO,
	IE.DATA AS DATA_ACAO
FROM
	INTERACAO_EMPRESA IE
	JOIN EMPRESA E ON E.ID = IE.ID_EMPRESA
	JOIN CANDIDATO C ON C.ID = IE.ID_CANDIDATO;

-- cria view e visualiza match
CREATE VIEW MATCHES AS
SELECT
	IC.ID_CANDIDATO,
	V.ID_EMPRESA,
	IC.ID_VAGA,
	GREATEST(IC.DATA, IE.DATA) AS DATA_MATCH
FROM
	INTERACAO_CANDIDATO IC
	JOIN VAGAS V ON V.ID = IC.ID_VAGA
	JOIN INTERACAO_EMPRESA IE ON IE.ID_EMPRESA = V.ID_EMPRESA
	AND IE.ID_CANDIDATO = IC.ID_CANDIDATO
WHERE
	IC.TIPO = 'LIKE'
	AND IE.TIPO = 'LIKE';

SELECT
	C.NOME AS CANDIDATO,
	E.NOME AS EMPRESA,
	V.NOME AS VAGA,
	M.DATA_MATCH AS DATA_MATCH
FROM
	MATCHES M
	JOIN CANDIDATO C ON C.ID = M.ID_CANDIDATO
	JOIN EMPRESA E ON E.ID = M.ID_EMPRESA
	JOIN VAGAS V ON V.ID = M.ID_VAGA;