package vinix.catalogo.dtos.response;

import vinix.catalogo.GrupoAlimentar;

public record CategoriaResponseDTO(
    Long id,
    String nome,
    GrupoAlimentar grupo
) {}
