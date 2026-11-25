package vinix.pedido.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemPedidoAlimentoRequestDTO(

    @NotNull(message = "O alimento é obrigatório")
    @Positive(message = "O ID do alimento deve ser positivo")
    Long alimentoId

) {}
