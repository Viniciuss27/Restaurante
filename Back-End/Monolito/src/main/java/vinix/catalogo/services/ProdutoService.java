package vinix.catalogo.services;

import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;

import java.util.List;

public interface ProdutoService {

  List<ProdutoResponseDTO> listar();

  List<ProdutoResponseDTO> listarDisponiveis();

  List<ProdutoResponseDTO> listarCategoriaNome(String nome);

  ProdutoResponseDTO buscarId(Long id);

  List<ProdutoResponseDTO> listarDescricao(String descricao);

  List<ProdutoResponseDTO> listarNome(String nome);

  ProdutoResponseDTO criar(ProdutoRequestDTO produto);

  ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO produto);

  void alterarDisponibilidade(Long id, boolean disponivel);

}
