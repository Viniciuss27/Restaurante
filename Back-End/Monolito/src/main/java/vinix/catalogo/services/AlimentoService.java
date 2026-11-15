package vinix.catalogo.services;

import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;

import java.util.List;

public interface AlimentoService {

  AlimentoResponseDTO buscarId(Long id);

  List<AlimentoResponseDTO> buscar(String nome, String descricao, String categoria, Boolean disponivelIgual);

  AlimentoResponseDTO criar(AlimentoRequestDTO alimento);

  AlimentoResponseDTO atualizar(Long id, AlimentoRequestDTO alimento);

  void alterarDisponibilidade(Long id, Boolean disponivel);

}
