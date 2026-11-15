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
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.services.AlimentoService;

@ExtendWith(MockitoExtension.class)
class AlimentoResourceTest {

  @Mock private AlimentoService service;
  @InjectMocks private AlimentoResource resource;

  private final AlimentoResponseDTO costela = new AlimentoResponseDTO(
      10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", true);
  private final AlimentoRequestDTO requisicao =
      new AlimentoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png");

  @Test
  void buscar_deveRepassarTodosOsFiltrosAoService() {
    when(service.buscar("cos", "assada", "carnes", true)).thenReturn(List.of(costela));

    ResponseEntity<List<AlimentoResponseDTO>> resposta = resource.buscar("cos", "assada", "carnes", true);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).containsExactly(costela);
  }

  @Test
  void buscar_semFiltros_deveRepassarNulos() {
    when(service.buscar(null, null, null, null)).thenReturn(List.of());

    ResponseEntity<List<AlimentoResponseDTO>> resposta = resource.buscar(null, null, null, null);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEmpty();
  }

  @Test
  void buscarId_deveRetornar200() {
    when(service.buscarId(10L)).thenReturn(costela);

    ResponseEntity<AlimentoResponseDTO> resposta = resource.buscarId(10L);

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(resposta.getBody()).isEqualTo(costela);
  }

  @Test
  void criar_deveRetornar201ComLocationDoNovoAlimento() {
    when(service.criar(requisicao)).thenReturn(costela);

    ResponseEntity<AlimentoResponseDTO> resposta =
        resource.criar(requisicao, UriComponentsBuilder.fromUriString("http://localhost"));

    assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(resposta.getHeaders().getLocation()).hasToString("http://localhost/alimentos/10");
    assertThat(resposta.getBody()).isEqualTo(costela);
  }

  @Test
  void atualizar_deveRetornar200() {
    when(service.atualizar(10L, requisicao)).thenReturn(costela);

    ResponseEntity<AlimentoResponseDTO> resposta = resource.atualizar(10L, requisicao);

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
