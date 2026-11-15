package vinix.catalogo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ProdutoResumoTest {

  private ProdutoResumo marmita(Map<Tamanho, BigDecimal> precos) {
    return new ProdutoResumo(1L, "Marmita", TipoProduto.MARMITA, precos, true);
  }

  @Test
  void precoPara_tamanhoVendido_deveRetornarOPreco() {
    ProdutoResumo resumo = marmita(Map.of(
        Tamanho.PEQUENA, new BigDecimal("18.00"), Tamanho.GRANDE, new BigDecimal("25.00")));

    assertThat(resumo.precoPara(Tamanho.GRANDE)).contains(new BigDecimal("25.00"));
  }

  @Test
  void precoPara_tamanhoNaoVendido_deveRetornarVazio() {
    ProdutoResumo resumo = marmita(Map.of(Tamanho.PEQUENA, new BigDecimal("18.00")));

    assertThat(resumo.precoPara(Tamanho.UNICO)).isEmpty();
  }

  @Test
  void precos_saoImutaveis() {
    ProdutoResumo resumo = marmita(Map.of(Tamanho.PEQUENA, new BigDecimal("18.00")));

    assertThatThrownBy(() -> resumo.precos().put(Tamanho.MEDIA, BigDecimal.ONE))
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void alterarOMapaOriginalDepoisDeCriar_naoAfetaOResumo() {
    Map<Tamanho, BigDecimal> original = new HashMap<>();
    original.put(Tamanho.PEQUENA, new BigDecimal("18.00"));
    ProdutoResumo resumo = marmita(original);

    original.put(Tamanho.MEDIA, new BigDecimal("20.00"));

    assertThat(resumo.precos()).containsOnlyKeys(Tamanho.PEQUENA);
  }
}
