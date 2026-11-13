package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.AlimentoConsulta;
import vinix.catalogo.AlimentoResumo;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.repositories.ProdutoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** Implementação interna (package-private): outros módulos só enxergam {@link AlimentoConsulta}. */
@Service
@RequiredArgsConstructor
class AlimentoConsultaImpl implements AlimentoConsulta {

  private final ProdutoRepository repository;

  @Override
  @Transactional(readOnly = true)
  public Optional<AlimentoResumo> buscarPorId(Long id) {
    return repository.findById(id).map(this::resumo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlimentoResumo> buscarPorIds(Collection<Long> ids) {
    return repository.findAllById(ids).stream().map(this::resumo).toList();
  }

  private AlimentoResumo resumo(Produto produto) {
    return new AlimentoResumo(
        produto.getId(), produto.getNome(), produto.getDescricao(), produto.isDisponivel());
  }
}
