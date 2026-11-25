package vinix.pedido.dtos.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import vinix.catalogo.Tamanho;

import java.util.List;


public record ItemPedidoRequestDTO(

    @NotNull(message = "O produto é obrigatório")
    @Positive(message = "O ID do produto deve ser positivo")
    Long produtoId,

    @NotNull(message = "O tamanho é obrigatório")
    Tamanho tamanho,

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    @Max(value = 20, message = "20 é a quantidade maxima permitida")
    Integer quantidade,

    @Valid
    @Size(max = 20, message = "maximo 20 itens por marmita")
    List<ItemPedidoAlimentoRequestDTO> alimentos

) {
    public ItemPedidoRequestDTO {
        alimentos = alimentos == null ? List.of() : alimentos;
    }
}
