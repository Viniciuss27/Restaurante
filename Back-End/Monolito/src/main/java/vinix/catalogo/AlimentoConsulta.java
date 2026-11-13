package vinix.catalogo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AlimentoConsulta {

  Optional<AlimentoResumo> buscarPorId(Long id);

  List<AlimentoResumo> buscarPorIds(Collection<Long> ids);
}
