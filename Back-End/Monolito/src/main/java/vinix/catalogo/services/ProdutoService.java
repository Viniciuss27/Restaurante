package vinix.catalogo.services;

import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;

import java.util.List;

public interface ProdutoService {

  ProdutoResponseDTO buscarId(Long id);

  List<ProdutoResponseDTO> buscar(String nome, String descricao, String categoria, Boolean disponivelIgual);

  ProdutoResponseDTO criar(ProdutoRequestDTO produto);

  ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO produto);

  void alterarDisponibilidade(Long id, Boolean disponivel);

}
