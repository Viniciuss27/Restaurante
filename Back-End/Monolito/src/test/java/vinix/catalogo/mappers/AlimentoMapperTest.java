package vinix.catalogo.mappers;

import vinix.catalogo.GrupoAlimentar;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Alimento;

class AlimentoMapperTest {

  private final AlimentoMapper mapper = new AlimentoMapperImpl();

  @Test
  void response_deveAchatarDadosDaCategoria() {
    Alimento alimento = Alimento.builder().id(10L).nome("Costela").descricao("Assada")
        .categoria(Categoria.builder().grupo(GrupoAlimentar.CARNE).id(1L).nome("Carnes").build())
        .imagemUrl("http://img/costela.png").disponivel(false).build();

    AlimentoResponseDTO dto = mapper.response(alimento);

    assertThat(dto).isEqualTo(new AlimentoResponseDTO(
        10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", false));
  }

  @Test
  void response_semCategoria_naoDeveLancarExcecao() {
    AlimentoResponseDTO dto = mapper.response(Alimento.builder().id(10L).nome("Costela").build());

    assertThat(dto.categoriaId()).isNull();
    assertThat(dto.categoriaNome()).isNull();
  }

  @Test
  void request_deveCopiarCamposEIgnorarIdCategoriaEDatas() {
    Alimento alimento = mapper.request(new AlimentoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png"));

    assertThat(alimento.getNome()).isEqualTo("Costela");
    assertThat(alimento.getDescricao()).isEqualTo("Assada");
    assertThat(alimento.getImagemUrl()).isEqualTo("http://img/costela.png");
    assertThat(alimento.getId()).isNull();
    assertThat(alimento.getCategoria()).isNull();
    assertThat(alimento.getDataCadastro()).isNull();
    assertThat(alimento.getDataAlteracao()).isNull();
  }

  @Test
  void request_alimentoNovoNasceDisponivelPeloPadraoDaEntidade() {
    Alimento alimento = mapper.request(new AlimentoRequestDTO("Costela", null, 1L, null));

    assertThat(alimento.isDisponivel()).isTrue();
  }

  @Test
  void entradasNulas_deveRetornarNulo() {
    assertThat(mapper.response(null)).isNull();
    assertThat(mapper.request(null)).isNull();
  }
}
