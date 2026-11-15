package vinix.catalogo;

// A categoria vem junto porque a composição da marmita é validada pelo grupo do alimento
public record AlimentoResumo(
    Long id,
    String nome,
    String descricao,
    boolean disponivel,
    Long categoriaId,
    String categoriaNome,
    GrupoAlimentar grupo) {

  // Derivado do grupo: bebida e sobremesa não entram na marmita
  public boolean compoeMarmita() {
    return grupo.compoeMarmita();
  }
}
