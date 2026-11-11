package vinix.catalogo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProdutoRequestDTO(

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
    String nome,

    @Size(max = 1000, message = "A descrição deve conter no máximo 1000 caracteres")
    String descricao,

    @NotNull(message = "O ID da categoria é obrigatório")
    @Positive(message = "O ID da categoria deve ser positivo")
    Long categoriaId,

    @Size(max = 2048, message = "A imagem deve ter no máximo 2048 caracteres")
    String imagemUrl

) {}
