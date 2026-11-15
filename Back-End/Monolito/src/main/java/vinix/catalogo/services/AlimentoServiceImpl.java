package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Alimento;
import vinix.catalogo.mappers.AlimentoMapper;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.AlimentoRepository;
import vinix.compartilhado.excecoes.NaoExistenteException;

import java.util.List;

import static vinix.catalogo.services.AlimentoSpecification.categoriaContem;
import static vinix.catalogo.services.AlimentoSpecification.descricaoContem;
import static vinix.catalogo.services.AlimentoSpecification.disponivelIgual;
import static vinix.catalogo.services.AlimentoSpecification.nomeContem;

@Service
@RequiredArgsConstructor
public class AlimentoServiceImpl implements AlimentoService {

  private final CategoriaRepository categoriaRepository;
  private final AlimentoRepository repository;
  private final AlimentoMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public AlimentoResponseDTO buscarId(Long id) {
    return mapper.response(verificaId(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlimentoResponseDTO> buscar(String nome, String descricao, String categoria, Boolean disponivel) {

    Specification<Alimento> spec = Specification.allOf(
        nomeContem(nome), descricaoContem(descricao),
        categoriaContem(categoria), disponivelIgual(disponivel));

    return repository.findAll(spec).stream().map(mapper::response).toList();
  }

  @Override
  @Transactional
  public AlimentoResponseDTO criar(AlimentoRequestDTO alimento) {
    Alimento p = mapper.request(alimento);
    Categoria categoria = verificaCategoria(alimento.categoriaId());
    p.setCategoria(categoria);
    return mapper.response(repository.save(p));
  }

  @Override @Transactional
  public AlimentoResponseDTO atualizar(Long id, AlimentoRequestDTO alimento) {
    Alimento verificado = verificaId(id);
    Categoria categoria = verificaCategoria(alimento.categoriaId());

    verificado.setNome(alimento.nome());
    verificado.setDescricao(alimento.descricao());
    verificado.setCategoria(categoria);
    verificado.setImagemUrl(alimento.imagemUrl());
    return mapper.response(verificado);
  }

  @Override @Transactional
  public void alterarDisponibilidade(Long id, Boolean disponivel) {
    verificaId(id).setDisponivel(disponivel);
  }

  private Alimento verificaId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NaoExistenteException(id + " -> id não encontrado"));
  }

  private Categoria verificaCategoria(Long categoriaId) {
    return categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new NaoExistenteException(categoriaId + " -> categoria não encontrada"));
  }
}
