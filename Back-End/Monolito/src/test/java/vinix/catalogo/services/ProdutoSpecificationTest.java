package vinix.catalogo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static vinix.catalogo.services.ProdutoSpecification.categoriaContem;
import static vinix.catalogo.services.ProdutoSpecification.descricaoContem;
import static vinix.catalogo.services.ProdutoSpecification.disponivelIgual;
import static vinix.catalogo.services.ProdutoSpecification.nomeContem;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vinix.catalogo.entities.Produto;

@ExtendWith(MockitoExtension.class)
class ProdutoSpecificationTest {

  @Mock private Root<Produto> root;
  @Mock private CriteriaQuery<?> query;
  @Mock private CriteriaBuilder cb;
  @Mock private Path<String> caminhoTexto;
  @Mock private Path<Boolean> caminhoBooleano;
  @Mock private Join<Object, Object> juncaoCategoria;
  @Mock private Expression<String> minusculo;
  @Mock private Predicate predicado;

  // ---------- filtros de texto ----------

  @Test
  void nomeContem_comTexto_deveComparar_emMinusculoComCuringas() {
    when(root.<String>get("nome")).thenReturn(caminhoTexto);
    when(cb.lower(caminhoTexto)).thenReturn(minusculo);
    when(cb.like(minusculo, "%pizza%")).thenReturn(predicado);

    assertThat(nomeContem("PiZZa").toPredicate(root, query, cb)).isSameAs(predicado);
  }

  @Test
  void descricaoContem_comTexto_deveComparar_emMinusculoComCuringas() {
    when(root.<String>get("descricao")).thenReturn(caminhoTexto);
    when(cb.lower(caminhoTexto)).thenReturn(minusculo);
    when(cb.like(minusculo, "%assada%")).thenReturn(predicado);

    assertThat(descricaoContem("ASSADA").toPredicate(root, query, cb)).isSameAs(predicado);
  }

  @Test
  void categoriaContem_comTexto_deveFazerJoinENomeDaCategoria() {
    when(root.<Object, Object>join("categoria")).thenReturn(juncaoCategoria);
    when(juncaoCategoria.<String>get("nome")).thenReturn(caminhoTexto);
    when(cb.lower(caminhoTexto)).thenReturn(minusculo);
    when(cb.like(minusculo, "%carnes%")).thenReturn(predicado);

    assertThat(categoriaContem("Carnes").toPredicate(root, query, cb)).isSameAs(predicado);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void filtrosDeTexto_nuloOuVazio_naoDevemFiltrar(String valor) {
    assertThat(nomeContem(valor).toPredicate(root, query, cb)).isNull();
    assertThat(descricaoContem(valor).toPredicate(root, query, cb)).isNull();
    assertThat(categoriaContem(valor).toPredicate(root, query, cb)).isNull();

    verifyNoInteractions(cb);
  }

  // ---------- disponibilidade ----------

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void disponivelIgual_comValor_deveComparar(boolean valor) {
    when(root.<Boolean>get("disponivel")).thenReturn(caminhoBooleano);
    when(cb.equal(caminhoBooleano, valor)).thenReturn(predicado);

    assertThat(disponivelIgual(valor).toPredicate(root, query, cb)).isSameAs(predicado);
  }

  @Test
  void disponivelIgual_nulo_naoDeveFiltrar() {
    assertThat(disponivelIgual(null).toPredicate(root, query, cb)).isNull();

    verifyNoInteractions(cb);
  }
}
