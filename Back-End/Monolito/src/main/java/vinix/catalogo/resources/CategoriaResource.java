package vinix.catalogo.resources;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.services.CategoriaService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

  private final CategoriaService service;

  @GetMapping
  public ResponseEntity<List<CategoriaResponseDTO>> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoriaResponseDTO> buscarId(@PathVariable Long id) {
    return ResponseEntity.ok(service.buscarId(id));
  }

  @GetMapping("/nome/{nome}")
  public ResponseEntity<CategoriaResponseDTO> buscarCategoria(@PathVariable String nome) {
    return ResponseEntity.ok(service.buscarCategoria(nome));
  }

  @PostMapping
  public ResponseEntity<CategoriaResponseDTO> criar(
      @RequestBody @Valid CategoriaRequestDTO request, UriComponentsBuilder builder) {

    CategoriaResponseDTO response = service.criar(request);
    URI uri = builder.path("/categorias/{id}").buildAndExpand(response.id()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoriaResponseDTO> atualizar(
      @PathVariable Long id, @RequestBody @Valid CategoriaRequestDTO request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
