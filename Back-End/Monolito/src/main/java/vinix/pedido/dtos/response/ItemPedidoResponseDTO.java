package vinix.pedido.dtos.response;

import vinix.catalogo.Tamanho;

import java.math.BigDecimal;
import java.util.List;

public record ItemPedidoResponseDTO(
    Long id,
    Long produtoId,
    String nomeProduto,
    Tamanho tamanho,
    BigDecimal precoUnitario,
    Integer quantidade,
    List<ItemPedidoAlimentoResponseDTO> alimentos
) {}
