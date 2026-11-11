package vinix.catalogo.services;

import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;

import java.util.List;

public interface CategoriaService {

  List<CategoriaResponseDTO> listar();

  CategoriaResponseDTO buscarId(Long id);

  CategoriaResponseDTO buscarCategoria(String nome);

  CategoriaResponseDTO criar(CategoriaRequestDTO request);

  CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO request);

  void excluir(Long id);
}
