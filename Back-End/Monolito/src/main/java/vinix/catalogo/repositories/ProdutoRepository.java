package vinix.catalogo.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import vinix.catalogo.entities.Produto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

  @Override
  @EntityGraph(attributePaths = {"alimento", "precos"})
  Optional<Produto> findById(Long id);

  @EntityGraph(attributePaths = {"alimento", "precos"})
  List<Produto> findAllByIdIn(Collection<Long> ids);
}
