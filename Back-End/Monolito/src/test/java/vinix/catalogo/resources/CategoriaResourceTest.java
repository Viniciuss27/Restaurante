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
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.services.CategoriaService;

@ExtendWith(MockitoExtension.class)
class CategoriaResourceTest {

  @Mock private CategoriaService service;
  @InjectMocks private CategoriaResource resource;

  private final CategoriaResponseDTO carnes = new CategoriaResponseDTO(1L, "Carnes");

  @Test
  void listar_deveRetornar200ComAsCategorias() {
    when(service.listar()).thenReturn(List.of(carnes));

    ResponseEntity<List<CategoriaResponseDTO>> resposta = resource.listar();

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).containsExactly(carnes);
  }

  @Test
  void buscarId_deveRetornar200() {
    when(service.buscarId(1L)).thenReturn(carnes);

    ResponseEntity<CategoriaResponseDTO> resposta = resource.buscarId(1L);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(carnes);
  }

  @Test
  void buscarCategoria_deveRetornar200() {
    when(service.buscarCategoria("carnes")).thenReturn(carnes);

    ResponseEntity<CategoriaResponseDTO> resposta = resource.buscarCategoria("carnes");

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(carnes);
  }

  @Test
  void criar_deveRetornar201ComLocationDaNovaCategoria() {
    var request = new CategoriaRequestDTO("Carnes");
    when(service.criar(request)).thenReturn(carnes);

    ResponseEntity<CategoriaResponseDTO> resposta =
        resource.criar(request, UriComponentsBuilder.fromUriString("http://localhost"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(resposta.getHeaders().getLocation()).hasToString("http://localhost/categorias/1");
    assertThat(resposta.getBody()).isEqualTo(carnes);
  }

  @Test
  void atualizar_deveRetornar200() {
    var request = new CategoriaRequestDTO("Carnes");
    when(service.atualizar(1L, request)).thenReturn(carnes);

    ResponseEntity<CategoriaResponseDTO> resposta = resource.atualizar(1L, request);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(carnes);
  }

  @Test
  void excluir_deveRetornar204SemCorpo() {
    ResponseEntity<Void> resposta = resource.excluir(1L);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(resposta.getBody()).isNull();
    verify(service).excluir(1L);
  }
}
