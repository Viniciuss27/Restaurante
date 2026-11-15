package vinix.catalogo.dtos.request;

import vinix.catalogo.GrupoAlimentar;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRequestDTO(

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    String nome,

    @NotNull(message = "O grupo da categoria é obrigatório")
    GrupoAlimentar grupo

) {}
