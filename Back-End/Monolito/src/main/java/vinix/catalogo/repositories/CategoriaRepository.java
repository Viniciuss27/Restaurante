package vinix.catalogo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinix.catalogo.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
