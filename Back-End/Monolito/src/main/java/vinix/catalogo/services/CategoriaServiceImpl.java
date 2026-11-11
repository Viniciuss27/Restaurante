package vinix.catalogo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.exceptions.JaExistenteException;
import vinix.catalogo.exceptions.NaoExistenteException;
import vinix.catalogo.mappers.CategoriaMapper;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.ProdutoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

  private final ProdutoRepository produtoRepository;
  private final CategoriaRepository repository;
  private final CategoriaMapper mapper;


  @Override @Transactional(readOnly = true)
  public List<CategoriaResponseDTO> listar() {
    return repository.findAll().stream().map(mapper::response).toList();
  }

  @Override @Transactional(readOnly = true)
  public CategoriaResponseDTO buscarId(Long id) {
    return mapper.response(verificaId(id));
  }

  @Override @Transactional(readOnly = true)
  public CategoriaResponseDTO buscarCategoria(String categoria) {
   return repository.findByNomeIgnoreCase(categoria).map(mapper::response).orElseThrow(
        () -> new NaoExistenteException(categoria + " -> não encontrado"));
  }

  @Override @Transactional
  public CategoriaResponseDTO criar(CategoriaRequestDTO request) {

    if (repository.existsByNomeIgnoreCase(request.nome())) {
      throw new JaExistenteException(request.nome() + " -> categoria já existe");
    }

    Categoria categoria = mapper.request(request);
    return mapper.response(repository.save(categoria));
  }

  @Override @Transactional
  public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO request) {
    Categoria verificado = verificaId(id);

    if (repository.existsByNomeIgnoreCaseAndIdNot(request.nome(), id)) {
      throw new JaExistenteException(request.nome() + " -> categoria já existe");
    }

    verificado.setNome(request.nome());
    return mapper.response(verificado);
  }

  @Override @Transactional
  public void excluir(Long id) {
    verificaId(id);
    verificaProdutoExistente(id);
    repository.deleteById(id);
  }

  private Categoria verificaId(Long id){
    return repository.findById(id).orElseThrow(() -> new NaoExistenteException(id + " -> id não encontrado"));
  }

  private void verificaProdutoExistente(Long categoriaId) {
    if (produtoRepository.existsByCategoriaId(categoriaId)) {
      throw new JaExistenteException(
          "Não foi possível excluir: existem produtos associados a essa categoria"
      );
    }
  }
}
