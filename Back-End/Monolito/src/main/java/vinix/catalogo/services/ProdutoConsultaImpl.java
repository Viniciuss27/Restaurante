package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.ProdutoConsulta;
import vinix.catalogo.ProdutoResumo;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.repositories.ProdutoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//package-private: outros módulos só enxergam {@link ProdutoConsulta}
@Service
@RequiredArgsConstructor
class ProdutoConsultaImpl implements ProdutoConsulta {

  private final ProdutoRepository repository;

  @Override
  @Transactional(readOnly = true)
  public Optional<ProdutoResumo> buscarPorId(Long id) {
    return repository.findById(id).map(this::resumo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResumo> buscarPorIds(Collection<Long> ids) {
    return repository.findAllByIdIn(ids).stream().map(this::resumo).toList();
  }

  private ProdutoResumo resumo(Produto produto) {
    return new ProdutoResumo(produto.getId(), produto.getNome(), produto.getTipo(),
        produto.getPrecos(), produto.estaDisponivelParaVenda());
  }
}
