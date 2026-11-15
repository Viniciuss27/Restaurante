package vinix.catalogo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//API pública do catálogo para consultar produtos vendáveis (marmitas, bebidas, sobremesas)
public interface ProdutoConsulta {

  Optional<ProdutoResumo> buscarPorId(Long id);

  // Uma única consulta para vários ids, Ids inexistentes simplesmente não aparecem no resultado
  List<ProdutoResumo> buscarPorIds(Collection<Long> ids);
}
