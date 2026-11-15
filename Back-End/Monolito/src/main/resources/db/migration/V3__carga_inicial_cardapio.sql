-- Regra da marmita: no máximo 2 alimentos por categoria. Por isso arroz e feijão são categorias separadas.
INSERT INTO tb_categorias (nome, compoe_marmita) VALUES
    ('Arroz',      TRUE),
    ('Feijão',     TRUE),
    ('Carnes',     TRUE),
    ('Massas',     TRUE),
    ('Saladas',    TRUE),
    ('Bebidas',    FALSE),
    ('Sobremesas', FALSE);

INSERT INTO tb_alimentos (nome, descricao, categoria_id, imagem_url)
SELECT v.nome, v.descricao, c.id, v.imagem_url
FROM (VALUES
    ('Arroz',     'Branco',      'Arroz',   '/imagens/arroz.png'),
    ('Feijão',    'em Caldo',    'Feijão',  '/imagens/feijao.png'),
    ('Feijão',    'Tropeiro',    'Feijão',  '/imagens/feijao_trompeiro.png'),
    ('Carne',     'ensopada',    'Carnes',  '/imagens/carne_ensopada.png'),
    ('Churrasco', 'no carvão',   'Carnes',  '/imagens/churrasco.jpeg'),
    ('Costela',   'assada',      'Carnes',  '/imagens/costela.png'),
    ('Frango',    'Assado',      'Carnes',  '/imagens/frango_assado.png'),
    ('Frango',    'Ensopado',    'Carnes',  '/imagens/frango_ensopado.png'),
    ('Macarrão',  'Parmegiana',  'Massas',  '/imagens/macarrao.jpeg'),
    ('Macarrão',  'Alho e Oleo', 'Massas',  '/imagens/macarrão_alho.jpg'),
    ('Salada',    'simples',     'Saladas', '/imagens/salada_simples.png')
) AS v (nome, descricao, categoria, imagem_url)
JOIN tb_categorias c ON c.nome = v.categoria;

-- Marmita: um único produto, vendido em três tamanhos (preços do site original)
INSERT INTO tb_produtos (nome, tipo) VALUES ('Marmita', 'MARMITA');

INSERT INTO tb_produto_precos (produto_id, tamanho, preco)
SELECT p.id, v.tamanho, v.preco
FROM tb_produtos p
CROSS JOIN (VALUES
    ('PEQUENA', 18.00),
    ('MEDIA',   20.00),
    ('GRANDE',  25.00)
) AS v (tamanho, preco)
WHERE p.nome = 'Marmita';

-- Bebidas e sobremesas: cadastre o alimento (o que a cozinha tem), o produto ligado a ele e o preço UNICO
-- INSERT INTO tb_alimentos (nome, categoria_id)
--     SELECT 'Coca-Cola', id FROM tb_categorias WHERE nome = 'Bebidas';
-- INSERT INTO tb_produtos (nome, tipo, alimento_id)
--     SELECT 'Coca-Cola', 'BEBIDA', id FROM tb_alimentos WHERE nome = 'Coca-Cola';
-- INSERT INTO tb_produto_precos (produto_id, tamanho, preco)
--     SELECT id, 'UNICO', 0.00 FROM tb_produtos WHERE nome = 'Coca-Cola';
