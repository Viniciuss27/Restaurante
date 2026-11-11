package vinix.catalogo.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Produto;

class ProdutoMapperTest {

  private final ProdutoMapper mapper = new ProdutoMapperImpl();

  @Test
  void response_deveAchatarDadosDaCategoria() {
    Produto produto = Produto.builder().id(10L).nome("Costela").descricao("Assada")
        .categoria(Categoria.builder().id(1L).nome("Carnes").build())
        .imagemUrl("http://img/costela.png").disponivel(false).build();

    ProdutoResponseDTO dto = mapper.response(produto);

    assertThat(dto).isEqualTo(new ProdutoResponseDTO(
        10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", false));
  }

  @Test
  void response_semCategoria_naoDeveLancarExcecao() {
    ProdutoResponseDTO dto = mapper.response(Produto.builder().id(10L).nome("Costela").build());

    assertThat(dto.categoriaId()).isNull();
    assertThat(dto.categoriaNome()).isNull();
  }

  @Test
  void request_deveCopiarCamposEIgnorarIdCategoriaEDatas() {
    Produto produto = mapper.request(new ProdutoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png"));

    assertThat(produto.getNome()).isEqualTo("Costela");
    assertThat(produto.getDescricao()).isEqualTo("Assada");
    assertThat(produto.getImagemUrl()).isEqualTo("http://img/costela.png");
    assertThat(produto.getId()).isNull();
    assertThat(produto.getCategoria()).isNull();
    assertThat(produto.getDataCadastro()).isNull();
    assertThat(produto.getDataAlteracao()).isNull();
  }

  @Test
  void request_produtoNovoNasceDisponivelPeloPadraoDaEntidade() {
    Produto produto = mapper.request(new ProdutoRequestDTO("Costela", null, 1L, null));

    assertThat(produto.isDisponivel()).isTrue();
  }

  @Test
  void entradasNulas_deveRetornarNulo() {
    assertThat(mapper.response(null)).isNull();
    assertThat(mapper.request(null)).isNull();
  }
}
