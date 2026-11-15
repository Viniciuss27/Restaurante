package vinix.catalogo;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

// precos traz o preço de cada tamanho em que o produto é vendido: quem consulta deve copiar o valor no momento da compra
public record ProdutoResumo(
    Long id, String nome, TipoProduto tipo, Map<Tamanho, BigDecimal> precos, boolean disponivel) {

  public ProdutoResumo {
    precos = Map.copyOf(precos);
  }

  // Preço do tamanho pedido, ou vazio se o produto não é vendido nesse tamanho
  public Optional<BigDecimal> precoPara(Tamanho tamanho) {
    return Optional.ofNullable(precos.get(tamanho));
  }
}
