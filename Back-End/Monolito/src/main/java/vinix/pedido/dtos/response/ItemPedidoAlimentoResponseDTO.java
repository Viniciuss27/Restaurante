package vinix.pedido.dtos.response;

public record ItemPedidoAlimentoResponseDTO(
    Long id,
    Long alimentoId,
    String nomeAlimento
) {}
