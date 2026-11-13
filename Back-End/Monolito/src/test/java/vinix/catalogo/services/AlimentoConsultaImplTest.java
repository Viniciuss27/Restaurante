package vinix.catalogo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vinix.catalogo.AlimentoResumo;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class AlimentoConsultaImplTest {

  @Mock private ProdutoRepository repository;
  @InjectMocks private AlimentoConsultaImpl consulta;

  private Produto produto(Long id, String nome, boolean disponivel) {
    return Produto.builder().id(id).nome(nome).descricao("Descrição " + nome)
        .categoria(Categoria.builder().id(1L).nome("Carnes").build())
        .disponivel(disponivel).build();
  }

  @Test
  void buscarPorId_existente_deveRetornarResumoSemExporAEntidade() {
    when(repository.findById(10L)).thenReturn(Optional.of(produto(10L, "Costela", true)));

    Optional<AlimentoResumo> resultado = consulta.buscarPorId(10L);

    assertThat(resultado).contains(new AlimentoResumo(10L, "Costela", "Descrição Costela", true));
  }

  @Test
  void buscarPorId_inexistente_deveRetornarVazio() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThat(consulta.buscarPorId(99L)).isEmpty();
  }

  @Test
  void buscarPorIds_deveMapearTodosInclusiveIndisponiveis() {
    when(repository.findAllById(Set.of(10L, 11L)))
        .thenReturn(List.of(produto(10L, "Costela", true), produto(11L, "Churrasco", false)));

    List<AlimentoResumo> resultado = consulta.buscarPorIds(Set.of(10L, 11L));

    assertThat(resultado).containsExactly(
        new AlimentoResumo(10L, "Costela", "Descrição Costela", true),
        new AlimentoResumo(11L, "Churrasco", "Descrição Churrasco", false));
  }

  @Test
  void buscarPorIds_idsInexistentes_naoAparecemNoResultado() {
    when(repository.findAllById(Set.of(99L))).thenReturn(List.of());

    assertThat(consulta.buscarPorIds(Set.of(99L))).isEmpty();
  }
}
