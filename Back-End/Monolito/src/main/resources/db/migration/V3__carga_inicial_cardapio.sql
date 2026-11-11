INSERT INTO tb_categorias (nome) VALUES
    ('Acompanhamentos'),
    ('Carnes'),
    ('Massas'),
    ('Saladas');

INSERT INTO tb_produtos (nome, descricao, categoria_id, imagem_url)
SELECT v.nome, v.descricao, c.id, v.imagem_url
FROM (VALUES
    ('Arroz',     'Branco',     'Acompanhamentos', '/imagens/arroz.png'),
    ('Feijão',    'em Caldo',   'Acompanhamentos', '/imagens/feijao.png'),
    ('Feijão',    'Tropeiro',   'Acompanhamentos', '/imagens/feijao_trompeiro.png'),
    ('Carne',     'ensopada',   'Carnes',          '/imagens/carne_ensopada.png'),
    ('Churrasco', 'no carvão',  'Carnes',          '/imagens/churrasco.jpeg'),
    ('Costela',   'assada',     'Carnes',          '/imagens/costela.png'),
    ('Frango',    'Assado',     'Carnes',          '/imagens/frango_assado.png'),
    ('Frango',    'Ensopado',   'Carnes',          '/imagens/frango_ensopado.png'),
    ('Macarrão',  'Parmegiana', 'Massas',          '/imagens/macarrao.jpeg'),
    ('Macarrão',  'Alho e Oleo','Massas',          '/imagens/macarrão_alho.jpg'),
    ('Salada',    'simples',    'Saladas',         '/imagens/salada_simples.png')
) AS v (nome, descricao, categoria, imagem_url)
JOIN tb_categorias c ON c.nome = v.categoria;
