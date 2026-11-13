package vinix.catalogo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * API pública do catálogo para consulta por outros módulos.
 * Somente leitura e sem regras de negócio de quem consulta (ex.: se pode ou não pedir um alimento).
 */
public interface AlimentoConsulta {

  Optional<AlimentoResumo> buscarPorId(Long id);

  /** Uma única consulta para vários ids. Ids inexistentes simplesmente não aparecem no resultado. */
  List<AlimentoResumo> buscarPorIds(Collection<Long> ids);
}
