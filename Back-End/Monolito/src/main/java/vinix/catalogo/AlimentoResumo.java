package vinix.catalogo;

// A categoria vem junto porque a composição da marmita é validada por categoria
public record AlimentoResumo(
    Long id,
    String nome,
    String descricao,
    boolean disponivel,
    Long categoriaId,
    String categoriaNome,
    boolean compoeMarmita) {}
