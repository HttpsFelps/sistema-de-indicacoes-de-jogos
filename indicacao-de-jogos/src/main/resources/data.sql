-- Inserir Empresas
INSERT INTO tb_empresa (nome, pais_origem, data_fundacao)
SELECT 'Nintendo', 'Japão', '1889-09-23'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresa WHERE nome = 'Nintendo'
);

INSERT INTO tb_empresa (nome, pais_origem, data_fundacao)
SELECT 'Sony Interactive Entertainment', 'Japão', '1993-11-16'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresa WHERE nome = 'Sony Interactive Entertainment'
);

INSERT INTO tb_empresa (nome, pais_origem, data_fundacao)
SELECT 'Valve Corporation', 'Estados Unidos', '1996-08-24'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresa WHERE nome = 'Valve Corporation'
);

-- Inserir Usuários
INSERT INTO tb_usuarios (nome_completo, usuario, data_nasc, imagem_usuario)
SELECT 'Lucas Silva', 'lucas_s', '2000-05-15', NULL
WHERE NOT EXISTS (
  SELECT 1 FROM tb_usuarios WHERE usuario = 'lucas_s'
);

INSERT INTO tb_usuarios (nome_completo, usuario, data_nasc, imagem_usuario)
SELECT 'Ana Costa', 'ana_c', '1995-03-22', NULL
WHERE NOT EXISTS (
  SELECT 1 FROM tb_usuarios WHERE usuario = 'ana_c'
);

-- Inserir Jogos
INSERT INTO tb_jogos (nome, descricao, data_lancamento, imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'The Legend of Zelda: Breath of the Wild', 'Jogo de aventura em mundo aberto.', '2017-03-03', NULL, 1, 1, 'AVENTURA'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_jogos WHERE nome = 'The Legend of Zelda: Breath of the Wild'
);

INSERT INTO tb_jogos (nome, descricao, data_lancamento, imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'Counter-Strike: 2', 'Jogo de tiro em primeira pessoa.', '2023-09-27', NULL, 3, 3, 'FPS'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_jogos WHERE nome = 'Counter-Strike: Global Offensive'
);

INSERT INTO tb_jogos (nome, descricao, data_lancamento, imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'God of War', 'Jogo de ação e aventura.', '2018-04-20', NULL, 2, 2, 'ACAO'
WHERE NOT EXISTS (
  SELECT 1 FROM tb_jogos WHERE nome = 'God of War'
);

-- Inserir Favoritos
INSERT INTO tb_favoritos (fk_usuario, fk_jogo)
SELECT 1, 1
WHERE NOT EXISTS (
  SELECT 1 FROM tb_favoritos WHERE fk_usuario = 1 AND fk_jogo = 1
);

INSERT INTO tb_favoritos (fk_usuario, fk_jogo)
SELECT 1, 2
WHERE NOT EXISTS (
  SELECT 1 FROM tb_favoritos WHERE fk_usuario = 1 AND fk_jogo = 2
);

INSERT INTO tb_favoritos (fk_usuario, fk_jogo)
SELECT 2, 3
WHERE NOT EXISTS (
  SELECT 1 FROM tb_favoritos WHERE fk_usuario = 2 AND fk_jogo = 3
);