package vinix.catalogo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinix.catalogo.entities.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

  Optional<Categoria> findByNomeIgnoreCase(String nome);
  boolean existsByNomeIgnoreCase(String nome);
  boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

}
