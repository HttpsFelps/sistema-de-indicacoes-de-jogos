-- Inserir Empresas (usando MERGE que funciona no H2 como UPSERT)
MERGE INTO tb_empresa (nome, pais_origem, data_fundacao) KEY(nome) VALUES 
  ('Nintendo', 'Japão', DATE '1889-09-23'),
  ('Sony Interactive Entertainment', 'Japão', DATE '1993-11-16'),
  ('Valve Corporation', 'Estados Unidos', DATE '1996-08-24');

-- Inserir Usuários
MERGE INTO tb_usuarios (usuario, nome_completo, data_nasc, imagem_usuario) KEY(usuario) VALUES 
  ('lucas_s', 'Lucas Silva', DATE '2000-05-15', NULL),
  ('ana_c', 'Ana Costa', DATE '1995-03-22', NULL);

-- Inserir Jogos (buscando os IDs das empresas por nome)
INSERT INTO tb_jogos (nome, descricao, data_lancamento, nome_imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'The Legend of Zelda: Breath of the Wild', 'Jogo de aventura em mundo aberto.', DATE '2017-03-03', NULL,
       (SELECT id FROM tb_empresa WHERE nome = 'Nintendo'),
       (SELECT id FROM tb_empresa WHERE nome = 'Nintendo'),
       'AVENTURA'
WHERE NOT EXISTS (SELECT 1 FROM tb_jogos WHERE nome = 'The Legend of Zelda: Breath of the Wild');

INSERT INTO tb_jogos (nome, descricao, data_lancamento, nome_imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'Counter-Strike: 2', 'Jogo de tiro em primeira pessoa.', DATE '2023-09-27', NULL,
       (SELECT id FROM tb_empresa WHERE nome = 'Valve Corporation'),
       (SELECT id FROM tb_empresa WHERE nome = 'Valve Corporation'),
       'FPS'
WHERE NOT EXISTS (SELECT 1 FROM tb_jogos WHERE nome = 'Counter-Strike: 2');

INSERT INTO tb_jogos (nome, descricao, data_lancamento, nome_imagem, id_empresa_dev, id_empresa_pub, genero)
SELECT 'God of War', 'Jogo de ação e aventura.', DATE '2018-04-20', NULL,
       (SELECT id FROM tb_empresa WHERE nome = 'Sony Interactive Entertainment'),
       (SELECT id FROM tb_empresa WHERE nome = 'Sony Interactive Entertainment'),
       'ACAO'
WHERE NOT EXISTS (SELECT 1 FROM tb_jogos WHERE nome = 'God of War');
-- Inserir empresas favoritas (buscando os IDs de usuários e empresas dinamicamente)
INSERT INTO tb_empresasfavoritas (usuario, empresa)
SELECT 
  (SELECT id FROM tb_usuarios WHERE usuario = 'lucas_s'),
  (SELECT id FROM tb_empresa WHERE nome = 'Nintendo')
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresasfavoritas 
  WHERE usuario = (SELECT id FROM tb_usuarios WHERE usuario = 'lucas_s') 
    AND empresa = (SELECT id FROM tb_empresa WHERE nome = 'Nintendo')
);

INSERT INTO tb_empresasfavoritas (usuario, empresa)
SELECT 
  (SELECT id FROM tb_usuarios WHERE usuario = 'lucas_s'),
  (SELECT id FROM tb_empresa WHERE nome = 'Valve Corporation')
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresasfavoritas 
  WHERE usuario = (SELECT id FROM tb_usuarios WHERE usuario = 'lucas_s') 
    AND empresa = (SELECT id FROM tb_empresa WHERE nome = 'Valve Corporation')
);

INSERT INTO tb_empresasfavoritas (usuario, empresa)
SELECT 
  (SELECT id FROM tb_usuarios WHERE usuario = 'ana_c'),
  (SELECT id FROM tb_empresa WHERE nome = 'Sony Interactive Entertainment')
WHERE NOT EXISTS (
  SELECT 1 FROM tb_empresasfavoritas 
  WHERE usuario = (SELECT id FROM tb_usuarios WHERE usuario = 'ana_c') 
    AND empresa = (SELECT id FROM tb_empresa WHERE nome = 'Sony Interactive Entertainment')
);
