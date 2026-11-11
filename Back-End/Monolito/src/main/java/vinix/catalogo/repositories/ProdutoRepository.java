package vinix.catalogo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vinix.catalogo.entities.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findAll();

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findAllByDisponivelTrue();

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByCategoriaNomeContainingIgnoreCase(String categoria);

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByDescricaoContainingIgnoreCase(String descricao);

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByNomeContainingIgnoreCase(String nome);

  boolean existsByCategoriaId(Long categoriaId);

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByCategoriaNome(String categoriaNome);

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByNome(String nome);

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findByDescricao(String descricao);
}
