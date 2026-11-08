package vinix.catalogo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vinix.catalogo.entities.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
  @EntityGraph(attributePaths = "categoria")
  List<Produto> findAllByDisponivelTrue();
}
