package vinix.catalogo.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.services.AlimentoService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/alimentos")
public class AlimentoResource {

  private final AlimentoService service;

  @GetMapping
  public ResponseEntity<List<AlimentoResponseDTO>> buscar(
      @RequestParam(required = false) String nome,
      @RequestParam(required = false) String descricao,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) Boolean disponivel) {
    return ResponseEntity.ok(service.buscar(nome, descricao, categoria, disponivel));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AlimentoResponseDTO> buscarId(@PathVariable Long id) {
    return ResponseEntity.ok(service.buscarId(id));
  }

  @PostMapping
  public ResponseEntity<AlimentoResponseDTO> criar(
      @RequestBody @Valid AlimentoRequestDTO alimento, UriComponentsBuilder builder) {

    AlimentoResponseDTO response =  service.criar(alimento);
    URI uri = builder.path("/alimentos/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<AlimentoResponseDTO> atualizar(
      @PathVariable Long id, @RequestBody @Valid AlimentoRequestDTO alimento) {
    return ResponseEntity.ok(service.atualizar(id, alimento));
  }

  @PatchMapping("/{id}/disponibilidade")
  public ResponseEntity<Void> alterarDisponibilidade(
      @PathVariable Long id, @RequestParam boolean disponivel) {

    service.alterarDisponibilidade(id, disponivel);
    return ResponseEntity.noContent().build();
  }
}
