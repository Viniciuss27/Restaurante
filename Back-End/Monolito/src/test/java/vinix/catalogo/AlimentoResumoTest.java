package vinix.catalogo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AlimentoResumoTest {

  private AlimentoResumo resumo(GrupoAlimentar grupo) {
    return new AlimentoResumo(1L, "Item", "Descrição", true, 1L, "Categoria", grupo);
  }

  @Test
  void compoeMarmita_ehDerivadoDoGrupo() {
    assertThat(resumo(GrupoAlimentar.CARNE).compoeMarmita()).isTrue();
    assertThat(resumo(GrupoAlimentar.BEBIDA).compoeMarmita()).isFalse();
  }
}
