package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.exceptions.JaExistenteException;
import vinix.catalogo.exceptions.NaoExistenteException;
import vinix.catalogo.mappers.ProdutoMapper;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.ProdutoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

  private final CategoriaRepository categoriaRepository;
  private final ProdutoRepository repository;
  private final ProdutoMapper mapper;

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> listar() {
    return repository.findAll().stream().map(mapper::response).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> listarDisponiveis() {
    return repository.findAllByDisponivelTrue().stream().map(mapper::response).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> listarCategoriaNome(String categoria) {
    return repository.findByCategoriaNomeContainingIgnoreCase(categoria).stream().map(mapper::response).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ProdutoResponseDTO buscarId(Long id) {
    return mapper.response(verificaId(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> listarDescricao(String descricao) {
    return repository.findByDescricaoContainingIgnoreCase(descricao).stream().map(mapper::response).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ProdutoResponseDTO> listarNome(String nome) {
    return repository.findByNomeContainingIgnoreCase(nome).stream().map(mapper::response).toList();
  }

  @Override
  @Transactional
  public ProdutoResponseDTO criar(ProdutoRequestDTO produto) {
    Produto p = mapper.request(produto);
    Categoria categoria = verificaCategoria(produto.categoriaId());
    p.setCategoria(categoria);
    return mapper.response(repository.save(p));
  }

  @Override @Transactional
  public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO produto) {
    Produto verificado = verificaId(id);
    Categoria categoria = verificaCategoria(produto.categoriaId());

    verificado.setNome(produto.nome());
    verificado.setDescricao(produto.descricao());
    verificado.setCategoria(categoria);
    verificado.setImagemUrl(produto.imagemUrl());
    return mapper.response(verificado);
  }

  @Override @Transactional
  public void alterarDisponibilidade(Long id, boolean disponivel) {
    verificaId(id).setDisponivel(disponivel);
  }

  private Produto verificaIdIndisponivel(Long id) {
    Produto verificado = verificaId(id);
    if (verificado.isDisponivel()) {
      throw new JaExistenteException(id + " -> O produto ja esta disponivel");
    }
    return verificado;
  }

  private Produto verificaId(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new NaoExistenteException(id + " -> id não encontrado"));
  }

  private Categoria verificaCategoria(Long categoriaId) {
    return categoriaRepository.findById(categoriaId)
        .orElseThrow(() -> new NaoExistenteException(categoriaId + " -> categoria não encontrada"));
  }
}
