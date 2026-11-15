package vinix.catalogo;

// Grupo estável usado pelas regras de composição da marmita (a categoria é um cadastro editável, o grupo não)
// Bebida e sobremesa são vendidas à parte e nunca entram na marmita
public enum GrupoAlimentar {
  CARNE(true),
  ARROZ(true),
  FEIJAO(true),
  MASSA(true),
  SALADA(true),
  ACOMPANHAMENTO(true),
  BEBIDA(false),
  SOBREMESA(false);

  private final boolean compoeMarmita;

  GrupoAlimentar(boolean compoeMarmita) {
    this.compoeMarmita = compoeMarmita;
  }

  public boolean compoeMarmita() {
    return compoeMarmita;
  }
}
