package vinix.pedido.dtos.response;

import vinix.pedido.entities.StatusPedido;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDTO(
    Long id,
    UUID clienteId,
    String nomeCliente,
    String telefoneCliente,
    StatusPedido status,
    BigDecimal total,
    Instant dataCadastro,
    List<ItemPedidoResponseDTO> itens
) {}
