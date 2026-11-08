package vinix.catalogo.dtos.response;

import java.math.BigDecimal;

public record ProdutoResponse(
    Long id,
    String nome,
    String descricao,
    Long categoriaId,
    String categoriaNome,
    String imagemUrl,
    boolean disponivel
) {}
