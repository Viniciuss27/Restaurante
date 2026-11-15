package vinix.catalogo.mappers;

import vinix.catalogo.GrupoAlimentar;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.entities.Categoria;

class CategoriaMapperTest {

  private final CategoriaMapper mapper = new CategoriaMapperImpl();

  @Test
  void response_deveCopiarIdENome() {
    CategoriaResponseDTO dto = mapper.response(Categoria.builder().grupo(GrupoAlimentar.CARNE).id(1L).nome("Carnes").build());

    assertThat(dto).isEqualTo(new CategoriaResponseDTO(1L, "Carnes", GrupoAlimentar.CARNE));
  }

  @Test
  void request_deveCopiarNomeEDeixarIdNulo() {
    Categoria categoria = mapper.request(new CategoriaRequestDTO("Massas", GrupoAlimentar.CARNE));

    assertThat(categoria.getNome()).isEqualTo("Massas");
    assertThat(categoria.getId()).isNull();
  }

  @Test
  void entradasNulas_deveRetornarNulo() {
    assertThat(mapper.response(null)).isNull();
    assertThat(mapper.request(null)).isNull();
  }

  @Test
  void response_deveCopiarGrupo() {
    CategoriaResponseDTO dto = mapper.response(
        Categoria.builder().id(2L).nome("Bebidas").grupo(GrupoAlimentar.BEBIDA).build());

    assertThat(dto).isEqualTo(new CategoriaResponseDTO(2L, "Bebidas", GrupoAlimentar.BEBIDA));
  }

  @Test
  void request_deveCopiarGrupo() {
    Categoria categoria = mapper.request(new CategoriaRequestDTO("Bebidas", GrupoAlimentar.BEBIDA));

    assertThat(categoria.getGrupo()).isEqualTo(GrupoAlimentar.BEBIDA);
  }
}
