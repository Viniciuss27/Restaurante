package vinix.catalogo.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.services.ProdutoService;

@ExtendWith(MockitoExtension.class)
class ProdutoResourceTest {

  @Mock private ProdutoService service;
  @InjectMocks private ProdutoResource resource;

  private final ProdutoResponseDTO costela = new ProdutoResponseDTO(
      10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", true);
  private final ProdutoRequestDTO requisicao =
      new ProdutoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png");

  @Test
  void buscar_deveRepassarTodosOsFiltrosAoService() {
    when(service.buscar("cos", "assada", "carnes", true)).thenReturn(List.of(costela));

    ResponseEntity<List<ProdutoResponseDTO>> resposta = resource.buscar("cos", "assada", "carnes", true);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).containsExactly(costela);
  }

  @Test
  void buscar_semFiltros_deveRepassarNulos() {
    when(service.buscar(null, null, null, null)).thenReturn(List.of());

    ResponseEntity<List<ProdutoResponseDTO>> resposta = resource.buscar(null, null, null, null);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEmpty();
  }

  @Test
  void buscarId_deveRetornar200() {
    when(service.buscarId(10L)).thenReturn(costela);

    ResponseEntity<ProdutoResponseDTO> resposta = resource.buscarId(10L);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(costela);
  }

  @Test
  void criar_deveRetornar201ComLocationDoNovoProduto() {
    when(service.criar(requisicao)).thenReturn(costela);

    ResponseEntity<ProdutoResponseDTO> resposta =
        resource.criar(requisicao, UriComponentsBuilder.fromUriString("http://localhost"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(resposta.getHeaders().getLocation()).hasToString("http://localhost/produtos/10");
    assertThat(resposta.getBody()).isEqualTo(costela);
  }

  @Test
  void atualizar_deveRetornar200() {
    when(service.atualizar(10L, requisicao)).thenReturn(costela);

    ResponseEntity<ProdutoResponseDTO> resposta = resource.atualizar(10L, requisicao);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(costela);
  }

  @Test
  void alterarDisponibilidade_deveRetornar204ERepassarOValor() {
    ResponseEntity<Void> resposta = resource.alterarDisponibilidade(10L, false);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(resposta.getBody()).isNull();
    verify(service).alterarDisponibilidade(10L, false);
  }
}
