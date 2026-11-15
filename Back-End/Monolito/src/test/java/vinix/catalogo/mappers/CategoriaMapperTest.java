package vinix.catalogo.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.entities.Categoria;

class CategoriaMapperTest {

  private final CategoriaMapper mapper = new CategoriaMapperImpl();

  @Test
  void response_deveCopiarIdENome() {
    CategoriaResponseDTO dto = mapper.response(Categoria.builder().id(1L).nome("Carnes").build());

    assertThat(dto).isEqualTo(new CategoriaResponseDTO(1L, "Carnes", true));
  }

  @Test
  void request_deveCopiarNomeEDeixarIdNulo() {
    Categoria categoria = mapper.request(new CategoriaRequestDTO("Massas", true));

    assertThat(categoria.getNome()).isEqualTo("Massas");
    assertThat(categoria.getId()).isNull();
  }

  @Test
  void entradasNulas_deveRetornarNulo() {
    assertThat(mapper.response(null)).isNull();
    assertThat(mapper.request(null)).isNull();
  }

  @Test
  void response_deveCopiarCompoeMarmita() {
    CategoriaResponseDTO dto = mapper.response(
        Categoria.builder().id(2L).nome("Bebidas").compoeMarmita(false).build());

    assertThat(dto).isEqualTo(new CategoriaResponseDTO(2L, "Bebidas", false));
  }

  @Test
  void request_deveCopiarCompoeMarmita() {
    Categoria categoria = mapper.request(new CategoriaRequestDTO("Bebidas", false));

    assertThat(categoria.isCompoeMarmita()).isFalse();
  }
}
