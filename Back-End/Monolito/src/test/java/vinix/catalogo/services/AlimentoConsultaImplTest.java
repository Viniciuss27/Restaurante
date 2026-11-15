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
import vinix.catalogo.entities.Alimento;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.repositories.AlimentoRepository;

@ExtendWith(MockitoExtension.class)
class AlimentoConsultaImplTest {

  @Mock private AlimentoRepository repository;
  @InjectMocks private AlimentoConsultaImpl consulta;

  private final Categoria carnes = Categoria.builder().id(1L).nome("Carnes").compoeMarmita(true).build();
  private final Categoria bebidas = Categoria.builder().id(2L).nome("Bebidas").compoeMarmita(false).build();

  private Alimento alimento(Long id, String nome, Categoria categoria, boolean disponivel) {
    return Alimento.builder().id(id).nome(nome).descricao("Descrição " + nome)
        .categoria(categoria).disponivel(disponivel).build();
  }

  @Test
  void buscarPorId_existente_deveRetornarResumoComDadosDaCategoria() {
    when(repository.findById(10L)).thenReturn(Optional.of(alimento(10L, "Costela", carnes, true)));

    assertThat(consulta.buscarPorId(10L))
        .contains(new AlimentoResumo(10L, "Costela", "Descrição Costela", true, 1L, "Carnes", true));
  }

  @Test
  void buscarPorId_inexistente_deveRetornarVazio() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThat(consulta.buscarPorId(99L)).isEmpty();
  }

  @Test
  void buscarPorIds_deveMapearTodosInclusiveIndisponiveisEForaDaMarmita() {
    when(repository.findAllByIdIn(Set.of(10L, 11L))).thenReturn(List.of(
        alimento(10L, "Costela", carnes, false), alimento(11L, "Coca-Cola", bebidas, true)));

    List<AlimentoResumo> resultado = consulta.buscarPorIds(Set.of(10L, 11L));

    assertThat(resultado).containsExactly(
        new AlimentoResumo(10L, "Costela", "Descrição Costela", false, 1L, "Carnes", true),
        new AlimentoResumo(11L, "Coca-Cola", "Descrição Coca-Cola", true, 2L, "Bebidas", false));
  }

  @Test
  void buscarPorIds_idsInexistentes_naoAparecemNoResultado() {
    when(repository.findAllByIdIn(Set.of(99L))).thenReturn(List.of());

    assertThat(consulta.buscarPorIds(Set.of(99L))).isEmpty();
  }
}
