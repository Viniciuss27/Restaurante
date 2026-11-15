package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.AlimentoConsulta;
import vinix.catalogo.AlimentoResumo;
import vinix.catalogo.entities.Alimento;
import vinix.catalogo.repositories.AlimentoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

//package-private: outros módulos só enxergam {@link AlimentoConsulta}
@Service
@RequiredArgsConstructor
class AlimentoConsultaImpl implements AlimentoConsulta {

  private final AlimentoRepository repository;

  @Override
  @Transactional(readOnly = true)
  public Optional<AlimentoResumo> buscarPorId(Long id) {
    return repository.findById(id).map(this::resumo);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlimentoResumo> buscarPorIds(Collection<Long> ids) {
    return repository.findAllByIdIn(ids).stream().map(this::resumo).toList();
  }

  private AlimentoResumo resumo(Alimento alimento) {
    return new AlimentoResumo(
        alimento.getId(), alimento.getNome(), alimento.getDescricao(), alimento.isDisponivel(),
        alimento.getCategoria().getId(), alimento.getCategoria().getNome(),
        alimento.getCategoria().isCompoeMarmita());
  }
}
