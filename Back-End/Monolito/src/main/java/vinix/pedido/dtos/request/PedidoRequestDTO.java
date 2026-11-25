package vinix.pedido.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record PedidoRequestDTO(

    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    @Valid
    List<ItemPedidoRequestDTO> itens,

    @NotNull(message = "Digite o horario da retirada")
    @Future(message = "digite uma data/hora futura")
    Instant retiradaEm

) {}
