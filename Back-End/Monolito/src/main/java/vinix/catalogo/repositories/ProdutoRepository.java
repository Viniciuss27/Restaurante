package vinix.catalogo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vinix.catalogo.entities.Produto;

import java.util.List;

public interface ProdutoRepository
    extends JpaRepository<Produto, Long>, JpaSpecificationExecutor<Produto> {

  @EntityGraph(attributePaths = "categoria")
  List<Produto> findAll(Specification<Produto> spec);

  boolean existsByCategoriaId(Long categoriaId);

}
