-- Inserir empresas (desenvolvedoras e publicadoras)
INSERT INTO tb_desenvolvedores (id, nome, pais_origem, data_fundacao) VALUES
  (1, 'Nintendo', 'Japão', '1889-09-23'),
  (2, 'Sony Interactive Entertainment', 'Japão', '1993-11-16'),
  (3, 'Valve Corporation', 'Estados Unidos', '1996-08-24'),
  (4, 'CD Projekt', 'Polônia', '1994-05-01');

-- Inserir jogos
INSERT INTO tb_jogos (id, nome, descricao, data_lancamento, imagem, id_empresa_dev, id_empresa_pub) VALUES
  (1, 'The Legend of Zelda: Breath of the Wild', 'Um jogo de aventura em mundo aberto.', '2017-03-03', NULL, 1, 1),
  (2, 'God of War (2018)', 'Kratos enfrenta deuses nórdicos em sua jornada com o filho.', '2018-04-20', NULL, 2, 2),
  (3, 'Half-Life: Alyx', 'Jogo de realidade virtual ambientado no universo Half-Life.', '2020-03-23', NULL, 3, 3),
  (4, 'The Witcher 3: Wild Hunt', 'RPG de ação baseado nos livros de Andrzej Sapkowski.', '2015-05-19', NULL, 4, 4);
