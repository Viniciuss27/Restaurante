package vinix.catalogo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.EnumSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class GrupoAlimentarTest {

  @ParameterizedTest
  @EnumSource(value = GrupoAlimentar.class, names = {"BEBIDA", "SOBREMESA"})
  void gruposVendidosAParte_naoComporMarmita(GrupoAlimentar grupo) {
    assertThat(grupo.compoeMarmita()).isFalse();
  }

  @Test
  void demaisGrupos_comporMarmita() {
    assertThat(EnumSet.complementOf(EnumSet.of(GrupoAlimentar.BEBIDA, GrupoAlimentar.SOBREMESA)))
        .allMatch(GrupoAlimentar::compoeMarmita);
  }
}
