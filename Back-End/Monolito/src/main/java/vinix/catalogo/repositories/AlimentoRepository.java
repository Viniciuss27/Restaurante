package vinix.catalogo.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vinix.catalogo.entities.Alimento;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlimentoRepository
    extends JpaRepository<Alimento, Long>, JpaSpecificationExecutor<Alimento> {

  @EntityGraph(attributePaths = "categoria")
  List<Alimento> findAll(Specification<Alimento> spec);

  @Override
  @EntityGraph(attributePaths = "categoria")
  Optional<Alimento> findById(Long id);

  @EntityGraph(attributePaths = "categoria")
  List<Alimento> findAllByIdIn(Collection<Long> ids);

  boolean existsByCategoriaId(Long categoriaId);

}
