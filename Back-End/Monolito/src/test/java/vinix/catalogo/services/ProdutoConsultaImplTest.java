package vinix.catalogo.services;

import vinix.catalogo.GrupoAlimentar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vinix.catalogo.ProdutoResumo;
import vinix.catalogo.Tamanho;
import vinix.catalogo.TipoProduto;
import vinix.catalogo.entities.Alimento;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoConsultaImplTest {

  @Mock private ProdutoRepository repository;
  @InjectMocks private ProdutoConsultaImpl consulta;

  private final Categoria bebidas = Categoria.builder().id(2L).nome("Bebidas").grupo(GrupoAlimentar.BEBIDA).build();

  private Map<Tamanho, BigDecimal> precosDaMarmita() {
    return Map.of(Tamanho.PEQUENA, new BigDecimal("18.00"),
        Tamanho.MEDIA, new BigDecimal("20.00"), Tamanho.GRANDE, new BigDecimal("25.00"));
  }

  private Produto marmita(boolean disponivel) {
    return Produto.builder().id(1L).nome("Marmita").tipo(TipoProduto.MARMITA)
        .precos(precosDaMarmita()).disponivel(disponivel).build();
  }

  private Produto bebida(boolean produtoAtivo, boolean alimentoDisponivel) {
    Alimento cocaCola = Alimento.builder().id(30L).nome("Coca-Cola")
        .categoria(bebidas).disponivel(alimentoDisponivel).build();
    return Produto.builder().id(50L).nome("Coca-Cola").tipo(TipoProduto.BEBIDA)
        .precos(Map.of(Tamanho.UNICO, new BigDecimal("6.00")))
        .alimento(cocaCola).disponivel(produtoAtivo).build();
  }

  // ---------- mapeamento ----------

  @Test
  void marmitaAtiva_deveTrazerOsPrecosDeTodosOsTamanhos() {
    when(repository.findById(1L)).thenReturn(Optional.of(marmita(true)));

    assertThat(consulta.buscarPorId(1L)).contains(
        new ProdutoResumo(1L, "Marmita", TipoProduto.MARMITA, precosDaMarmita(), true));
  }

  @Test
  void bebida_deveTrazerPrecoUnico() {
    when(repository.findById(50L)).thenReturn(Optional.of(bebida(true, true)));

    assertThat(consulta.buscarPorId(50L)).get()
        .satisfies(r -> assertThat(r.precoPara(Tamanho.UNICO)).contains(new BigDecimal("6.00")));
  }

  // ---------- disponibilidade para venda ----------

  @Test
  void marmitaInativa_deveEstarIndisponivel() {
    when(repository.findById(1L)).thenReturn(Optional.of(marmita(false)));

    assertThat(consulta.buscarPorId(1L)).get().extracting(ProdutoResumo::disponivel).isEqualTo(false);
  }

  @Test
  void bebidaAtivaComAlimentoDisponivel_deveEstarDisponivel() {
    when(repository.findById(50L)).thenReturn(Optional.of(bebida(true, true)));

    assertThat(consulta.buscarPorId(50L)).get().extracting(ProdutoResumo::disponivel).isEqualTo(true);
  }

  @Test
  void bebidaAtivaComAlimentoIndisponivel_deveEstarIndisponivel() {
    when(repository.findById(50L)).thenReturn(Optional.of(bebida(true, false)));

    assertThat(consulta.buscarPorId(50L)).get().extracting(ProdutoResumo::disponivel).isEqualTo(false);
  }

  @Test
  void bebidaInativaComAlimentoDisponivel_deveEstarIndisponivel() {
    when(repository.findById(50L)).thenReturn(Optional.of(bebida(false, true)));

    assertThat(consulta.buscarPorId(50L)).get().extracting(ProdutoResumo::disponivel).isEqualTo(false);
  }

  // ---------- busca ----------

  @Test
  void buscarPorId_inexistente_deveRetornarVazio() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThat(consulta.buscarPorId(99L)).isEmpty();
  }

  @Test
  void buscarPorIds_deveMapearTodos() {
    when(repository.findAllByIdIn(Set.of(1L, 50L)))
        .thenReturn(List.of(marmita(true), bebida(true, true)));

    assertThat(consulta.buscarPorIds(Set.of(1L, 50L))).extracting(ProdutoResumo::nome)
        .containsExactly("Marmita", "Coca-Cola");
  }

  @Test
  void buscarPorIds_idsInexistentes_naoAparecemNoResultado() {
    when(repository.findAllByIdIn(Set.of(99L))).thenReturn(List.of());

    assertThat(consulta.buscarPorIds(Set.of(99L))).isEmpty();
  }
}
