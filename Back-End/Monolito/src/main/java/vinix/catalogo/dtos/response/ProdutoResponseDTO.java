package vinix.catalogo.dtos.response;

public record ProdutoResponseDTO(
    Long id,
    String nome,
    String descricao,
    Long categoriaId,
    String categoriaNome,
    String imagemUrl,
    boolean disponivel
) {}
