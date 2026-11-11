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
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.services.ProdutoService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoResource {

  private final ProdutoService service;

  @GetMapping
  public ResponseEntity<List<ProdutoResponseDTO>> buscar(
      @RequestParam(required = false) String nome,
      @RequestParam(required = false) String descricao,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) Boolean disponivel) {
    return ResponseEntity.ok(service.buscar(nome, descricao, categoria, disponivel));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProdutoResponseDTO> buscarId(@PathVariable Long id) {
    return ResponseEntity.ok(service.buscarId(id));
  }

  @PostMapping
  public ResponseEntity<ProdutoResponseDTO> criar(
      @RequestBody @Valid ProdutoRequestDTO produto, UriComponentsBuilder builder) {

    ProdutoResponseDTO response =  service.criar(produto);
    URI uri = builder.path("/produtos/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProdutoResponseDTO> atualizar(
      @PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO produto) {
    return ResponseEntity.ok(service.atualizar(id, produto));
  }

  @PatchMapping("/{id}/disponibilidade")
  public ResponseEntity<Void> alterarDisponibilidade(
      @PathVariable Long id, @RequestParam boolean disponivel) {

    service.alterarDisponibilidade(id, disponivel);
    return ResponseEntity.noContent().build();
  }
}
